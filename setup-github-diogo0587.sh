#!/bin/bash
# setup-github-diogo0587.sh - Configuração GitHub para diogo0587
# PRONTO PARA EXECUTAR - NÃO PRECISA EDITAR

GITHUB_USER="diogo0587"

# ============================================================
# EXECUÇÃO AUTOMÁTICA
# ============================================================

set -e

echo "🚀 Configurando GitHub para ReaBuzz Admin"
echo "👤 Usuário: $GITHUB_USER"
echo "========================================="

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
echo "🔗 Conectando ao GitHub (diogo0587/reabuzz-admin)..."
git remote add origin https://github.com/diogo0587/reabuzz-admin.git
git branch -M main

echo ""
echo "📤 FAZENDO PUSH PARA GITHUB..."
git push -u origin main

echo ""
echo "🎉 CONCLUÍDO! Repositório criado:"
echo "   https://github.com/diogo0587/reabuzz-admin"
echo ""
echo "🔐 PRÓXIMO PASSO OBRIGATÓRIO - Configure Secrets:"
echo "   https://github.com/diogo0587/reabuzz-admin/settings/secrets/actions"
echo ""
echo "   Adicione estes 4 secrets (New repository secret):"
echo "   ┌─────────────────────────┬────────────────────────────────────┐"
echo "   │ RELEASE_STORE_FILE      │ base64 do keystore.jks             │"
echo "   │ RELEASE_STORE_PASSWORD  │ sua senha do keystore              │"
echo "   │ RELEASE_KEY_ALIAS       │ reabuzz                            │"
echo "   │ RELEASE_KEY_PASSWORD    │ senha da chave (pode ser mesma)    │"
echo "   └─────────────────────────┴────────────────────────────────────┘"
echo ""
echo "   Gerar keystore (se não tiver):"
echo "   make keystore"
echo "   # ou: keytool -genkey -v -keystore keystore.jks -keyalg RSA -keysize 2048 -validity 10000 -alias reabuzz"
echo ""
echo "   Converter para base64:"
echo "   base64 -w0 keystore.jks   # Linux/Mac"
echo "   # ou: cat keystore.jks | base64 -w0"
echo ""
echo "🛡️ Branch Protection (recomendado):"
echo "   https://github.com/diogo0587/reabuzz-admin/settings/branches"
echo "   Add rule > main > ✓ Require PR reviews > ✓ Require: lint, unit-tests, build-debug"
echo ""
echo "🎯 Primeira release:"
echo "   git tag v1.0.0 && git push origin v1.0.0"
echo "   # Ou: Actions > Release > Run workflow"
