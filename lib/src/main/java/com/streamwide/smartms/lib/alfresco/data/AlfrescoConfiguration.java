/*
 *
 * 	StreamWIDE (Team on The Run)
 *
 * @createdBy  AndroidTeam on Fri, 28 Aug 2020 14:34:44 +0100
 * @copyright  Copyright (c) 2020 StreamWIDE UK Ltd (Team on the Run)
 * @email      support@teamontherun.com
 *
 * 	© Copyright 2020 StreamWIDE UK Ltd (Team on the Run). StreamWIDE is the copyright holder
 * 	of all code contained in this file. Do not redistribute or
 *  	re-use without permission.
 *
 * @lastModifiedOn Fri, 28 Aug 2020 14:34:26 +0100
 */

package com.streamwide.smartms.lib.alfresco.data;

import com.streamwide.smartms.lib.alfresco.data.io.DefaultIoFileStrategy;
import com.streamwide.smartms.lib.alfresco.data.preferences.DefaultSharedPreferenceStrategy;
import com.streamwide.smartms.lib.template.data.SharedPreferenceStrategy;
import com.streamwide.smartms.lib.template.file.IOFileStrategy;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AlfrescoConfiguration {

    private static AlfrescoConfiguration alfrescoConfigurationInstance;

    private IOFileStrategy mIoFileStrategy;
    private SharedPreferenceStrategy mSharedPreferenceStrategy;

    private AlfrescoConfiguration()
    {
    }

    @NonNull
    public static AlfrescoConfiguration getInstance()
    {
        if (alfrescoConfigurationInstance == null) {
            alfrescoConfigurationInstance = new AlfrescoConfiguration();
        }
        return alfrescoConfigurationInstance;
    }

    public void setIoFileStrategy(@Nullable IOFileStrategy ioFileStrategy)
    {
        mIoFileStrategy = ioFileStrategy;
    }

    @NonNull
    public IOFileStrategy getIoFileStrategy()
    {
        if (mIoFileStrategy == null) {
            return new DefaultIoFileStrategy();
        }

        return mIoFileStrategy;
    }

    public void setSharedPreferenceStrategy(@Nullable SharedPreferenceStrategy sharedPreferenceStrategy)
    {
        mSharedPreferenceStrategy = sharedPreferenceStrategy;
    }

    @NonNull
    public SharedPreferenceStrategy getSharedPreferenceStrategy(@NonNull Context context)
    {
        if (mSharedPreferenceStrategy == null) {
            return new DefaultSharedPreferenceStrategy(context);
        }

        /*
         * This case is util in the migration to the 3.6.0 that apply the secure file
         * encryption by default.
         * since all alfresco prefs not really important, so we can remove all
         */
        new DefaultSharedPreferenceStrategy(context).clearAll();
        return mSharedPreferenceStrategy;
    }
}
