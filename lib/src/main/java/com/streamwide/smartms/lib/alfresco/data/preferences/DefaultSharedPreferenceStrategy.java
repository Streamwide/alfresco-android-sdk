/*
 *
 * 	StreamWIDE (Team on The Run)
 *
 * @createdBy  AndroidTeam on ven., 26 mars 2021 14:42:10 +0100
 * @copyright  Copyright (c) 2021 StreamWIDE UK Ltd (Team on the Run)
 * @email      support@teamontherun.com
 *
 * 	© Copyright 2021 StreamWIDE UK Ltd (Team on the Run). StreamWIDE is the copyright holder
 * 	of all code contained in this file. Do not redistribute or
 *  	re-use without permission.
 *
 * @lastModifiedOn ven., 26 mars 2021 14:37:06 +0100
 */

package com.streamwide.smartms.lib.alfresco.data.preferences;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import com.streamwide.smartms.lib.template.data.SharedPreferenceStrategy;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DefaultSharedPreferenceStrategy extends AlfrescoPreferences implements SharedPreferenceStrategy {

    /**
     * System preference class
     */
    private final SharedPreferences mPreferences;

    private final Context mContext;
    public DefaultSharedPreferenceStrategy(@NonNull Context context)
    {
        String sharedPreferencesFileName = context.getPackageName() + "_alfresco_preferences";
        mPreferences = context.getSharedPreferences(sharedPreferencesFileName, Context.MODE_PRIVATE);
        mContext = context;
    }

    @Override
    public void put(@NonNull String key, @Nullable Object value)
    {
        if (value instanceof Boolean) {
            putBoolean(key, (Boolean) value);
        } else if (value instanceof String) {
            putString(key, (String) value);
        } else if (value instanceof Integer) {
            putInt(key, (Integer) value);
        } else if (value instanceof Long) {
            putLong(key, (Long) value);
        } else if (value instanceof Float) {
            putFloat(key, (Float) value);
        } else if (value instanceof Double) {
            putLong(key, ((Double) value).longValue());
        }
    }

    @Override
    public void putString(@NonNull String key, @Nullable String value)
    {
        SharedPreferences.Editor edit = mPreferences.edit();
        if (value != null) {
            edit.putString(key, value.replace(DOUBLE_QUOTE_CHAR, DOUBLE_QUOTE_CHAR_REPLACEMENT));
        } else {
            edit.putString(key, null);
        }
        commitSharedPrefs(edit);
    }

    @Override
    public void putLong(@NonNull String key, long value)
    {
        SharedPreferences.Editor edit = mPreferences.edit();
        edit.putLong(key, value);
        commitSharedPrefs(edit);
    }

    @Override
    public void putInt(@NonNull String key, int value)
    {
        SharedPreferences.Editor edit = mPreferences.edit();
        edit.putInt(key, value);
        commitSharedPrefs(edit);
    }

    @Override
    public void putFloat(@NonNull String key, float value)
    {
        SharedPreferences.Editor edit = mPreferences.edit();
        edit.putFloat(key, value);
        commitSharedPrefs(edit);
    }

    @Override
    public void putBoolean(@NonNull String key, boolean value)
    {
        SharedPreferences.Editor edit = mPreferences.edit();
        edit.putBoolean(key, value);
        commitSharedPrefs(edit);
    }

    @Nullable
    @Override
    public String getString(@NonNull String key, @Nullable String defaultValue)
    {
        String value = mPreferences.getString(key, defaultValue);

        if (!TextUtils.isEmpty(value)) {
            value = value.replace(DOUBLE_QUOTE_CHAR_REPLACEMENT, DOUBLE_QUOTE_CHAR);
        }
        return value;
    }

    @Nullable
    @Override
    public Set<String> getStringSet(@NonNull String key, @Nullable Set<String> defaultValues)
    {
        return mPreferences.getStringSet(key, defaultValues);
    }

    @Override
    public int getInt(@NonNull String key, int defaultValue)
    {
        return mPreferences.getInt(key, defaultValue);
    }

    @Override
    public long getLong(@NonNull String key, long defaultValue)
    {
        return mPreferences.getLong(key, defaultValue);
    }

    @Override
    public float getFloat(@NonNull String key, float defaultValue)
    {
        return mPreferences.getFloat(key, defaultValue);
    }

    @Override
    public boolean getBoolean(@NonNull String key, boolean defaultValue)
    {
        return mPreferences.getBoolean(key, defaultValue);
    }

    @Override
    public void remove(@NonNull String key)
    {
        SharedPreferences.Editor edit = mPreferences.edit();
        edit.remove(key);
        commitSharedPrefs(edit);
    }

    private void commitSharedPrefs(SharedPreferences.Editor editor)
    {
        editor.commit();
    }

    @Override
    public boolean contains(@NonNull String key)
    {
        return mPreferences.contains(key);
    }

    @NonNull
    @Override
    public Map<String, ?> getAll()
    {
        Map<String, Object> fixedMap = new HashMap<>();
        Map<String, ?> map = mPreferences.getAll();

        if (map.isEmpty()) {
            return fixedMap;
        }

        for (Map.Entry<String, ?> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value instanceof Boolean) {
                fixedMap.put(key, getBoolean(key, false));
            } else if (value instanceof String) {
                fixedMap.put(key, getString(key, null));
            } else if (value instanceof Integer) {
                fixedMap.put(key, getInt(key, 0));
            } else if (value instanceof Long) {
                fixedMap.put(key, getLong(key, 0L));
            } else if (value instanceof Float) {
                fixedMap.put(key, getFloat(key, 0));
            } else if (value instanceof Double) {
                fixedMap.put(key, getLong(key, 0));
            } else {
                fixedMap.put(key, value);
            }
        }

        return fixedMap;
    }

    @Override
    public void clearAll()
    {
        Map<String, ?> preferences = mPreferences.getAll();

        SharedPreferences.Editor edit = mPreferences.edit();
        for (Map.Entry<String, ?> me : preferences.entrySet()) {
            String key = me.getKey();
            edit.remove(key);
        }

        commitSharedPrefs(edit);
    }

    @Override
    public void suspendedClearAll() {
        clearAll();
    }

    @NonNull
    @Override
    public String dumpSharedPrefs()
    {

        try {
            JSONObject jsonObject = new JSONObject(mPreferences.getAll());
            return jsonObject.toString();
        } catch (Exception ignored) {
            // do nothing
        }
        return "";
    }

    @Nullable
    @Override
    public Uri dumpSharedPrefs(@NonNull String fileNamePrefix) {
        String content = dumpSharedPrefs();

        if (TextUtils.isEmpty(content) || mContext == null) {
            return null;
        }


        try {
            File file = File.createTempFile("user", fileNamePrefix, mContext.getCacheDir());

            if (!file.exists() || file.length() == 0) {
                return null;
            }

            return Uri.fromFile(file);
        } catch (IOException ignored) {
            // Do nothing
        }

        return null;
    }

    @Override
    public void registerOnSharedPreferenceChangeListener(@NonNull SharedPreferences.OnSharedPreferenceChangeListener listener)
    {
        mPreferences.registerOnSharedPreferenceChangeListener(listener);
    }

    @Override
    public void unregisterOnSharedPreferenceChangeListener(@NonNull SharedPreferences.OnSharedPreferenceChangeListener listener)
    {
        mPreferences.unregisterOnSharedPreferenceChangeListener(listener);
    }
}
