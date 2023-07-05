package delight.graaljssandbox;

import javax.script.ScriptException;

import org.junit.Assert;
import org.junit.Test;

 
import delight.nashornsandbox.exceptions.ScriptCPUAbuseException;

public class TestEval {

	@Test
	public void test_graal() throws ScriptCPUAbuseException, ScriptException {
		final GraalSandbox sandbox = GraalSandboxes.create();
		final Object res = sandbox.eval("var x = 1 + 1; x;");
		Assert.assertEquals(Integer.valueOf(2), res);
	}
}
