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
import java.util.Map;

import com.streamwide.smartms.lib.alfresco.logger.Logger;
import com.streamwide.smartms.lib.alfresco.util.Utility;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by ahmed on 17/02/16.
 */
public abstract class StandardDialogFragment extends DialogFragment {

    private static final String CLASS_NAME = "StandardDialogFragment";
    public static final String TAG = "STANDARD_DIALOG_FRAGMENT";

    protected static @NonNull Map<String, Object> dialogParams = new HashMap<>();
    protected @Nullable ViewGroup mContainer;
    protected @Nullable View mView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, 0);
    }

    @Override
    @NonNull
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        Utility.hideSoftInput(getActivity());
        setContainer(container);
        View view = inflater.inflate(getLayout(), container, false);
        setView(view);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        initView(view);
        bindView();
        return view;
    }

    public static boolean checkVisibilityDialogFragmentByTag(@NonNull Activity activity, @NonNull String dialogTag)
    {
        Fragment dialogFragment = activity.getFragmentManager().findFragmentByTag(dialogTag);

        return isVisible(dialogFragment);
    }

    @Override
    public void show(@NonNull FragmentManager manager, @NonNull String tag)
    {
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        ft.commitAllowingStateLoss();
    }

    private static boolean isVisible(Fragment dialog)
    {
        return (dialog != null && dialog.isHidden() == false && dialog.isAdded());
    }

    public static boolean dismissVisibleDialogFragmentByTag(@Nullable Activity activity, @NonNull String dialogTag)
    {
        if (activity == null) {
            Logger.warn(CLASS_NAME, "activity is null");
            return false;
        }
        Fragment dialogFragment = activity.getFragmentManager().findFragmentByTag(dialogTag);
        if (dialogFragment != null && isVisible(dialogFragment)) {
            DialogFragment dialog = (DialogFragment) dialogFragment;
            dialog.dismissAllowingStateLoss();
            return true;
        }

        return false;
    }

    public void setContainer(@NonNull ViewGroup container)
    {
        mContainer = container;
    }

    public @Nullable ViewGroup getContainer()
    {
        return mContainer;
    }

    public void setView(@NonNull View view)
    {

        mView = view;
    }

    public @Nullable View getView()
    {
        return mView;
    }

    public abstract @NonNull StandardDialogFragment newInstance(@NonNull DialogParamsBuilder paramsMap);

    public abstract void initView(@NonNull View view);

    public abstract void bindView();

    public abstract int getLayout();
}
