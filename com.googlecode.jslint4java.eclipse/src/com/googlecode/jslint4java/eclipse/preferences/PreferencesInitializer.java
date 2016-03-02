package com.googlecode.jslint4java.eclipse.preferences;

import java.util.EnumSet;
import java.util.Set;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;

import com.googlecode.jslint4java.Option;
import com.googlecode.jslint4java.eclipse.JSLintPlugin;

/**
 * Set up the default preferences. By default,we enable:
 * <ul>
 * <li> {@link Option#BROWSER}
 * <li> {@link Option#DEVEL}
 * <li> {@link Option#ES6}
 * <li> {@link Option#FOR}
 * <li> {@link Option#NODE}
 * <li> {@link Option#THIS}
 * <li> {@link Option#WHITE}
 * </ul>
 * <p>
 * And assign default values to:
 * <ul>
 * <li> {@link Option#MAXERR}
 * <li> {@link Option#MAXLEN}
 * </ul>
 */
public class PreferencesInitializer extends AbstractPreferenceInitializer {

    public static final String PREDEF_ID = JSLintPlugin.PLUGIN_ID + ".preference.predef";
    public static final int DEFAULT_MAXERR = 50;
    public static final int DEFAULT_MAXLEN = 256;

    private final Set<Option> defaultEnable = EnumSet.of(Option.BROWSER, Option.DEVEL, Option.ES6, Option.FOR, Option.NODE, Option.THIS, Option.WHITE);

    @Override
    public void initializeDefaultPreferences() {
        IEclipsePreferences node = DefaultScope.INSTANCE.getNode(JSLintPlugin.PLUGIN_ID);
        for (Option o : defaultEnable) {
            node.putBoolean(o.getLowerName(), true);
        }
        // Hand code these.
        node.putInt(Option.MAXERR.getLowerName(), DEFAULT_MAXERR);
        node.putInt(Option.MAXLEN.getLowerName(), DEFAULT_MAXLEN);
    }

}
