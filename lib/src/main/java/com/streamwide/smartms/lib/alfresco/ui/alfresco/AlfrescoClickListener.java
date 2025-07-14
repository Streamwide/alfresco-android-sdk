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
 * @lastModifiedOn jeu., 5 mars 2020 11:46:23 +0100
 */
package com.streamwide.smartms.lib.alfresco.ui.alfresco;

import android.view.View;

import androidx.annotation.NonNull;

public interface AlfrescoClickListener {

    void onAlfrescoItemClick(@NonNull View view, int position);
}
