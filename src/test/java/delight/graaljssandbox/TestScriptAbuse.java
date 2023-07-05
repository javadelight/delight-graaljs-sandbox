package delight.graaljssandbox;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.script.ScriptException;

import org.junit.Assert;
import org.junit.Test;

 
import delight.nashornsandbox.exceptions.ScriptCPUAbuseException;

public class TestScriptAbuse {
	
	@Test
	public void test_valid_graal() throws ScriptCPUAbuseException, ScriptException {

		String js = "var x = 1;\nwhile (true) { }\n";

		GraalSandbox sandbox = GraalSandboxes.create();
		sandbox.setMaxCPUTime(100);
		sandbox.setMaxMemory(1000 * 1000 * 100); // GraalVM needs more
		sandbox.allowNoBraces(false);
		sandbox.disallowAllClasses();

		try {
		    ExecutorService executor = Executors.newSingleThreadExecutor();
		    sandbox.setExecutor(executor);
		    sandbox.eval(js);
		    sandbox.getExecutor().shutdown();
		} catch (final Exception e) {
		    Assert.assertEquals(ScriptCPUAbuseException.class, e.getClass());
		}


	}
	
}
