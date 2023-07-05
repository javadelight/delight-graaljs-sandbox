package delight.graaljssandbox;

import java.util.function.Function;

import javax.script.ScriptException;

import org.junit.Assert;
import org.junit.Test;

import delight.nashornsandbox.NashornSandbox;
import delight.nashornsandbox.exceptions.ScriptCPUAbuseException;

@SuppressWarnings("all")
public class TestAccessFunction {
	
	@Test
	  public void test_access_variable_graal() throws ScriptCPUAbuseException, ScriptException {
	    final GraalSandbox sandbox = GraalSandboxes.create();
	    sandbox.eval("function callMe() { return 42; };");
	    final Object _get = sandbox.get("callMe");
	    Assert.assertEquals(Integer.valueOf(42), ((Function<Object[], Object>) _get).apply(null));
	    final Object _eval = sandbox.eval("callMe");
	    Assert.assertEquals(Integer.valueOf(42), ((Function<Object[], Object>) _eval).apply(null));
	  }
}
