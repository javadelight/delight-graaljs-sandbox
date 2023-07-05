package delight.graaljssandbox;

import static org.junit.Assert.assertEquals;

import javax.script.Invocable;
import javax.script.ScriptException;

import org.junit.Test;

 
import delight.nashornsandbox.exceptions.ScriptCPUAbuseException;

public class TestInvocable {

	@Test
	public void testInvokeFunction_graal() throws ScriptCPUAbuseException, ScriptException, NoSuchMethodException {
		final GraalSandbox sandbox = GraalSandboxes.create();
		final String script = "function x(){return 1;}\n";
		sandbox.eval(script);
		Invocable invocable = sandbox.getSandboxedInvocable();
		assertEquals(1, invocable.invokeFunction("x"));
	}

	@Test
	public void testInvokeMethod_graal() throws ScriptCPUAbuseException, ScriptException, NoSuchMethodException {
		final GraalSandbox sandbox = GraalSandboxes.create();
		final String script = "var obj = {n: 1, x:function(arg){return this.n + arg;}};";
		sandbox.eval(script);
		Object thisobj = sandbox.get("obj");
		Invocable invocable = sandbox.getSandboxedInvocable();

		assertEquals(3, invocable.invokeMethod(thisobj, "x", 2));
	}

}
