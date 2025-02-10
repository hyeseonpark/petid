# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.kts.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# data class
-keep class com.petid.data.ml.** {*;}
-keep class com.petid.data.source.local.entity.** {*;}
-keep class com.petid.data.util.S3UploadHelper {*;}
-keep class com.petid.data.repository.local.** {*;}
-keep class com.petid.data.repository.remote.** {*;}
-keep class com.petid.domain.entity.** {*;}
-keep class com.petid.data.dto.** {*;}

-keep interface com.petid.data.util.PreferencesHelper { *; }
-keep class com.petid.data.util.ExtensionsKt {*;}
-keep class com.petid.data.source.remote.** {*;}
-keep class com.petid.data.source.local.dao.** {*;}
-keep class com.petid.data.api.** {*;}
-keep class com.petid.data.di.** {*;}

# Hilt 관련 Keep 규칙
-keep class dagger.* { *; }
#-keep class * extends dagger.internal.Binding
#-keep class * extends dagger.internal.ModuleAdapter
#-keep class * extends dagger.internal.StaticInjection

# Hilt Android 특정 규칙
-keep @dagger.hilt.android.HiltAndroidApp class *
-keepclassmembers,allowobfuscation class * {
    @javax.inject.Inject <init>();
    @javax.inject.Inject <fields>;
    @javax.inject.Inject <methods>;
}

# Hilt Generated Code 보존
-keep class **.*_Impl
-keep class **.*Hilt*
#-keepclassmembers class ** {
#    @com.google.inject.Inject <init>(...);
#}

# Firebase Crashlytics
-keep class com.google.firebase.crashlytics.** {*;}
-keep interface com.google.firebase.crashlytics.** {*;}

# Hilt
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-dontwarn javax.annotation.**

# AWS SDK
-dontwarn com.amazonaws.**
-keepattributes *Annotation*
-keep class com.amazonaws.** { *; }

# Mediapipe
-keep class com.google.mediapipe.proto.** { *; }
-keepclassmembers class * extends com.google.protobuf.GeneratedMessageLite { *; }
-keep class com.google.common.flogger.** { *; }

#-keep public class com.google.mediapipe.** { ; }
# -keep public class com.google.mediapipe.framework.Graph { ; }
# -keep public class com.google.common. { ; }
# -keep public interface com.google.common.* { ; }
# -keep class com.google.mediapipe.solutioncore.* {*;}
# -keep class com.google.protobuf.** { ; }
# -keepclassmembers class com.google.protobuf.* { *; }
# -keepclassmembers class * extends com.google.protobuf.GeneratedMessageLite {
# *;
# }
# -dontwarn com.google.mediapipe.proto.CalculatorProfileProto$CalculatorProfile
# -dontwarn com.google.mediapipe.proto.GraphTemplateProto$CalculatorGraphTemplate

# Room DB
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-dontwarn androidx.room.paging.**

# R8
#-keep class org.bouncycastle.jsse.** { *; }
#-keep class org.bouncycastle.jsse.provider.** { *; }
#-keep class org.conscrypt.** { *; }
#-keep class org.openjsse.javax.net.ssl.** { *; }
#-keep class org.openjsse.net.ssl.** { *; }