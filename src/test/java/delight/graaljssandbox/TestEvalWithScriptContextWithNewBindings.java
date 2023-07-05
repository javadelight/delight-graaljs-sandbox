package delight.graaljssandbox;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptException;
import javax.script.SimpleBindings;
import javax.script.SimpleScriptContext;

import org.junit.Assert;
import org.junit.Test;

 
import delight.nashornsandbox.SandboxScriptContext;
import delight.nashornsandbox.exceptions.ScriptCPUAbuseException;

public class TestEvalWithScriptContextWithNewBindings {
	
	@Test
	public void testWithNewBindings_graal() throws ScriptCPUAbuseException, ScriptException {
		final GraalSandbox sandbox = GraalSandboxes.create();
		// Create new binding to override the ECMAScript Global properties
		Bindings newBinding = sandbox.createBindings();
		@SuppressWarnings("unused")
		Object obj = newBinding.get("Date");
		newBinding.put("Date", "2112018");

		final Object res = sandbox.eval("function method() { return parseInt(Date);} method();", newBinding);
		Assert.assertEquals(2112018, res);
	}
	
	@Test
	public void testWithNewSimpleBindings_graal() throws ScriptCPUAbuseException, ScriptException {
		final GraalSandbox sandbox = GraalSandboxes.create();
		// Create new binding to override the ECMAScript Global properties
		Bindings newBinding = new SimpleBindings();
		newBinding.put("Date", "2112018");

		final Object res = sandbox.eval("function method() { return parseInt(Date);} method();", newBinding);
		Assert.assertTrue(res.equals(2112018));
	}
	
	@Test
	public void testWithNewBindingsScriptContext_graal() throws ScriptCPUAbuseException, ScriptException {
		final GraalSandbox sandbox = GraalSandboxes.create();
		SandboxScriptContext newContext = sandbox.createScriptContext();
		// Create new binding to override the ECMAScript Global properties 
		Bindings newBinding = sandbox.createBindings();
		newBinding.put("Date", "2112018");
		newContext.getContext().setBindings(newBinding, ScriptContext.ENGINE_SCOPE);

		final Object res = sandbox.eval("function method() { return parseInt(Date);} method();", newContext);
		Assert.assertEquals(2112018, res);
	}
	
	@Test
	public void testWithExistingBindings_graal() throws ScriptCPUAbuseException, ScriptException {
		final GraalSandbox sandbox = GraalSandboxes.create();
	  SandboxScriptContext newContext = sandbox.createScriptContext();
		Bindings newBinding = newContext.getContext().getBindings(ScriptContext.ENGINE_SCOPE);
		// This will not be updated by using existing bindings, since Date is a 
		// ECMAScript "global" properties and it is being in ENGINE_SCOPE
		newBinding.put("Date", "2112018");

		final Object res = sandbox.eval("function method() { return parseInt(Date);} method();", newContext);
		Assert.assertTrue(res.equals(2112018));
	}
	
}
