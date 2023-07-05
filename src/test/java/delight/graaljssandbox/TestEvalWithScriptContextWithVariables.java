package delight.graaljssandbox;

import java.util.concurrent.Executors;

import javax.script.ScriptContext;
import javax.script.ScriptException;
import javax.script.SimpleScriptContext;

import org.junit.Assert;
import org.junit.Test;

import delight.nashornsandbox.NashornSandbox;
import delight.nashornsandbox.SandboxScriptContext;
import delight.nashornsandbox.exceptions.ScriptCPUAbuseException;

public class TestEvalWithScriptContextWithVariables {

	@Test
	public void test_graal() throws ScriptCPUAbuseException, ScriptException {
		final NashornSandbox sandbox = GraalSandboxes.create();
		SandboxScriptContext newContext1 = sandbox.createScriptContext();
		SandboxScriptContext newContext2 = sandbox.createScriptContext();

		sandbox.eval("function cal() {var x = 1; return x;}", newContext1);
		sandbox.eval("function cal() {var x = 2; return x;}", newContext2);

		final Object res1 = sandbox.eval("cal();", newContext1);
		Assert.assertEquals(1, res1);

		final Object res2 = sandbox.eval("cal();", newContext2);
		Assert.assertEquals(2, res2);

	}

	@Test
	public void testWithCPUAndMemory_graal() throws ScriptCPUAbuseException, ScriptException {
		final NashornSandbox sandbox = GraalSandboxes.create();
		sandbox.setMaxCPUTime(100);
		sandbox.setMaxMemory(1000 * 1024);
		sandbox.setExecutor(Executors.newSingleThreadExecutor());
		SandboxScriptContext newContext1 = sandbox.createScriptContext();
		SandboxScriptContext newContext2 = sandbox.createScriptContext();

		sandbox.eval("function cal() {var x = 1; return x;}", newContext1);
		sandbox.eval("function cal() {var x = 2; return x;}", newContext2);

		final Object res1 = sandbox.eval("cal();", newContext1);
		Assert.assertEquals(1, res1);

		final Object res2 = sandbox.eval("cal();", newContext2);
		Assert.assertEquals(2, res2);

	}

}
