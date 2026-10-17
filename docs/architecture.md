# Architecture Documentation

## Original APK Architecture (PolyBuzz_2.2.82.apk)

### APK Structure
```
PolyBuzz_2.2.82.apk
├── AndroidManifest.xml          # Compressed in ZIP
├── classes.dex                  # Main DEX (offset ~4.7MB)
├── resources.arsc              # Compiled resources
├── res/                         # Resources
│   ├── layout/                  # XML layouts
│   ├── drawable/                # Images
│   ├── values/                  # Strings, dims, colors
│   └── xml/                     # Config XMLs
├── lib/                         # Native libraries
│   └── arm64-v8a/              # ARM64 .so files
├── META-INF/                    # Signing metadata
│   ├── MANIFEST.MF
│   ├── CERT.RSA
│   └── CERT.SF
└── assets/                      # Raw assets
```

### AndroidManifest Components (Inferred)
- **Package**: com.polybuzz.app (inferred)
- **Min SDK**: 21+ (Android 5.0)
- **Target SDK**: 34 (Android 14)
- **Permissions**: INTERNET, ACCESS_NETWORK_STATE, CAMERA, RECORD_AUDIO, etc.
- **Components**: 
  - Multiple Activities (Main, Chat, Profile, Settings)
  - Services (Push, Media, Background)
  - Receivers (Boot, Network, Push)
  - Providers (Content, File)

### DEX Analysis
- **Format**: Non-standard "dx\n037\0" header (not standard 0x1b707874)
- **Classes**: Multiple (exact count requires JADX)
- **Methods**: Multiple (exact count requires JADX)
- **Strings**: Found references to "admin", "moderator", "role", "permission"

---

## New Application Architecture

### Package Structure
```
com.reabuzz.admin/
├── MainActivity                 # Entry point, navigation setup
├── viewmodel/
│   └── AdminViewModel          # State management, auth, admin logic
├── ui/
│   ├── home/
│   │   └── HomeFragment        # Home screen
│   ├── profile/
│   │   └── ProfileFragment     # User profile
│   └── admin/
│       └── AdminDashboardFragment # Admin panel
├── theme/
│   └── UiTheme                 # Material3 theming
└── model/
    └── User                    # Data models
```

### Navigation Graph
```
mobile_navigation (start: nav_home)
├── nav_home         -> HomeFragment
├── nav_profile      -> ProfileFragment  
└── nav_admin_dashboard -> AdminDashboardFragment
```

### Data Flow
```
User Input -> ViewModel -> Repository -> API/Local DB
     ^                                     |
     |_____________ State Flow ____________|
```

### Security Architecture
```
┌─────────────┐     ┌──────────────┐     ┌─────────────┐
│   Client    │---->│  API Gateway │---->│  Auth Svc   │
│  (UI/VM)    │     │  (Validation)│     │  (JWT/OAuth)│
└─────────────┘     └──────────────┘     └─────────────┘
       |                                        |
       v                                        v
┌─────────────────────────────────────────────────────┐
│                   Audit Log                          │
│  (All admin actions logged server-side)             │
└─────────────────────────────────────────────────────┘
```

---

## Component Relationships

| Component | Depends On | Responsibility |
|-----------|------------|----------------|
| MainActivity | Navigation, ViewModel | App bootstrap, nav setup |
| AdminViewModel | NavController, Repository | Auth state, admin ops |
| HomeFragment | ViewModel | Home UI |
| ProfileFragment | ViewModel | Profile UI, role display |
| AdminDashboardFragment | ViewModel | Admin UI, actions |

---

## Technology Stack

| Layer | Technology |
|-------|------------|
| Language | Kotlin 1.9+ |
| UI | Jetpack Compose / XML + ViewBinding |
| Architecture | MVVM + Repository |
| DI | Manual (no Dagger/Hilt for simplicity) |
| Network | Retrofit (planned) |
| Database | Room (planned) |
| Auth | Custom + Backend validation |
| Testing | JUnit, Espresso, Compose Test |

---

## Build Configuration

```gradle
android {
    compileSdk 34
    defaultConfig {
        minSdk 21
        targetSdk 34
        versionCode 1
        versionName "1.0"
    }
    buildFeatures { viewBinding true }
}
```

---

## Dependencies

```gradle
dependencies {
    implementation 'androidx.core:core-ktx:1.13.0'
    implementation 'androidx.lifecycle:lifecycle-runtime-ktx:2.8.0'
    implementation 'androidx.appcompat:appcompat:1.7.0'
    implementation 'com.google.android.material:material:2.0.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
}
```
