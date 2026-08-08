# Paper DB-/Thread-Audit

Stand: 2026-08-08

Scope: sämtliche laufenden Paper/Bukkit-Pfade, Storage-/Repository-Grenze,
SQLite und MariaDB/MySQL, Cache-Konsistenz, Scheduler-Handoffs und Lifecycle.

## Ergebnis

Die Big-Bang-Migration ist für die produktiv verdrahteten Serverpfade umgesetzt.
Commands, Argumentparser, Invites, Power, Siege, Relations, Home, Wilderness,
Claim-/Map-/Cluster-Darstellung, Listener, Placeholder und Charts treffen laufende
Gameplay-Entscheidungen ausschließlich aus einem atomar publizierten
`GameStateSnapshot`. Diese Objekte und ihre verschachtelten Collections werden vor
der Publikation defensiv kopiert und enthalten weder Bukkit- noch Exposed-Typen.

Alle laufenden DB-Zugriffe gehen durch `StorageManager` und
`JdbcStorageRepository`/`KnownPlayerRepository`. `GameStateCommands` enthält die
skalaren JDBC-Commands. Writes laufen in einer expliziten Transaktion; erst nach
erfolgreichem Commit wird ein vollständiger Snapshot neu geladen und atomar
publiziert. Bei Rollback oder fehlgeschlagenem Reload bleibt der zuletzt vollständig
publizierte Zustand sichtbar. Generationsnummern verhindern, dass ein langsamerer
älterer MariaDB-Reload einen neueren Snapshot überschreibt.

## Connection- und Worker-Policy

- SQLite: Hikari `maximumPoolSize=1`, genau ein Storage-Worker, WAL und
  `busy_timeout=10000`. Die zuvor bestehende SQLite-FK-Semantik wurde nicht verändert.
- MariaDB/MySQL: Hikari und Storage-Worker besitzen dieselbe konfigurierte, auf 1 bis
  16 begrenzte Parallelität (Standard 4).
- Die Worker-Queue ist auf 1024 Elemente begrenzt und verwendet benannte Daemon-
  Threads. Nach Shutdown werden neue Tasks abgewiesen und noch wartende Tasks
  exceptional abgeschlossen.
- `StorageManager.close()` entfernt Repositorys und Snapshot-Listener, beendet den
  Dispatcher, schließt Hikari und löscht den Cache. Erwartete Rejections während des
  Shutdowns werden nicht als irreführende Reload-Fehler geloggt.

## Startup und sicherer Bootstrap

Flyway und das Exposed-Initialschema laufen weiterhin synchron während des
Plugin-Starts. Der danach gestartete `StorageBootstrap` lädt den gesamten
JDBC-Snapshot ausschließlich auf dem Storage-Worker. Erst die Main-Thread-
Continuation aktiviert Faction-Listener und Integrationen. Bis zur erfolgreichen
Publikation ist der Cache explizit `not ready`; Protection-/PvP-/TNT-Pfade verhalten
sich in diesem Zustand fail-closed. Ein Bootstrap-Fehler publiziert keinen
Teilzustand, wird geloggt und lässt den Cache sicher `not ready`.

MariaDB benötigt wegen der bestehenden FK-Definition von `faction_users.faction_id`
einen internen factionlosen Sentinel. Migration
`mysql/V2__add_factionless_sentinel.sql` legt deshalb Faction `-1` an; der Loader
filtert diesen internen Datensatz aus Gameplay-Snapshots. SQLite wurde dabei nicht
auf nachträgliche FK-Prüfung umgestellt.

Die zwischenzeitliche SQLite-/Exposed-Regression hatte genau hier ihre Ursache:
`PRAGMA foreign_keys=ON` ließ bestehende Legacy-Fixtures mit `faction_id=-1` ohne
Parent-Faction fehlschlagen. Die gemeinsame Datasource behält deshalb für SQLite die
bisherige FK-Policy bei; MariaDB erhält den benötigten Parent stattdessen sauber über
Flyway V2.

## Thread-Grenzen

- DB-Worker erhalten nur kopierte UUIDs, IDs, Strings, Zahlen und immutable DTOs.
- `GameStateCommands.kt` importiert weder Bukkit noch Exposed.
- Command-Antworten, Bukkit-Events, World-/Player-Zugriffe, Siege-Folgeschritte und
  Dynmap-Rendering werden über `MainThreadContinuation` auf den klassischen
  Bukkit-Scheduler zurückgeführt. Es werden keine Folia-only APIs verwendet.
- Command-Sender werden vor der Worker-Phase als skalare Referenz erfasst und erst in
  der Main-Thread-Continuation erneut aufgelöst.
- Placeholder-Anfragen von Async-Consumern liefern den konfigurierten Fallback,
  statt Bukkit-`OfflinePlayer`-Zustand anzufassen oder auf den Main Thread zu warten.
- Die öffentlichen Faction-Events tragen nur `FactionSnapshot`, `UserSnapshot` oder
  skalare Create-Daten. Exposed-DAOs verlassen die interne Schema-/Testschicht nicht.
- Im produktiven Source-Set existiert kein `Future.get`, `join`, `runBlocking` oder
  vergleichbares blockierendes Warten in einem Main-Thread-Workflow.

## Migrierte laufende Pfade

- Claims/Unclaims, Zonenclaims, Force-Varianten, Claim-Map und Claim-Schutz
- Create/Delete/Rename/Icon/Join-Type, Join/Leave/Kick/Ban/Unban/Ownership
- Invites einschließlich Ablaufentscheidung und Main-Thread-Broadcast
- Ranks, Permissions und Default-/Fallback-Rank-Workflows
- Home setzen/lesen/teleportieren und Wilderness-Zielsuche
- Relations, Ally-Invites, War/Peace sowie Relations-Listen
- Power-Reads/-Writes, periodische Akkumulation/Kosten und Siege-Victory-Unclaim
- Move, PvP, TNT, Partikel, Placeholder, Charts und Dynmap-Snapshot-Rendering
- Known-player Upsert beim Join

Die alten mutierenden Home-, Dynmap-, Invite-, Power- und Cluster-Handles wurden
entfernt. Exposed-Entity-Klassen sind `internal` und auf Schemaabbildung reduziert.

## Repo-weite Sync-DB-Suche und Klassifikation

Gesucht wurde in `src/main/kotlin` nach `loggedTransaction`, `transaction {`,
`SizedIterable` sowie Exposed-DAO-Operationen (`find`, `findById`, `all`, `new`,
`count`). Ergebnis:

1. **Startup/Initialschema (zulässig):**
   `database/DatabaseManager.kt` und `ranks/FactionRankHandler.kt`. Hier werden
   Tabellen/Spalten und der Guest-Rank vor laufendem Gameplay initialisiert.
2. **Interner Clustering-Testalgorithmus (nicht produktiv verdrahtet):**
   `claims/clustering/detector/ClaimClusterDetector.kt`,
   `claims/clustering/cluster/Cluster.kt`, `claims/FactionClaimHandler.kt` und die
   zugehörigen internen Schema-Entities. Der frühere Runtime-Provider und sämtliche
   Base-/Faction-/Dynmap-Aufrufe wurden entfernt. Dieser Code wird nur von
   `ClaimClusterDetectorTest` verwendet; produktive Raidability und Renderingdaten
   entstehen im JDBC-Snapshot-Lader.
3. **Laufende Produktivpfade:** keine Fundstelle. Ein Source-Boundary-Test scannt
   Commands, Listener, Module, Integrationen, Charts und API auf synchrone Exposed-
   Tokens und schützt außerdem die interne DAO-/Snapshot-Event-Grenze.

Damit verbleibt keine synchron erreichbare DB-Nische in einem laufenden Serverpfad.

## Regressionstests

Die ergänzten/erweiterten Tests decken ab:

- atomare und defensive Snapshot-Publikation, Cache-Invarianten und abgelaufene Invites;
- erfolgreicher/fehlgeschlagener Bootstrap und fail-closed MockBukkit-Protection;
- SQLite-Serialisierung, begrenzte MariaDB-Parallelität, Queue-/Shutdown-Lifecycle;
- Commit-vor-Publish, Rollback-ohne-Publish und konkurrierende Reload-Generationen;
- vollständiges JDBC-Materialisieren für SQLite und MariaDB einschließlich Timestamp-
  Varianten und factionlosem Sentinel;
- vollständige GameStateCommands-Workflows für Factions, Claims, Home, Invites,
  Relations, Usage-Limits und Ranks;
- Hot-Paths ohne DB-Roundtrip/Blocking, Codegenerator-Grenze sowie explizite
  Main-Thread-Continuations;
- Snapshot-basierte/cancellable Bukkit-Events und bestehende Command-, Module- und
  Clustering-Regressionen.

Ausgeführt ausschließlich in frischen temporären Arbeitskopien:

```text
MARIADB_TEST_URL=jdbc:mariadb://127.0.0.1:3307/improvedfactions
MARIADB_TEST_USER=improvedfactions
MARIADB_TEST_PASSWORD=improvedfactions
./gradlew test --no-daemon --console=plain

BUILD SUCCESSFUL
32 Testsuites, 96 Tests, 0 Failures, 0 Errors, 0 Skipped
```

Die MariaDB-Migrations-, Loader- und Write/Commit/Reload-Tests liefen tatsächlich und
wurden nicht per Assumption übersprungen.

## Verbleibende Risiken

- Jeder erfolgreiche Write lädt derzeit den vollständigen Snapshot neu. Das ist klar
  und konsistent, kann aber bei sehr großen Datenmengen zum nächsten Skalierungsengpass
  werden; inkrementelle, versionierte Snapshot-Patches wären eine spätere Optimierung.
- Abgelaufene Invite-Zeilen werden aus Entscheidungen gefiltert, aber nicht periodisch
  physisch gelöscht. Lang laufende Server können daher Wartungsbedarf für diese Tabelle
  entwickeln.
- Der interne alte Cluster-Algorithmus bleibt für seine bestehenden Unit-Tests im
  Main-Source-Set. Er ist `internal`, besitzt keinen Runtime-Aufrufer und wird durch den
  Boundary-Test vor einer versehentlichen Rückverdrahtung geschützt; eine spätere
  reine In-Memory-Neuimplementierung könnte diesen Test-Altbestand ganz entfernen.
- Flyway/Exposed-Initialisierung bleibt bewusst synchron und kann bei einer langsamen
  Remote-Datenbank den Plugin-Start verlängern. Der initiale Snapshot ist davon getrennt
  und läuft async; Gameplay-Entscheidungen bleiben bis zur erfolgreichen Publikation
  fail-closed.
- Das Schema erzwingt Faction-Namen weiterhin nicht mit einem DB-Unique-Constraint.
  Die Repository-Prüfung verhindert normale Duplikate, aber mehrere gleichzeitig auf
  dieselbe Datenbank schreibende Serverinstanzen wären ohne Schema-Constraint nicht
  vollständig abgesichert.
- Die Event-API ist absichtlich Big-Bang-inkompatibel geändert: Handler, die bisher
  Exposed-`Faction`/`FactionUser` erwarteten, müssen auf Snapshot-DTOs beziehungsweise
  `ownerId`/`factionName` umgestellt werden. Das verhindert DAO-Leaks, erfordert aber
  eine Anpassung externer Plugin-Integrationen beim Upgrade.

`improved-factions/src/main/resources/plugin.yml` und
`improved-factions/version.properties` wurden bei dieser Arbeit weder geändert noch
wiederhergestellt; ihre bereits vorhandenen Worktree-Änderungen blieben unangetastet.
