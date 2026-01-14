# Add project specific ProGuard rules here.

# Tuya SDK
-keep class com.thingclips.**{*;}
-dontwarn com.thingclips.**

# FastJson
-keep class com.alibaba.fastjson.**{*;}
-dontwarn com.alibaba.fastjson.**

# RxJava
-dontwarn rx.**
-keep class rx.** {*;}
-keep class io.reactivex.**{*;}
-dontwarn io.reactivex.**
-keep class rx.android.**{*;}

# Fresco
-keep class com.facebook.drawee.backends.pipeline.Fresco
-keep @com.facebook.common.internal.DoNotStrip class *
-keepclassmembers class * {
    @com.facebook.common.internal.DoNotStrip *;
}

# OkHttp
-dontwarn okhttp3.**
-keep class okhttp3.** { *; }
-dontwarn okio.**
-keep class okio.** { *; }

# Kotlin
-keep class kotlin.** { *; }
-keep class kotlin.Metadata { *; }
-dontwarn kotlin.**
-keepclassmembers class **$WhenMappings {
    <fields>;
}
-keepclassmembers class kotlin.Metadata {
    public <methods>;
}

# Keep generic signature of Call, Response (R8 full mode strips signatures from non-kept items).
-keep,allowobfuscation,allowshrinking interface retrofit2.Call
-keep,allowobfuscation,allowshrinking class retrofit2.Response
