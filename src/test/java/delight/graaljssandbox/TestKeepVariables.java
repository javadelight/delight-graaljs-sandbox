package delight.graaljssandbox;

import javax.script.ScriptException;

import org.junit.Assert;
import org.junit.Test;

 
import delight.nashornsandbox.exceptions.ScriptCPUAbuseException;

public class TestKeepVariables {

	@Test
	public void test_graal() throws ScriptCPUAbuseException, ScriptException {
		final GraalSandbox sandbox = GraalSandboxes.create();
		sandbox.eval("var window={};");
		sandbox.eval("window.val1 = \"myval\";");
		final Object res = sandbox.eval("window.val1;");
		Assert.assertEquals("myval", res);
	}
}
