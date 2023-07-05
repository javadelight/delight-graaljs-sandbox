package delight.graaljssandbox;

import java.util.concurrent.Executors;

import javax.script.ScriptException;

import org.junit.Assert;
import org.junit.Test;

 
import delight.nashornsandbox.NashornSandboxes;
import delight.nashornsandbox.exceptions.ScriptCPUAbuseException;

public class TestManyEvalsAndInjections {

	@Test
	public void test_graal() throws ScriptCPUAbuseException, ScriptException {
		final GraalSandbox sandbox = GraalSandboxes.create();
		sandbox.inject("num", Integer.valueOf(10));
		sandbox.eval("res = num + 1;");
		Assert.assertEquals(Integer.valueOf(11), sandbox.get("res"));
		sandbox.inject("str", "20");
		sandbox.eval("res = num + str;");
		Assert.assertEquals("1020", sandbox.get("res"));
		final GraalSandbox sandboxInterruption = GraalSandboxes.create();
		sandboxInterruption.setMaxCPUTime(50);
		sandboxInterruption.setExecutor(Executors.newSingleThreadExecutor());
		sandboxInterruption.eval("res = 1;");
		sandboxInterruption.inject("num", Integer.valueOf(10));
		sandboxInterruption.eval("res = num + 1;");
		Assert.assertTrue(Double.valueOf(11).equals(sandboxInterruption.get("res")) || Integer.valueOf(11).equals(sandboxInterruption.get("res")));
		sandboxInterruption.inject("str", "20");
		sandboxInterruption.eval("res = num + str;");
		Assert.assertEquals("1020", sandboxInterruption.get("res"));
	}
}
