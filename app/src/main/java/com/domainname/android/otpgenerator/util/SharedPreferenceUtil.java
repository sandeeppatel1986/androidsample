package com.domainname.android.otpgenerator.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Map;

/**
 * Creates SharedPreference for the application. and provides access to it
 */
public class SharedPreferenceUtil {

    private static SharedPreferences sharedPreferences = null;

    private static SharedPreferences.Editor editor = null;

    /**
     * Initialize the SharedPreferences instance for the app.
     * This method must be called before using any
     * other methods of this class.
     *
     * @param mContext {@link Context}
     */
    @SuppressLint("CommitPrefEdits")
    public static void init(Context mContext) {

        if (sharedPreferences == null) {
            sharedPreferences = PreferenceManager
                    .getDefaultSharedPreferences(mContext);
            editor = sharedPreferences.edit();
        }

    }

    /**
     * Puts new Key and its Values into SharedPreference map.
     *
     * @param key   : key for Preference value
     * @param value : value for Preference value
     */
    public static void putValue(String key, float value) {
        editor.putFloat(key, value);
        editor.commit();

    }

    /**
     * Puts new Key and its Values into SharedPreference map.
     *
     * @param key   : key for Preference value
     * @param value : value for Preference value
     */
    public static void putValue(String key, String value) {
        editor.putString(key, value);
        editor.commit();

    }

    /**
     * Puts new Key and its Values into SharedPreference map.
     *
     * @param key   : key for Preference value
     * @param value : value for Preference value
     */
    public static void putValue(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }

    /**
     * Puts new Key and its Values into SharedPreference map.
     *
     * @param key   : key for Preference value
     * @param value : value for Preference value
     */
    public static void putValue(String key, long value) {
        editor.putLong(key, value);
        editor.commit();
    }

    /**
     * Puts new Key and its Values into SharedPreference map.
     *
     * @param key   : key for Preference value
     * @param value : value for Preference value
     */
    public static void putValue(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }

    /**
     * Remove specific Preference value from db
     *
     * @param key : Key for remove Preference value
     */
    public static boolean remove(String key) {
        editor.remove(key);
        return editor.commit();
    }

    /**
     * returns a values associated with a Key default value ""
     *
     * @return String
     */
    public static String getString(String key, String defValue) {
        return sharedPreferences.getString(key, defValue);
    }


    /**
     * returns a values associated with a Key default value ""
     *
     * @return String
     */
    public static float getFloat(String key, float defValue) {
        return sharedPreferences.getFloat(key, defValue);
    }


    /**
     * returns a values associated with a Key default value -1
     *
     * @return String
     */
    public static int getInt(String key, int defValue) {
        return sharedPreferences.getInt(key, defValue);
    }

    /**
     * returns a values associated with a Key default value -1
     *
     * @return String
     */
    public static long getLong(String key, long defValue) {
        return sharedPreferences.getLong(key, defValue);
    }

    /**
     * returns a values associated with a Key default value false
     *
     * @return String
     */
    public static boolean getBoolean(String key, boolean defValue) {
        return sharedPreferences.getBoolean(key, defValue);
    }

    /**
     * Checks if key is exist in SharedPreference
     *
     * @param key : Checks if key is exist in SharedPreference
     * @return boolean
     */
    public static boolean contains(String key) {
        return sharedPreferences.contains(key);
    }

    /**
     * returns map of all the key value pair available in SharedPreference
     *
     * @return Map
     */
    public static Map<String, ?> getAll() {
        return sharedPreferences.getAll();
    }

    /**
     * clear all available sharedPreference
     */
    public static void clearAll() {
        editor.clear().commit();
    }


}
