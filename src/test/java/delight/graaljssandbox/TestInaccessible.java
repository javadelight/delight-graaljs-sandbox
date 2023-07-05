package delight.graaljssandbox;

import javax.script.ScriptException;

import org.junit.Test;

import delight.nashornsandbox.NashornSandbox;
import delight.nashornsandbox.exceptions.ScriptCPUAbuseException;

public class TestInaccessible {

	@Test(expected = Exception.class)
	public void test_system_exit_graal() throws ScriptCPUAbuseException, ScriptException {
		final GraalSandbox sandbox = GraalSandboxes.create();
		sandbox.eval("java.lang.System.exit(0);");
	}

	@Test(expected = Exception.class)
	public void test_file_graal() throws ScriptCPUAbuseException, ScriptException {
		final GraalSandbox sandbox = GraalSandboxes.create();
		sandbox.eval("var File = Java.type(\"java.io.File\"); File;");
	}

}
