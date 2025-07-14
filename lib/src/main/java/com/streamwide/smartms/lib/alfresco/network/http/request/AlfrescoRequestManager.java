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

package com.streamwide.smartms.lib.alfresco.network.http.request;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.alfresco.mobile.android.api.exceptions.AlfrescoSessionException;
import org.alfresco.mobile.android.api.exceptions.ErrorCodeRegistry;
import org.alfresco.mobile.android.api.model.Folder;
import org.alfresco.mobile.android.api.model.Node;
import org.alfresco.mobile.android.api.services.DocumentFolderService;
import org.alfresco.mobile.android.api.session.AlfrescoSession;
import org.alfresco.mobile.android.api.session.RepositorySession;

import com.streamwide.smartms.lib.alfresco.logger.Logger;

import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AlfrescoRequestManager {

    private static final String CLASS_NAME = "AlfrescoRequestManager";
    private static final String ONPREMISE_TRUSTMANAGER_CLASSNAME =
        "org.alfresco.mobile.binding.internal.https.trustmanager";

    private static AlfrescoRequestManager mInstance = null;

    private static AlfrescoSession mAlfrescoSession;
    private static DocumentFolderService mDocumentFolderService;

    private ConnectToAlfrescoRepoTask mConnectToAlfrescoRepoTask = null;
    private FetchFolderTask mGetFolderTask;

    private String mBaseUrl;
    private String mUsername;
    private String mPassword;

    private boolean mSecuredConnection;
    private IConnectToAlfrescoSessionCallback mListener;

    private AlfrescoRequestManager()
    {
    }

    public static @NonNull
    AlfrescoRequestManager getInstance()
    {
        synchronized (AlfrescoRequestManager.class) {
            if (mInstance == null) {
                mInstance = new AlfrescoRequestManager();
            }
        }

        return mInstance;
    }

    public void clearAlfrescoSession()
    {
        if (mAlfrescoSession != null) {
            mAlfrescoSession.clear();
        }
    }

    public static @Nullable
    AlfrescoSession getAlfrescoSession()
    {
        return mAlfrescoSession;
    }

    /*package*/DocumentFolderService getDocumentFolderService()
    {
        if (mDocumentFolderService == null) {
            mDocumentFolderService = mAlfrescoSession.getServiceRegistry().getDocumentFolderService();
        }

        return mDocumentFolderService;
    }

    public void connectToAlfrescoRepository(@NonNull String baseUrl, @NonNull String username, @NonNull String password, boolean securedConnection,
                                            @Nullable IConnectToAlfrescoSessionCallback listener)
    {
        if (mAlfrescoSession != null) {
            mAlfrescoSession.clear();
        }

        this.mBaseUrl = baseUrl;
        this.mUsername = username;
        this.mPassword = password;
        this.mSecuredConnection = securedConnection;
        this.mListener = listener;
        cancelLoginTask();
        mConnectToAlfrescoRepoTask = new ConnectToAlfrescoRepoTask(this);
        mConnectToAlfrescoRepoTask.execute();
    }

    public void cancelLoginTask()
    {
        if (mConnectToAlfrescoRepoTask != null) {
            mConnectToAlfrescoRepoTask.cancel(true);
            mConnectToAlfrescoRepoTask = null;
        }
    }

    private static class ConnectToAlfrescoRepoTask extends AsyncTask<Void, Void, Integer> {

        private final WeakReference<AlfrescoRequestManager> mRequestManagerWeakReference;

        ConnectToAlfrescoRepoTask(AlfrescoRequestManager manager)
        {
            mRequestManagerWeakReference = new WeakReference<>(manager);
        }

        @Override
        protected Integer doInBackground(Void... params)
        {
            if (mRequestManagerWeakReference.get() == null) {
                return null;
            }
            int callBackCode;
            try {
                if (mRequestManagerWeakReference.get().isSecuredConnection()) {
                    // Prepare Settings
                    Map<String, Serializable> settings = prepareSSLSettings();
                    setAlfrescoSession(RepositorySession.connect(mRequestManagerWeakReference.get().getBaseUrl(),
                                    mRequestManagerWeakReference.get().getUsername(),
                                    mRequestManagerWeakReference.get().getPassword(), settings));
                } else {
                    setAlfrescoSession(RepositorySession.connect(mRequestManagerWeakReference.get().getBaseUrl(),
                                    mRequestManagerWeakReference.get().getUsername(),
                                    mRequestManagerWeakReference.get().getPassword()));
                }

                if (getAlfrescoSession() != null) {
                    callBackCode = IConnectToAlfrescoSessionCallback.SUCCESS;
                } else {
                    Logger.warn(CLASS_NAME,
                                    "ConnectToAlfrescoRepo : error occured when connecting to Alfresco repository. No Session available !");
                    callBackCode = ErrorCodeRegistry.SESSION_GENERIC;
                }
            } catch (AlfrescoSessionException e) {
                Logger.error(CLASS_NAME,
                                "ConnectToAlfrescoRepo exception : error occured when connecting to Alfresco repository!.other error",
                                e);

                int errorType = AlfrescoExceptionHelper.getErrorType(e);
                if (errorType > 0) {
                    callBackCode = errorType;
                } else {
                    callBackCode = e.getErrorCode();
                }

            } catch (Exception e) {
                callBackCode = ErrorCodeRegistry.SESSION_GENERIC;
            }

            return callBackCode;
        }

        @Override
        protected void onPostExecute(Integer connectionState)
        {
            super.onPostExecute(connectionState);

            if (mRequestManagerWeakReference.get() != null && mRequestManagerWeakReference.get().getListener() != null) {
                mRequestManagerWeakReference.get().getListener().onHanldeAlfrescoConnectionCallback(connectionState);
            }
        }

        private Map<String, Serializable> prepareSSLSettings()
        {
            Map<String, Serializable> settings = new HashMap<>(1);
            // ssl certificate
            try {
                settings.put(ONPREMISE_TRUSTMANAGER_CLASSNAME,
                                "com.streamwide.smartms.alfresco.network.http.request.AlfrescoTrustManager");
            } catch (Exception e) {
                // Nothing special
            }

            return settings;
        }
    }

    public void fetchChilds(@NonNull Node node, @NonNull IGetChildsCallback getChildsCallbackListener)
    {
        cancelFolderFetcherTask();
        mGetFolderTask = new FetchFolderTask(this, getChildsCallbackListener);
        mGetFolderTask.execute(node);
    }

    public void cancelFolderFetcherTask()
    {
        if (mGetFolderTask != null) {
            mGetFolderTask.cancel(true);
            mGetFolderTask = null;
        }
    }

    public interface IGetChildsCallback {

        void onHandleChildsCallback(@Nullable List<Node> listNodes);
    }

    static class FetchFolderTask extends AsyncTask<Node, Void, List<Node>> {

        private IGetChildsCallback mGetChildsCallbackListener;
        private final WeakReference<AlfrescoRequestManager> mRequestManagerWeakReference;

        public WeakReference<AlfrescoRequestManager> getRequestManagerWeakReference() {
            return mRequestManagerWeakReference;
        }
        public IGetChildsCallback getGetChildsCallbackListener() {
            return mGetChildsCallbackListener;
        }

        public FetchFolderTask(AlfrescoRequestManager manager, IGetChildsCallback getChildsCallbackListener)
        {
            mRequestManagerWeakReference = new WeakReference<>(manager);
            this.mGetChildsCallbackListener = getChildsCallbackListener;
        }

        @Override
        protected List<Node> doInBackground(Node... params)
        {
            Node node = params[0];
            if (node == null) {
                node = getAlfrescoSession().getRootFolder();

            }
            // Get children of document library
            List<Node> nodes = null;
            if (getRequestManagerWeakReference().get() != null) {
                nodes = getRequestManagerWeakReference().get().getDocumentFolderService().getChildren(((Folder) node));
            }

            return nodes;
        }

        @Override
        protected void onPostExecute(List<Node> listNodes)
        {
            super.onPostExecute(listNodes);
            if (getGetChildsCallbackListener() != null) {
                getGetChildsCallbackListener().onHandleChildsCallback(listNodes);
            }
        }
    }

    public interface IConnectToAlfrescoSessionCallback {

        int SUCCESS = 0;

        /**
         * handle connection error in alfresco repository session
         */
        void onHanldeAlfrescoConnectionCallback(int callbackCode);

    }

    static void setAlfrescoSession(AlfrescoSession mAlfrescoSession) {
        AlfrescoRequestManager.mAlfrescoSession = mAlfrescoSession;
    }

    boolean isSecuredConnection() {
        return mSecuredConnection;
    }

    String getBaseUrl() {
        return mBaseUrl;
    }

    String getUsername() {
        return mUsername;
    }

    String getPassword() {
        return mPassword;
    }

    IConnectToAlfrescoSessionCallback getListener() {
        return mListener;
    }

}
