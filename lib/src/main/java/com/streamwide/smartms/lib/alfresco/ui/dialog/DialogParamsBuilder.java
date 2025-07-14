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

package com.streamwide.smartms.lib.alfresco.ui.dialog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.widget.BaseAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by ahmed on 19/02/16.
 */
public class DialogParamsBuilder {

    public static final String TAG = "STANDARD_DIALOG_FRAGMENT";

    public static final String TITLE = "title";
    public static final String CONTENT = "content";
    public static final String POSITIVE_RES_ID = "positive_res_id";
    public static final String NEGATIVE_RES_ID = "negative_res_id";
    public static final String POSITIVE_TEXT = "positive_text";
    public static final String NEGATIVE_TEXT = "negative_text";
    public static final String HINT_TEXT = "hint_text";
    public static final String MAX_CHARACTER_NBR = "max_character_number";
    public static final String WITH_ASK_AGAIN_OPTION = "with_ask_again_option";
    public static final String ERROR = "error";
    public static final String OPTIONS_LIST = "options_list";
    public static final String OPTIONS_LIST_ITEM = "options_list_item";
    public static final String OPTIONS_LIST_ADAPTER = "options_list_adapter";
    public static final String DEFAULT_POSITION = "default_position";
    public static final String FILE_SIZE = "file_size";
    public static final String ALERTER = "alerter";
    public static final String ALERT_TYPE = "alert_type";
    public static final String ALERT_MESSAGE = "alert_message";
    public static final String ASK_AGAIN_OPTION_TEXT = "ask_again_option_text";

    public static final String ON_CHECK_CHANGED = "check_changed";

    private Map<String, Object> mParamsMap = new HashMap<>();

    public @NonNull Map<String, Object> getParams()
    {
        return mParamsMap;
    }

    public void setTitle(@Nullable String title)
    {
        mParamsMap.put(TITLE, title);
    }

    public void setError(@NonNull String error)
    {
        mParamsMap.put(ERROR, error);
    }

    public void setContent(@Nullable String content)
    {
        mParamsMap.put(CONTENT, content);
    }

    public void setPositiveResId(int positiveResId)
    {
        mParamsMap.put(POSITIVE_RES_ID, positiveResId);
    }

    public void setNegativeResId(int negativeResId)
    {
        mParamsMap.put(NEGATIVE_RES_ID, negativeResId);
    }

    public void setPositiveString(@NonNull String positiveString)
    {
        mParamsMap.put(POSITIVE_TEXT, positiveString);
    }

    public void setNegativeString(@NonNull String negativeString)
    {
        mParamsMap.put(NEGATIVE_TEXT, negativeString);
    }

    public void setHintResId(@NonNull String hint)
    {
        mParamsMap.put(HINT_TEXT, hint);
    }

    public void setMaxCharacterNbr(int maxCharacterNbr)
    {
        mParamsMap.put(MAX_CHARACTER_NBR, maxCharacterNbr);
    }

    public void setWithAskAgainOption(boolean withAskAgainOption)
    {
        mParamsMap.put(WITH_ASK_AGAIN_OPTION, withAskAgainOption);
    }

    public void setOptionsList(@NonNull List<String> optionsList)
    {
        mParamsMap.remove(OPTIONS_LIST_ITEM);
        mParamsMap.put(OPTIONS_LIST, optionsList);
    }

    public void setOptionsListAdapter(@NonNull BaseAdapter adapter)
    {
        mParamsMap.put(OPTIONS_LIST_ADAPTER, adapter);
    }

    public void setDefaultPosition(int defaultPosition)
    {
        mParamsMap.put(DEFAULT_POSITION, defaultPosition);
    }

    public void setFileSize(long fileSize)
    {
        mParamsMap.put(FILE_SIZE, fileSize);
    }

    public void setAlerter(@NonNull String alerterPhoneNumber)
    {
        mParamsMap.put(ALERTER, alerterPhoneNumber);
    }

    public void setAlertType(@NonNull String alertType)
    {
        mParamsMap.put(ALERT_TYPE, alertType);
    }

    public void setAlertMessage(@NonNull String alertMessage)
    {
        mParamsMap.put(ALERT_MESSAGE, alertMessage);
    }

    public void setcheckChanged(@NonNull Boolean checkChanged)
    {
        mParamsMap.put(ON_CHECK_CHANGED, checkChanged);
    }

    public void setAskAgainOptionText(int askAgainOptionText)
    {
        mParamsMap.put(ASK_AGAIN_OPTION_TEXT, askAgainOptionText);
    }
}
