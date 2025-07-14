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

import java.util.Map;

import com.streamwide.smartms.lib.alfresco.R;
import com.streamwide.smartms.lib.alfresco.constant.Constant;
import com.streamwide.smartms.lib.alfresco.logger.Logger;

import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;

/**
 * Created by streamwide on 2/17/17.
 */

public class ProgressDialogFragment extends StandardDialogFragment {

    public static final String TAG = "PROGRESS" + Constant.UNDERSCORE_SEPARATOR + StandardDialogFragment.TAG;
    private static final String CLASS_NAME = "ProgressDialogFragment";
    private String mTitle;
    private String mContent;

    private AppCompatTextView mContentTextView;
    private AppCompatTextView mTitleTextView;
    private View mViewSeparator;
    private DialogInterface.OnDismissListener mOnDismissListener;

    @Override
    @NonNull
    public ProgressDialogFragment newInstance(@NonNull DialogParamsBuilder paramsMap)
    {
        dialogParams = paramsMap.getParams();
        ProgressDialogFragment progressDialog = new ProgressDialogFragment();
        for (Map.Entry<String, Object> entry : dialogParams.entrySet()) {
            String entryKey = entry.getKey();
            Object entryValue = entry.getValue();

            if (entryKey.equals(DialogParamsBuilder.TITLE) && entryValue != null) {
                mTitle = (String) entryValue;
            } else if (entryKey.equals(DialogParamsBuilder.CONTENT) && entryValue != null) {
                mContent = (String) entryValue;
            }
        }
        return progressDialog;
    }

    @Override
    public int getLayout()
    {
        return R.layout.progress_dialog_fragment;
    }

    @Override
    public void initView(@NonNull View view)
    {
        mContentTextView = view.findViewById(R.id.progress_dialog_fragment_content);
        mTitleTextView = view.findViewById(R.id.progress_dialog_fragment_title);
        mViewSeparator = view.findViewById(R.id.progress_dialog_fragment_separator);
    }

    @Override
    public void bindView()
    {
        if (TextUtils.isEmpty(mTitle)) {
            mTitleTextView.setVisibility(View.GONE);
            mViewSeparator.setVisibility(View.GONE);

        } else {
            mTitleTextView.setText(mTitle);
        }
        if (TextUtils.isEmpty(mContent)) {
            mContentTextView.setVisibility(View.GONE);
        } else {
            mContentTextView.setText(mContent);
        }

        if (TextUtils.isEmpty(mTitle) && TextUtils.isEmpty(mContent)) {

            try {
                getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                ViewGroup viewGroup = (ViewGroup) getView().getRootView();
                if (viewGroup != null) {
                    viewGroup.setBackgroundResource(android.R.color.transparent);
                }
            } catch (Exception e) {
                Logger.error(CLASS_NAME, "Unable to set transparent background ==> ", e);
            }
        }

        if (isCancelable()) {
            getDialog().setCanceledOnTouchOutside(false);
        }
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog)
    {
        super.onDismiss(dialog);
        if (mOnDismissListener != null) {
            mOnDismissListener.onDismiss(dialog);
        }
    }

    public void setOnDismissListener(@NonNull DialogInterface.OnDismissListener dismissListener)
    {
        mOnDismissListener = dismissListener;
    }
}
