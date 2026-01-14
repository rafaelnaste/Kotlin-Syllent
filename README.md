# Syllent Connect

Aplicativo Android em Kotlin para integrar com o SDK da Tuya e BizBundle para pareamento de dispositivos IoT.

## Requisitos

- Android Studio Hedgehog (2023.1.1) ou superior
- JDK 17
- Android SDK 35
- Gradle 8.4

## Configuracao

### Chaves do SDK Tuya

As chaves ja estao configuradas no projeto:
- **AppKey**: `puctaw54epemuetxm4j5`
- **AppSecret**: `ghfuvprrq5f39rwwcr4jhetgqgsxuusg`
- **SHA256**: `D2:D4:F0:F1:7E:E0:A5:A5:61:03:86:C7:3C:72:D7:59:FC:39:A9:FD:91:E1:E7:3A:51:C9:92:D4:D9:30:14:CD`

### Como Executar

1. Clone o repositorio:
```bash
git clone https://github.com/rafaelnaste/Kotlin-Syllent.git
cd Kotlin-Syllent
```

2. Abra o projeto no Android Studio

3. Sincronize o Gradle (Sync Now)

4. Execute o aplicativo em um dispositivo ou emulador

## Estrutura do Projeto

```
app/
├── src/main/
│   ├── java/com/syllent/connectdev/
│   │   ├── SyllentApplication.kt      # Inicializacao do SDK Tuya
│   │   ├── service/
│   │   │   └── BizBundleFamilyServiceImpl.kt  # Servico de familia/casa
│   │   └── ui/
│   │       ├── LoginActivity.kt       # Tela de login com email/senha
│   │       └── MainActivity.kt        # Tela principal com pareamento
│   ├── res/
│   │   ├── layout/                    # Layouts XML
│   │   ├── values/                    # Cores, strings, estilos
│   │   └── drawable/                  # Drawables e icones
│   ├── assets/
│   │   ├── thingTheme/               # Configuracao de tema
│   │   └── activator_auto_search_capacity.json
│   └── AndroidManifest.xml
└── build.gradle.kts
```

## Funcionalidades

### Login
- Autenticacao com email e senha via SDK Tuya
- Validacao de campos
- Redirecionamento automatico se ja logado

### Tela Principal
- Carregamento automatico da primeira casa do usuario
- Inicializacao do BizBundle apos carregar a casa
- Botao para adicionar dispositivos (abre UI de pareamento)
- Botao para escanear QR Code
- Logout com confirmacao

## Dependencias Principais

- **Tuya SmartLife SDK**: `com.thingclips.smart:thingsmart:6.11.1`
- **Tuya BizBundle BOM**: `com.thingclips.smart:thingsmart-BizBundlesBom:6.11.1`
- **Device Activator**: `com.thingclips.smart:thingsmart-bizbundle-device_activator`
- **QR Code Scanner**: `com.thingclips.smart:thingsmart-bizbundle-qrcode_mlkit`
- AndroidX, Material Components, Kotlin Coroutines

## Fluxo de Uso

1. Abra o app e faca login com suas credenciais Tuya
2. Apos login, o app carrega automaticamente a primeira casa
3. O BizBundle e inicializado com os dados da casa
4. Use "Adicionar Dispositivo" para parear novos dispositivos
5. Use "Escanear QR Code" para parear via QR code

## Documentacao Tuya

- [SmartLife App SDK](https://developer.tuya.com/en/docs/app-development/android-sdk/integration/integrate-sdk)
- [UI BizBundle](https://developer.tuya.com/en/docs/app-development/android-bizbundle-sdk/introduction)
- [Device Pairing](https://developer.tuya.com/en/docs/app-development/android-bizbundle-sdk/device-pairing)

## Licenca

Proprietary - Syllent
