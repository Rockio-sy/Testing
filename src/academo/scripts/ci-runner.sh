#!/usr/bin/env bash
set -euo pipefail

# Helper: wipe target without removing the mount point
safe_clean() { rm -rf target/* target/.[!.]* || true; }

echo "== Stage 1: UNIT (offline, H2) =="
safe_clean
mvn -B test \
  -Dspring.profiles.active=unit \
  -Dgroups=unit -DexcludedGroups=db \
  -Dallure.results.directory=target/allure-results

echo "== Stage 2: DB (classic 'db' group) =="
# keep previous results dirs clean
rm -rf target/allure-results target/site/allure-maven-plugin || true
mvn -B test \
  -Dspring.profiles.active=unit \
  -Dgroups=db \
  -Dallure.results.directory=target/allure-results

echo "== Stage 3: Integration / E2E (Postgres Testcontainers) =="
# IMPORTANT: do not run surefire again; call failsafe only
mvn -B test-compile \
  -Dit.test=MvpE2EITCase \
  failsafe:integration-test failsafe:verify

echo "== DONE =="
