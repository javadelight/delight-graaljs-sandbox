package delight.graaljssandbox;

import java.util.concurrent.Executors;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptException;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import delight.nashornsandbox.SandboxScriptContext;
import delight.nashornsandbox.exceptions.ScriptCPUAbuseException;

public class TestEvalWithScriptContext {

	@Test
	public void test_graal() throws ScriptCPUAbuseException, ScriptException {
		final GraalSandbox sandbox = GraalSandboxes.create();
		SandboxScriptContext newContext1 = sandbox.createScriptContext();

		Bindings engineScope1 = newContext1.getContext().getBindings(ScriptContext.ENGINE_SCOPE);
		engineScope1.put("y", 2);

		SandboxScriptContext newContext2 = sandbox.createScriptContext(); 
		Bindings engineScope2 = newContext2.getContext().getBindings(ScriptContext.ENGINE_SCOPE);
		engineScope2.put("y", 4);

		final Object res1 = sandbox.eval("function cal() {var x = y + 1; return x;} cal();", newContext1);
		Assert.assertEquals(3, res1);

		final Object res2 = sandbox.eval("function cal() {var x = y + 1; return x;} cal();", newContext2);
		Assert.assertEquals(5, res2);

	}

	@Ignore
	@Test
	public void testWithCPUAndMemory_graal() throws ScriptCPUAbuseException, ScriptException {
		final GraalSandbox sandbox = GraalSandboxes.create();
		sandbox.setMaxCPUTime(100);
		sandbox.setMaxMemory(1000 * 1024);
		sandbox.setExecutor(Executors.newSingleThreadExecutor());
		SandboxScriptContext newContext1 = sandbox.createScriptContext();
		Bindings engineScope1 = newContext1.getContext().getBindings(ScriptContext.ENGINE_SCOPE);
		engineScope1.put("y", 2);

		SandboxScriptContext newContext2 = sandbox.createScriptContext();
		Bindings engineScope2 = newContext2.getContext().getBindings(ScriptContext.ENGINE_SCOPE);
		engineScope2.put("y", 4);

		final Object res1 = sandbox.eval("function cal() {var x = y + 1; return x;} cal();", newContext1);
		Assert.assertEquals(3, res1);

		final Object res2 = sandbox.eval("function cal() {var x = y + 1; return x;} cal();", newContext2);
		Assert.assertEquals(5, res2);

	}

}
