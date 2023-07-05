package delight.graaljssandbox;

import javax.script.ScriptException;

import org.junit.Test;

import delight.nashornsandbox.NashornSandbox;
import delight.nashornsandbox.exceptions.ScriptCPUAbuseException;
import junit.framework.Assert;

public class TestEngine {
	
	@Test(expected = ScriptException.class)
	public void test_graal() throws ScriptCPUAbuseException, ScriptException {

		GraalSandbox sandbox = GraalSandboxes.create();
		GraalSandbox sandbox2 = GraalSandboxes.create();

		Assert.assertEquals(null, sandbox.eval("this.engine.factory"));
		Assert.assertTrue(sandbox2 != null);

	}
	
	@Test(expected = ScriptException.class)
	public void test_with_delete_graal() throws ScriptCPUAbuseException, ScriptException {

		GraalSandbox sandbox = GraalSandboxes.create();

		sandbox.eval("Object.defineProperty(this, 'engine', {});\n" +  "Object.defineProperty(this, 'context', {});");
        sandbox.eval("delete this.__noSuchProperty__;");
		sandbox.eval("delete this.engine; this.engine.factory;");
		sandbox.eval("delete this.engine; this.engine.factory.scriptEngine.compile('var File = Java.type(\"java.io.File\"); File;').eval()");

		Assert.assertEquals(null, sandbox.eval("delete this.engine; this.engine.factory;"));

	}
	
}
