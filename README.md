[![Build Status](https://travis-ci.org/FelixKlauke/doctrin.svg?branch=dev)](https://travis-ci.org/FelixKlauke/doctrin)
[![codecov](https://codecov.io/gh/FelixKlauke/doctrin/branch/dev/graph/badge.svg)](https://codecov.io/gh/FelixKlauke/doctrin)

# Doctrin

Ever searched for a reliable and full featured java pub sub library that is still lightweight, easy to use and
configurable for anyone? Then you will kinda like doctrin. It provides a very basic api but can be extended very
easy to use more complex and powerful libraries like rx java. It integrates easily in every infrastructure.

# Usage

_Maven Repository:_
```xml
<repository>
    <id>felix-klauke-releases</id>
    <url>https://repo.felix-klauke.de/repository/maven-releases/</url>
</repository>
```

_Maven Dependencies:_

Client:
```xml
<dependency>
    <groupId>de.felix-klauke</groupId>
    <artifactId>doctrin-client</artifactId>
    <version>1.0.0-RELEASE</version>
</dependency>
```

RxClient:
```xml
<dependency>
    <groupId>de.felix-klauke</groupId>
    <artifactId>doctrin-client-rx</artifactId>
    <version>1.0.0-RELEASE</version>
</dependency>
```

Server (If you want to embed it into your applications):
```xml
<dependency>
    <groupId>de.felix-klauke</groupId>
    <artifactId>doctrin-server</artifactId>
    <version>1.0.0-RELEASE</version>
</dependency>
```

Commons (If you wan to use our utils, warning: May change at any time without warning and backwards support):
```xml
<dependency>
    <groupId>de.felix-klauke</groupId>
    <artifactId>doctrin-commons</artifactId>
    <version>1.0.0-RELEASE</version>
</dependency>
```

# Example
```xml

```

# Architecture
We use Google Guice for internal dependency injection. The logging is managed via logback and the networking is powered
by netty.