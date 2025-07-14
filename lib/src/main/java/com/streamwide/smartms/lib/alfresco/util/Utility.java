/*
 *
 * 	StreamWIDE (Team on The Run)
 *
 * @createdBy  AndroidTeam on Mon, 6 Jan 2025 14:59:05 +0100
 * @copyright  Copyright (c) 2025 StreamWIDE UK Ltd (Team on the Run)
 * @email      support@teamontherun.com
 *
 * 	© Copyright 2025 StreamWIDE UK Ltd (Team on the Run). StreamWIDE is the copyright holder
 * 	of all code contained in this file. Do not redistribute or
 *  	re-use without permission.
 *
 * @lastModifiedOn Mon, 6 Jan 2025 14:55:47 +0100
 */

package com.streamwide.smartms.lib.alfresco.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.streamwide.smartms.lib.alfresco.constant.Constant;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Utility {

    /**
     * check the phone has permanent menu key
     *
     * @param context
     * @return
     */
    public static boolean hasPermanentMenuKey(@NonNull Context context)
    {
        return ViewConfiguration.get(context).hasPermanentMenuKey();
    }

    /**
     * hideSoftInput
     */
    public static void hideSoftInput(@NonNull Activity context)
    {
        // check the input is active
        InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        View currentFocusedView = context.getCurrentFocus();
        if (manager.isActive() && currentFocusedView != null) {
            IBinder windowToken = currentFocusedView.getWindowToken();
            manager.hideSoftInputFromWindow(windowToken, 0);
        }
    }

    public static @Nullable String getAlfrescoFolderPath(@Nullable String rootPath)
    {
        if (TextUtils.isEmpty(rootPath)) {
            return null;
        }

        File attachmentFolder = new File(rootPath + File.separator + Constant.ALFRESCO_FOLDER_NAME);
        return getFolderPath(attachmentFolder);
    }

    /**
     * create file folder
     *
     * @return filePath
     */
    public static @NonNull String getFolderPath(@NonNull File file)
    {
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsolutePath();
    }

    /**
     * retrieve current system language
     *
     * @param resources
     * @return integer
     *         DEFAULT_LANGUAGE = -1;
     *         ENGLISH_LANGUAGE = 0;
     *         FRENSH_LANGUAGE = 1;
     *         SPANISH_LANGUAGE = 2;
     *         PORTUGUESE_LANGUAGE = 3;
     *         ENGLISH_UK_LANGUAGE = 4;
     *         ENGLISH_US_LANGUAGE = 5;
     *         CHINESE_LANGUAGE = 6;
     *         ARABIC_LANGUAGE = 7;
     */
    public static int getDefaultSystemLanguage(@NonNull Resources resources)
    {
        int currentLanguage = Constant.DEFAULT_LANGUAGE;
        Locale spanishLocale = new Locale("es", "ES");
        Locale portugueseLocale = new Locale("pt", "PT");
        Locale dutchLocale = new Locale("nl", "NL");
        Locale danishLocale = new Locale("da", "DA");

        Configuration config = new Configuration(resources.getConfiguration());
        Locale currentLocalLanguage = config.getLocales().get(0);
        if (Locale.US.equals(currentLocalLanguage)) {
            currentLanguage = Constant.ENGLISH_US_LANGUAGE;
        } else if (Locale.UK.equals(currentLocalLanguage)) {
            currentLanguage = Constant.ENGLISH_UK_LANGUAGE;
        } else if (Locale.ENGLISH.getLanguage().equals(currentLocalLanguage.getLanguage())) {
            currentLanguage = Constant.ENGLISH_LANGUAGE;
        } else if (Locale.FRENCH.getLanguage().equals(currentLocalLanguage.getLanguage())) {
            currentLanguage = Constant.FRENSH_LANGUAGE;
        } else if (spanishLocale.getLanguage().equals(currentLocalLanguage.getLanguage())) {
            currentLanguage = Constant.SPANISH_LANGUAGE;
        } else if (portugueseLocale.getLanguage().equals(currentLocalLanguage.getLanguage())) {
            currentLanguage = Constant.PORTUGUESE_LANGUAGE;
        } else if (Locale.CHINESE.getLanguage().equals(currentLocalLanguage.getLanguage())) {
            currentLanguage = Constant.CHINESE_LANGUAGE;
        } else if (Locale.GERMAN.getLanguage().equals(currentLocalLanguage.getLanguage())) {
            currentLanguage = Constant.GERMANY_LANGUAGE;
        } else if (dutchLocale.getLanguage().equals(currentLocalLanguage.getLanguage())) {
            currentLanguage = Constant.DUTCH_LANGAUGE;
        }else if (danishLocale.getLanguage().equals(currentLocalLanguage.getLanguage())) {
            currentLanguage = Constant.DANISH_LANGUAGE;
        }

        return currentLanguage;
    }

    public static @NonNull Locale getLocaleDefaultSupported()
    {
        if (Locale.getDefault().getLanguage().equals(Locale.FRENCH.getLanguage())
            || Locale.getDefault().getLanguage().equals(Constant.LOCALE_SPAIN_LANGUAGE)
            || Locale.getDefault().getLanguage().equals(Constant.LOCALE_PORTUGUESE_LANGUAGE)
            || Locale.getDefault().getLanguage().equals(Locale.CHINESE.getLanguage())
            || Locale.getDefault().getLanguage().equals(Locale.GERMAN.getLanguage())) {
            return Locale.getDefault();
        }
        return Locale.US;
    }

    /**
     *
     * @param messageDate
     * @return true if the date is today, false otherwise.
     */
    private static boolean isSameDay(Date messageDate)
    {
        Calendar messageCalendar = Calendar.getInstance();
        messageCalendar.setTime(messageDate);

        return Calendar.getInstance().get(Calendar.YEAR) == messageCalendar.get(Calendar.YEAR)
            && Calendar.getInstance().get(Calendar.DAY_OF_YEAR) == messageCalendar.get(Calendar.DAY_OF_YEAR);
    }

    private static boolean isSameYear(Date messageDate)
    {
        Calendar messageCalendar = Calendar.getInstance();
        messageCalendar.setTime(messageDate);
        return Calendar.getInstance().get(Calendar.YEAR) == messageCalendar.get(Calendar.YEAR);
    }

    public static @NonNull String getDayStyle(@NonNull Context context, @NonNull Date messageDate)
    {
        return getDayStyle(context, messageDate, true);
    }

    public static @NonNull String getDayStyle(@NonNull Context mContext, @NonNull Date messageDate, boolean displayOnLowerCase)
    {
        String dateFormat;
        boolean isSameDay = isSameDay(messageDate);
        boolean isSameYear = isSameYear(messageDate);
        int currentLanguage = getDefaultSystemLanguage(mContext.getResources());

        if (isSameDay) {

            /*
             * in case of current language is English , display am,pm
             */
            if (currentLanguage == Constant.FRENSH_LANGUAGE || currentLanguage == Constant.SPANISH_LANGUAGE
                || currentLanguage == Constant.PORTUGUESE_LANGUAGE || currentLanguage == Constant.ENGLISH_UK_LANGUAGE
                || currentLanguage == Constant.CHINESE_LANGUAGE || currentLanguage == Constant.GERMANY_LANGUAGE) {
                return format(messageDate, "HH:mm", getLocaleDefaultSupported());
            } else {

                dateFormat = format(messageDate, "hh:mm a", getLocaleDefaultSupported());
                if (displayOnLowerCase) {
                    dateFormat = dateFormat.toLowerCase(Locale.getDefault());
                }
            }
        } else {
            if (isSameYear) {
                if (currentLanguage == Constant.FRENSH_LANGUAGE || currentLanguage == Constant.SPANISH_LANGUAGE
                    || currentLanguage == Constant.PORTUGUESE_LANGUAGE
                    || currentLanguage == Constant.ENGLISH_UK_LANGUAGE || currentLanguage == Constant.CHINESE_LANGUAGE
                    || currentLanguage == Constant.GERMANY_LANGUAGE) {
                    dateFormat = format(messageDate, "HH:mm dd/MM", getLocaleDefaultSupported());
                } else {
                    dateFormat = format(messageDate, "hh:mm a MM/dd", getLocaleDefaultSupported());
                    if (displayOnLowerCase) {
                        dateFormat = dateFormat.toLowerCase(Locale.getDefault());
                    }
                }
            } else {
                if (currentLanguage == Constant.FRENSH_LANGUAGE || currentLanguage == Constant.SPANISH_LANGUAGE
                    || currentLanguage == Constant.PORTUGUESE_LANGUAGE
                    || currentLanguage == Constant.ENGLISH_UK_LANGUAGE || currentLanguage == Constant.CHINESE_LANGUAGE
                    || currentLanguage == Constant.GERMANY_LANGUAGE) {
                    dateFormat = format(messageDate, "HH:mm dd/MM/yy", getLocaleDefaultSupported());
                } else {
                    dateFormat = format(messageDate, "hh:mm a MM/dd/yy", getLocaleDefaultSupported());
                    if (displayOnLowerCase) {
                        dateFormat = dateFormat.toLowerCase(Locale.getDefault());
                    }
                }
            }
        }

        return dateFormat;
    }

    public static @NonNull String format(@Nullable Date date, @NonNull String pattern, @NonNull Locale locale)
    {
        String returnValue = "";
        try {
            if (date != null) {
                SimpleDateFormat df = new SimpleDateFormat(pattern, locale);
                returnValue = df.format(date);
            }
        } catch (Exception e) {
            return returnValue;
        }

        return (returnValue);
    }
}
