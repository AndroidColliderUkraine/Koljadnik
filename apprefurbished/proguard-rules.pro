# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/pseveryn/Android/Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html
# Add any project specific keep options here:
# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
#Keep the annotated things annotated
-keepattributes *Annotation*
#Keep the dagger annotation classes themselves
-keep @interface dagger.*,javax.inject.*
#Keep the Modules intact
-keep @dagger.Module class *
#-Keep the fields annotated with @Inject of any class that is not deleted.
-keepclassmembers class * {
  @javax.inject.* <fields>;
}
#-Keep the names of classes that have fields annotated with @Inject and the fields themselves.
-keepclasseswithmembernames class * {
  @javax.inject.* <fields>;
}
# Keep the generated classes by dagger-compile
-keep class **$$ModuleAdapter
-keep class **$$InjectAdapter
-keep class **$$StaticInjection
-dontwarn java.lang.invoke.*
-dontwarn okio.**
-dontwarn java.lang.invoke**
-dontwarn retrofit2.Platform$Java8
-dontwarn com.viewpagerindicator.**
-dontwarn org.apache.**
##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature
# Gson specific classes
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }
# Application classes that will be serialized/deserialized over Gson
# -keep class mypersonalclass.data.model.** { *; }
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }
# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}
-keep class com.defietser.models.responses.errors.** { *; }
-dontwarn android.net.http.SslError
-keep public class android.net.http.SslError
-keep public class android.webkit.WebViewClient
-dontwarn android.webkit.WebView
-dontwarn android.webkit.WebViewClient