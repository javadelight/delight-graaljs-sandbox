package delight.graaljssandbox;

import delight.graaljssandbox.internal.GraalSandboxImpl;

/**
 * The sandbox factory for GraalJS.
 * 
 * @author marcoellwanger
 */
public class GraalSandboxes {
	
	public static GraalSandbox create() {
		return new GraalSandboxImpl();
	}
	
	public static GraalSandbox create(String... args) {
		return new GraalSandboxImpl(args);
	}
}
