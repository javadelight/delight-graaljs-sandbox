package delight.graaljssandbox;

import java.io.File;

import javax.script.ScriptException;

import org.junit.Test;

import delight.nashornsandbox.NashornSandbox;
import delight.nashornsandbox.exceptions.ScriptCPUAbuseException;

public class TestScriptEngineParameters {
	
	
	@Test(expected=ScriptException.class)
	public void test_graal() throws ScriptCPUAbuseException, ScriptException {

		final NashornSandbox sandbox = GraalSandboxes.create("-strict");
	    sandbox.allow(File.class);

	    // should throw an exception since 'Java' is not allowed.
	    sandbox.eval("idontexist = 1;");
	}
}
