package edu.cscc.topics.tools.build;

import com.google.common.base.CaseFormat;

public class ConstantToCamel {
    private ConstantToCamel() {
        /* No public constructor */
    }

    public static void main(String[] args) {
        System.out.println(convert(args[0]));
    }

    public static String convert(String s) {
        return CaseFormat.UPPER_UNDERSCORE.converterTo(CaseFormat.LOWER_CAMEL).convert(s);
    }

}
