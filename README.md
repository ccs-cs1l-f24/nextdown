<!--suppress HtmlDeprecatedAttribute -->
<h1 align="center">NextDown</h1>

<!--suppress HtmlDeprecatedAttribute -->
<p align="center">
    <a href="/LICENSE">
        <img src="https://img.shields.io/badge/License-MIT-blue" alt="License: MIT">
    </a>
    <a href="https://github.com/ccs-cs1l-f24/nextdown/actions/workflows/build.yml">
        <img src="https://github.com/ccs-cs1l-f24/nextdown/actions/workflows/build.yml/badge.svg" alt="Build app">
    </a>
</p>

A new calendar app.

NextDown currently targets **desktop** (Linux and Windows) and **web**. It's written in Kotlin and built with Gradle.

**Check out the production deployment at [nextdown.edwinchang.dev](https://nextdown.edwinchang.dev).**

## Development

You can develop NextDown using [Android Studio](https://developer.android.com/studio)
or [IntelliJ IDEA](https://www.jetbrains.com/idea/).
Open the project in your IDE, and the Gradle project should be detected.

## Building

First, clone the repo:

<details open><summary><b>HTTPS</b></summary>

```sh
git clone https://github.com/ccs-cs1l-f24/nextdown.git
```

</details>

<details><summary><b>SSH</b></summary>

```sh
git@github.com:ccs-cs1l-f24/nextdown.git
```

</details>

<details><summary><b>GitHub CLI</b></summary>

```sh
gh repo clone ccs-cs1l-f24/nextdown
```

</details>

Then, you can build the app for desktop or web.

### Desktop

> [!WARNING]
> Currently, builds on macOS and non-Debian-based Linux distros aren't supported.

You can only build the desktop app for the platform you're on,
either Windows or a Debian-based Linux distro.

<details open><summary><b>Linux (Debian-based)</b></summary>

To build a `.deb`:

```sh
./gradlew packageDeb
```

The `.deb` will be placed in `build/compose/binaries/main/deb/`.

You can also create an optimized release build:

```sh
./gradlew packageDeb
```

The `.deb` will be placed in `build/compose/binaries/main-release/deb/`.

</details>

<details><summary><b>Windows</b></summary>

To build a `.exe`:

```ps1
.\gradlew.bat packageExe
```

The `.exe` will be placed in `build\compose\binaries\main\exe\`.

You can also create an optimized release build:

```ps1
.\gradlew.bat packageReleaseExe
```

The `.exe` will be placed in `build\compose\binaries\main-release\exe\`.

</details>

### Web

To build a distribution of the website:

<details open><summary><b>macOS/Linux</b></summary>

```sh
./gradlew webDev
```

The distribution will be located at `build/dist/web/dev/`.

</details>

<details><summary><b>Windows</b></summary>

```ps1
.\gradlew.bat webDev
```

The distribution will be located at `build\dist\web\dev\`.

</details>

You can also build a release distribution of the website,
which takes longer but produces smaller assets:

<details open><summary><b>macOS/Linux</b></summary>

```sh
./gradlew webProd
```

The distribution will be located at `build/dist/web/prod/`.

</details>

<details><summary><b>Windows</b></summary>

```ps1
.\gradlew.bat webProd
```

The distribution will be located at `build\dist\web\prod\`.

</details>

## License

[MIT](./LICENSE)
