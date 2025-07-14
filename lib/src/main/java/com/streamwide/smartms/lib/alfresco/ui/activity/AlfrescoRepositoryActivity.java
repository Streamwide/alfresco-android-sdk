/*
 *
 * 	StreamWIDE (Team on The Run)
 *
 * @createdBy  AndroidTeam on ven., 6 mars 2020 15:47:55 +0100
 * @copyright  Copyright (c) 2020 StreamWIDE UK Ltd (Team on the Run)
 * @email      support@teamontherun.com
 *
 * 	© Copyright 2020 StreamWIDE UK Ltd (Team on the Run). StreamWIDE is the copyright holder
 * 	of all code contained in this file. Do not redistribute or
 *  	re-use without permission.
 *
 * @lastModifiedOn ven., 6 mars 2020 15:42:45 +0100
 */

package com.streamwide.smartms.lib.alfresco.ui.activity;

import static com.streamwide.smartms.lib.alfresco.constant.Constant.EXTRA_FOLDER_PATH;
import static com.streamwide.smartms.lib.alfresco.constant.Constant.EXTRA_SELECTED_THEME;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.alfresco.mobile.android.api.model.ContentFile;
import org.alfresco.mobile.android.api.model.Document;
import org.alfresco.mobile.android.api.model.Node;

import com.streamwide.smartms.lib.alfresco.R;
import com.streamwide.smartms.lib.alfresco.data.item.AlfrescoNodeComparator;
import com.streamwide.smartms.lib.alfresco.logger.Logger;
import com.streamwide.smartms.lib.alfresco.network.http.request.AlfrescoRequestManager;
import com.streamwide.smartms.lib.alfresco.network.http.request.AlfrescoRequestManager.IGetChildsCallback;
import com.streamwide.smartms.lib.alfresco.ui.alfresco.AlfrescoClickListener;
import com.streamwide.smartms.lib.alfresco.ui.alfresco.AlfrescoRecyclerAdapter;
import com.streamwide.smartms.lib.alfresco.ui.dialog.CustomHorizontalProgressDialog;
import com.streamwide.smartms.lib.alfresco.ui.dialog.DialogUtil;
import com.streamwide.smartms.lib.alfresco.ui.dialog.ProgressDialogFragment;
import com.streamwide.smartms.lib.alfresco.ui.view.AlfrescoBottomBar;
import com.streamwide.smartms.lib.alfresco.ui.view.AlfrescoBottomBar.AlfrescoBottomBarListener;
import com.streamwide.smartms.lib.alfresco.util.DownloadTask;
import com.streamwide.smartms.lib.alfresco.util.ThemeUtils;
import com.streamwide.smartms.lib.alfresco.util.Utility;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AlfrescoRepositoryActivity extends AppCompatActivity
    implements OnItemClickListener, AlfrescoBottomBarListener, AlfrescoClickListener {

    private static final String CLASS_NAME = "AlfrescoRepositoryActivity";
    private String mThemeName;
    private String mFolderPath;
    private AlfrescoRecyclerAdapter mAdapter;
    private RecyclerView mRepositoryFolderRecyclerView;
    private AlfrescoBottomBar mAlfrescoBottomBar;

    protected @Nullable List<Node> mNodeQueue;
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
            themeResourceIdentifier =  getResources().getIdentifier(mThemeName,null,null);

        if (themeResourceIdentifier != 0)
            ThemeUtils.changeToTheme(this, themeResourceIdentifier);

        setContentView(R.layout.activity_alfresco_repository);

        initView();
        initData();
        initEvent();
    }

    private String getSelectedTheme() {
        Intent intent = getIntent();

        if (intent == null || intent.getExtras() == null) {
            return "";
        }
        return intent.getStringExtra(EXTRA_SELECTED_THEME);
    }

    private String getFolderPath() {
        Intent intent = getIntent();

        if (intent == null || intent.getExtras() == null) {
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
        mToolBar.setTitle(getString(R.string.alfresco_default_title));
        final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.ic_back_icon);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(upArrow);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                Logger.info(CLASS_NAME, "Back button action bar is pressed");
                onBackPressed();
            }
        });

        mRepositoryFolderRecyclerView = findViewById(R.id.repository_folder_list);
        DividerItemDecoration itemDecorator = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        mRepositoryFolderRecyclerView.addItemDecoration(itemDecorator);
        mRepositoryFolderRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAlfrescoBottomBar = findViewById(R.id.alfresco_bottom_bar);
    }

    private void initData()
    {
        getChilds(null);
    }

    private void initEvent()
    {
        mAlfrescoBottomBar.setAlfrescoBottomBarListener(this);
    }

    private IGetChildsCallback mChildsCallback = new IGetChildsCallback() {

        @Override
        public void onHandleChildsCallback(@Nullable List<Node> listNodes)
        {
            DialogUtil.dismissDialogByTag(AlfrescoRepositoryActivity.this, ProgressDialogFragment.TAG);

            if (mNodeQueue != null && !mNodeQueue.isEmpty()) {
                getToolBar().setTitle(mNodeQueue.get(mNodeQueue.size() - 1).getName());
            } else {
                getToolBar().setTitle(getString(R.string.alfresco_default_title));
            }

            if (listNodes != null) {
                Collections.sort(listNodes, new AlfrescoNodeComparator());

                setAdapter(new AlfrescoRecyclerAdapter(AlfrescoRepositoryActivity.this, listNodes,
                                AlfrescoRepositoryActivity.this));
                getRepositoryFolderRecyclerView().setAdapter(getAdapter());
            }
        }
    };

    private void getChilds(Node currentNode)
    {
        DialogUtil.showProgressDialog(this, null, null, true);
        AlfrescoRequestManager.getInstance().fetchChilds(currentNode, mChildsCallback);
    }

    Toolbar getToolBar() {
        return mToolBar;
    }

    AlfrescoRecyclerAdapter getAdapter() {
        return mAdapter;
    }

    void setAdapter(AlfrescoRecyclerAdapter adapter) {
        this.mAdapter = adapter;
    }

    RecyclerView getRepositoryFolderRecyclerView() {
        return mRepositoryFolderRecyclerView;
    }

    @Override
    public void onItemClick(@NonNull AdapterView<?> parent, @NonNull View view, int position, long id)
    {
        Node currentNode = mAdapter.getItem(position);
        if (mNodeQueue == null) {
            mNodeQueue = new ArrayList<>();
        }
        if (currentNode != null && currentNode.isFolder()) {
            mNodeQueue.add(currentNode);
            getChilds(currentNode);
        } else if (currentNode != null && currentNode.isDocument()) {

            final Document document = (Document) currentNode;
            File dlFile = getDownloadFile(document);
            if (dlFile == null) {
                DialogUtil.showSimpleDialog(this, R.string.dialog_error_title, R.string.global_sdcard_not_available, R.string.global_positive, 0, true,
                                null, null);
                Logger.debug(CLASS_NAME, "download file is null");
                return;
            }

            long totalSize = document.getContentStreamLength();

            CustomHorizontalProgressDialog.showHorizontalProgressDialog(this, "",
                            getString(R.string.message_attachment_downloading), true, totalSize,
                            new DownloadTask(AlfrescoRequestManager.getAlfrescoSession(),
                                            ((Document) currentNode), dlFile),
                            new CustomHorizontalProgressDialog.IDownloadTaskCompletedCallback() {

                                @Override
                                public void onDownloadTaskCompleted(ContentFile contentFile)
                                {
                                    if (contentFile != null && contentFile.getFile() != null) {
                                        File currentFile = contentFile.getFile();
                                        String contentStreamMimeType = document.getContentStreamMimeType();

                                        Intent intent = new Intent();
                                        Uri data = Uri.fromFile(currentFile);
                                        intent.setDataAndType(data,
                                                        contentStreamMimeType.toLowerCase(Locale.getDefault()));

                                        setResult(RESULT_OK, intent);
                                        finish();

                                    } else {
                                        Toast.makeText(AlfrescoRepositoryActivity.this,
                                                        R.string.alfresco_download_error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
        }
    }

    private File getDownloadFile(Document doc)
    {
        if (AlfrescoRequestManager.getAlfrescoSession() == null) {
            return null;
        }
        File tmpFile = getTempFile(this, doc,mFolderPath);
        if (tmpFile != null) {
            org.alfresco.mobile.android.api.utils.IOUtils.ensureOrCreatePathAndFile(tmpFile);
        }
        return tmpFile;
    }

    public static @Nullable File getTempFile(@Nullable final Activity activity, @Nullable final Node node, @Nullable String folderPath)
    {
        if (activity != null && node != null) {

            String alfrescoPath = Utility.getAlfrescoFolderPath(folderPath);

            if (!TextUtils.isEmpty(alfrescoPath)) {
                File folder = new File(alfrescoPath);

                boolean isFolderCreated = folder.mkdirs();

                if (!isFolderCreated) {
                    Logger.warn(CLASS_NAME, "The folder was not created !");
                }

                return new File(folder, node.getName());
            }
        }

        return null;
    }

    @Override
    public void onDisconnect(@NonNull View view)
    {
        DialogUtil.showSimpleDialog(this, R.string.alfresco_logout, R.string.alfresco_logout_message, R.string.global_yes, R.string.global_no, true,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                // Perform delete task
                                AlfrescoRequestManager.getInstance().clearAlfrescoSession();
                                finish();
                            }
                        }, null);
    }

    @Override
    public void onBackPressed()
    {
        DialogUtil.dismissDialogByTag(AlfrescoRepositoryActivity.this, ProgressDialogFragment.TAG);

        if (mNodeQueue != null && !mNodeQueue.isEmpty()) {
            mNodeQueue.remove(mNodeQueue.size() - 1);
            if (!mNodeQueue.isEmpty()) {
                getChilds(mNodeQueue.get(mNodeQueue.size() - 1));
            } else {
                getChilds(null);
            }
        } else {
            AlfrescoRequestManager.getInstance().cancelFolderFetcherTask();
            finish();
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy()
    {
        mNodeQueue = null;
        super.onDestroy();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    @Override
    public void onAlfrescoItemClick(@NonNull View view, int position)
    {
        Node currentNode = mAdapter.getItem(position);

        if (mNodeQueue == null) {
            mNodeQueue = new ArrayList<>();
        }
        if (currentNode != null && currentNode.isFolder()) {
            mNodeQueue.add(currentNode);
            getChilds(currentNode);
        } else if (currentNode != null && currentNode.isDocument()) {

            final Document document = (Document) currentNode;
            File dlFile = getDownloadFile(document);
            if (dlFile == null) {
                DialogUtil.showSimpleDialog(this, R.string.dialog_error_title, R.string.global_sdcard_not_available, R.string.global_positive, 0, true,
                                null, null);
                Logger.debug(CLASS_NAME, "download file is null");
                return;
            }

            long totalSize = document.getContentStreamLength();

            CustomHorizontalProgressDialog.showHorizontalProgressDialog(this, "",
                            getString(R.string.message_attachment_downloading), true, totalSize,
                            new DownloadTask(AlfrescoRequestManager.getAlfrescoSession(),
                                            ((Document) currentNode), dlFile),
                            new CustomHorizontalProgressDialog.IDownloadTaskCompletedCallback() {

                                @Override
                                public void onDownloadTaskCompleted(ContentFile contentFile)
                                {
                                    if (contentFile != null && contentFile.getFile() != null) {
                                        File currentFile = contentFile.getFile();
                                        String contentStreamMimeType = document.getContentStreamMimeType();

                                        Intent intent = new Intent();
                                        Uri data = Uri.fromFile(currentFile);
                                        intent.setDataAndType(data,
                                                        contentStreamMimeType.toLowerCase(Locale.getDefault()));

                                        setResult(RESULT_OK, intent);
                                        finish();

                                    } else {
                                        Toast.makeText(AlfrescoRepositoryActivity.this,
                                                        R.string.alfresco_download_error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
        }
    }
}
