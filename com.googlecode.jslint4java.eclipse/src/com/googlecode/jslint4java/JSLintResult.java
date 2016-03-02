package com.googlecode.jslint4java;

import java.util.ArrayList;
import java.util.List;

/**
 * The result of a JSLint run.
 * @author leechwin1@gmail.com
 */
public class JSLintResult {

    private final List<Issue> issues = new ArrayList<Issue>();

    JSLintResult(List<Issue> issues) {
        this.issues.addAll(issues);
    }

    /**
     * Return a list of all issues that JSLint found with this source code.
     */
    public List<Issue> getIssues() {
        return issues;
    }

}
