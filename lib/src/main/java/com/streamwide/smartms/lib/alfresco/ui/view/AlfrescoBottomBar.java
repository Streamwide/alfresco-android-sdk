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
package com.streamwide.smartms.lib.alfresco.ui.view;

import com.streamwide.smartms.lib.alfresco.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;

public class AlfrescoBottomBar extends RelativeLayout {

    private AppCompatImageButton mAlfrescoDisconnectButton;
    /** listener to handle click event */
    private AlfrescoBottomBarListener mBarListener;

    public AlfrescoBottomBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);

        initView(context);
    }

    public AlfrescoBottomBar(@NonNull Context context, @Nullable AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public AlfrescoBottomBar(@NonNull Context context)
    {
        this(context, null);
    }

    private void initView(Context context)
    {
        View.inflate(context, R.layout.alfresco_bottom_bar, this);
        mAlfrescoDisconnectButton = findViewById(R.id.alfresco_bottom_bar_disconnect);
    }

    private void initEvent()
    {
        mAlfrescoDisconnectButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (getBarListener() != null) {
                    getBarListener().onDisconnect(getAlfrescoDisconnectButton());
                }
            }
        });
    }

    public void setAlfrescoBottomBarListener(@Nullable AlfrescoBottomBarListener listener)
    {
        this.mBarListener = listener;
        initEvent();
    }

    public interface AlfrescoBottomBarListener {

        void onDisconnect(@NonNull View view);
    }


    AppCompatImageButton getAlfrescoDisconnectButton() {
        return mAlfrescoDisconnectButton;
    }

    AlfrescoBottomBarListener getBarListener() {
        return mBarListener;
    }
}
