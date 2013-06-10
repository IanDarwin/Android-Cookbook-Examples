package org.ehealthinnovation.server;

import java.io.PrintStream;

/**
 * A component for creating personal greetings - just to exercise Arquillian testing
 */
public class Greeter {
    public void greet(PrintStream to, String name) {
        to.println(createGreeting(name));
    }

    public String createGreeting(String name) {
        return "Hello, " + name + "!";
    }
}