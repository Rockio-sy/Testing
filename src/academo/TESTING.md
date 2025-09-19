## UNIT (offline) :: report

```bash
rm -rf target
mvn clean test -Dgroups=unit -DexcludedGroups=db -Dallure.results.directory=target/allure-results
mvn io.qameta.allure:allure-maven:2.12.0:report
```

## DB (Testcontainers) :: single report

```bash
rm -rf target/allure-results target/site/allure-maven-plugin
mvn test -Dgroups=db -Dallure.results.directory=target/allure-results
mvn io.qameta.allure:allure-maven:2.12.0:report
```

## Combined (unit + db) :: single report

```bash
rm -rf target
mvn clean test -Dgroups="unit,db" -Dallure.results.directory=target/allure-results
mvn io.qameta.allure:allure-maven:2.12.0:report
```

## JVM process experiments — count forks 

```bash
# Single JVM for UNIT (expect output: 1)
mvn -Psingle-jvm clean test -Dgroups=unit -DexcludedGroups=db -X | grep -c "Forking command line"

# One JVM per TEST CLASS for DB ITs (≈ number of test classes executed)
mvn -Pper-class-fork clean test -Dgroups=db -X | grep -c "Forking command line"

# Parallel forks for DB ITs (≈ number of concurrent forks created up front)
mvn -Pparallel-forks clean test -Dgroups=db -X | grep -c "Forking command line"
```

## JVM process experiments — show each fork line

```bash
mvn -Psingle-jvm clean test -Dgroups=unit -DexcludedGroups=db -X | grep "Forking command line"
mvn -Pper-class-fork clean test -Dgroups=db -X | grep "Forking command line"
mvn -Pparallel-forks clean test -Dgroups=db -X | grep "Forking command line"
```
