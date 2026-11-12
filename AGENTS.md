# AGENTS.md

Persistent notes for agents working in this repository.

## Project

`reabuzz-admin` — an Android admin panel (`com.reabuzz.admin`, Kotlin + Compose,
AGP 8.3.2 / Kotlin 1.9.22, minSdk 24, targetSdk 34). It was created from REA
reverse-engineering analysis of a PolyBuzz APK. Reference APKs belong to the
`ai.socialapps.speakmaster` / `polyspeak.ai` family.

## Build

```bash
./gradlew assembleDebug          # debug APK (includes src/debug tooling)
./gradlew assembleRelease        # unsigned release APK, no debug code
./gradlew testDebugUnitTest      # 22 JVM unit tests, no device required
./gradlew assembleDebugAndroidTest   # compiles the instrumentation tests
./gradlew lintDebug                   # Android lint
```

The container is ephemeral: `apt` packages (the JDK) vanish on restart while
`/workspace` survives. Reinstall the JDK first if `./gradlew` reports an invalid
`JAVA_HOME`. `ANDROID_HOME=/workspace/android-sdk` and `local.properties`
point at the SDK.

Release signing reads `RELEASE_STORE_FILE`, `RELEASE_STORE_PASSWORD`,
`RELEASE_KEY_ALIAS`, `RELEASE_KEY_PASSWORD` from the environment. When
`RELEASE_STORE_FILE` is unset the release config falls back to the debug
keystore, so `assembleRelease` still yields an APK that `adb install` accepts —
an unsigned one is rejected. CI overrides this with real secrets, and
`release.yml` refuses to publish a debug-signed build. Never commit keystores or
those values.

## CI

Two workflows, both validated with `actionlint`:

- `ci.yml` — on push/PR to `main`/`develop`. Job `verify` builds, runs the unit
  tests and lint; job `apk` re-builds and uploads the debug APK, first asserting
  with `apksigner verify` and `aapt2 dump badging` that it is signed and has a
  launchable activity.
- `release.yml` — on `v*` tags or manual dispatch. Requires the four signing
  secrets, builds the signed release, refuses a debug-signed result, and
  publishes a GitHub Release with a SHA-256 file.

`android-actions/setup-android` only installs cmdline-tools, so each job runs
`sdkmanager` explicitly for `android-34` and `build-tools;34.0.0`; without
build-tools `apksigner` and `aapt2` are absent. The container here is
x86_64 with no KVM, so `connectedAndroidTest` is not part of CI — the Compose
instrumentation tests are compiled but not executed.

## Reverse engineering an APK with REA

REA is available as an MCP server and a skill (`reverse-engineer-anything`).
It is NOT preinstalled in a fresh container. Reproduce this setup:

```bash
# 1. Full JDK 17+ (jdk.compiler is required, headless build is enough)
sudo apt-get update && sudo apt-get install -y openjdk-21-jdk-headless

# 2. Pinned headless JADX MCP bridge (REA does not ship it)
mkdir -p ~/.local/share/rea/android
curl -fL -o ~/.local/share/rea/android/jadx-headless-mcp-0.7.1-all.jar \
  https://github.com/1013503897/jadx-headless-mcp/releases/download/v0.7.1/jadx-headless-mcp-0.7.1-all.jar
# SHA-256 must be 6e5eacf500b64292bfb73c49797c1958f6ee44646e43e868039ae7feb573ff75

# 3. Optional: apktool for decode_android_resources
mkdir -p ~/.local/share/rea/android/bin
curl -fL -o ~/.local/share/rea/android/bin/apktool.jar \
  https://github.com/iBotPeaches/Apktool/releases/download/v2.11.1/apktool_2.11.1.jar
printf '#!/bin/sh\nexec java -jar "$HOME/.local/share/rea/android/bin/apktool.jar" "$@"\n' \
  > ~/.local/share/rea/android/bin/apktool && chmod +x ~/.local/share/rea/android/bin/apktool
```

Required environment for every REA invocation:

```bash
export REA_JADX_MCP_JAR="$HOME/.local/share/rea/android/jadx-headless-mcp-0.7.1-all.jar"
export REA_JADX_OPERATION_TIMEOUT_MS=600000   # large APKs exceed the 120000 default
export REA_JADX_HEAP_MIB=6144
export REA_APKTOOL_COMMAND="$HOME/.local/share/rea/android/bin/apktool"  # optional
```

Verify with `npx -y rea-agents@latest doctor --provider jadx --json`.

### Practical notes

- The first call in a process pays ~35 s to start the JADX worker and index the
  APK; subsequent calls are sub-second. Keep one process/session alive for a
  batch of queries instead of re-invoking the CLI per query.
- `search_android_classes` matches class names only (substring). `"com.snapquiz.app"`
  returns all 1661 app-owned classes; a term like `Export` returns almost nothing
  because names are obfuscated.
- `inspect_android_package` gives identity, permissions, components and the
  decoded manifest text inline.
- `inspect_artifact` output can exceed the 10 MiB stdio budget over MCP. Use the
  CLI (`rea inspect-artifact <apk> --json`) and feed the saved JSON to
  `rea project-android-application-graph <inputJson>`.
- `project_android_application_graph` requires `inspect_artifact` Evidence, not
  `inspect_android_package` Evidence.
- REA retains source ranges, not literal source text, in its graphs. For exact
  string/path questions, read the decompiled source or decode resources directly.

## Architecture

- `model/` — `Role`, `Permission`, `Account`. Roles are additive; VIP is
  independent of ADMIN.
- `auth/AdminPolicy.kt` — the single source of truth mapping roles to
  permissions. `AuthorizationService` is the seam a real backend implements;
  `DemoAuthorizationService` is the offline stand-in (its roles are compiled in,
  so it protects nothing real).
- `audit/` — append-only audit trail. Denied attempts are recorded too.
- `ui/` — Compose screens. `MainActivity` is the single activity; navigation is
  in-app state, not fragments.
- `src/debug/` — debug-only `DebugApplication` + `DebugAuthorizationService`,
  selected by a manifest override. `Permission.DEBUG_TOOLS` additionally requires
  `BuildConfig.DEBUG_TOOLS_ENABLED`, so a release build cannot grant it even to
  an admin. Verified: the release APK's dex contains no debug symbols.

The owner account (seeded ADMIN + VIP) is set by the `reabuzz.ownerUsername`
Gradle property in `gradle.properties`.

## Target APK analysed (2026-11)

`target.apk` — `ai.socialapps.speakmaster` (label **PolyBuzz**) v2.3.0 (353),
minSdk 24 / targetSdk 36, 162 MB, 72 241 classes, 20 dex files.
sha256 `ad2169046dc271e137bd548332ecbc310440d67ded85bcaeddc6f75acd753d40`.

Findings worth remembering:

- Backend hosts: `api.polyspeak.ai`, `app.polyspeak.ai`, `cdn.polyspeak.ai`,
  `sanxia.polyspeak.ai`, `stats-cdn.polyspeak.ai`.
- App is heavily hybrid: `com.snapquiz.app.HyWebActivity` and
  `com.snapquiz.app.home.HomeWebFragment` render remote HTML from
  `app.polyspeak.ai/static/hy/...` in a WebView.
- `res/xml/network_security_config.xml` sets `cleartextTrafficPermitted="true"`
  and trusts user-installed CAs in `debug-overrides`.
- Native libs are absent (`native_libraries: 0`), so the bridge-candidate surface
  is empty; `extractNativeLibs="true"` is still declared.
- Application class: `com.zuoyebang.appfactory.base.BaseApplication`
  (`android:persistent="true"`, `allowBackup="false"`, `largeHeap="true"`).
- Launcher entry: `com.snapquiz.app.InitActivity` (singleTask, exported).
