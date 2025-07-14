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

package com.streamwide.smartms.lib.alfresco.ui.activity;

import static com.streamwide.smartms.lib.alfresco.constant.Constant.EXTRA_FOLDER_PATH;
import static com.streamwide.smartms.lib.alfresco.constant.Constant.EXTRA_SELECTED_THEME;

import org.alfresco.mobile.android.api.exceptions.ErrorCodeRegistry;

import com.google.android.material.textfield.TextInputLayout;
import com.streamwide.smartms.lib.alfresco.R;
import com.streamwide.smartms.lib.alfresco.constant.Constant;
import com.streamwide.smartms.lib.alfresco.data.AlfrescoConfiguration;
import com.streamwide.smartms.lib.alfresco.data.preferences.AlfrescoPreferences;
import com.streamwide.smartms.lib.alfresco.logger.Logger;
import com.streamwide.smartms.lib.alfresco.network.http.request.AlfrescoExceptionHelper.AlfrescoConnectionErrorType;
import com.streamwide.smartms.lib.alfresco.network.http.request.AlfrescoRequestManager;
import com.streamwide.smartms.lib.alfresco.network.http.request.AlfrescoRequestManager.IConnectToAlfrescoSessionCallback;
import com.streamwide.smartms.lib.alfresco.ui.dialog.DialogUtil;
import com.streamwide.smartms.lib.alfresco.ui.dialog.ProgressDialogFragment;
import com.streamwide.smartms.lib.alfresco.util.ThemeUtils;
import com.streamwide.smartms.lib.alfresco.util.Utility;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

public class AlfrescoLoginActivity extends AppCompatActivity
    implements OnClickListener, OnCheckedChangeListener, IConnectToAlfrescoSessionCallback {

    private static final String CLASS_NAME = "AlfrescoLoginActivity";

    private EditText mAlfrescoLoginUrlEdit;
    private EditText mAlfrescoLoginUsernameEdit;
    private EditText mAlfrescoLoginPasswordEdit;
    private CheckBox mAlfrescoLoginSslCkeck;
    private AppCompatButton mAlfrescoLoginBtn;
    private AppCompatTextView mAlfrescoLoginErrorTxt;

    private String mBaseUrl;
    private String mUserName;
    private String mPassword;
    private boolean mSecuredConnection;
    private LinearLayout mAlfrescoLoginUrlContainer;
    private LinearLayout mAlfrescoLoginUsernameContainer;
    private LinearLayout mAlfrescoLoginPasswordContainer;

    private String mThemeName;
    private String mFolderPath;

    private Toolbar mToolBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // change style in runTime
        mThemeName = getSelectedTheme();
        mFolderPath = getFolderPath();
        int themeResourceIdentifier = R.style.Theme_SmartMSLib_NoActionBar_Lib;

        if (!"".equals(mThemeName))
            themeResourceIdentifier = getResources().getIdentifier(mThemeName, null, null);

        if (themeResourceIdentifier != 0)
            ThemeUtils.changeToTheme(this, themeResourceIdentifier);

        setContentView(R.layout.activity_alfresco_login);

        initView();
        initData();
        initEvent();
    }

    private String getSelectedTheme()
    {
        Intent intent = getIntent();

        if (intent == null || intent.getExtras() == null || !intent.hasExtra(EXTRA_SELECTED_THEME)) {
            return "";
        }

        return intent.getStringExtra(EXTRA_SELECTED_THEME);
    }

    private String getFolderPath()
    {
        Intent intent = getIntent();

        if (intent == null || intent.getExtras() == null || !intent.hasExtra(EXTRA_FOLDER_PATH)) {
            return "";
        }

        return intent.getStringExtra(EXTRA_FOLDER_PATH);
    }

    private void initView()
    {
        /*
         * add ToolBar
         */
        mToolBar = findViewById(R.id.activity_alfresco_toolbar);
        setSupportActionBar(mToolBar);
        final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.ic_back_icon);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(upArrow);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        mToolBar.setNavigationOnClickListener(v -> {
            Logger.info(CLASS_NAME, "Back button action bar is pressed");
            onBackPressed();
        });

        mAlfrescoLoginUrlEdit = findViewById(R.id.alfresco_login_url_edittext);
        TextInputLayout alfrescoLoginUrlTextInputLayout = findViewById(R.id.alfresco_login_url_input_layout);
        mAlfrescoLoginUsernameEdit = findViewById(R.id.alfresco_login_username_edittext);
        TextInputLayout alfrescoLoginUserNameTextInputLayout = findViewById(R.id.alfresco_login_username_input_layout);
        mAlfrescoLoginPasswordEdit = findViewById(R.id.alfresco_login_password_edittext);
        TextInputLayout alfrescoLoginPasswordTextInputLayout = findViewById(R.id.alfresco_login_password__input_layout);
        mAlfrescoLoginSslCkeck = findViewById(R.id.alfresco_login_ssl_ckeck);
        mAlfrescoLoginBtn = findViewById(R.id.alfresco_login_btn);
        mAlfrescoLoginErrorTxt = findViewById(R.id.alfresco_login_error_txt);
        mAlfrescoLoginUrlContainer = findViewById(R.id.alfresco_login_url_container);
        mAlfrescoLoginUsernameContainer = findViewById(R.id.alfresco_login_username_container);
        mAlfrescoLoginPasswordContainer = findViewById(R.id.alfresco_login_password_container);

        mAlfrescoLoginErrorTxt.setVisibility(View.INVISIBLE);
        alfrescoLoginUrlTextInputLayout.setVisibility(View.VISIBLE);
        alfrescoLoginUserNameTextInputLayout.setVisibility(View.VISIBLE);
        alfrescoLoginPasswordTextInputLayout.setVisibility(View.VISIBLE);
    }

    private void initData()
    {
        mAlfrescoLoginUrlContainer.setBackgroundResource(android.R.color.transparent);

        mAlfrescoLoginUsernameContainer.setBackgroundResource(android.R.color.transparent);

        mAlfrescoLoginPasswordContainer.setBackgroundResource(android.R.color.transparent);

        String baseUrl = AlfrescoConfiguration.getInstance().getSharedPreferenceStrategy(this).getString(
                        AlfrescoPreferences.ALFRESCO_BASE_URL, AlfrescoPreferences.Default.DEFAULT_ALFRESCO_BASE_URL);

        mAlfrescoLoginUrlEdit.setText(baseUrl);

        String username = AlfrescoConfiguration.getInstance().getSharedPreferenceStrategy(this).getString(
                        AlfrescoPreferences.ALFRESCO_USER_NAME, AlfrescoPreferences.Default.DEFAULT_ALFRESCO_USERNAME);

        mAlfrescoLoginUsernameEdit.setText(username);
        if (TextUtils.isEmpty(baseUrl)) {
            mAlfrescoLoginUrlEdit.requestFocus();
        } else if (TextUtils.isEmpty(username)) {
            mAlfrescoLoginUsernameEdit.requestFocus();
        } else {
            mAlfrescoLoginPasswordEdit.requestFocus();
        }

        mSecuredConnection = AlfrescoConfiguration.getInstance().getSharedPreferenceStrategy(this).getBoolean(
                        AlfrescoPreferences.ALFRESCO_SSL_CONNECTION,
                        AlfrescoPreferences.Default.DEFAULT_ALFRESCO_SSL_CONNECTION);

        mAlfrescoLoginSslCkeck.setChecked(mSecuredConnection);

        mAlfrescoLoginPasswordEdit.setText("");
    }

    private void initEvent()
    {
        mAlfrescoLoginBtn.setOnClickListener(this);
        mAlfrescoLoginSslCkeck.setOnCheckedChangeListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            if (requestCode == Constant.RequestCode.ALFRESCO_REPOSITORY) {
                if (resultCode == RESULT_OK) {
                    setResult(RESULT_OK, data);
                    finish();
                }
            }
        }
    }

    private boolean checkFormValidation()
    {
        Utility.hideSoftInput(this);

        mBaseUrl = mAlfrescoLoginUrlEdit.getText().toString().trim();
        mUserName = mAlfrescoLoginUsernameEdit.getText().toString().trim();
        mPassword = mAlfrescoLoginPasswordEdit.getText().toString().trim();

        if (TextUtils.isEmpty(mBaseUrl)) {
            Toast.makeText(this, R.string.alfresco_login_error_url_empty, Toast.LENGTH_SHORT).show();
            Logger.warn(CLASS_NAME, "checkFormValidation : url is empty");
            mAlfrescoLoginUrlContainer.setBackgroundResource(R.drawable.error_login_style);
            return false;
        } else {
            mAlfrescoLoginUrlContainer.setBackgroundResource(android.R.color.transparent);
        }

        if (TextUtils.isEmpty(mUserName)) {
            Toast.makeText(this, R.string.alfresco_login_error_username_empty, Toast.LENGTH_SHORT).show();
            Logger.warn(CLASS_NAME, "checkFormValidation : username is empty");
            mAlfrescoLoginUsernameContainer.setBackgroundResource(R.drawable.error_login_style);
            return false;
        } else {
            mAlfrescoLoginUsernameContainer.setBackgroundResource(android.R.color.transparent);
        }

        if (TextUtils.isEmpty(mPassword)) {
            Toast.makeText(this, R.string.alfresco_login_error_password_empty, Toast.LENGTH_SHORT).show();
            Logger.warn(CLASS_NAME, "checkFormValidation : password is empty");
            mAlfrescoLoginPasswordContainer.setBackgroundResource(R.drawable.error_login_style);
            return false;
        } else {
            mAlfrescoLoginPasswordContainer.setBackgroundResource(android.R.color.transparent);
        }

        return true;
    }

    private void startLogin()
    {
        DialogUtil.showProgressDialog(this, "", "", false);
        mAlfrescoLoginErrorTxt.setVisibility(View.INVISIBLE);
        AlfrescoRequestManager.getInstance().connectToAlfrescoRepository(mBaseUrl, mUserName, mPassword,
                        mSecuredConnection, this);
    }

    @Override
    public void onClick(@NonNull View v)
    {
        if (!checkFormValidation()) {
            return;
        }

        startLogin();
    }

    @Override
    public void onCheckedChanged(@NonNull CompoundButton buttonView, boolean isChecked)
    {
        mSecuredConnection = isChecked;
    }

    @Override
    public void onHanldeAlfrescoConnectionCallback(int callBackCode)
    {
        DialogUtil.dismissDialogByTag(this, ProgressDialogFragment.TAG);
        switch (callBackCode) {
        case IConnectToAlfrescoSessionCallback.SUCCESS: {
            Logger.debug(CLASS_NAME, "onHanldeConnectionCallBack : success to connecte to the Alfresco");
            mAlfrescoLoginErrorTxt.setVisibility(View.INVISIBLE);

            DialogUtil.showSimpleDialog(this, R.string.alfresco_login_dialog_title,
                            R.string.alfresco_login_dialog_message, R.string.global_positive, 0, false,
                    (dialog, which) -> {

                        // save Alfresco login parameters
                        AlfrescoConfiguration.getInstance()
                                        .getSharedPreferenceStrategy(AlfrescoLoginActivity.this)
                                        .putString(AlfrescoPreferences.ALFRESCO_BASE_URL, getBaseUrl());
                        AlfrescoConfiguration.getInstance()
                                        .getSharedPreferenceStrategy(AlfrescoLoginActivity.this)
                                        .putString(AlfrescoPreferences.ALFRESCO_USER_NAME, getUserName());
                        AlfrescoConfiguration.getInstance()
                                        .getSharedPreferenceStrategy(AlfrescoLoginActivity.this)
                                        .putBoolean(AlfrescoPreferences.ALFRESCO_SSL_CONNECTION,
                                                        isSecuredConnection());
                        Intent intent =
                            new Intent(AlfrescoLoginActivity.this, AlfrescoRepositoryActivity.class);

                        intent.putExtra(EXTRA_SELECTED_THEME, getThemeName());
                        intent.putExtra(EXTRA_FOLDER_PATH, getInternalFolderPath());
                        startActivityForResult(intent, Constant.RequestCode.ALFRESCO_REPOSITORY);
                    }, null);
        }
            break;
        case AlfrescoConnectionErrorType.UNAUTHORIZED_USER:
        case ErrorCodeRegistry.SESSION_UNAUTHORIZED: {
            Logger.warn(CLASS_NAME, "onHanldeConnectionCallBack : error in alfresco repos connection callBackCode : "
                + callBackCode);
            mAlfrescoLoginErrorTxt.setVisibility(View.VISIBLE);
            mAlfrescoLoginErrorTxt.setText(R.string.alfresco_login_error_bad_arg);

            mAlfrescoLoginUsernameContainer.setBackgroundResource(R.drawable.error_login_style);

            mAlfrescoLoginPasswordContainer.setBackgroundResource(R.drawable.error_login_style);
        }
            break;
        case AlfrescoConnectionErrorType.NETWORK_ACCESS_ERROR:
        case ErrorCodeRegistry.SESSION_GENERIC: {
            Logger.warn(CLASS_NAME, "onHanldeConnectionCallBack : error in alfresco repos connection callBackCode : "
                + callBackCode);
            mAlfrescoLoginErrorTxt.setVisibility(View.VISIBLE);
            mAlfrescoLoginErrorTxt.setText(R.string.alfresco_login_error_bad_url);

            mAlfrescoLoginUrlContainer.setBackgroundResource(R.drawable.error_login_style);
        }
            break;
        case AlfrescoConnectionErrorType.NO_CONNECTION_ERROR:
            Toast.makeText(this, R.string.network_dialog_title, Toast.LENGTH_SHORT).show();
            break;
        case AlfrescoConnectionErrorType.UNKNOWN_ERROR:
        default: {
            Logger.warn(CLASS_NAME, "onHanldeConnectionCallBack : error in alfresco repos connection callBackCode : "
                + callBackCode);
            mAlfrescoLoginErrorTxt.setVisibility(View.VISIBLE);
            mAlfrescoLoginErrorTxt.setText(R.string.alfresco_login_error_global);

            mAlfrescoLoginUrlContainer.setBackgroundResource(android.R.color.transparent);

            mAlfrescoLoginUsernameContainer.setBackgroundResource(android.R.color.transparent);

            mAlfrescoLoginPasswordContainer.setBackgroundResource(android.R.color.transparent);
        }
        }
    }

    @Override
    protected void onStop() {
        AlfrescoRequestManager.getInstance().cancelLoginTask();
        Utility.hideSoftInput(AlfrescoLoginActivity.this);
        super.onStop();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        mToolBar.setTitle(getString(R.string.alfresco_default_title));
        initData();
    }

    String getBaseUrl()
    {
        return mBaseUrl;
    }

    String getUserName()
    {
        return mUserName;
    }

    boolean isSecuredConnection()
    {
        return mSecuredConnection;
    }

    String getThemeName()
    {
        return mThemeName;
    }

    String getInternalFolderPath()
    {
        return mFolderPath;
    }

}
