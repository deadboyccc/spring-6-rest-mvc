[![Java CI with Maven](https://github.com/deadboyccc/spring-6-rest-mvc/actions/workflows/maven.yml/badge.svg)](https://github.com/deadboyccc/spring-6-rest-mvc/actions/workflows/maven.yml)
[![CodeQL - Java Only](https://github.com/deadboyccc/spring-6-rest-mvc/actions/workflows/codeql.yml/badge.svg)](https://github.com/deadboyccc/spring-6-rest-mvc/actions/workflows/codeql.yml)
[![Dependabot Updates](https://github.com/deadboyccc/spring-6-rest-mvc/actions/workflows/dependabot/dependabot-updates/badge.svg)](https://github.com/deadboyccc/spring-6-rest-mvc/actions/workflows/dependabot/dependabot-updates)

# spring-6-rest-mvc

Minimal example REST API using Spring Framework 6 and Spring MVC.

## Tech stack

- Java (recommended 17+)
- Spring Framework 6 (Spring MVC for REST controllers)
- Maven for build and dependency management
- Testing: JUnit (unit tests), Spring Test / MockMvc (controller/integration testing)
- Docker (optional — repository includes a Dockerfile)
- CI: GitHub Actions (Maven build), CodeQL, Dependabot

## TDD — how this project is tested-first

This project follows a Test-Driven Development (TDD) approach: tests are written (or updated) before implementation changes, then code is implemented to make the tests pass and finally refactored.

Typical workflow:
1. Add a failing unit or controller test that describes the desired behavior (use JUnit + Mockito or MockMvc for controller tests).
2. Run the test suite and confirm the new test fails.
3. Implement the minimal code to make the test pass.
4. Run the full test suite and refactor while keeping tests green.
5. Commit and push; CI validates with GitHub Actions.

Common commands:
- Run all tests:
  ```bash
  mvn test
  ```
- Run the application:
  ```bash
  mvn spring-boot:run
  # or
  java -jar target/*.jar
  ```
- Run a single test (example):
  ```bash
  mvn -Dtest=MyControllerTest test
  ```

IDE support (IntelliJ/VS Code) can be used to run and debug individual tests during TDD cycles.

## Quick start

Prerequisites:
- Java 17+
- Maven 3.8+

Build and run:
```bash
mvn clean package
mvn spring-boot:run
```

The application runs by default on port 8080. Controller sources and API routes are in `src/main/java`.

## Contributing

Contributions are welcome. Follow the TDD workflow: open an issue or a PR with tests demonstrating the change.

## License

See the repository for license information.