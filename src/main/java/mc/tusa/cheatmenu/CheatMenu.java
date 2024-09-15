package mc.tusa.cheatmenu;

import mc.tusa.cheatmenu.Commands.CheatMenuCommand;
import mc.tusa.cheatmenu.Listeners.OnVelocity;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public final class CheatMenu extends JavaPlugin {

    public CheatMenuCommand cheatMenuCommand;

    public Map<Player, Functions> cheaters = new HashMap<>();

    @Override
    public void onEnable() {
        cheatMenuCommand = new CheatMenuCommand(this);
        // Plugin startup logic
        loadCommands();
        loadListeners(this);
    }

    private void loadCommands() {
        getCommand("cheatgui").setExecutor(cheatMenuCommand);
    }

    private void loadListeners(CheatMenu plugin) {
        Bukkit.getPluginManager().registerEvents(new OnVelocity(plugin), plugin);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

}
