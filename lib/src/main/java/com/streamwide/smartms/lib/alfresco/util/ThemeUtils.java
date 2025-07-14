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

import android.app.Activity;

import androidx.annotation.NonNull;

public class ThemeUtils {


    public static void changeToTheme(@NonNull Activity activity, int theme) {

        activity.setTheme(theme);
    }
}
