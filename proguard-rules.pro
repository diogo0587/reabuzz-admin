# ProGuard/R8 Rules for ReaBuzz Admin

# Keep application class
-keep class com.reabuzz.admin.ReabuzzApplication { *; }

# Keep ViewModels
-keep class com.reabuzz.admin.AdminViewModel { *; }

# Keep Fragments
-keep class com.reabuzz.admin.HomeFragment { *; }
-keep class com.reabuzz.admin.ProfileFragment { *; }
-keep class com.reabuzz.admin.AdminDashboardFragment { *; }

# Keep data classes
-keep class com.reabuzz.admin.AdminViewModel$User { *; }

# Keep enum values
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Keep Parcelable implementations
-keep class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

# Keep Room entities (if added)
-keep class * {
    @androidx.room.Entity *;
    @androidx.room.PrimaryKey *;
    @androidx.room.ColumnInfo *;
    @androidx.room.ForeignKey *;
    @androidx.room.Index *;
}

# Keep Retrofit interfaces (if added)
-keep interface * {
    @retrofit2.http.* *;
}

# Keep WorkManager workers
-keep class * extends androidx.work.Worker { *; }

# Obfuscation settings
-dontoptimize
-dontusemixedcaseclassnames

# Remove logging in release
-assumenosideeffects class android.util.Log {
    public static int v(...);
    public static int d(...);
    public static int i(...);
}
