package delight.graaljssandbox;

import delight.nashornsandbox.NashornSandbox;
import javax.script.Bindings;

public interface GraalSandbox extends NashornSandbox {

	public Bindings createNewBindings ();
}
