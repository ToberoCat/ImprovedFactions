# Database Setup

ImprovedFactions uses **SQLite by default**, so a fresh installation does not
require a database server. For larger or multi-server installations, you can
use **MariaDB or MySQL** instead.

The plugin uses MariaDB Connector/J for both database products. In the
configuration, the external database option is named `mysql` even when the
server is MariaDB.

## 1. Create a database and user

Create a dedicated database and user on your MariaDB/MySQL server. For
example:

```sql
CREATE DATABASE improvedfactions
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

CREATE USER 'improvedfactions'@'minecraft-server-host'
  IDENTIFIED BY 'replace-with-a-strong-password';

GRANT ALL PRIVILEGES ON improvedfactions.*
  TO 'improvedfactions'@'minecraft-server-host';

FLUSH PRIVILEGES;
```

Replace `minecraft-server-host` with the hostname or IP address from which
your Paper server connects. If your hosting provider requires a wildcard
host, use `'%'` only when necessary and protect the database with a firewall
or private network.

Most Minecraft hosting providers create the database and credentials for you.
Use the host, port, database name, username, and password shown in the
provider's database panel.

## 2. Configure the plugin

Edit `plugins/ImprovedFactions/config.yml`:

```yaml
database: "mysql"

mysql:
  host: "db.example.com"
  port: 3306
  database: "improvedfactions"
  user: "improvedfactions"
  password: "replace-with-a-strong-password"
  maximum-pool-size: 4
```

The default connection port is `3306`. `maximum-pool-size` controls the
bounded connection pool used by the plugin; values are limited to the range
`1` to `16`. Start with the default and increase it only when your server and
database need more concurrent database work.

Restart the Paper server after changing the database configuration. A plugin
reload is not recommended for changing database backends.

## Docker example

When Paper and MariaDB run in Docker, do not use `localhost` as the database
host. `localhost` points to the Paper container itself. Use the MariaDB
service/container name on a shared Docker network instead:

```yaml
mysql:
  host: "mariadb"
  port: 3306
  database: "improvedfactions"
  user: "improvedfactions"
  password: "replace-with-a-strong-password"
```

The database port only needs to be published to the host when connections
from outside the Docker network are required.

## Migrations and existing data

ImprovedFactions runs Flyway migrations automatically during plugin startup.
The migration scripts are bundled with the plugin, so you do not need to
create tables manually or run SQL scripts by hand.

Migration locations are selected automatically:

- SQLite: `db/migration/sqlite`
- MariaDB/MySQL: `db/migration/mysql`

Do not edit or delete the `flyway_schema_history` table. Back up the database
before upgrading the plugin or changing database infrastructure.

Switching `database: "sqlite"` to `database: "mysql"` does **not** copy data
from the SQLite file into MariaDB/MySQL. It points the plugin at a different
database. If an existing server must retain its SQLite data, make a backup
and use a separately planned data migration before switching the live
server.

## Verify the connection

After restarting, check the Paper console for messages similar to:

```text
Using database MYSQL as database
[Flyway] Starting migrations: ... jdbc:mariadb://...
Database: jdbc:mariadb://... (MariaDB ...)
[Flyway] Migration run completed: 0 migration(s), success=true
ImprovedFactions-mysql - Start completed.
```

The number of applied migrations may be greater than zero on the first start.
`No pending migrations` means the schema is already current; it is not an
error.

## Troubleshooting

### Connection refused or timed out

Check that MariaDB/MySQL is running, that the host and port are reachable
from the Paper server, and that the database firewall allows the Paper
server's IP address. In Docker, verify that both containers share a network
and that the configured host is the database service name.

### Access denied

Verify the username, password, database name, and the host part of the SQL
user grant. A user granted only for `localhost` cannot necessarily connect
from a separate Paper host or container.

### Migration errors

Read the first Flyway error in the Paper console and restore the database
backup if a failed upgrade leaves the server unable to start. Do not delete
the migration history to work around a validation error; investigate the
schema and migration version first.
