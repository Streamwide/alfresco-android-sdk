/*
 *
 * 	StreamWIDE (Team on The Run)
 *
 * @createdBy  AndroidTeam on Fri, 28 Aug 2020 14:46:27 +0100
 * @copyright  Copyright (c) 2020 StreamWIDE UK Ltd (Team on the Run)
 * @email      support@teamontherun.com
 *
 * 	© Copyright 2020 StreamWIDE UK Ltd (Team on the Run). StreamWIDE is the copyright holder
 * 	of all code contained in this file. Do not redistribute or
 *  	re-use without permission.
 *
 * @lastModifiedOn Mon, 24 Aug 2020 21:57:21 +0100
 */

package com.streamwide.smartms.lib.alfresco.data.preferences;

public class AlfrescoPreferences {

    protected static final String DOUBLE_QUOTE_CHAR = "\"";
    protected static final String DOUBLE_QUOTE_CHAR_REPLACEMENT = "&#34;";

    /**
     * String : alfresco base url
     */
    public static final String ALFRESCO_BASE_URL = "alfresco_base_url";
    /**
     * String : alfresco username
     */
    public static final String ALFRESCO_USER_NAME = "alfresco_username";
    /**
     * String : alfresco secured connection (SSL)
     */
    public static final String ALFRESCO_SSL_CONNECTION = "alfresco_ssl_connection";

    public interface Default {

        String DEFAULT_ALFRESCO_BASE_URL = "";
        String DEFAULT_ALFRESCO_USERNAME = "";
        boolean DEFAULT_ALFRESCO_SSL_CONNECTION = false;
    }
}
