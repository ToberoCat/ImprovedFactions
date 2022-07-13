package io.github.toberocat.core.factions;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.utility.data.database.sql.MySqlDatabase;
import io.github.toberocat.core.utility.data.database.sql.SqlCode;
import io.github.toberocat.core.utility.data.database.sql.SqlVar;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Level;

public class FactionDatabaseHandler {
    private final MySqlDatabase sql;
    private final Faction f;

    public FactionDatabaseHandler(@NotNull MySqlDatabase sql, @NotNull Faction faction) {
        this.sql = sql;
        this.f = faction;
    }

    public boolean save() {
        if (!sql.isConnected()) return logSaveError(null);

        faction(exists("factions") ? SqlCode.UPDATE_FACTION : SqlCode.CREATE_FACTION);
        description();
        bans();
        relations();

        return true;
    }

    public boolean delete() {
        if (!sql.isConnected()) return logSaveError(null);

        run(SqlCode.DELETE_FACTION, SqlVar.of("registry", f.getRegistryName()));

        return true;
    }

    public Faction read() {
        return null;
    }

    private boolean exists(@NotNull final String table) {
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

    private void bans() {
        SqlCode.execute(sql, SqlCode.DELETE_BANS);
        f.getFactionMemberManager().getBanned().forEach(this::setBanLine);

    }

    private void description() {
        SqlCode.execute(sql, SqlCode.DELETE_DESCRIPTION);
        for (int i = 0; i < f.getDescription().length; i++) setDescriptionLine(SqlCode.ADD_DESCRIPTION, i);
    }

    private void relations() {
        f.getRelationManager().getAllies().forEach(uuid -> {

        });
    }

    private void setDescriptionLine(@NotNull String rowAction, int line) {
        run(rowAction,
                SqlVar.of("registry", f.getRegistryName()),
                SqlVar.of("line", line),
                SqlVar.of("content", f.getDescription()[line]));
    }

    private void setBanLine(@NotNull UUID uuid) {
        run(SqlCode.ADD_BANS,
                SqlVar.of("registry", f.getRegistryName()),
                SqlVar.of("banned", uuid.toString()));
    }

    private void faction(@NotNull String rowAction) {
        run(rowAction,
                SqlVar.of("registry", f.getRegistryName()),
                SqlVar.of("display", f.getDisplayName()),
                SqlVar.of("motd", f.getMotd()),
                SqlVar.of("tag", f.getTag()),
                SqlVar.of("frozen", f.isFrozen()),
                SqlVar.of("permanent", f.isPermanent()),
                SqlVar.of("created-at", f.getCreatedAt()),
                SqlVar.of("owner", f.getOwner().toString()),
                SqlVar.of("claimed_chunks", f.getClaimedChunks()),
                SqlVar.of("balance", f.getBalance()),
                SqlVar.of("current_power", f.getPowerManager().getCurrentPower()),
                SqlVar.of("max_power", f.getPowerManager().getMaxPower())
        );
    }

    private void run(@NotNull final String action, SqlVar... placeholders) {
        SqlCode.execute(sql, action, placeholders)
                .get(PreparedStatement::executeUpdate)
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
