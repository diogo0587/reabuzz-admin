# Pull Request Template

## 📋 Description
<!-- Provide a clear description of the changes -->

## 🎯 Type of Change
- [ ] 🐛 Bug fix (non-breaking change fixing an issue)
- [ ] ✨ New feature (non-breaking change adding functionality)
- [ ] 💥 Breaking change (fix or feature causing existing functionality to change)
- [ ] 📝 Documentation update
- [ ] 🔧 Refactoring (no functional changes)
- [ ] ⚡ Performance improvement
- [ ] 🔒 Security fix
- [ ] 🧪 Test addition/update

## 🔗 Related Issues
<!-- Link related issues using keywords: Fixes #123, Closes #456, Related to #789 -->
Fixes #
Closes #
Related to #

## 🧪 Testing
<!-- Describe how you tested your changes -->

### Unit Tests
- [ ] All existing unit tests pass
- [ ] New unit tests added for new functionality
- [ ] Test coverage maintained/improved

### Instrumented Tests
- [ ] Tested on API 21 (Android 5.0)
- [ ] Tested on API 34 (Android 14)
- [ ] Tested on arm64 and armeabi-v7a

### Manual Testing
- [ ] App launches without crashes
- [ ] Navigation works correctly
- [ ] Admin dashboard accessible only to admins
- [ ] Role assignment works
- [ ] Audit logging functional
- [ ] Dark/light mode works
- [ ] Screen rotations handled

## 🔒 Security Checklist
- [ ] No hardcoded secrets/keys/passwords
- [ ] No debug logs with sensitive data
- [ ] Network security config updated if needed
- [ ] ProGuard rules updated for new classes
- [ ] Permissions minimized (no unnecessary permissions)
- [ ] Certificate pinning not broken

## 📱 Android Specific
- [ ] minSdk/targetSdk appropriate
- [ ] Version code incremented
- [ ] Version name follows semantic versioning
- [ ] No deprecated APIs used without migration plan
- [ ] App bundle/APK builds successfully
- [ ] Signing config works (release builds)

## 📚 Documentation
- [ ] README updated if needed
- [ ] Code comments added for complex logic
- [ ] Architecture docs updated if architecture changed
- [ ] API changes documented
- [ ] CHANGELOG.md updated

## 📸 Screenshots/Videos
<!-- Add screenshots or screen recordings for UI changes -->

| Before | After |
|--------|-------|
| ![before](url) | ![after](url) |

## ✅ Checklist
- [ ] My code follows the project's style guidelines
- [ ] I have performed a self-review of my code
- [ ] I have commented my code where necessary
- [ ] My changes generate no new warnings/errors
- [ ] I have added tests that prove my fix/feature works
- [ ] New and existing unit tests pass locally
- [ ] Any dependent changes have been merged and published

## 👥 Reviewers
<!-- Tag relevant reviewers -->
/cc @reabuzz-team/android-team
/cc @reabuzz-team/security (for security-related changes)
