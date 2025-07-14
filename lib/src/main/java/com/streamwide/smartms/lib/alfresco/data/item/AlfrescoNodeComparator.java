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

package com.streamwide.smartms.lib.alfresco.data.item;

import androidx.annotation.Nullable;

import java.util.Comparator;

import org.alfresco.mobile.android.api.model.Node;

public class AlfrescoNodeComparator implements Comparator<Node> {

    @Override
    public int compare(@Nullable Node nodeA, @Nullable Node nodeB)
    {
        if (nodeA == null || nodeB == null) {
            return 0;
        }

        if ((nodeA.isDocument() && nodeB.isDocument()) || (nodeA.isFolder() && nodeB.isFolder())) {
            return 0;
        }

        if (nodeA.isFolder()) {
            return -1;
        } else if (nodeB.isFolder()) {
            return 1;
        }

        return 0;
    }
}
