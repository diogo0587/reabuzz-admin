#!/bin/bash
# setup-github-personalized.sh - Configuração GitHub com SEU usuário
# EDITE A VARIÁVEL ABAIXO COM SEU USERNAME DO GITHUB

GITHUB_USER="diogo0587"  # ← ALTERE AQUI

# ============================================================
# NÃO EDITE ABAIXO - EXECUÇÃO AUTOMÁTICA
# ============================================================

set -e

echo "🚀 Configurando GitHub para ReaBuzz Admin"
echo "👤 Usuário: $GITHUB_USER"
echo "========================================="

if [ "$GITHUB_USER" = "SEU_USERNAME_AQUI" ]; then
    echo "❌ ERRO: Edite o script e altere GITHUB_USER="
    exit 1
fi

# Verificar diretório
if [ ! -f "build.gradle" ] || [ ! -d "src/main/java/com/reabuzz/admin" ]; then
    echo "❌ Execute na raiz do projeto newapp/"
    exit 1
fi

# 1. Git init
echo "📦 Inicializando Git..."
git init

# 2. .gitignore
if [ ! -f ".gitignore" ]; then
    cat > .gitignore << 'GITIGNORE_EOF'
# Gradle
.gradle/
build/
local.properties
*.iml
# IDE
.idea/
*.iml
# OS
.DS_Store
Thumbs.db
# Keystore (NUNCA commitar!)
*.jks
*.keystore
keystore.jks
# APKs
*.apk
*.aab
GITIGNORE_EOF
    echo "✅ .gitignore criado"
fi

# 3. Add & Commit
echo "📁 Adicionando arquivos..."
git add .
git commit -m "feat: initial commit - ReaBuzz Admin v1.0.0

- Android app with Kotlin + Jetpack Compose
- Admin dashboard with RBAC (ADMIN/MODERATOR/USER)
- JWT authentication + EncryptedSharedPreferences
- Audit logging for admin actions
- Material3 theming + dark mode
- Certificate pinning + ProGuard
- Unit + instrumented tests
- GitHub Actions CI/CD pipeline
- Complete documentation (6 technical docs)"

# 4. Remote & Push
echo "🔗 Conectando ao GitHub..."
git remote add origin https://github.com/$GITHUB_USER/reabuzz-admin.git
git branch -M main

echo ""
echo "✅ Repositório configurado!"
echo ""
echo "📤 FAZENDO PUSH..."
git push -u origin main

echo ""
echo "🎉 CONCLUÍDO! Verifique:"
echo "   https://github.com/$GITHUB_USER/reabuzz-admin"
echo ""
echo "🔐 PRÓXIMO: Configure Secrets no GitHub:"
echo "   Settings > Secrets > Actions > New repository secret"
echo "   RELEASE_STORE_FILE        # base64(keystore.jks)"
echo "   RELEASE_STORE_PASSWORD"
echo "   RELEASE_KEY_ALIAS         # reabuzz"
echo "   RELEASE_KEY_PASSWORD"
echo ""
echo "🛡️ Branch Protection:"
echo "   Settings > Branches > Add rule > main"
echo "   ✓ Require PR reviews"
echo "   ✓ Require: lint, unit-tests, build-debug"
