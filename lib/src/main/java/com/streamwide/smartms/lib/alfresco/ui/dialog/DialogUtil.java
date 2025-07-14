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

import com.streamwide.smartms.lib.alfresco.R;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

public class DialogUtil {

    private static DialogParamsBuilder mParamsMap = new DialogParamsBuilder();

    public static @Nullable
    ProgressDialogFragment showProgressDialog(@Nullable Activity activity, @Nullable String title, @Nullable String content,
                                              boolean isCancelable)
    {
        if (activity == null) {
            return null;
        }

        FragmentManager fragmentManager = activity.getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment prevFragment = fragmentManager.findFragmentByTag(ProgressDialogFragment.TAG);

        if (prevFragment != null) {
            ProgressDialogFragment dialog = (ProgressDialogFragment) prevFragment;

            if (dialog.getDialog() != null && prevFragment.getActivity() != null
                && activity.getLocalClassName().equals(prevFragment.getActivity().getLocalClassName())) {
                return dialog;
            }

            dialog.dismissAllowingStateLoss();
            fragmentTransaction.remove(prevFragment);
        }
        fragmentTransaction.addToBackStack(null).commitAllowingStateLoss();
        mParamsMap.setTitle(title);
        mParamsMap.setContent(content);
        ProgressDialogFragment dialog = new ProgressDialogFragment();
        dialog.newInstance(mParamsMap);
        dialog.setCancelable(isCancelable);
        dialog.show(fragmentManager, ProgressDialogFragment.TAG);
        return dialog;

    }

    public static @NonNull
    AlertDialog showSimpleDialog(@NonNull Context context, int title, int content, int positiveText,
                                 int negativeText, boolean isCancelable,
                                 @Nullable DialogInterface.OnClickListener positiveClickListener,
                                 @Nullable DialogInterface.OnClickListener negativeClickListener)
    {
        String titleString = "";
        if (title != 0) {
            titleString = context.getString(title);
        }
        String contentString = "";
        if (content != 0) {
            contentString = context.getString(content);
        }

        return showSimpleDialog(context, titleString, contentString, positiveText, negativeText, isCancelable, positiveClickListener, negativeClickListener);
    }

    public static @NonNull
    AlertDialog showSimpleDialog(@NonNull Context context, @Nullable String title, @Nullable String content, int positiveText,
                                 int negativeText, boolean isCancelable,
                                 @Nullable DialogInterface.OnClickListener positiveClickListener,
                                 @Nullable DialogInterface.OnClickListener negativeClickListener)
    {

        return showSimpleDialog(context, title, content, positiveText,
                negativeText, 0, isCancelable, positiveClickListener, negativeClickListener);
    }

    public static @NonNull
    AlertDialog showSimpleDialog(@NonNull Context context, @Nullable CharSequence title, @Nullable CharSequence content, int positiveText,
                                 int negativeText, int iconRes, boolean isCancelable,
                                 @Nullable DialogInterface.OnClickListener positiveClickListener,
                                 @Nullable DialogInterface.OnClickListener negativeClickListener)
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.Theme_SmartMSLib_Dialog);
        if (TextUtils.isEmpty(title) == false) {
            builder.setTitle(title);
        }

        if (iconRes > 0) {
            builder.setIcon(iconRes);
        }

        if (TextUtils.isEmpty(content) == false) {
            builder.setMessage(content);
        }

        builder.setCancelable(isCancelable);

        if (positiveText > 0) {
            builder.setPositiveButton(positiveText, positiveClickListener);
        }

        if (negativeText > 0) {
            builder.setNegativeButton(negativeText, negativeClickListener);
        }

        AlertDialog dialog = builder.create();
        dialog.show();

        return dialog;
    }

    public static void dismissDialogByTag(@Nullable Activity activity, @NonNull String tag)
    {

        if (activity == null) {
            return;
        }

        FragmentManager fragmentManager = activity.getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if (fragment != null) {
            DialogFragment dialog = (DialogFragment) fragment;
            dialog.dismissAllowingStateLoss();
            fragmentTransaction.remove(fragment);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }
}
