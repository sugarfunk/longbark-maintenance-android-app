# LongBark Maintenance Android App

A native Android application for monitoring and managing 60+ client websites with the LongBark platform. Built specifically for GrapheneOS with Android 15 (API 35).

## Features

- **Real-time Monitoring**: Track uptime, SSL certificates, broken links, and performance metrics
- **Invoice Ninja Integration**: Full billing and financial tracking with Invoice Ninja API
- **Client Management**: Organize and monitor sites by client with financial insights
- **Billing Dashboard**: View invoices, payments, quotes, and financial stats per client
- **Push Notifications**: Complete NTFY implementation with JSON parsing and deep links
- **Biometric Authentication**: Fingerprint/face unlock support for secure access
- **Deep Linking**: Navigate directly to clients, sites, and notifications from URLs
- **Offline Support**: Full offline caching with SQLCipher encrypted database
- **Dashboard**: Overview of all site health statuses with recent alerts
- **Reports**: Generate and view site health reports
- **Security**: JWT authentication, biometric unlock, encrypted database
- **Material 3 Design**: Modern UI with dynamic theming

## Technical Stack

- **Language**: Kotlin
- **Min SDK**: API 35 (Android 15)
- **Architecture**: MVVM + Clean Architecture
- **DI**: Hilt/Dagger
- **UI**: Jetpack Compose with Material 3
- **Networking**: Retrofit2 + OkHttp
- **Database**: Room + SQLCipher
- **Async**: Kotlin Coroutines + Flow
- **Background**: WorkManager + Foreground Service

## Project Structure

```
app/
├── src/main/java/com/longbark/maintenance/
│   ├── data/                    # Data layer
│   │   ├── local/              # Room database, DAOs, entities
│   │   │   ├── dao/
│   │   │   ├── entities/
│   │   │   ├── database/
│   │   │   └── preferences/
│   │   ├── remote/             # API services, DTOs
│   │   │   ├── api/
│   │   │   ├── dto/
│   │   │   └── interceptors/
│   │   └── repository/         # Repository implementations
│   ├── domain/                 # Domain layer
│   │   ├── model/              # Domain models
│   │   ├── repository/         # Repository interfaces
│   │   └── usecase/            # Business logic use cases
│   ├── presentation/           # UI layer
│   │   ├── auth/               # Login screen
│   │   ├── dashboard/          # Dashboard screen
│   │   ├── navigation/         # Navigation graph
│   │   └── theme/              # Material 3 theme
│   ├── di/                     # Dependency injection modules
│   ├── service/                # Background services
│   └── util/                   # Utilities
└── build.gradle.kts
```

## Build Instructions

### Prerequisites

- Android Studio Ladybug or newer
- JDK 17
- Android SDK 35 (Android 15)
- Gradle 8.7+

### Building the App

1. **Clone the repository**:
   ```bash
   git clone https://github.com/sugarfunk/longbark-maintenance-android-app.git
   cd longbark-maintenance-android-app
   ```

2. **Configure API endpoint** (optional):
   Edit `app/build.gradle.kts` and update:
   ```kotlin
   buildConfigField("String", "API_BASE_URL", "\"https://your-api-domain.com/api/\"")
   buildConfigField("String", "NTFY_SERVER_URL", "\"https://your-ntfy-server.com/\"")
   ```

3. **Build variants**:
   - **Debug**: `./gradlew assembleDebug`
   - **Staging**: `./gradlew assembleStaging`
   - **Release**: `./gradlew assembleRelease`

4. **Install on device**:
   ```bash
   ./gradlew installDebug
   ```

### Release Build

For production release:

1. **Create keystore** (first time only):
   ```bash
   keytool -genkey -v -keystore keystore/release.keystore \
     -alias longbark -keyalg RSA -keysize 2048 -validity 10000
   ```

2. **Configure signing** in `app/build.gradle.kts`:
   ```kotlin
   signingConfigs {
       create("release") {
           storeFile = file("../keystore/release.keystore")
           storePassword = System.getenv("KEYSTORE_PASSWORD")
           keyAlias = System.getenv("KEY_ALIAS")
           keyPassword = System.getenv("KEY_PASSWORD")
       }
   }
   ```

3. **Build release APK**:
   ```bash
   export KEYSTORE_PASSWORD=your_password
   export KEY_ALIAS=longbark
   export KEY_PASSWORD=your_key_password
   ./gradlew assembleRelease
   ```

4. **Output**: `app/build/outputs/apk/release/app-release.apk`

## Configuration

### API Backend

The app connects to a FastAPI backend. Configure the base URL in:
- `app/build.gradle.kts` (compile-time)
- Or via Settings screen (runtime)

Expected endpoints:
- `POST /api/auth/login` - Authentication
- `GET /api/dashboard/stats` - Dashboard data
- `GET /api/clients` - List clients
- `GET /api/sites` - List sites
- `GET /api/reports` - List reports
- And more...

### Invoice Ninja Integration

Configure Invoice Ninja in app preferences:
1. Set Invoice Ninja URL (e.g., https://invoicing.co or your self-hosted instance)
2. Generate API token from Invoice Ninja dashboard
3. Save API token in app settings
4. Link sites to Invoice Ninja clients

The app uses these Invoice Ninja endpoints:
- `GET /api/v1/clients` - List all Invoice Ninja clients
- `GET /api/v1/clients/{id}` - Get client with invoices, payments, quotes
- `GET /api/v1/invoices?client_id={id}` - Client invoices
- `GET /api/v1/payments?client_id={id}` - Client payments
- `GET /api/v1/quotes?client_id={id}` - Client quotes

**Linking Sites to Clients:**
- Navigate to site settings
- Select "Link to Invoice Ninja Client"
- Choose client from dropdown
- View billing information in client detail screen

### NTFY Notifications

Configure NTFY server in the app Settings:
1. Open Settings
2. Navigate to Sync Settings
3. Set NTFY Server URL
4. Enable notifications

The app subscribes to topics for real-time alerts.

### Database Encryption

The app uses SQLCipher for database encryption. Database schema v2 includes:
- **Clients table**: Client data with health tracking
- **Sites table**: Site monitoring data
- **Notifications table**: Alert history
- **Reports table**: Generated reports
- **Site-InvoiceNinja Links table**: Mapping between sites and Invoice Ninja clients

The encryption passphrase is hardcoded in `DatabaseModule.kt`. For production, consider:
- Storing passphrase in Android Keystore
- Generating unique passphrase per installation
- Using user-derived encryption key

### Biometric Authentication

Enable biometric authentication:
1. Open Settings → Security
2. Toggle "Enable Biometric Authentication"
3. System will prompt for fingerprint/face setup if not configured
4. Biometric prompt appears on app launch

Biometric authentication works alongside password authentication as a convenience feature.

## Security Features

- **JWT Authentication**: Secure token-based auth
- **Android Keystore**: Secure credential storage
- **Biometric Auth**: Optional fingerprint/face unlock
- **Certificate Pinning**: Prevent MITM attacks
- **Encrypted Database**: SQLCipher encryption
- **TLS 1.3 Only**: Modern secure connections
- **No Google Services**: Zero GMS dependencies
- **GrapheneOS Optimized**: Leverages hardened security

## GrapheneOS Considerations

This app is specifically designed for GrapheneOS:

1. **No GMS**: Zero dependency on Google Play Services
2. **Hardened Security**: Takes advantage of GrapheneOS security features
3. **Privacy-Focused**: Respects GrapheneOS privacy controls
4. **F-Droid Ready**: Structured for potential F-Droid distribution
5. **Reproducible Builds**: Follows best practices for build reproducibility

## Permissions

Required permissions:
- `INTERNET` - API communication
- `ACCESS_NETWORK_STATE` - Check connectivity
- `POST_NOTIFICATIONS` - Android 13+ notifications
- `FOREGROUND_SERVICE` - NTFY background service
- `FOREGROUND_SERVICE_DATA_SYNC` - Data sync type
- `USE_BIOMETRIC` - Biometric authentication (optional)

## Development

### Running Tests

```bash
# Unit tests
./gradlew test

# Instrumentation tests
./gradlew connectedAndroidTest

# Test coverage
./gradlew jacocoTestReport
```

### Code Style

The project uses standard Kotlin coding conventions:
```bash
./gradlew ktlintCheck
./gradlew ktlintFormat
```

### Adding Features

1. Create domain models in `domain/model/`
2. Define repository interface in `domain/repository/`
3. Implement repository in `data/repository/`
4. Create use cases if needed in `domain/usecase/`
5. Build ViewModel in `presentation/<feature>/`
6. Design UI with Compose in `presentation/<feature>/`

## NEW FEATURES (Latest Update)

### Invoice Ninja Integration
- Full Invoice Ninja API integration for billing management
- Link sites to Invoice Ninja clients for unified tracking
- View invoices, payments, quotes, and financial stats
- Track overdue invoices and payment history
- Billing statistics dashboard per client

### Biometric Authentication
- Fingerprint and face recognition support
- Secure biometric unlock using BiometricPrompt API
- Settings toggle for biometric authentication
- Fallback to password authentication

### Complete NTFY Implementation
- Full JSON message parsing with all NTFY fields
- Priority-based notification channels (Critical, Warning, Info)
- Deep link support from notifications
- Big text style for long messages
- Custom notification actions

### Deep Linking
- Universal links support (https://longbark.app/app/*)
- Custom URL scheme (longbark://*)
- Navigate to specific clients, sites, and notifications
- Deep link handling from NTFY notifications

### Client Management
- Client list with search and filtering
- Client detail screens (in progress)
- Site-to-client association
- Health status tracking per client

## Future Enhancements

- **Client Detail Tabs**: Complete Sites, Billing, and Settings tabs
- **Site Detail Screens**: Full site management interface
- **Settings Screen**: Comprehensive app configuration
- **Reports Viewer**: In-app PDF and HTML report viewing
- **Widgets**: Home screen widgets with Glance API
- **Multi-language**: i18n support
- **Unit Tests**: Comprehensive test coverage (>70%)

## API Integration

### Login Example

```kotlin
POST /api/auth/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password"
}

Response:
{
  "access_token": "eyJ...",
  "refresh_token": "eyJ...",
  "token_type": "bearer",
  "expires_in": 3600,
  "user": {
    "id": "uuid",
    "email": "user@example.com",
    "name": "User Name",
    "created_at": 1234567890
  }
}
```

### Dashboard Example

```kotlin
GET /api/dashboard/stats
Authorization: Bearer eyJ...

Response:
{
  "total_sites": 65,
  "healthy_sites": 60,
  "warning_sites": 3,
  "critical_sites": 2,
  "recent_alerts": [...]
}
```

## Troubleshooting

### Build Issues

**Problem**: `Duplicate class` errors
**Solution**: Clean and rebuild:
```bash
./gradlew clean build
```

**Problem**: `Unable to find kotlinCompilerPluginClasspath`
**Solution**: Update Gradle and AGP versions

### Runtime Issues

**Problem**: App crashes on login
**Solution**: Check API endpoint configuration and network connectivity

**Problem**: Notifications not working
**Solution**: Verify notification permissions granted (Android 13+)

**Problem**: Database errors
**Solution**: Clear app data and reinstall

## Contributing

1. Fork the repository
2. Create feature branch: `git checkout -b feature/amazing-feature`
3. Commit changes: `git commit -m 'Add amazing feature'`
4. Push to branch: `git push origin feature/amazing-feature`
5. Open Pull Request

## License

Copyright © 2024 LongBark. All rights reserved.

## Support

For issues and questions:
- GitHub Issues: https://github.com/sugarfunk/longbark-maintenance-android-app/issues
- Email: support@longbark.app

---

**Built with ❤️ for GrapheneOS and privacy-focused Android users**
