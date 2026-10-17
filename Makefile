# Makefile for ReaBuzz Admin - Local Development Commands
# Usage: make <target>

# Default target
.DEFAULT_GOAL := help

# Variables
GRADLE := ./gradlew
APK_DEBUG := app/build/outputs/apk/debug/app-debug.apk
APK_RELEASE := app/build/outputs/apk/release/app-release.apk

# Colors
GREEN  := \033[0;32m
YELLOW := \033[1;33m
RED    := \033[0;31m
NC     := \033[0m

# ============================================================
# Help
# ============================================================
help:
	@echo "$(GREEN)ReaBuzz Admin - Development Commands$(NC)"
	@echo ""
	@echo "$(YELLOW)Build:$(NC)"
	@echo "  make debug       - Build debug APK"
	@echo "  make release     - Build release APK (requires keystore)"
	@echo "  make clean       - Clean build artifacts"
	@echo ""
	@echo "$(YELLOW)Test:$(NC)"
	@echo "  make test        - Run unit tests"
	@echo "  make test-all    - Run all tests (unit + instrumented)"
	@echo "  make connected   - Run instrumented tests (needs device/emulator)"
	@echo ""
	@echo "$(YELLOW)Quality:$(NC)"
	@echo "  make lint        - Run lint checks"
	@echo "  make detekt      - Run detekt static analysis"
	@echo "  make format      - Format code with ktlint"
	@echo ""
	@echo "$(YELLOW)Install:$(NC)"
	@echo "  make install     - Install debug APK to connected device"
	@echo "  make uninstall   - Uninstall app from device"
	@echo ""
	@echo "$(YELLOW)Utilities:$(NC)"
	@echo "  make deps        - Show dependency tree"
	@echo "  make deps-update - Check for dependency updates"
	@echo "  make apk-info    - Show APK info (size, permissions, etc.)"
	@echo "  make help        - Show this help"

# ============================================================
# Build Targets
# ============================================================
debug:
	@echo "$(GREEN)Building Debug APK...$(NC)"
	$(GRADLE) assembleDebug

release:
	@echo "$(GREEN)Building Release APK...$(NC)"
	@if [ ! -f keystore.jks ]; then \
		echo "$(RED)Error: keystore.jks not found!$(NC)"; \
		echo "Run: make keystore"; \
		exit 1; \
	fi
	$(GRADLE) assembleRelease

clean:
	@echo "$(YELLOW)Cleaning build artifacts...$(NC)"
	$(GRADLE) clean

# ============================================================
# Test Targets
# ============================================================
test:
	@echo "$(GREEN)Running Unit Tests...$(NC)"
	$(GRADLE) test

test-all:
	@echo "$(GREEN)Running All Tests...$(NC)"
	$(GRADLE) test connectedAndroidTest

connected:
	@echo "$(GREEN)Running Instrumented Tests...$(NC)"
	$(GRADLE) connectedAndroidTest

# ============================================================
# Quality Targets
# ============================================================
lint:
	@echo "$(GREEN)Running Lint...$(NC)"
	$(GRADLE) lint

detekt:
	@echo "$(GREEN)Running Detekt...$(NC)"
	$(GRADLE) detekt

format:
	@echo "$(GREEN)Formatting code with ktlint...$(NC)"
	$(GRADLE) ktlintFormat

# ============================================================
# Install/Device Targets
# ============================================================
install:
	@echo "$(GREEN)Installing Debug APK...$(NC)"
	$(GRADLE) installDebug

uninstall:
	@echo "$(YELLOW)Uninstalling app...$(NC)"
	adb uninstall com.reabuzz.newapp

install-release:
	@echo "$(GREEN)Installing Release APK...$(NC)"
	$(GRADLE) installRelease

# ============================================================
# Utility Targets
# ============================================================
deps:
	@echo "$(GREEN)Dependency Tree...$(NC)"
	$(GRADLE) dependencies

deps-update:
	@echo "$(GREEN)Checking for Dependency Updates...$(NC)"
	$(GRADLE) dependencyUpdates

apk-info:
	@echo "$(GREEN)Debug APK Info:$(NC)"
	@if [ -f $(APK_DEBUG) ]; then \
		echo "Size: $$(du -h $(APK_DEBUG) | cut -f1)"; \
		aapt dump badging $(APK_DEBUG) | head -30; \
	else \
		echo "$(RED)Debug APK not found. Run 'make debug' first.$(NC)"; \
	fi

keystore:
	@echo "$(GREEN)Generating keystore...$(NC)"
	@read -p "Keystore password: " -s STORE_PASS; echo; \
	read -p "Key password: " -s KEY_PASS; echo; \
	read -p "Key alias [reabuzz]: " ALIAS; ALIAS=$${ALIAS:-reabuzz}; \
	keytool -genkey -v -keystore keystore.jks -keyalg RSA -keysize 2048 -validity 10000 \
		-alias $$ALIAS -storepass $$STORE_PASS -keypass $$KEY_PASS \
		-dname "CN=ReaBuzz, OU=Mobile, O=ReaBuzz, L=City, ST=State, C=BR"; \
	echo "$(GREEN)Keystore created: keystore.jks$(NC)"

# ============================================================
# CI Simulation
# ============================================================
ci-local:
	@echo "$(GREEN)Simulating CI pipeline locally...$(NC)"
	$(MAKE) clean
	$(MAKE) lint
	$(MAKE) test
	$(MAKE) debug
	@echo "$(GREEN)✅ Local CI simulation complete!$(NC)"

# ============================================================
# Release Preparation
# ============================================================
prepare-release:
	@echo "$(GREEN)Preparing release...$(NC)"
	@read -p "Version (e.g., 1.0.0): " VERSION; \
	sed -i "s/versionName \".*\"/versionName \"$$VERSION\"/" build.gradle; \
	sed -i "s/versionCode .*/versionCode $$(date +%s)/" build.gradle; \
	echo "$(GREEN)Version updated to $$VERSION$(NC)"

# ============================================================
# Docker (for consistent builds)
# ============================================================
docker-build:
	@echo "$(GREEN)Building with Docker...$(NC)"
	docker run --rm -v $$(pwd):/workspace -w /workspace \
		openjdk:17-slim ./gradlew assembleDebug

.PHONY: help debug release clean test test-all connected lint detekt format install uninstall install-release deps deps-update apk-info keystore ci-local prepare-release docker-build
