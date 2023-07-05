package delight.graaljssandbox;

import javax.script.ScriptException;

import org.junit.Test;

 
import delight.nashornsandbox.exceptions.ScriptCPUAbuseException;

public class TestGlobalVariable {

	@Test
	public void test_java_variable_graal() throws ScriptCPUAbuseException, ScriptException {
		final GraalSandbox sandbox = GraalSandboxes.create();
		final Object _object = new Object();
		sandbox.inject("fromJava", _object);
		sandbox.allow(String.class);
		sandbox.allow(Class.class);
		sandbox.eval("fromJava.toString();");
	}
}
