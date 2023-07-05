package delight.graaljssandbox;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.fail;

import java.util.concurrent.Executors;

import javax.script.ScriptException;

import org.junit.Ignore;
import org.junit.Test;

import delight.nashornsandbox.NashornSandbox;
import delight.nashornsandbox.exceptions.BracesException;
import delight.nashornsandbox.exceptions.ScriptCPUAbuseException;
import delight.nashornsandbox.exceptions.ScriptMemoryAbuseException;

public class TestMemoryLimit {
	private static final int MEMORY_LIMIT = 1500 * 1024;

	@Test
	public void test_graal() throws ScriptCPUAbuseException, ScriptException {
		final GraalSandbox sandbox = GraalSandboxes.create();
		try {
			sandbox.setMaxMemory(MEMORY_LIMIT);
			sandbox.setExecutor(Executors.newSingleThreadExecutor());
			final String js = "var o={},i=0; while (true) {o[i++] = 'abc'}";
			sandbox.eval(js);
			fail("Exception should be thrown");
		} catch (final ScriptMemoryAbuseException e) {
			assertFalse(e.isScriptKilled());
		} finally {
			sandbox.getExecutor().shutdown();
		}
	}

	@Test(expected = BracesException.class)
	public void test_noexpectedbraces_graal() throws ScriptCPUAbuseException, ScriptException {
		final GraalSandbox sandbox = GraalSandboxes.create();
		try {
			sandbox.setMaxMemory(MEMORY_LIMIT);
			sandbox.setExecutor(Executors.newSingleThreadExecutor());
			final String js = "var o={},i=0; while (true) o[i++] = 'abc'";
			sandbox.eval(js);
			fail("Exception should be thrown");
		} finally {
			sandbox.getExecutor().shutdown();
		}
	}

	@Test
	public void test_killed_graal() throws ScriptCPUAbuseException, ScriptException {
		final GraalSandbox sandbox = GraalSandboxes.create();
		try {
			sandbox.setMaxMemory(MEMORY_LIMIT);
			sandbox.setExecutor(Executors.newSingleThreadExecutor());
			sandbox.allowNoBraces(true);
			final String js = "var o={},i=0; while (true) o[i++] = 'abc'";
			sandbox.eval(js);
			fail("Exception should be thrown");
		} catch (final ScriptMemoryAbuseException e) {
			assertFalse(e.isScriptKilled()); // thread.interrupt() works on GraalVM, no forceful script kill needed -
												// see https://www.graalvm.org/docs/reference-manual/embed/
		} finally {
			sandbox.getExecutor().shutdown();
		}
	}

	@Test
	public void test_no_abuse_graal() throws ScriptCPUAbuseException, ScriptException {
		final GraalSandbox sandbox = GraalSandboxes.create();
		try {
			sandbox.setMaxMemory(MEMORY_LIMIT);
			sandbox.setExecutor(Executors.newSingleThreadExecutor());
			final String js = "var o={},i=0; while(i<10) {o[i++] = 'abc';}";
			sandbox.eval(js);
		} catch (final Exception e) {
			throw new RuntimeException("No exception should be thrown", e);
		} finally {
			sandbox.getExecutor().shutdown();
		}
	}
}
