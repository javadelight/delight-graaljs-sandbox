package delight.graaljssandbox;

import delight.graaljssandbox.internal.GraalSandboxImpl;
import delight.nashornsandbox.NashornSandbox;

/**
 * The sandbox factory for GraalJS.
 * 
 * @author marcoellwanger
 */
public class GraalSandboxes {
	
	public static NashornSandbox create() {
		return new GraalSandboxImpl();
	}
	
	public static NashornSandbox create(String... args) {
		return new GraalSandboxImpl(args);
	}
}
