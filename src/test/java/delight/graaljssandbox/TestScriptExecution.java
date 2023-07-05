package delight.graaljssandbox;

import java.util.concurrent.Executors;

import javax.script.ScriptException;

import org.junit.Test;

import delight.nashornsandbox.NashornSandbox;

public class TestScriptExecution {

	@Test
	public void test_case1_graal() {
		String script = "var t1 = \"(function)\";\n" + "var person={\n" + "    \"name\": \"test\"\n" + "};\n"
				+ "print(\"t1\" + person.name);";

		GraalSandbox sandbox = GraalSandboxes.create();
		try {
			sandbox.setMaxCPUTime(6000);
			sandbox.setMaxMemory(50 * 1024 * 1024L);
			sandbox.allowNoBraces(true);
			sandbox.allowExitFunctions(true);
			sandbox.allowGlobalsObjects(true);
			sandbox.allowLoadFunctions(true);
			sandbox.allowPrintFunctions(true);
			sandbox.allowReadFunctions(true);
			sandbox.setMaxPreparedStatements(10000);
			sandbox.setExecutor(Executors.newSingleThreadExecutor());
			sandbox.eval(script);
		} catch (ScriptException e) {
			throw new RuntimeException(e);
		} finally {
			sandbox.getExecutor().shutdown();
		}
	}

	@Test
	public void test_issue_66_case2_graal() {
		String script = "var name = 'n'; " + "function a() {\n" + "}\n" + "switch (name) {\n" + "    case \"s\":\n"
				+ "    case \"n\":\n" + "}";

		GraalSandbox sandbox = GraalSandboxes.create();
		try {
			sandbox.setMaxCPUTime(6000);
			sandbox.setMaxMemory(50 * 1024 * 1024L);
			sandbox.allowNoBraces(true);
			sandbox.allowExitFunctions(true);
			sandbox.allowGlobalsObjects(true);
			sandbox.allowLoadFunctions(true);
			sandbox.allowPrintFunctions(true);
			sandbox.allowReadFunctions(true);
			sandbox.setMaxPreparedStatements(10000);
			sandbox.setExecutor(Executors.newSingleThreadExecutor());
			sandbox.eval(script);
		} catch (ScriptException e) {
			throw new RuntimeException(e);
		} finally {
			sandbox.getExecutor().shutdown();
		}
	}
}
