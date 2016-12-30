package bathControl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import bsh.EvalError;
import bsh.Interpreter;

public class Test_Beanshell {

	private Interpreter bshInterpreter;

	@Before
	public void setUp() throws Exception {
		bshInterpreter = new Interpreter();

		// -- Run the Script

		bshInterpreter.source("/home/joern/bathControl.bsh");
	}

	@Test
	public void testAllOff() {
		try {
			bshInterpreter.set("tempWintergarten", 0);
			bshInterpreter.set("tempBadezimmer", 0);
			bshInterpreter.set("feuchteBad", 0);
			assertEquals("OFF", ((String) bshInterpreter.get("zuluft")).trim().toUpperCase());
			assertEquals("OFF", ((String) bshInterpreter.get("abluft")).trim().toUpperCase());
			System.out.println("Zuluft:" + ((String) bshInterpreter.get("zuluft")).trim().toUpperCase());
			System.out.println("Abluft:" + ((String) bshInterpreter.get("abluft")).trim().toUpperCase());

		} catch (EvalError e) {
			fail(e.getMessage());
		}

	}
	@Test
	public void testAbluft() {
		try {
			bshInterpreter.set("tempWintergarten", 0);
			bshInterpreter.set("tempBadezimmer", 0);
			bshInterpreter.set("feuchteBad", 95);
			assertEquals("OFF", ((String) bshInterpreter.get("zuluft")).trim().toUpperCase());
			assertEquals("ON", ((String) bshInterpreter.get("abluft")).trim().toUpperCase());
			System.out.println("Zuluft:" + ((String) bshInterpreter.get("zuluft")).trim().toUpperCase());
			System.out.println("Abluft:" + ((String) bshInterpreter.get("abluft")).trim().toUpperCase());

		} catch (EvalError e) {
			fail(e.getMessage());
		}

	}

}
