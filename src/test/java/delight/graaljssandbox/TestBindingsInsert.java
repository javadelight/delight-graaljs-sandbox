package delight.graaljssandbox;

import static org.junit.Assert.assertNull;

import javax.script.Bindings;
import javax.script.ScriptException;

import org.junit.Ignore;
import org.junit.Test;

import delight.nashornsandbox.NashornSandbox;
import delight.nashornsandbox.exceptions.ScriptCPUAbuseException;

public class TestBindingsInsert {

	@Test
	@Ignore("This shows that you can override the secure settings")
	public void testOverride_graal() throws ScriptCPUAbuseException, ScriptException {
		final NashornSandbox sandbox = GraalSandboxes.create();
		assertNull(sandbox.eval("var $ARG=\"a\"; $ARG;"));
	}

	@Test
	// This shows that with the override we will restore to the secure settings so
	// the next script that runs through should be fine
	public void testInsertOptions_graal() throws ScriptCPUAbuseException, ScriptException {
		final NashornSandbox sandbox = GraalSandboxes.create();
		sandbox.eval("var $ARG=\"a\";");
		assertNull(sandbox.eval("$ARG;"));
	}

	@Test
	public void testInsertOptionsWithBinding_graal() throws ScriptCPUAbuseException, ScriptException {
		final NashornSandbox sandbox = GraalSandboxes.create();
		final Bindings bindings = sandbox.createBindings();
		bindings.put("$ARG", "a");
		sandbox.eval("$ARG", bindings);
		assertNull(sandbox.eval("$ARG;"));
	}

	@Test
	public void testInsertOptionsWithBinding2_graal() throws ScriptCPUAbuseException, ScriptException {
		final NashornSandbox sandbox = GraalSandboxes.create();
		final Bindings bindings = sandbox.createBindings();
		bindings.put("$ARG", "a");
		sandbox.eval("var $ARG=\"a\";", bindings);
		assertNull(sandbox.eval("$ARG;"));
	}

}
