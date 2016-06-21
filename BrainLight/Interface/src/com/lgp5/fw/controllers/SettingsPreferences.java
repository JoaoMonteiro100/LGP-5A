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
    private static Boolean recordHistoryDefault = true;
    private static Boolean neverDeleteDefault = false;
    private static int daysOfHistoryDefault = 120;

    /**
     * Constructor
     */
    public SettingsPreferences() {
        prefs = Preferences.userRoot().node(this.getClass().getName());
    }

    /**
     * Changes "Record History?" preference
     * @param status true if user wants to record history
     */
    public void setRecordHistoryPreference(Boolean status) {
        prefs.putBoolean(recordHistory, status);
    }

    /**
     * Returns "Record History?" preference
     * @return Preference, or default if none
     */
    public Boolean getRecordHistoryPreference() {
        return prefs.getBoolean(recordHistory, recordHistoryDefault);
    }

    /**
     * Changes "Never Delete History" preference
     * @param status true if user wants the history to be saved for ever
     */
    public void setNeverDeletePreference(Boolean status) {
        prefs.putBoolean(neverDelete, status);
    }

    /**
     * Returns "Never Delete History" preference
     * @return Preference, or default if none
     */
    public Boolean getNeverDeletePreference() {
        return prefs.getBoolean(neverDelete, neverDeleteDefault);
    }

    /**
     * Changes "Number of Days History is Saved for" preference
     * @param days number of days of history
     */
    public void setDaysOfHistoryPreference(int days) {
        prefs.putInt(daysOfHistory, days);
    }

    /**
     * Returns "Number of Days History is Saved for" preference
     * @return Preference, or default if none
     */
    public int getDaysOfHistoryPreference() {
        return prefs.getInt(daysOfHistory, daysOfHistoryDefault);
    }

    /**
     * Removes preferences
     */
    public void reset() {
        prefs.remove(daysOfHistory);
        prefs.remove(neverDelete);
        prefs.remove(recordHistory);
    }

    /**
     * Change the default values
     * @param recordHistoryBoolean New default value for "Record History" preference
     * @param neverDeleteBoolean New default value for "Never Delete History" preference
     * @param days New default value for "Number of Days History is Saved for" preference
     */
    public void changeDefaults(Boolean recordHistoryBoolean, Boolean neverDeleteBoolean, int days) {
        recordHistoryDefault = recordHistoryBoolean;
        neverDeleteDefault = neverDeleteBoolean;
        daysOfHistoryDefault = days;
        reset();
    }
}