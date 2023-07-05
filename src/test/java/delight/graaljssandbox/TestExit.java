package delight.graaljssandbox;

import javax.script.ScriptException;
import javax.script.SimpleScriptContext;

import org.junit.Ignore;
import org.junit.Test;

import delight.nashornsandbox.NashornSandbox;
import delight.nashornsandbox.exceptions.ScriptCPUAbuseException;

public class TestExit {

	@Test
	public void testExit_graal() throws ScriptCPUAbuseException, ScriptException {
		final GraalSandbox sandbox = GraalSandboxes.create();
		sandbox.eval("exit();");
	}

	@Test
	public void testQuit_graal() throws ScriptCPUAbuseException, ScriptException {
		final GraalSandbox sandbox = GraalSandboxes.create();
		sandbox.eval("quit();");
	}

	@Test
	public void testQuitWithBindings_graal() throws ScriptCPUAbuseException, ScriptException {
		final GraalSandbox sandbox = GraalSandboxes.create();
		sandbox.eval("quit();", sandbox.createBindings());
	}

	@Test
	@Ignore("This will fail as there is no confirmation on Script Contexts")
	public void testQuitWithScriptContext_graal() throws ScriptCPUAbuseException, ScriptException {
		final GraalSandbox sandbox = GraalSandboxes.create();
		sandbox.eval("quit();", sandbox.createScriptContext());
	}

}
