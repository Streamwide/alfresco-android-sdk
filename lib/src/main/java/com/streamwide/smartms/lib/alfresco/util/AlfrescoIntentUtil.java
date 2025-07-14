/*
 *
 * 	StreamWIDE (Team on The Run)
 *
 * @createdBy  AndroidTeam on jeu., 5 mars 2020 14:13:37 +0100
 * @copyright  Copyright (c) 2020 StreamWIDE UK Ltd (Team on the Run)
 * @email      support@teamontherun.com
 *
 * 	© Copyright 2020 StreamWIDE UK Ltd (Team on the Run). StreamWIDE is the copyright holder
 * 	of all code contained in this file. Do not redistribute or
 *  	re-use without permission.
 *
 * @lastModifiedOn jeu., 5 mars 2020 12:28:05 +0100
 */

package com.streamwide.smartms.lib.alfresco.util;

import static com.streamwide.smartms.lib.alfresco.constant.Constant.EXTRA_FOLDER_PATH;
import static com.streamwide.smartms.lib.alfresco.constant.Constant.EXTRA_SELECTED_THEME;

import com.streamwide.smartms.lib.alfresco.ui.activity.AlfrescoLoginActivity;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by streamwide on 1/19/18.
 */
public class AlfrescoIntentUtil {
    /**
     * request code for opening vcard activity
     */
    public static final int ALFRESCO_LOGIN = 0x370;

    private AlfrescoIntentUtil() {
        //Left empty
    }

    private static Class<? extends Activity> getAlfrescoLoginClass()
    {
        return AlfrescoLoginActivity.class;
    }

    public static void startAlfresco(@NonNull Activity activity, int theme, @Nullable String alfrescoFolderPath) {
        String resourceName = activity.getResources().getResourceName(theme);
        Intent intent = new Intent(activity, AlfrescoIntentUtil.getAlfrescoLoginClass());
        intent.putExtra(EXTRA_FOLDER_PATH, alfrescoFolderPath);
        intent.putExtra(EXTRA_SELECTED_THEME, resourceName);
        activity.startActivityForResult(intent, ALFRESCO_LOGIN);
    }

    public static void startAlfresco(@NonNull Activity activity, @Nullable String alfrescoFolderPath) {
        Intent intent = new Intent(activity, AlfrescoIntentUtil.getAlfrescoLoginClass());
        intent.putExtra(EXTRA_FOLDER_PATH, alfrescoFolderPath);
        activity.startActivityForResult(intent, ALFRESCO_LOGIN);
    }

    @NonNull
    public static Intent setupIntent(@NonNull Activity activity, @Nullable String alfrescoFolderPath)
    {
        Intent intent = new Intent(activity, AlfrescoIntentUtil.getAlfrescoLoginClass());
        intent.putExtra(EXTRA_FOLDER_PATH, alfrescoFolderPath);
        return intent;
    }

}
