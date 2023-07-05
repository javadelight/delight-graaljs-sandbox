package delight.graaljssandbox;

import java.util.ArrayList;

import javax.script.ScriptException;

import org.graalvm.polyglot.PolyglotException;
import org.junit.Test;

import delight.nashornsandbox.NashornSandbox;
import junit.framework.Assert;

public class TestClassForName {
	
	@Test
	public void test_access_granted_graal() throws Exception {
		
		final GraalSandbox sandbox = GraalSandboxes.create();
		sandbox.allow(ArrayList.class);
		sandbox.allow(Class.class);
		sandbox.eval("var ArrayList = Java.type('java.util.ArrayList');");
		sandbox.eval("var Class1 = ArrayList.class;");
		sandbox.eval("var Class2 = Java.type('java.lang.Class');");
		
		Throwable t = null;
		try {
			sandbox.eval("var Class3 = Class1.forName('java.util.ArrayList');");
		} catch (ScriptException e) {
			t = e;
		}
		Assert.assertTrue(t instanceof ScriptException);
	}
	
	@Test
	public void test_access_denied_graal() throws Exception {
		
		final GraalSandbox sandbox1 = GraalSandboxes.create();
		Throwable t = null;
		try {
			sandbox1.eval("var ArrayList = Java.type('java.util.ArrayList');");
		} catch (ScriptException e) {
			t = e;
		}
		Assert.assertEquals(PolyglotException.class, t.getCause().getClass());
		
		final GraalSandbox sandbox2 = GraalSandboxes.create();
		sandbox2.allow(String.class);
		try {
			sandbox2.eval("var String = Java.type('java.lang.String');");
			sandbox2.eval("var Class1 = String.class;");
			sandbox2.eval("var Class2 = Class1.forName('java.util.ArrayList');");
		} catch (ScriptException e) {
			t = e;
		}
		Assert.assertTrue(t instanceof ScriptException);
		
	}
	
	
}
