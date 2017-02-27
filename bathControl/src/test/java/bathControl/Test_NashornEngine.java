package bathControl;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.FileReader;

import org.junit.Before;
import org.junit.Test;
import javax.script.*;

public class Test_NashornEngine {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {

		// create a script engine manager
		ScriptEngineManager factory = new ScriptEngineManager();
		// create a Nashorn script engine
		ScriptEngine engine = factory.getEngineByName("nashorn");
		// evaluate JavaScript statement

		try {
			engine.eval(new FileReader("/home/joern/bathControl.js"));

			Invocable invocable = (Invocable) engine;

			Object result = invocable.invokeFunction("zuluft",20, 25);

			System.out.println("**"+result);
			
		} catch (final ScriptException | NoSuchMethodException | FileNotFoundException se) {
			se.printStackTrace();
		}

	}

}
