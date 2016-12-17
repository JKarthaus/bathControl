	package bathControl;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;

import bsh.EvalError;
import bsh.Interpreter;
import bsh.TargetError;
import de.filiberry.bathcontrol.model.BathControlContext;

public class Test_Zuluft_ON {

	@Test
	@Ignore
	public void test() {
		try {
			Interpreter bshInterpreter = new Interpreter();

			BathControlContext bcc = new BathControlContext();
			bshInterpreter.set(BathControlContext.ID, bcc);
			bshInterpreter.set("test", 1);

			bshInterpreter.set("tempWintergarten", 30);

			bshInterpreter
					.source("/home/joern/Entwicklung/workspace_neon/bathControl/src/test/java/bathControl/rules.bsh");

			System.out.println(bshInterpreter.get("zuluft"));
			System.out.println(bshInterpreter.get("abluft"));

		} catch (TargetError e) {
			System.out.println("The script or code called by the script threw an exception: " + e.getTarget());
		} catch (EvalError e2) {
			System.out.println("There was an error in evaluating the script:" + e2);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
