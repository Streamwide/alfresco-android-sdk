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

import java.util.HashMap;
import java.util.Map;

import com.streamwide.smartms.lib.alfresco.R;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;



public class AttachmentUtil {

    private static final String CLASS_NAME = "AttachmentUtil";
    /** message attachment request code used for start activity for result */

    private static final Map<String, Integer> IconFilekeyMap = new HashMap<>();

    public static @NonNull
    Map<String, Integer> getIconFilekeyMap()
    {
        return IconFilekeyMap;
    }

    /**
     * populates the mapping list: full mime type --> icon.
     */
    static {
        // pdf icon
        IconFilekeyMap.put("application/pdf", R.drawable.icon_pdf_attachment);

        // word icon
        IconFilekeyMap.put("application/msword", R.drawable.icon_word_attachment);
        IconFilekeyMap.put("application/vnd.openxmlformats-officedocument.wordprocessingml.document", R.drawable.icon_word_attachment);
        IconFilekeyMap.put("application/vnd.ms-word.document.macroEnabled.12", R.drawable.icon_word_attachment);
        IconFilekeyMap.put("application/vnd.ms-word.template.macroEnabled.12", R.drawable.icon_word_attachment);
        IconFilekeyMap.put("application/vnd.oasis.opendocument.text", R.drawable.icon_word_attachment);
        IconFilekeyMap.put("application/vnd.oasis.opendocument.text-master", R.drawable.icon_word_attachment);
        IconFilekeyMap.put("application/vnd.oasis.opendocument.text-template", R.drawable.icon_word_attachment);
        IconFilekeyMap.put("application/vnd.oasis.opendocument.text-web", R.drawable.icon_word_attachment);
        IconFilekeyMap.put("application/vnd.openxmlformats-officedocument.wordprocessingml.document", R.drawable.icon_word_attachment);
        IconFilekeyMap.put("application/vnd.openxmlformats-officedocument.wordprocessingml.template", R.drawable.icon_word_attachment);

        // powerpoint icon
        IconFilekeyMap.put("application/vnd.openxmlformats-officedocument.presentationml.presentation",
                        R.drawable.icon_powerpoint_attachment);
        IconFilekeyMap.put("application/vnd.ms-powerpoint", R.drawable.icon_powerpoint_attachment);
        IconFilekeyMap.put("application/mspowerpoint", R.drawable.icon_powerpoint_attachment);
        IconFilekeyMap.put("application/vnd.ms-powerpoint.addin.macroEnabled.12",
                        R.drawable.icon_powerpoint_attachment);
        IconFilekeyMap.put("application/vnd.ms-powerpoint.presentation.macroEnabled.12",
                        R.drawable.icon_powerpoint_attachment);
        IconFilekeyMap.put("application/vnd.ms-powerpoint.slideshow.macroEnabled.12",
                        R.drawable.icon_powerpoint_attachment);
        IconFilekeyMap.put("application/vnd.ms-powerpoint.template.macroEnabled.12",
                        R.drawable.icon_powerpoint_attachment);
        IconFilekeyMap.put("application/vnd.oasis.opendocument.presentation", R.drawable.icon_powerpoint_attachment);
        IconFilekeyMap.put("application/vnd.oasis.opendocument.presentation-template",
                        R.drawable.icon_powerpoint_attachment);
        IconFilekeyMap.put("application/vnd.openxmlformats-officedocument.presentationml.slideshow",
                        R.drawable.icon_powerpoint_attachment);
        IconFilekeyMap.put("application/vnd.openxmlformats-officedocument.presentationml.template",
                        R.drawable.icon_powerpoint_attachment);
        IconFilekeyMap.put("application/x-iwork-keynote-sffkey", R.drawable.icon_powerpoint_attachment);

        // excel icon
        IconFilekeyMap.put("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", R.drawable.icon_excel_attachment);
        IconFilekeyMap.put("application/vnd.openxmlformats-officedocument.spreadsheetml.template", R.drawable.icon_excel_attachment);
        IconFilekeyMap.put("application/vnd.ms-excel", R.drawable.icon_excel_attachment);
        IconFilekeyMap.put("application/msexcel", R.drawable.icon_excel_attachment);
        IconFilekeyMap.put("application/vnd.ms-excel.addin.macroEnabled.12", R.drawable.icon_excel_attachment);
        IconFilekeyMap.put("application/vnd.ms-excel.sheet.binary.macroEnabled.12", R.drawable.icon_excel_attachment);
        IconFilekeyMap.put("application/vnd.ms-excel.sheet.macroEnabled.12", R.drawable.icon_excel_attachment);
        IconFilekeyMap.put("application/vnd.ms-excel.template.macroEnabled.12", R.drawable.icon_excel_attachment);
        IconFilekeyMap.put("application/vnd.oasis.opendocument.spreadsheet", R.drawable.icon_excel_attachment);
        IconFilekeyMap.put("application/vnd.oasis.opendocument.spreadsheet-template", R.drawable.icon_excel_attachment);
        IconFilekeyMap.put("application/x-iwork-numbers-sffnumbers", R.drawable.icon_excel_attachment);
        IconFilekeyMap.put("text/csv", R.drawable.icon_excel_attachment);

        // word rtf icon
        IconFilekeyMap.put("application/rtf", R.drawable.icon_word_rtf_attachment);
        IconFilekeyMap.put("text/rtf", R.drawable.icon_word_rtf_attachment);

        // file icon
        IconFilekeyMap.put("text/plain", R.drawable.icon_generic_text_attachment);
    }

    private void changeDrawableFillColor(Context context, int color) {

        DrawableCompat.setTint(ContextCompat.getDrawable(context, R.drawable.icon_pdf_attachment), ContextCompat.getColor(context, color));
        DrawableCompat.setTint(ContextCompat.getDrawable(context, R.drawable.icon_word_attachment), ContextCompat.getColor(context, color));
        DrawableCompat.setTint(ContextCompat.getDrawable(context, R.drawable.icon_powerpoint_attachment), ContextCompat.getColor(context, color));
        DrawableCompat.setTint(ContextCompat.getDrawable(context, R.drawable.icon_excel_attachment), ContextCompat.getColor(context, color));
        DrawableCompat.setTint(ContextCompat.getDrawable(context, R.drawable.icon_word_rtf_attachment), ContextCompat.getColor(context, color));
        DrawableCompat.setTint(ContextCompat.getDrawable(context, R.drawable.icon_generic_text_attachment), ContextCompat.getColor(context, color));
    }

}
