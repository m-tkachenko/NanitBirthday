# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# ===== GENERAL RULES =====
# Keep source file names and line numbers for better crash reports
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# Keep generic signatures for better reflection support
-keepattributes Signature
-keepattributes *Annotation*

# ===== KOTLIN SPECIFIC RULES =====
# Keep Kotlin metadata for reflection
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt

# Keep Kotlin coroutines
-keepclassmembernames class kotlinx.** {
    volatile <fields>;
}

# ===== ANDROIDX AND COMPOSE RULES =====
# Keep Compose classes
-keep class androidx.compose.runtime.** { *; }
-keep class androidx.compose.ui.** { *; }
-keep class androidx.compose.material3.** { *; }

# Keep ViewModel classes
-keep class * extends androidx.lifecycle.ViewModel {
    <init>(...);
}

# ===== ROOM DATABASE RULES =====
# Keep Room entities and DAOs
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-keep @androidx.room.Dao class *
-keep class * extends androidx.room.RoomDatabase {
    public abstract *;
}

# ===== HILT/DAGGER RULES =====
# Keep Hilt generated classes
-keep class dagger.hilt.** { *; }
-keep class * extends dagger.hilt.android.internal.lifecycle.HiltViewModelMap$KeySet { *; }
-keep class **_HiltModules { *; }
-keep class **_HiltModules$* { *; }

# Keep injected constructors
-keepclasseswithmembers class * {
    @javax.inject.Inject <init>(...);
}

# ===== GSON/JSON RULES (if you add JSON parsing later) =====
# Keep data classes for JSON serialization
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# ===== PERFORMANCE OPTIMIZATIONS =====
# Optimize code more aggressively
-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*
-optimizationpasses 5
-allowaccessmodification

# Remove logging in release builds
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
}

# ===== ADDITIONAL SAFETY RULES =====
# Don't warn about missing classes that are not part of the app
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**

# Keep native methods
-keepclasseswithmembernames class * {
    native <methods>;
}

# Keep enums
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}