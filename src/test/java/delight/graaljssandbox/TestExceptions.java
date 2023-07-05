package delight.graaljssandbox;

import java.util.concurrent.Executors;

import javax.script.ScriptException;

import org.junit.Assert;
import org.junit.Test;

import delight.nashornsandbox.NashornSandbox;
import delight.nashornsandbox.exceptions.ScriptCPUAbuseException;

@SuppressWarnings("all")
public class TestExceptions {

	@Test(expected = Exception.class)
	public void test_graal() throws ScriptCPUAbuseException, ScriptException {
		final GraalSandbox sandbox = GraalSandboxes.create();
		sandbox.eval("blah_blah_blah();");
	}

	@Test
	public void test_with_catch_graal() {
		try {
			final GraalSandbox sandbox = GraalSandboxes.create();
			sandbox.eval("blah_blah_blah();");
		} catch (final Throwable _t) {
			if (_t instanceof Throwable) {
				final Throwable t = _t;
				return;
			} else {
				throw new RuntimeException(_t);
			}
		}
		Assert.fail("Exception not thrown!");
	}

	@Test
	public void test_with_thread_graal() {
		try {
			final GraalSandbox sandbox = GraalSandboxes.create();
			sandbox.setMaxCPUTime(100);
			sandbox.setExecutor(Executors.newSingleThreadExecutor());
			sandbox.eval("blah_blah_blah();");
		} catch (final Throwable _t) {
			if (_t instanceof Throwable) {
				final Throwable t = _t;
				return;
			} else {
				throw new RuntimeException(_t);
			}
		}
		Assert.fail("Exception not thrown!");
	}

	@Test
	public void test_with_line_number_graal() {
		GraalSandbox sandbox = null;
		try {
			sandbox = GraalSandboxes.create();
			sandbox.setMaxCPUTime(5000);
			sandbox.setExecutor(Executors.newSingleThreadExecutor());
			final StringBuilder _builder = new StringBuilder();
			_builder.append("var in_the_first_line_all_good;\n");
			_builder.append("\t\t\t");
			_builder.append("var so_is_the_second;\n");
			_builder.append("\t\t\t");
			_builder.append("var and_the_third;\n");
			_builder.append("\t\t\t");
			_builder.append("blah_blah_blah();");
			sandbox.eval(_builder.toString());
		} catch (final Throwable _t) {
			if (_t instanceof Throwable) {
				final Throwable t = _t;
				sandbox.getExecutor().shutdown();
				// no line numbers reported for graal
				Assert.assertTrue(t.getMessage().contains("blah_blah_blah is not defined"));
				return;
			} else {
				throw new RuntimeException(_t);
			}
		}
		Assert.fail("Exception not thrown!");
	}

}
