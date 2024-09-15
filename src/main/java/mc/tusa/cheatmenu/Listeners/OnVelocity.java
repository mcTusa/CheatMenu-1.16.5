package mc.tusa.cheatmenu.Listeners;

import mc.tusa.cheatmenu.CheatMenu;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerVelocityEvent;

public class OnVelocity implements Listener {
    CheatMenu cheatMenu;

    public OnVelocity(CheatMenu cheatMenu)
    {
        this.cheatMenu = cheatMenu;
    }

    @EventHandler
    public void onVelocity(PlayerVelocityEvent event)
    {
        if (cheatMenu.cheaters.containsKey(event.getPlayer()) && cheatMenu.cheaters.get(event.getPlayer()).velocity)
        {
            event.setCancelled(true);
        }
    }
}
