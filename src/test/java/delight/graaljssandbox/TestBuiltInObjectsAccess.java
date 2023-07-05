package delight.graaljssandbox;

import javax.script.ScriptException;

import org.junit.Test;

import delight.nashornsandbox.NashornSandbox;
import delight.nashornsandbox.exceptions.ScriptCPUAbuseException;

public class TestBuiltInObjectsAccess {
	
	@Test
	public void test_block_access_graal() throws ScriptCPUAbuseException, ScriptException {
		final GraalSandbox sandbox = GraalSandboxes.create();
		
		sandbox.eval("exit()");
		sandbox.eval("quit()");
	}
	
}
