# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\Administrator\AppData\Local\Android\Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.

# Keep Kotlin Serialization models
-keepattributes *Annotation*, InnerClasses, EnclosingMethod, Signature
-keepclassmembers class ** {
    @kotlinx.serialization.Serializable *;
}

# Keep the serializer() method for all @Serializable classes
-keepclassmembers class * {
    *** Companion;
}

-keepclasseswithmembers class * {
    @kotlinx.serialization.Serializable <methods>;
}

# Keep the domain models specifically to prevent renaming
-keep class com.idontwantcancer.app.domain.model.** { *; }

# Keep presentation models
-keep class com.idontwantcancer.app.presentation.model.** { *; }

# Keep data transfer objects
-keep class com.idontwantcancer.app.data.remote.model.** { *; }

# Hilt and Dagger usually have their own rules, but keeping these helps
-keep class **_HiltModules* { *; }
-keep class **_Factory { *; }
-keep class **_MembersInjector { *; }

# Room rules are usually inherited, but keeping entities is good practice
-keep class com.idontwantcancer.app.data.local.entity.** { *; }
