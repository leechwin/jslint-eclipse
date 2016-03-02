package com.googlecode.jslint4java;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextAction;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;

import com.googlecode.jslint4java.eclipse.JSLintPlugin;
import com.googlecode.jslint4java.eclipse.preferences.PreferencesInitializer;

/**
 * A utility class to check JavaScript source code for potential problems.
 * @author leechwin1@gmail.com
 * @author dom
 * @see JSLintBuilder Construction of JSLint
 */
public class JSLint {

    private final Map<Option, Object> options = new EnumMap<Option, Object>(Option.class);

    private final ContextFactory contextFactory;

    private final Function lintFunc;

    /**
     * singleton instance
     */
    protected final static JSLint instance = new JSLintBuilder().fromDefault();

    /**
     * access method for singleton object
     * @return singleton instance
     */
    public static JSLint getInstance() {
        return instance;
    }

    /**
     * Create a new {@link JSLint} object. You must pass in a {@link Function}, which is the JSLINT function defined by jslint.js. You are expected to use {@link JSLintBuilder} rather than calling
     * this constructor.
     */
    JSLint(ContextFactory contextFactory, Function lintFunc) {
        this.contextFactory = contextFactory;
        this.lintFunc = lintFunc;
    }

    /**
     * Add an option to change the behaviour of the lint. This will be passed in with a value of "true".
     * @param o Any {@link Option}.
     */
    public void addOption(Option o) {
        options.put(o, Boolean.TRUE);
    }

    /**
     * Add an option to change the behaviour of the lint. The option will be parsed as appropriate using an {@link OptionParser}.
     * @param o Any {@link Option}.
     * @param arg The value to associate with <i>o</i>.
     */
    public void addOption(Option o, String arg) {
        OptionParser optionParser = new OptionParser();
        options.put(o, optionParser.parse(o.getType(), arg));
    }

    @NeedsContext
    private JSLintResult doLint(final String javaScript) {
        return (JSLintResult) contextFactory.call(new ContextAction() {
            @SuppressWarnings("rawtypes")
            public JSLintResult run(Context cx) {
                String src = javaScript == null ? "" : javaScript;
                Object[] args = new Object[] { src, optionsAsJavaScriptObject(), optionsAsGlobalObject() };
                Map result = (Map) lintFunc.call(cx, lintFunc, null, args);
                NativeArray nativeList = (NativeArray) result.get("warnings");

                ArrayList<Issue> issueList = new ArrayList<Issue>();
                for (int i = 0; i < nativeList.getLength(); i++) {
                    NativeObject nativeObj = (NativeObject) nativeList.get(i);
                    Issue issue = new Issue(
                            ((Double) nativeObj.get("line")).intValue() + 1,
                            ((Double) nativeObj.get("column")).intValue(),
                            (String) nativeObj.get("message"),
                            (String) nativeObj.get("name"));
                    issueList.add(issue);
                }
                return new JSLintResult(issueList);
            }
        });
    }

    /**
     * Check for problems in a {@link Reader} which contains JavaScript source.
     * @param systemId a filename
     * @param reader a {@link Reader} over JavaScript source code.
     * @return a {@link JSLintResult}.
     */
    public JSLintResult lint(String systemId, Reader reader) throws IOException {
        return lint(systemId, Util.readerToString(reader));
    }

    /**
     * Check for problems in JavaScript source.
     * @param systemId a filename
     * @param javaScript a String of JavaScript source code.
     * @return a {@link JSLintResult}.
     */
    public JSLintResult lint(String systemId, String javaScript) {
        // This is synchronized, even though Rhino is thread safe, because we have multiple
        // accesses to the scope, which store state in between them. This synchronized block
        // is slightly larger than I would like, but in practical terms, it doesn't make much
        // difference. The cost of running lint is larger than the cost of pulling out the
        // results.
        synchronized (this) {
            return doLint(javaScript);
        }
    }

    /**
     * Turn the set of options into a JavaScript object, where the key is the name of the option and the value is true.
     */
    @NeedsContext
    private Scriptable optionsAsJavaScriptObject() {
        return (Scriptable) contextFactory.call(new ContextAction() {
            public Object run(Context cx) {
                // default option setting for Activator
                // applyDefaultOptions();
                Scriptable opts = cx.newObject(lintFunc);
                for (Entry<Option, Object> entry : options.entrySet()) {
                    String key = entry.getKey().getLowerName();
                    // Use our "custom" version in order to get native arrays.
                    Object value = Util.javaToJS(entry.getValue(), opts);
                    opts.put(key, opts, value);
                }
                return opts;
            }
        });
    }

    @NeedsContext
    private Scriptable optionsAsGlobalObject() {
        return (Scriptable) contextFactory.call(new ContextAction() {
            public Scriptable run(Context cx) {
                List<Object> globalVariableList = new ArrayList<Object>();
                String predefinedStrings = JSLintPlugin.getDefault().getPreferenceStore().getString(PreferencesInitializer.PREDEF_ID);
                String[] predefinedGlobalVariables = Util.split(predefinedStrings.replaceAll(" ", ""), ",");
                // An array of strings containing global variables that the file is allowed readonly access.
                for (String globalVariable : predefinedGlobalVariables) {
                    globalVariableList.add(Context.toObject(globalVariable, lintFunc));
                }
                return cx.newArray(lintFunc, globalVariableList.toArray());
            }
        });
    }

    /**
     * Clear out all options that have been set with {@link #addOption(Option)}.
     */
    public void resetOptions() {
        options.clear();
    }

}
