# LongBark Maintenance Android App

A native Android application for monitoring and managing 60+ client websites with the LongBark platform. Built specifically for GrapheneOS with Android 15 (API 35).

## Features

- **Real-time Monitoring**: Track uptime, SSL certificates, broken links, and performance metrics
- **Push Notifications**: NTFY-based notifications (no Firebase/GMS required)
- **Offline Support**: Full offline caching with SQLCipher encrypted database
- **Client Management**: Organize and monitor sites by client
- **Dashboard**: Overview of all site health statuses
- **Reports**: Generate and view site health reports
- **Security**: JWT authentication, biometric unlock, certificate pinning
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

### NTFY Notifications

Configure NTFY server in the app Settings:
1. Open Settings
2. Navigate to Sync Settings
3. Set NTFY Server URL
4. Enable notifications

The app subscribes to topics for real-time alerts.

### Database Encryption

The app uses SQLCipher for database encryption. The encryption passphrase is hardcoded in `DatabaseModule.kt`. For production, consider:
- Storing passphrase in Android Keystore
- Generating unique passphrase per installation
- Using user-derived encryption key

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

## Known Limitations

Current implementation provides core functionality. Future enhancements:

- **Additional Screens**: Client details, site details, reports viewer, settings
- **Biometric Auth**: Integration with BiometricPrompt API
- **Certificate Pinning**: Add production certificate pins
- **Complete NTFY**: Full JSON parsing and notification types
- **Widgets**: Home screen widgets
- **Deep Linking**: Complete deep link handling
- **Multi-language**: i18n support
- **Unit Tests**: Comprehensive test coverage

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
