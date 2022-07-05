package io.github.toberocat.core.factions;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.utility.language.Parseable;
import io.github.toberocat.core.utility.sql.MySql;
import io.github.toberocat.core.utility.sql.SqlCode;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

public class FactionDatabaseHandler {
    private final MySql sql;
    private final Faction f;

    public FactionDatabaseHandler(@NotNull MySql sql, @NotNull Faction faction) {
        this.sql = sql;
        this.f = faction;
    }

    public boolean save() {
        if (!sql.isConnected()) return logSaveError(null);

        faction(exists("factions") ? SqlCode.UPDATE_FACTION : SqlCode.CREATE_FACTION);

        return true;
    }

    private boolean exists(@NotNull String table) {
        try {
            PreparedStatement ps = sql.eval("SELECT registry_id FROM %s " +
                            "WHERE registry_id = %s", table, f.getRegistryName());
            ResultSet results = ps.executeQuery();
            return results.next();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void description() {

    }

    private void faction(@NotNull String rowAction) {
        SqlCode.execute(sql, rowAction,
                        new Parseable("@registry", f.getRegistryName()),
                        new Parseable("@display", f.getDisplayName()),
                        new Parseable("@motd", f.getMotd()),
                        new Parseable("@tag", f.getTag()),
                        new Parseable("@frozen", f.isFrozen()),
                        new Parseable("@permanent", f.isPermanent()),
                        new Parseable("@created-at", f.getCreatedAt()),
                        new Parseable("@owner", f.getOwner().toString()),
                        new Parseable("@claimed_chunks", f.getClaimedChunks()),
                        new Parseable("@balance", f.getBalance()),
                        new Parseable("@current_power", f.getPowerManager().getCurrentPower()),
                        new Parseable("@max_power", f.getPowerManager().getMaxPower())
                ).get(PreparedStatement::executeUpdate)
                .except(this::logSaveError);
    }

    private boolean logSaveError(Exception e) {
        if (e == null) MainIF.logMessage(Level.SEVERE, "Tried to save faction " +
                f.getRegistryName() + " to sql, but sql seams like it isn't connected");
        else {
            MainIF.logMessage(Level.SEVERE, "Tried to save faction " +
                    f.getRegistryName() + " to sql. Error: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}
