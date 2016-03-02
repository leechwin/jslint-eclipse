package com.googlecode.jslint4java;


/**
 * A single issue with the code that is being checked for problems.
 *
 * @author leechwin1@gmail.com
 */
public class Issue {

    private final int line;
    private final int column;
    private final String message;
    private final String name;

    public Issue(int line, int column, String message, String name) {
        this.line = line;
        this.column = column;
        this.message = message;
        this.name = name;
    }

    /**
     * @return the number of the line on which this issue occurs.
     */
    public int getLine() {
        return line;
    }

    /**
     * @return the position of the issue within the line. Starts at 0.
     */
    public int getColumn() {
        return column;
    }

    /**
     * @return a textual description of this issue.
     */
    public String getMessage() {
        return message;
    }

    /**
     * @return the name of the issue type.
     */
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getLine() + ":" + getColumn() + ":" + getMessage() + ":" + getName();
    }

}
