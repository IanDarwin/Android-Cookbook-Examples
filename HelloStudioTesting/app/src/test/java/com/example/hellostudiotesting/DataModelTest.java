package com.example.hellostudiotesting;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class DataModelTest {
    @Test
    public void NameCorrect() throws Exception {
        assertEquals("Robin Good", new DataModel().getName());
    }
}