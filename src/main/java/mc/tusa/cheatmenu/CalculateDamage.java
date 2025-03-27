package mc.tusa.cheatmenu;

import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class CalculateDamage {
    public static void attackPlayer(Player player, Player victim) {
        ((CraftPlayer) player).getHandle().attack(((CraftPlayer) victim).getHandle());
        player.swingMainHand();
    }
}
