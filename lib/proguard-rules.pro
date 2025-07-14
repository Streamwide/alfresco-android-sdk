# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

-repackageclasses com.streamwide.smartms.lib.alfresco

-keep interface com.streamwide.smartms.lib.alfresco.ui.view.AlfrescoBottomBar$AlfrescoBottomBarListener* { *; }
-keep public class com.streamwide.smartms.lib.alfresco.util.AlfrescoIntentUtil { *; }
-keep public class com.streamwide.smartms.lib.alfresco.logger.** {*;}
-keep public class com.streamwide.smartms.lib.alfresco.constant.** {*;}
-keep public class com.streamwide.smartms.lib.alfresco.data.AlfrescoConfiguration {*;}
-keep public class com.streamwide.smartms.lib.alfresco.ui.** {*;}