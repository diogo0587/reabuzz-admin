#!/bin/bash
# Setup GitHub for ReaBuzz Admin
# Execute este script na sua máquina local após clonar/copiar o projeto

set -e

echo "🚀 Configurando GitHub para ReaBuzz Admin"
echo "========================================="

# Verificar se estamos no diretório correto
if [ ! -f "build.gradle" ] || [ ! -d "src/main/java/com/reabuzz/admin" ]; then
    echo "❌ Execute este script na raiz do projeto newapp/"
    exit 1
fi

# 1. Inicializar Git
echo "📦 Inicializando repositório Git..."
git init

# 2. Configurar .gitignore se não existir
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
*.iws
*.ipr

# OS
.DS_Store
Thumbs.db

# Keystore (NUNCA commitar!)
*.jks
*.keystore
keystore.jks

# APKs gerados
*.apk
*.aab

# Logs
*.log

# Temp
*.tmp
*.temp
GITIGNORE_EOF
    echo "✅ .gitignore criado"
fi

# 3. Adicionar todos os arquivos
echo "📁 Adicionando arquivos..."
git add .

# 4. Commit inicial
echo "💾 Criando commit inicial..."
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

# 4. Instruções para GitHub
echo ""
echo "✅ Repositório local criado com sucesso!"
echo ""
echo "🔗 PRÓXIMOS PASSOS NO GITHUB:"
echo "================================"
echo ""
echo "1. Crie o repositório no GitHub:"
echo "   https://github.com/new"
echo "   - Nome: reabuzz-admin"
echo "   - Descrição: Android admin panel with RBAC, JWT auth, audit logging"
echo "   - Público ou Privado (sua escolha)"
echo "   - NÃO inicialize com README/LICENSE/.gitignore (já temos)"
echo ""
echo "2. Conecte e faça push:"
echo "   git remote add origin https://github.com/SEU_USUARIO/reabuzz-admin.git"
echo "   git branch -M main"
echo "   git push -u origin main"
echo ""
echo "3. Configure Secrets (Settings > Secrets > Actions):"
echo "   RELEASE_STORE_FILE        # base64(keystore.jks)"
echo "   RELEASE_STORE_PASSWORD    # senha do keystore"
echo "   RELEASE_KEY_ALIAS         # ex: reabuzz"
echo "   RELEASE_KEY_PASSWORD      # senha da chave"
echo ""
echo "   Opcional (Play Store):"
echo "   PLAY_SERVICE_ACCOUNT_JSON # Service account JSON"
echo ""
echo "4. Configure Branch Protection (Settings > Branches):"
echo "   - Require PR reviews (1+)"
echo "   - Require status checks: lint, unit-tests, build-debug"
echo "   - Require signed commits"
echo ""
echo "5. Gere keystore (se não tiver):"
echo "   make keystore"
echo "   # ou: keytool -genkey -v -keystore keystore.jks -keyalg RSA -keysize 2048 -validity 10000 -alias reabuzz"
echo ""
echo "6. Primeira release:"
echo "   git tag v1.0.0 && git push origin v1.0.0"
echo "   # Ou use workflow_dispatch na aba Actions"
echo ""
echo "🎉 Pronto! O pipeline CI/CD estará ativo no primeiro push."
