<h1 align="center">
  <br>
  <a>[FPTU] LABATE</a>
  <br>
  MENTORING ME [BACKEND]
  <br>
</h1>

<h4 align="center"> ---</h4>

<p align="center">
    <a alt="Java">
        <img src="https://img.shields.io/badge/Java-v11-orange.svg" />
    </a>
    <a alt="Spring Boot">
        <img src="https://img.shields.io/badge/Spring%20Boot-v2.5.8-brightgreen.svg" />
    </a>
    <a alt="MySql">
        <img src="https://img.shields.io/badge/MySql-v8.0-blue.svg" />
    </a>
    <a alt="Docker">
        <img src="https://img.shields.io/badge/Docker-v20-yellowgreen.svg" />
    </a>
    <a alt="Dependencies">
        <img src="https://img.shields.io/badge/dependencies-up%20to%20date-brightgreen.svg" />
    </a>
</p>
<hr>

#### 1. Document

#### 2. API Document

- [https://mentoring-me.labatelab.com/swagger-ui.html#/](https://mentoring-me.labatelab.com/swagger-ui.html#/)

#### 3. Structure project

---

# Workforce Mentoring Me Backend documents

## Overview

The Mentoring Me Backend is a web application that is used to:

+ Connect with the students and mentors without third-party services
+ Provide a platform for students to find mentors
+ Provide a platform for mentors to find students
+ ...

## Architecture

More details can be found [here](docs/architecture.md).

## Domains

| Environment | Domain                             |
|-------------|------------------------------------|
| LAB         | https://mentoring-me.labatelab.com |
| PRODUCTION  | https://mentoring-me.labate.com    |

## Tech stack

### Dependent services

| Name       | Type     | Version | Desc                                                         |
|------------|----------|---------|--------------------------------------------------------------|
| Mysql      | Database | 8.0     | Mysql replication with Master/Slave mode, binlog enable      |

### Development platform/language

| Service/Apps | Language | Platform/Lib           |
|--------------|----------|------------------------|
| Backends     | Java     | Spring Framework 2.5.8 |

### Third-party services

| Name | Purpose | Type | Desc |
|------|---------|------|------|

## Features

More details can be found [here](docs/feature/features.md).

## Components

The components are scoped into only 3 main sections:

More details can be found [here](docs/components/README.md).

## Third-party integration

...
