package edu.cscc.topics.tools.build;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ConstantToCamelTest {
    @Test
    public void replacesJavaStyleConstantWithCamelCase() {
        assertEquals("thisIsAConstant", ConstantToCamel.convert("THIS_IS_A_CONSTANT"));
    }
}
