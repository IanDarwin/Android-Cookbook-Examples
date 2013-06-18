package com.darwinsys.server;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * This is for the benefit of Java EE (specifically JAX-RS), and is only (at present)
 * used to specify the URL mapping for this app. As the JBoss sample app states, 
 * "A class extending {@link Application} and annotated with @ApplicationPath is the 
 * Java EE 6 "no XML" approach to activating JAX-RS."
 * @author Ian Darwin
 */
@ApplicationPath("/rest")
public class MyApp extends Application {
	// Look ma! No code!
}
