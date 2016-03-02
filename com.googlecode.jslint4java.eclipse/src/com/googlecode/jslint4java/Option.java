package com.googlecode.jslint4java;

import java.util.Locale;

/**
 * All available options for tuning the behaviour of JSLint. TODO Add a "Handler" class for each type, which knows whether it needs an arg, how to parse it, etc.
 * @author dom
 * @author leechwin1@gmail.com
 */
public enum Option {
    // BEGIN-OPTIONS
    BITWISE("If bitwise operators should be allowed", Boolean.class),

    BROWSER("If the standard browser globals should be predefined", Boolean.class),

    COUCH("If Couch DB globals should be predefined", Boolean.class),

    DEVEL("If browser globals that are useful in development should be predefined", Boolean.class),

    ES6("If es6 syntax should be allowed", Boolean.class),

    EVAL("If eval should be allowed", Boolean.class),

    FOR("If the for statement should be allowed", Boolean.class),

    FUDGE("If the origin of a text file is line 1 column 1", Boolean.class),

    MAXERR("The maximum number of warnings reported", Integer.class),

    MAXLEN("The maximum number of characters in a line", Integer.class),

    MULTIVAR("If a var, let, or const statement can declare two or more variables in a single statement", Boolean.class),

    NODE("If Node.js globals should be predefined", Boolean.class),

    WHITE("If the whitespace rules should be ignored", Boolean.class),

    THIS("If this should be allowed", Boolean.class),

    PREDEF("The names of predefined global variables", String.class)
    // END-OPTIONS
    ;

    private String description;
    private Class<?> type;

    private Option(String description, Class<?> type) {
        this.description = description;
        this.type = type;
    }

    /**
     * Return a description of what this option affects.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Return the lowercase name of this option.
     */
    public String getLowerName() {
        return name().toLowerCase(Locale.getDefault());
    }

    /**
     * What type does the value of this option have?
     */
    public Class<?> getType() {
        return type;
    }

    /**
     * Calculate the maximum length of all of the {@link Option} names.
     * @return the length of the largest name.
     */
    public static int maximumNameLength() {
        int maxOptLen = 0;
        for (Option o : values()) {
            int len = o.name().length();
            if (len > maxOptLen) {
                maxOptLen = len;
            }
        }
        return maxOptLen;
    }

    /**
     * Show this option and its description.
     */
    @Override
    public String toString() {
        return getLowerName() + "[" + getDescription() + "]";
    }
}
