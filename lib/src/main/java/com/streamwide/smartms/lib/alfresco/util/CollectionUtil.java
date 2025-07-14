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

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CollectionUtil {

    /**
     * private constructor to hide the implicit public one.
     */
    private CollectionUtil()
    {
        // do nothing...
    }

    public static @Nullable <E> List<E> copyList(@Nullable List<E> sourceList)
    {
        return sourceList != null ? new ArrayList<>(sourceList) : null;
    }

    public static @Nullable <E> ArrayList<E> copyArrayList(@Nullable List<E> sourceList)
    {
        return sourceList != null ? new ArrayList<>(sourceList) : null;
    }

    public static @Nullable <E> Set<E> copySet(@Nullable Set<E> sourceList)
    {
        return sourceList != null ? new HashSet<>(sourceList) : null;
    }

}
