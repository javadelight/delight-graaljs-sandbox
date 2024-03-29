package delight.graaljssandbox.internal;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptException;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;
import org.graalvm.polyglot.PolyglotAccess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oracle.truffle.js.scriptengine.GraalJSScriptEngine;

import delight.graaljssandbox.GraalSandbox;
import delight.nashornsandbox.SandboxScriptContext;
import delight.nashornsandbox.exceptions.ScriptCPUAbuseException;
import delight.nashornsandbox.internal.EvaluateOperation;
import delight.nashornsandbox.internal.JsSanitizer;
import delight.nashornsandbox.internal.NashornSandboxImpl;

/**
 * Nashorn sandbox implementation for GraalJS
 * Due to the active development nature of GraalJS many of these provisions may
 * be temporary.
 * 
 * @author marcoellwanger
 */
public class GraalSandboxImpl extends NashornSandboxImpl implements GraalSandbox {

	static final Logger LOG = LoggerFactory.getLogger(GraalSandboxImpl.class);

	public final boolean isStrict;

	public GraalSandboxImpl() {
		this(new String[0]);
	}

	public GraalSandboxImpl(String... params) {
		// HostAccess and PolyglotAccess is necessary to access Java classes
		super(GraalJSScriptEngine.create(null, Context.newBuilder()
				.allowExperimentalOptions(true)
				.allowPolyglotAccess(PolyglotAccess.ALL)
				.allowHostAccess(HostAccess.ALL)
				.allowAllAccess(true)), params);
		isStrict = Arrays.asList(params).contains("-strict");
		Bindings bindings = this.scriptEngine.getBindings(ScriptContext.ENGINE_SCOPE);
		// allow the lookup of Java classes via the (deprecated) ClassFilter
		bindings.put("polyglot.js.allowHostClassLookup",
				(Predicate<String>) s -> sandboxClassFilter.getStringCache().contains(s));
	}

	@Override
	public Bindings createBindings() {
		return scriptEngine.createBindings();
	}

	@Override
	public Bindings createNewBindings() {
		return scriptEngine.createBindings();
	}

	protected void produceSecureBindings() {
		try {
			final StringBuilder sb = new StringBuilder();
			cached = scriptEngine.getBindings(ScriptContext.ENGINE_SCOPE);
			sanitizeBindings(cached);
			if (!allowExitFunctions) {
				sb.append("var quit=function(){};var exit=function(){};");
			}
			if (!allowPrintFunctions) {
				sb.append("var print=function(){};var echo = function(){};");
			}
			if (!allowReadFunctions) {
				sb.append("var readFully=function(){};").append("var readLine=function(){};");
			}
			if (!allowLoadFunctions) {
				sb.append("var load=function(){};var loadWithNewGlobal=function(){};");
			}
			if (!allowGlobalsObjects) {
				// Max 22nd of Feb 2018: I don't think these are strictly necessary since they
				// are only available in scripting mode
				sb.append("var $ARG=null;var $ENV=null;var $EXEC=null;");
				sb.append("var $OPTIONS=null;var $OUT=null;var $ERR=null;var $EXIT=null;");
			}
			scriptEngine.eval(sb.toString());
			this.engineAsserted.set(true);
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected Bindings secureBindings(Bindings bindings) {
		if (bindings == null)
			return null;

		bindings.putAll(cached);

		return bindings;
	}

	/**
	 * If a script context is provided, its bindings will be evaluated inside the
	 * script engine itself and merged into the engine bindings.
	 * Nashorn checks against globals but Graal seems to override global entries if
	 * the same key is present in the script context's bindings.
	 *
	 * For strict mode for GraalJS we need to prefix the sanitized code with 'use
	 * strict;'
	 */
	@Override
	public Object eval(final String js, final SandboxScriptContext scriptContext, final Bindings bindings)
			throws ScriptCPUAbuseException, ScriptException {
		produceSecureBindings(); // We need this here for bindings
		final JsSanitizer sanitizer = getSanitizer();
		// see https://github.com/javadelight/delight-nashorn-sandbox/issues/73
		final String blockAccessToEngine = "Object.defineProperty(this, 'engine', {});" +
				"Object.defineProperty(this, 'context', {});delete this.__noSuchProperty__;";
		final String securedJs;
		if (scriptContext == null) {
			securedJs = blockAccessToEngine + sanitizer.secureJs(js);
		} else {
			// Unfortunately, blocking access to the engine property interferes with setting
			// a script context needs further investigation
			securedJs = sanitizer.secureJs(js);
		}

		EvaluateOperation op;
		final Bindings securedBindings = secureBindings(bindings);
		if (scriptContext != null) {
			op = new EvaluateOperation(isStrict ? "'use strict';" + securedJs : securedJs,
					scriptContext.getContext(),
					securedBindings);
		} else {
			op = new EvaluateOperation(isStrict ? "'use strict';" + securedJs : securedJs,
					null, securedBindings);
		}
		return executeSandboxedOperation(op);
	}
}
