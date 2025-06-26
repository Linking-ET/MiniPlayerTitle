package cn.lunadeer.miniplayertitle;

import cn.lunadeer.miniplayertitle.commands.TitleCard;
import cn.lunadeer.miniplayertitle.dtos.TitleDTO;
import cn.lunadeer.miniplayertitle.events.Events;
import cn.lunadeer.miniplayertitle.events.PaperChat;
import cn.lunadeer.miniplayertitle.events.SpigotChat;
import cn.lunadeer.miniplayertitle.managers.Configuration;
import cn.lunadeer.miniplayertitle.utils.Misc;
import cn.lunadeer.miniplayertitle.utils.Notification;
import cn.lunadeer.miniplayertitle.utils.XLogger;
import cn.lunadeer.miniplayertitle.utils.bStatsMetrics;
import cn.lunadeer.miniplayertitle.utils.databse.DatabaseManager;
import cn.lunadeer.miniplayertitle.utils.scheduler.Scheduler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public final class MiniPlayerTitle extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        new Scheduler(this);
        new Notification(this);
        new XLogger(this);

        try {
            Configuration.loadConfigurationAndDatabase(instance.getServer().getConsoleSender());
        } catch (Exception e) {
            XLogger.error(e);
        }

        if (usingPapi()) {
            new Expansion(this);
        }

        Bukkit.getPluginManager().registerEvents(new Events(), this);
        Bukkit.getPluginManager().registerEvents(new TitleCard(), this);
        if (Misc.isPaper()) {
            Bukkit.getPluginManager().registerEvents(new PaperChat(), this);
        } else {
            Bukkit.getPluginManager().registerEvents(new SpigotChat(), this);
        }
        Objects.requireNonNull(Bukkit.getPluginCommand("MiniPlayerTitle")).setExecutor(new Commands());
        Objects.requireNonNull(Bukkit.getPluginCommand("MiniPlayerTitle")).setTabCompleter(new Commands());

        new bStatsMetrics(this, 21444);

        XLogger.info("称号插件已加载");
        XLogger.info("版本: " + this.getDescription().getVersion());
        // http://patorjk.com/software/taag/#p=display&f=Big&t=MiniPlayerTitle
        XLogger.info("  __  __ _       _ _____  _                    _______ _ _   _");
        XLogger.info(" |  \\/  (_)     (_)  __ \\| |                  |__   __(_) | | |");
        XLogger.info(" | \\  / |_ _ __  _| |__) | | __ _ _   _  ___ _ __| |   _| |_| | ___");
        XLogger.info(" | |\\/| | | '_ \\| |  ___/| |/ _` | | | |/ _ \\ '__| |  | | __| |/ _ \\");
        XLogger.info(" | |  | | | | | | | |    | | (_| | |_| |  __/ |  | |  | | |_| |  __/");
        XLogger.info(" |_|  |_|_|_| |_|_|_|    |_|\\__,_|\\__, |\\___|_|  |_|  |_|\\__|_|\\___|");
        XLogger.info("                                   __/ |");
        XLogger.info("                                  |___/");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        DatabaseManager.instance.close();
    }

    public static MiniPlayerTitle instance;
    private final Map<UUID, TitleDTO> playerUsingTitle = new HashMap<>();

    public static boolean usingPapi() {
        return Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");
    }

    public void setPlayerUsingTitle(UUID uuid, TitleDTO title) {
        playerUsingTitle.put(uuid, title);
    }

    public TitleDTO getPlayerUsingTitle(UUID uuid) {
        return playerUsingTitle.get(uuid);
    }
}
