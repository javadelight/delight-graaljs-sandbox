package delight.graaljssandbox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;

import javax.script.ScriptException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

 
import delight.nashornsandbox.exceptions.BracesException;
import delight.nashornsandbox.exceptions.ScriptCPUAbuseException;

public class TestAdvancedScriptParsing {
		
	Logger loggerGraal;
	GraalSandbox sandboxGraal;


	public static class Logger {

		List<String> msgs = new ArrayList<String>();

		public void debug(String msg) {
			msgs.add(msg);
		}

		public String getOutput() {
			return Arrays.toString(msgs.toArray());
		}
	}
	
	@Before
	public void setUp() {

		sandboxGraal = GraalSandboxes.create();
		sandboxGraal.setMaxCPUTime(100); // in millis
		sandboxGraal.setMaxMemory(1000 * 1000 * 100); // 100 MB for GraalVM
		sandboxGraal.allowNoBraces(false);
		sandboxGraal.allowPrintFunctions(true);
		sandboxGraal.setMaxPreparedStatements(30);
		sandboxGraal.setExecutor(Executors.newSingleThreadExecutor());
		loggerGraal = new Logger();
		sandboxGraal.inject("logger", loggerGraal);
	}
	
	@Test
	public void test_Scenario1_graal() throws ScriptCPUAbuseException, ScriptException {
		String js = "";
		js += "function main(){\n";
		js += "	for(var i=0;i<2;i++)\n";
		js += "	logger.debug('loop cnt-'+i);\n";
		js += "}\n";
		js += "function main2(){\n";
		js += "}\n";
		js += "main();\n";

		sandboxGraal.eval(js);

		Assert.assertTrue(loggerGraal.getOutput().contains("loop cnt-0"));

	}
	
	@Test
	public void test_Scenario2_graal() throws ScriptCPUAbuseException, ScriptException {
		String js = "";
		js += "function main(){\n" + "logger.debug(\"... In fun1()....\");\n" + "for(var i=0;i<2;i++)//{\n"
				+ "logger.debug(\"loop cnt-\"+i);\n" + "}\n" + "main();";
		
		
		
		Throwable ex = null;
		try {
			sandboxGraal.eval(js);
		} catch (Throwable t) {
			ex = t;
		}

		Assert.assertTrue(ex instanceof BracesException);

	}
	
	@Test
	public void test_Scenario3_graal() throws ScriptCPUAbuseException, ScriptException {
		String js = "";
		js += "function loopTest(){\n" + "var i=0;\n" + "do{\n" + "logger.debug(\"loop cnt=\"+(++i));\n"
				+ "}while(i<11)\n" + "}\n" + "loopTest();";

		sandboxGraal.eval(js);

		Assert.assertTrue(loggerGraal.getOutput().contains("loop cnt=6"));

	}
	
	@Test
	public void test_Scenario3_2_graal() throws ScriptCPUAbuseException, ScriptException {
    String js = "//simple do-while loop for demo\n";
		js += "function loopTest(){\n" +
    "var i=0;\n" +
				"do{\n" +
    "logger.debug(\"loop cnt=\"+(++i));\n"
				+ "}while(i<11);" + "}\n" +
    "loopTest();";

		
		sandboxGraal.eval(js);

		Assert.assertTrue(loggerGraal.getOutput().contains("loop cnt=6"));

	}
	
	@Test
	public void test_Scenario4_graal()  {
		String js = "";
		js += "if(srctable.length) srctable.length = 0;__if();\n" + "else {\n" + "for(var key in srctable) {__if();\n"
				+ "delete srctable[key];\n" + "}\n" + "}";

		Throwable ex = null;
		try {
			sandboxGraal.eval(js);
		} catch (Throwable t) {
			ex = t;
		}

		Assert.assertTrue(ex instanceof IllegalArgumentException);

	}
	
	@Test(expected=ScriptCPUAbuseException.class)
	public void test_Scenario5_graal() {
		String js = "";
		js += "function loopTest(){\n" + 
				"var i=0;\n" + 
				"do{\n" + 
				"i++;\n" + 
				"}while(true){\n" + 
				"}}\n" + 
				"loopTest();";

		Throwable ex = null;
		try {
			sandboxGraal.eval(js);
		}
			catch (ScriptException ex2) {
				throw new RuntimeException(ex2);
			}
		// } catch (Throwable t) {
		// 	ex = t;
		// }
		
		// Assert.assertTrue("Did not expect "+ex.getClass(), ex instanceof ScriptCPUAbuseException);

	}
	
	@After
	public void tearDown() {
		sandboxGraal.getExecutor().shutdown();
	}

	
}
