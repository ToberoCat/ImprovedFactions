# Issue- und Fork-Kontext

Stand: 2026-08-08

## Ausgangslage

Repository: `ToberoCat/ImprovedFactions`

Der aktuelle Arbeitsstand ist `main`. Beim ersten Check war der lokale Workspace bereits nicht sauber: Änderungen an `improved-factions/src/main/resources/plugin.yml` und `improved-factions/version.properties` stammen vom Benutzer und dürfen nicht überschrieben werden.

`./gradlew test --no-daemon` läuft im aktuellen Stand erfolgreich. Issue #350 (Build hängt bei den Faction-Tests) ist daher auf diesem Stand nicht reproduzierbar.

## Priorisierte autonom bearbeitbare Issues

1. **#147 – Invite expiry**
   In `improved-factions/src/main/kotlin/io/github/toberocat/improvedfactions/factions/Faction.kt` wird das gespeicherte Ablaufdatum weiterhin hart auf fünf Minuten gesetzt, obwohl die Konfiguration geladen wird. Ablaufdatum und Scheduler müssen dieselbe konfigurierte Dauer verwenden. Regressionstest ergänzen.

2. **#289 – Claim-particle interval**
   `ClaimParticleModule.onEnable()` plant den Renderer vor `reloadConfig()` mit dem Default-Intervall ein. Das konfigurierte `particle-spawn-interval` wird dadurch beim Start ignoriert. Fix inklusive Test für die Initialisierungsreihenfolge bzw. Task-Planung.

3. **#276 – Offline-Spielername bei Invite**
   Der Invite-Broadcast verwendet `Bukkit.getPlayer(uuid)` und zeigt bei Offline-Spielern „Not found“. Der bekannte Offline-Spielername muss verwendet werden. Regressionstest ergänzen.

4. **#255 – MiniMessage-Tags in Konsolenausgabe**
   Nicht-Spieler erhalten aktuell die unformatierte Nachricht mit `<red>`-artigen Tags. Konsolenausgabe muss in eine geeignete ANSI-/Legacy-Darstellung oder eine korrekt gerenderte Component umgewandelt werden. Test ergänzen.

5. **#322 – Mitglieder-Placeholder**
   `%faction_members_total%` und `%faction_members_online%` fehlen. Der Fork `dathannobrega/ImprovedFactions` enthält dafür einen kleinen Prototyp, dessen alte Pfade manuell an die aktuelle Modulstruktur angepasst werden müssen.

## Bereits vorhandene Fixes / nicht doppelt bearbeiten

- #330 (Home-Berechtigung) ist in `dev` bereits behoben.
- #348 (Ban entfernt Spieler aus fremder Faction) ist in `dev` bereits behoben; der aktuelle `main`-Code enthält die relevante Zugehörigkeitsprüfung ebenfalls.
- #340/#361 (doppelte Sprachdateien) sind in `dev` durch die Sprachdatei-Umstellung adressiert, aber noch nicht im aktuellen `main`.
- #350 ist lokal nicht reproduzierbar.
- #359 ist wegen Arclight/Forge-Umgebung nicht autonom belastbar reproduzierbar.

## Fork- und Branch-Erkenntnisse

Es wurden 24 Forks mit insgesamt 70 Branches geprüft.

- `ToberoCat/dev` ist der wichtigste Wiederverwendungsstand. PR #362 wurde dorthin gemergt und enthält das Paper-/Java-Update sowie mehrere Runtime-Fixes. Nicht blind auf `main` zurückmergen; Änderungen selektiv prüfen.
- `BasperLasper/ImprovedFactions:paper26-2` war der Vorgänger von #362 und ist durch den Upstream-Stand ersetzt.
- `dathannobrega/ImprovedFactions:add-members_total-and-members_online-placeholders` ist als Vorlage für #322 brauchbar.
- `BadAimWeeb/ImprovedFactions:fix/millisecond-is-millisecond` enthält einen plausiblen kleinen Power-Timing-Fix; vor Übernahme gegen den aktuellen Code prüfen.
- `WinterPhish/ImprovedFactions-globemc:globemc-dev` enthält server-/projektbezogene Siege-, Checkpoint- und Invite-Änderungen; nicht direkt übernehmen.
- Aleksanders BlueMap-/Protection-/StreamChat-Branch ist umfangreich und alt; höchstens einzelne Ideen verwenden.
- Die übrigen Fork-Branches sind Spiegel, veraltet oder enthalten bereits gemergte Upstream-Änderungen.

## Arbeitsregeln für die Umsetzung

- Test-driven: zuerst einen reproduzierbaren Regressionstest schreiben, dann minimal implementieren, anschließend die gesamte Testsuite ausführen.
- Keine Änderungen an vorhandenen Benutzeränderungen ohne ausdrückliche Freigabe.
- Exposed soll nur noch für Entities/Queries verwendet werden; Schema-Erstellung und Schemaänderungen sollen über Flyway-Versionierung und nachvollziehbare SQL-Migrationsskripte laufen.
- Flyway-Migrationen müssen für die unterstützten Datenbanken (SQLite und MySQL/MariaDB) geprüft werden; keine automatische destruktive Datenänderung.
