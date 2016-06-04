package com.lgp5.fw.controllers;

import java.util.prefs.Preferences;

/**
 * Saves the preferred values of the user in relation to their settings (ie, history period, option to record history or not, etc)
 */
public class SettingsPreferences {
    private static Preferences prefs;
    private static String recordHistory = "recordHistory";
    private static String neverDelete = "neverDelete";
    private static String daysOfHistory = "daysOfHistory";

    public SettingsPreferences() {
        prefs = Preferences.userRoot().node(this.getClass().getName());
    }

    public void setRecordHistoryPreference(Boolean status) {
        prefs.putBoolean(recordHistory, status);
    }

    public Boolean getRecordHistoryPreference() {
        return prefs.getBoolean(recordHistory, true);
    }

    public void setNeverDeletePreference(Boolean status) {
        prefs.putBoolean(neverDelete, status);
    }

    public Boolean getNeverDeletePreference() {
        return prefs.getBoolean(neverDelete, false);
    }

    public void setDaysOfHistoryPreference(int days) {
        prefs.putInt(daysOfHistory, days);
    }

    public int getDaysOfHistoryPreference() {
        return prefs.getInt(daysOfHistory, 120);
    }

    public void reset() {
        prefs.remove(daysOfHistory);
        prefs.remove(neverDelete);
        prefs.remove(recordHistory);
    }
}
