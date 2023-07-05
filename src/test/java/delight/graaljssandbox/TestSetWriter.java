package delight.graaljssandbox;

import java.io.StringWriter;

import javax.script.ScriptException;

import org.junit.Assert;
import org.junit.Test;

import delight.nashornsandbox.NashornSandbox;
import delight.nashornsandbox.exceptions.ScriptCPUAbuseException;

public class TestSetWriter {
	@Test
	public void test_graal() throws ScriptCPUAbuseException, ScriptException {
		final GraalSandbox sandbox = GraalSandboxes.create();
		sandbox.allowPrintFunctions(true);
		final StringWriter writer = new StringWriter();
		sandbox.setWriter(writer);
		sandbox.eval("print(\"Hi there!\");");
		// \n at the end of the string is not enough.
		// javascript adds an extra carrige return.
		Assert.assertTrue("Hi there!\r\n".equals(writer.toString()) || "Hi there!\n".equals(writer.toString()));
	}
}
