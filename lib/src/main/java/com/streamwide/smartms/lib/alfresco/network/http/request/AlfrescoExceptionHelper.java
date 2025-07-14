/*
 *
 * 	StreamWIDE (Team on The Run)
 *
 * @createdBy  AndroidTeam on Wed, 15 May 2019 10:51:18 +0100
 * @copyright  Copyright (c) 2019 StreamWIDE UK Ltd (Team on the Run)
 * @email      support@teamontherun.com
 *
 * 	© Copyright 2019 StreamWIDE UK Ltd (Team on the Run). StreamWIDE is the copyright holder
 * 	of all code contained in this file. Do not redistribute or
 *  	re-use without permission.
 *
 * @lastModifiedOn Tue, 14 May 2019 12:55:34 +0100
 */

package com.streamwide.smartms.lib.alfresco.network.http.request;

import java.net.ConnectException;
import java.net.UnknownHostException;

import org.alfresco.mobile.android.api.exceptions.AlfrescoSessionException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisConnectionException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisUnauthorizedException;

import com.streamwide.smartms.lib.alfresco.logger.Logger;

/**
 * @author Atef
 *
 */
public class AlfrescoExceptionHelper {

    private static final String CLASS_NAME = "AlfrescoExceptionHelper";

    public final class AlfrescoConnectionErrorType {

        public static final int UNAUTHORIZED_USER = 1;
        public static final int NETWORK_ACCESS_ERROR = 2;
        public static final int NO_CONNECTION_ERROR = 3;
        public static final int UNKNOWN_ERROR = 4;

        private AlfrescoConnectionErrorType()
        {
            // Left empty
        }
    }

    /**
     * @param e
     *            the {@link AlfrescoSessionException} returned Alfresco sdk on
     *            connection failed
     * @return errorType: the real cause of connection fail
     */
    static int getErrorType(AlfrescoSessionException e)
    {

        Throwable exceptionCause = e.getCause();
        if (exceptionCause == null) {
            return AlfrescoConnectionErrorType.UNKNOWN_ERROR;
        }
        Throwable realCause = exceptionCause.getCause();

        try {
            throw realCause;
        } catch (CmisUnauthorizedException cmisUnauthorizedException) {

            Logger.warn(CLASS_NAME, "CmisUnauthorizedException : Bad userName or/and password ");
            return AlfrescoConnectionErrorType.UNAUTHORIZED_USER;

        } catch (CmisConnectionException cmisConnectionException) {
            Throwable cause = cmisConnectionException.getCause();
            try {
                throw cause;
            } catch (UnknownHostException unknownHostException) {
                Logger.warn(CLASS_NAME, "UnknownHostException : No connection");
                return AlfrescoConnectionErrorType.NO_CONNECTION_ERROR;
            } catch (ConnectException connectException) {
                Logger.warn(CLASS_NAME, "ConnectException : network access issue");
                return AlfrescoConnectionErrorType.NETWORK_ACCESS_ERROR;
            } catch (CmisConnectionException exc) {
                return AlfrescoConnectionErrorType.UNKNOWN_ERROR;
            } catch (Throwable throwable) {
                Logger.warn(CLASS_NAME, "Throwable : UnknownException :" + throwable.getMessage());
            }
            return AlfrescoConnectionErrorType.UNKNOWN_ERROR;
        } catch (Throwable throwable) {
            return AlfrescoConnectionErrorType.UNKNOWN_ERROR;
        }
    }

}
