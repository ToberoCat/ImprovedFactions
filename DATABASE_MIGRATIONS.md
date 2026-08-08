# Database migrations

ImprovedFactions supports SQLite for zero-configuration installations and
MariaDB/MySQL through MariaDB Connector/J. Exposed remains the DAO/query layer;
it no longer creates or alters tables.

Flyway runs before Exposed is initialized:

- SQLite migrations live in `src/main/resources/db/migration/sqlite`.
- MariaDB/MySQL migrations live in `src/main/resources/db/migration/mysql`.
- New changes must be added as a new versioned migration. Existing migration
  files must never be edited after release.

The initial migration uses `baselineOnMigrate`. A fresh database executes V1;
an existing database created by the former Exposed schema automation is
baselined so its data is left untouched. Future schema changes must be
backwards-compatible and must not drop or rewrite user data implicitly.

The Paper-threading audit and remaining synchronous DAO call sites are tracked
in `PAPER_DB_AUDIT.md`. Database work must be moved to an async/serialized
storage boundary before it is taken off the server thread; Bukkit objects and
event cancellation must remain on the main thread.

The MariaDB integration test is enabled with:

```text
MARIADB_TEST_URL=jdbc:mariadb://127.0.0.1:3307/improvedfactions
MARIADB_TEST_USER=improvedfactions
MARIADB_TEST_PASSWORD=improvedfactions
```

Without `MARIADB_TEST_URL`, the live MariaDB test is skipped so normal local
builds do not require a database server.
