/*
 *
 * 	StreamWIDE (Team on The Run)
 *
 * @createdBy  AndroidTeam on lun., 24 août 2020 12:37:16 +0200
 * @copyright  Copyright (c) 2020 StreamWIDE UK Ltd (Team on the Run)
 * @email      support@teamontherun.com
 *
 * 	© Copyright 2020 StreamWIDE UK Ltd (Team on the Run). StreamWIDE is the copyright holder
 * 	of all code contained in this file. Do not redistribute or
 *  	re-use without permission.
 *
 * @lastModifiedOn ven., 21 août 2020 10:32:08 +0200
 */

package com.streamwide.smartms.lib.alfresco.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;

import org.alfresco.mobile.android.api.model.ContentFile;
import org.alfresco.mobile.android.api.model.ContentStream;
import org.alfresco.mobile.android.api.model.Document;
import org.alfresco.mobile.android.api.model.impl.ContentFileImpl;
import org.alfresco.mobile.android.api.session.AlfrescoSession;
import org.alfresco.mobile.android.api.utils.IOUtils;

import com.streamwide.smartms.lib.alfresco.data.AlfrescoConfiguration;
import com.streamwide.smartms.lib.alfresco.logger.Logger;

import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Provides an asynchronous task to download the content of a document
 * object.</br>
 * onProgressUpdate returns the progress of dthe download in
 * percentage.
 *
 * @author Jean Marie Pascal
 */
public class DownloadTask extends AsyncTask<Void, Integer, ContentFile> {

    private static final String TAG = "DownloadTask";

    private static final int MAX_BUFFER_SIZE = 1024;

    private int downloaded;

    private int totalDownloaded;

    private AlfrescoSession session;

    private Document doc;

    private File destFile;

    private DownloadTaskListener dl;

    public DownloadTask(@Nullable AlfrescoSession session, @NonNull Document document, @NonNull File destFile)
    {
        this.session = session;
        this.destFile = destFile;
        this.doc = document;
    }

    public interface DownloadTaskListener {

        void onPreExecute();

        void onPostExecute(@Nullable ContentFile f);

        void onProgressUpdate(@NonNull Integer... values);
    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
        if (dl != null) {
            dl.onPreExecute();
        }
        downloaded = 0;
        totalDownloaded = 0;
    }

    @Override
    @Nullable
    protected ContentFile doInBackground(@Nullable Void... params)
    {
        try {
            ContentStream contentStream = session.getServiceRegistry().getDocumentFolderService().getContentStream(doc);
            copyFile(contentStream.getInputStream(), contentStream.getLength(), destFile);
            return new ContentFileImpl(destFile);
        } catch (Exception e) {
            Logger.error(TAG, Log.getStackTraceString(e));
        }
        return null;
    }

    @Override
    protected void onPostExecute(@Nullable ContentFile f)
    {
        super.onPostExecute(f);
        if (dl != null) {
            dl.onPostExecute(f);
        }
    }

    @Override
    protected void onProgressUpdate(@NonNull Integer... values)
    {
        super.onProgressUpdate(values);
        if (dl != null) {
            dl.onProgressUpdate(values);
        }
    }

    private boolean copyFile(InputStream src, long size, File dest)
    {
        IOUtils.ensureOrCreatePathAndFile(dest);
        boolean copied = true;
        try {
            AlfrescoConfiguration.getInstance().getIoFileStrategy().write(src, dest.getPath(), false);
        } catch (IOException | GeneralSecurityException ignored) {
            copied = false;
        }

            byte[] buffer = new byte[MAX_BUFFER_SIZE];

            while (size - downloaded > 0) {
                if (size - downloaded < MAX_BUFFER_SIZE) {
                    buffer = new byte[(int) (size - downloaded)];
                }

                int read = 0;
                try {
                    read = src.read(buffer);
                } catch (IOException e) {
                    Logger.error(TAG, " IOException : "+ e);
                }
                if (read == -1) {
                    break;
                }

                downloaded += read;
                totalDownloaded += read;
                publishProgress(totalDownloaded);
            }

        IOUtils.closeStream(src);

        return copied;
    }

    public void setDl(@NonNull DownloadTaskListener dl)
    {
        this.dl = dl;
    }
}
