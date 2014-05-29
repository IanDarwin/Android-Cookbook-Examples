package com.darwinsys.server;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;

//@RunWith(Arquillian.class)
public class GreeterTest {

    //@Deployment
    public static JavaArchive createDeployment() {
        final JavaArchive jar = ShrinkWrap.create(JavaArchive.class)
            .addClass(Greeter.class)
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
        System.out.println(jar);
		return jar;
    }

    /** This is the heart of the matter: let's make sure that Arquillian
     * has told Weld to inject our dependencies.
     */
    //@Inject
    private Greeter greeter;

    //@Test @Ignore
    public void should_create_greeting() {
        Assert.assertEquals("Hello, Earthling!",
            greeter.createGreeting("Earthling"));
        greeter.greet(System.out, "Earthling");
    }
}
