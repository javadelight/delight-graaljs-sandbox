package delight.graaljssandbox;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.script.ScriptException;

import org.junit.Test;

import delight.nashornsandbox.NashornSandbox;
import delight.nashornsandbox.exceptions.ScriptCPUAbuseException;
import junit.framework.Assert;

public class TestBooleanPassing {
	
	@Test
	public void test_graal() throws ScriptCPUAbuseException, ScriptException {
		GraalSandbox sandbox = GraalSandboxes.create();
		sandbox.setMaxCPUTime(100);
		sandbox.setMaxMemory(1000 * 1000);
		sandbox.allowNoBraces(false);
		ExecutorService executor = Executors.newSingleThreadExecutor();
		sandbox.setExecutor(executor);
		Boolean done = (Boolean) sandbox.eval("done = false;");
		Assert.assertFalse(done);

		sandbox.getExecutor().shutdown();

	}
}
