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

import org.alfresco.mobile.android.api.model.ContentFile;

import com.streamwide.smartms.lib.alfresco.util.DownloadTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by streamwide on 1/27/17.
 */
public class CustomHorizontalProgressDialog extends ProgressDialog implements DownloadTask.DownloadTaskListener {

    private IDownloadTaskCompletedCallback mDownloadTaskCompletedCallback;
    private DownloadTask mDownloadTask;

    private long mTotalFileSize;

    public CustomHorizontalProgressDialog(@NonNull Context context, @NonNull DownloadTask downloadTask,
                    @NonNull IDownloadTaskCompletedCallback iDownloadTaskCompletedCallback)
    {
        super(context);
        this.mDownloadTask = downloadTask;
        this.mDownloadTaskCompletedCallback = iDownloadTaskCompletedCallback;
        initEvent();
    }

    public void setTotalFileSize(long mTotalFileSize)
    {
        this.mTotalFileSize = mTotalFileSize;
    }

    private void initEvent()
    {
        mDownloadTask.setDl(this);
        mDownloadTask.execute();
        setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog)
            {
                if (getDownloadTask() != null) {
                    getDownloadTask().cancel(false);
                }
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onPreExecute()
    {
        // Nothing to do
    }

    @Override
    public void onPostExecute(@Nullable ContentFile contentFile)
    {
        mDownloadTaskCompletedCallback.onDownloadTaskCompleted(contentFile);
        dismiss();
    }

    @Override
    public void onProgressUpdate(@NonNull Integer... integers)
    {
        int percent = (int) Math.round(((double) integers[0] / mTotalFileSize) * 100);
        setProgress(percent);
    }

    public interface IDownloadTaskCompletedCallback {

        void onDownloadTaskCompleted(@Nullable ContentFile results);
    }

    public static @NonNull ProgressDialog showHorizontalProgressDialog(@NonNull Context context, @Nullable String title, @Nullable String content,
                                                                       boolean isCancelable, long totalFileSize,
                                                                       @NonNull DownloadTask downloadTask,
                                                                       @NonNull CustomHorizontalProgressDialog.IDownloadTaskCompletedCallback downloadTaskCompletedCallback)
    {

        CustomHorizontalProgressDialog progressDialog =
            new CustomHorizontalProgressDialog(context, downloadTask, downloadTaskCompletedCallback);
        if (TextUtils.isEmpty(title) == false) {
            progressDialog.setTitle(title);
        }
        if (TextUtils.isEmpty(content) == false) {
            progressDialog.setMessage(content);
        }
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(isCancelable);
        progressDialog.setTotalFileSize(totalFileSize);

        progressDialog.show();
        return progressDialog;
    }

    DownloadTask getDownloadTask() {
        return mDownloadTask;
    }
}
