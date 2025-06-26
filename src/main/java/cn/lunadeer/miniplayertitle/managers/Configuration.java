package cn.lunadeer.miniplayertitle.managers;

import cn.lunadeer.miniplayertitle.MiniPlayerTitle;
import cn.lunadeer.miniplayertitle.utils.VaultConnect.VaultConnect;
import cn.lunadeer.miniplayertitle.utils.XLogger;
import cn.lunadeer.miniplayertitle.utils.configuration.*;
import cn.lunadeer.miniplayertitle.utils.databse.DatabaseManager;
import org.bukkit.command.CommandSender;

import java.io.File;

public class Configuration extends ConfigurationFile {
    @Comments("Do not modify this value.")
    public static int version = 2;  // <<<<<< When you change the configuration, you should increment this value.

    @Comments("The settings of the database.")
    public static Database database = new Database();

    public static class Database extends ConfigurationPart {
        @Comments("Supported types: sqlite, mysql, pgsql")
        public String type = "sqlite";

        @Comments("The host of the database.")
        public String host = "localhost";

        @Comments("The port of the database.")
        public String port = "3306";

        @Comments("The database name.")
        public String database = "miniplayertitle";

        @Comments("The username of the database.")
        public String username = "miniplayertitle";

        @Comments("The password of the database.")
        public String password = "miniplayertitle";
    }

    @Comments("The prefix of the title.")
    public static String prefix = "&7[";

    @Comments("The suffix of the title.")
    public static String suffix = "&7]";

    @Comments("Allow player to customize their title.")
    public static CustomTitle customTitle = new CustomTitle();

    public static class CustomTitle extends ConfigurationPart {
        @Comments("Whether to enable custom title feature.")
        public boolean enabled = true;
        @Comments("The cost of setting a custom title.")
        public Double cost = 1000.0;
        @Comments("The maximum length of the custom title. (Color characters are not counted.)")
        public Integer maxLength = 8;
    }

    public static DefaultTitle defaultTitle = new DefaultTitle();

    public static class DefaultTitle extends ConfigurationPart {
        public String title = "新手";
        public String description = "初来乍到！";
    }

    @Comments("The default coin amount for the internal economy.")
    public static Double internalEconomyDefault = 1000.0;

    @Comments("Whether to use an external economy plugin. (Need Vault & an economy plugin)")
    public static boolean usingExternalEconomy = false;

    public static boolean debug = false;

    @PostProcess
    public void setDebug() {
        XLogger.setDebug(debug);
    }

    @PostProcess
    public void prepareExternalEconomy() {
        if (usingExternalEconomy) {
            new VaultConnect(MiniPlayerTitle.instance);
        }
    }

    public static void loadConfigurationAndDatabase(CommandSender sender) throws Exception {
        // Load configuration
        ConfigurationManager.load(Configuration.class, new File(MiniPlayerTitle.instance.getDataFolder(), "config.yml"), "version");
        // database
        if (DatabaseManager.instance == null) {
            new DatabaseManager(MiniPlayerTitle.instance,
                    Configuration.database.type,
                    Configuration.database.host,
                    Configuration.database.port,
                    Configuration.database.database,
                    Configuration.database.username,
                    Configuration.database.password
            );
        } else {
            DatabaseManager.instance.set(
                    Configuration.database.type,
                    Configuration.database.host,
                    Configuration.database.port,
                    Configuration.database.database,
                    Configuration.database.username,
                    Configuration.database.password
            );
        }
        DatabaseManager.instance.reconnect();
        DatabaseTables.migrate();
    }
}
