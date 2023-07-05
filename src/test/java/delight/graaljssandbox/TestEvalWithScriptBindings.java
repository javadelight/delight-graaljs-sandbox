package delight.graaljssandbox;

import java.util.concurrent.Executors;

import javax.script.Bindings;
import javax.script.ScriptException;

import org.junit.Assert;
import org.junit.Test;

import delight.nashornsandbox.NashornSandbox;
import delight.nashornsandbox.exceptions.ScriptCPUAbuseException;

public class TestEvalWithScriptBindings {

	@Test
	public void test_graal() throws ScriptCPUAbuseException, ScriptException {
		final GraalSandbox sandbox = GraalSandboxes.create();

		Bindings binding1 = sandbox.createBindings();
		binding1.put("y", 2);

		final Object res1 = sandbox.eval("function cal() {var x = y + 1; return x;} cal();", binding1);
		Assert.assertEquals(3, res1);

		Bindings binding2 = sandbox.createBindings();
		binding2.put("y", 4);

		final Object res2 = sandbox.eval("function cal() {var x = y + 1; return x;} cal();", binding2);
		Assert.assertEquals(5, res2);

	}

	@Test
	public void testWithCPUAndMemory_graal() throws ScriptCPUAbuseException, ScriptException {
		final GraalSandbox sandbox = GraalSandboxes.create();
		sandbox.setMaxCPUTime(100);
		sandbox.setMaxMemory(1000 * 1024);
		sandbox.setExecutor(Executors.newSingleThreadExecutor());
		Bindings binding1 = sandbox.createBindings();
		binding1.put("y", 2);

		final Object res1 = sandbox.eval("function cal() {var x = y + 1; return x;} cal();", binding1);
		Assert.assertEquals(3, res1);

		Bindings binding2 = sandbox.createBindings();
		binding2.put("y", 4);

		final Object res2 = sandbox.eval("function cal() {var x = y + 1; return x;} cal();", binding2);
		Assert.assertEquals(5, res2);

	}
}
