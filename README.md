# idea-helm-viewer
<!-- Plugin description -->
## Description
* Плагин для [IntelliJ IDEA](https://www.jetbrains.com/idea/)
* Позволяет в реальном времени просматривать результирующий YAML, после команды `helm template build .`
* Особенности:
  * Обновление зависимостей.
  * Возможность указать ca.crt репозитория с зависимостями.
  * Возможность указать логин и пароль репозитория с зависимостями.
cswssaxsss
## Settings 
* `Project chart path` - директория с Chart.yaml
* `Values path` - путь к файлу values.yaml
* `Helm path` - путь к утилите helm.exe
* `Update chart dependency` - загрузка зависимостей чарта.
* `optional` - не обязательные настройки
  * `ca.crt` - Корневой сертификат приватного репозитория
  * `User` - Пользователь приватного репозитория
  * `Password` - Пароль приватного репозитория

## Installation

- Manually:

  Download the [latest release](https://github.com/FZEN475/idea-helm-viewer/releases/latest) and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>

<!-- Plugin description end -->

---
Plugin based on the [IntelliJ Platform Plugin Template][template].

[template]: https://github.com/JetBrains/intellij-platform-plugin-template
[docs:plugin-description]: https://plugins.jetbrains.com/docs/intellij/plugin-user-experience.html#plugin-description-and-presentation
