# Paper DB-/Thread-Audit

Stand: 2026-08-08

Scope: Paper/Bukkit-Main-Thread, Exposed/JDBC-Zugriffe, Scheduler, Async-Grenzen,
Thread-Sicherheit und Plugin-Lifecycle. Flyway wurde bewusst nicht verändert.

## Ergebnis

Die Anwendung verwendet Exposed derzeit synchron. `DatabaseManager.loggedTransaction`
ist ein blockierendes `transaction { ... }`; es gibt keinen gemeinsamen Async-Executor
oder eine Repository-Abstraktion. Deshalb sind die meisten Produktiv-Aufrufe aus
Commands und synchronen Events Main-Thread-DB-Zugriffe.

Paper dokumentiert, dass synchron ausgeführte Datenbankzugriffe die Serverperformance
beeinträchtigen können und dass die Bukkit-API in Async-Tasks weitgehend nicht sicher
ist. Daraus folgt: DB-only-Arbeit darf async laufen, Bukkit-/World-Zugriffe müssen auf
dem zuständigen Serverthread bleiben.

## Konkrete Findings

| Schwere | Fundstelle | Befund |
| --- | --- | --- |
| Hoch | `listeners/move/MoveListener.kt` | `PlayerMoveEvent` (bei Chunkwechsel) startet synchron eine Transaktion, Claim-/Faction-/Cluster-DAOs und weitere Bukkit-Zugriffe. |
| Hoch | `listeners/claim/ProtectionListener.kt` | Block-, Entity- und Interaktionsschutz fragt Claims, FactionUser und Cluster synchron im Event ab. Das betrifft häufige Gameplay-Events. |
| Hoch | `listeners/claim/GeneralPvPListener.kt`, `InFactionPvPListener.kt` | PvP-Events öffnen synchron Transaktionen und laden Claims/FactionUser. |
| Hoch | `listeners/claim/ClaimTntListener.kt`, `ClaimFullTntListener.kt` | Explosionen iterieren synchron über Blocklisten und führen pro Chunk Claim-Abfragen aus. |
| Mittel | `modules/power/impl/FactionPowerRaidModuleHandleImpl.kt` | Wiederholte Tasks laufen synchron; `accumulateAll` und `claimKeepCostsCollector` führen DB-Schreib-/Lesearbeit aus. Blindes Umschalten auf Async wäre hier nicht sicher, weil die Berechnung anschließend Bukkit, Events, Broadcasts und Cluster/Dynmap-Pfade berührt. |
| Mittel | `modules/claimparticle/handles/RenderParticlesTask.kt` | Synchroner Wiederholungs-Task kombiniert Cluster-/DAO-Zugriffe mit `Bukkit.getOnlinePlayers`, Player-Locations und Partikel-Rendering. Async ist wegen Bukkit nicht zulässig; die DB-Seite muss später von der Render-Phase getrennt werden. |
| Mittel | `listeners/PlayerJoinListener.kt` | Join-Handler schreibt synchron in `KnownOfflinePlayer`. Ein Async-Versuch wurde verworfen, weil er mit den übrigen synchronen Exposed-Transaktionen SQLite-Locks/Rennen erzeugte; der Pfad bleibt offen für eine serialisierte DB-Queue. |
| Mittel | `integrations/papi/PlaceholderIntegration.kt` | Placeholder-Auflösung öffnet synchron eine Transaktion. PlaceholderAPI ruft Expansionen typischerweise im Serverthread auf; bei vielen Placeholdern kann das den Tick blockieren. Caching/async Placeholder-Design ist offen. |
| Mittel | `commands/general/InfoCommand.kt`, `ListFactionsCommand.kt` | Command-Ausführung läuft synchron; `members().count()`, `claims().count()`, Relations-Counts und Faction-Listen sind DB-Abfragen im Command-Tick. |
| Mittel | `BaseModule.onEverythingEnabled` / `ClaimClusterDetector` | Cluster-Erkennung lädt und bearbeitet Daten synchron beim Enable-Abschluss. Das blockiert nicht den laufenden Tick dauerhaft, kann aber den Start verlängern; für eine Async-Verlagerung müssten alle Bukkit-/Dynmap-Übergänge getrennt werden. |
| Niedrig | `DatabaseConnector`, `DatabaseManager` | DB-Verbindung, Connectivity-Check und `SchemaUtils.createMissingTablesAndColumns` laufen beim Enable synchron. Für Startup grundsätzlich zulässig, aber bei Remote-DB/Schemaänderungen potenziell lange Enable-Zeit. Flyway bleibt außerhalb dieses Audits. |
| Niedrig | `FactionInvites.scheduleInviteExpirations`, `LazyUpdate` | Zeitgesteuerte Tasks werden synchron geplant und führen später teilweise DB-Arbeit synchron aus. Bukkit bindet die Tasks an das Plugin; explizite Task-Verwaltung ist trotzdem uneinheitlich. |

## Behobene Fälle

1. `ClaimParticleModule` speichert den Repeating-Task, lädt das Intervall vor dem
   Scheduling und cancelt den Task beim Disable. Dafür gibt es Regressionstests.

Der zunächst getestete Async-Join-Persistenzversuch wurde wegen realer SQLite-
Nebenläufigkeitsfehlern zurückgenommen. Es bleibt damit kein unvollständig
parallelisierter DB-Pfad im Produktivcode.

## Lifecycle-/Thread-Sicherheitsprüfung

- `ImprovedFactionsPlugin.onDisable()` deaktiviert Module; Paper/Bukkit cancelt die
  plugingebundenen Scheduler-Tasks. Der Claim-Particle-Task wird zusätzlich explizit
  beendet.
- `BaseModule.onDisable()` schließt Adventure, aber keine explizite DB-Ressource.
  Exposed/SQLite-/MySQL-Verbindungslebenszyklus bleibt ein offener Punkt für die
  spätere Storage-/Flyway-Arbeit.
- Exposed-DAO-Objekte sind nicht als thread-sichere DTOs zu betrachten. Sie dürfen
  nicht aus einer Transaktion in einen Async-Callback getragen und dort weiterverwendet
  werden. Ein isolierter Async-Join-Callback wurde deshalb wegen SQLite-Locks mit den
  übrigen synchronen Transaktionen wieder entfernt.
- Async-Code darf keine Bukkit-Welt-, Entity-, Player-, Location-, Event- oder
  Rendering-Operationen ausführen. Deshalb wurden Move-/Protection-/Particle-/Power-
  Pfade nicht halbautomatisch async gemacht.

## Offene Punkte

- Claim-/Protection-Entscheidungen benötigen einen thread-sicheren, atomar aktualisierten
  Snapshot/Cache oder eine vorgelagerte Datenhaltung. Eine reine Umstellung auf
  `runTaskAsynchronously` würde Event-Cancellation zu spät ausführen und Bukkit-API
  aus Async-Code aufrufen.
- Commands brauchen langfristig async DB-Arbeit mit Main-Thread-Continuation für
  Nachrichten, Events und Bukkit-Objekte.
- Placeholder sollten gecacht oder über eine API mit explizitem async/sync-Verhalten
  bereitgestellt werden.
- Power-, Invite- und Cluster-Tasks brauchen einen einheitlichen Lifecycle-/Executor-
  Besitz, bevor sie umfassend parallelisiert werden.
- DB-Migrationen/Flyway, MariaDB/MySQL, SQLite-Schema und Connection-Pool sind bewusst
  nicht Teil dieser Änderung.

## Verifikation

- TDD-Tests für Claim-Particle-Intervall und Disable-Cleanup: erfolgreich.
- Der Async-Join-Test war zunächst grün, aber die Vollsuite zeigte 17 SQLite-
  Nebenläufigkeitsfehler; Implementierung und Test wurden deshalb zurückgenommen.
- Die vollständige Testsuite wird nach dieser Rücknahme erneut ausgeführt; `plugin.yml`
  und `version.properties` sind dabei als bestehende Benutzeränderungen zu bewahren.

Referenzen: [Paper: Using databases](https://docs.papermc.io/paper/dev/using-databases/),
[Paper: Scheduling](https://docs.papermc.io/paper/dev/scheduler/),
[Paper: Plugin lifecycle](https://docs.papermc.io/paper/dev/how-do-plugins-work/).
