package mc.tusa.cheatmenu.Listeners;

import mc.tusa.cheatmenu.CheatMenu;
import mc.tusa.cheatmenu.Functions;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CheatGUI implements Listener {
    private final Inventory inv;

    CheatMenu cheatmenu;

    public CheatGUI(CheatMenu cheatmenu) {
        this.cheatmenu = cheatmenu;
        inv = Bukkit.createInventory(null, 9, "CheatGUI");

        initializeItems();
    }

    public void initializeItems() {
        inv.addItem(createGuiItem(Material.DIAMOND_SWORD, "§c§lAimAssist", "§aUseful AimAssist."));
        inv.addItem(createGuiItem(Material.IRON_HELMET, "§4KillAura", "§aTargets all entities around you."));
        inv.addItem(createGuiItem(Material.FEATHER, "§fSpeed", "§aAdds more speed to you!"));
        inv.addItem(createGuiItem(Material.NETHERITE_BOOTS, "§lVelocity", "§bYou take no knockback."));
    }

    protected ItemStack createGuiItem(final Material material, final String name, final String... lore) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(name);

        meta.setLore(Arrays.asList(lore));

        item.setItemMeta(meta);

        return item;
    }

    public void openInventory(final HumanEntity ent) {
        ent.openInventory(inv);
    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        if (!e.getInventory().equals(inv)) return;

        e.setCancelled(true);

        final ItemStack clickedItem = e.getCurrentItem();

        if (clickedItem == null || clickedItem.getType().isAir()) return;

        final Player player = (Player) e.getWhoClicked();

        boolean killAura = false;
        boolean aimAssist = false;
        boolean speed = false;
        boolean velocity = false;
        try {
            aimAssist = cheatmenu.cheaters.get(player).aimAssist;
        }
        catch (Exception ignored) {
            aimAssist = false;
        }

        try {
            killAura = cheatmenu.cheaters.get(player).killAura;
        }
        catch (Exception ignored) {
            killAura = false;
        }

        try {
            speed = cheatmenu.cheaters.get(player).speed;
        }
        catch (Exception ignored) {
            speed = false;
        }

        try {
            velocity = cheatmenu.cheaters.get(player).velocity;
        }
        catch (Exception ignored) {
            velocity = false;
        }
        switch (e.getRawSlot())
        {
            case 0:
                if (!cheatmenu.cheaters.containsKey(player))
                {
                    cheatmenu.cheaters.put(player, new Functions(true, false, false, false));
                    player.sendMessage("You enabled AimAssist.");
                    cheatmenu.cheatMenuCommand.runTaskAim(player);
                }
                else if (cheatmenu.cheaters.containsKey(player) && cheatmenu.cheaters.get(player).aimAssist)
                {
                    cheatmenu.cheaters.remove(player, new Functions(true, killAura, speed, velocity));
                    cheatmenu.cheaters.put(player, new Functions(false, killAura, speed, velocity));
                    player.sendMessage("You disabled AimAssist.");
                }
                else if (cheatmenu.cheaters.containsKey(player) && !cheatmenu.cheaters.get(player).aimAssist)
                {
                    cheatmenu.cheaters.remove(player, new Functions(false, killAura, speed, velocity));
                    cheatmenu.cheaters.put(player, new Functions(true, killAura, speed, velocity));
                    player.sendMessage("You enabled AimAssist.");
                    cheatmenu.cheatMenuCommand.runTaskAim(player);
                }
                break;
            case 1:
                if (!cheatmenu.cheaters.containsKey(player))
                {
                    cheatmenu.cheaters.put(player, new Functions(false, true, false, false));
                    player.sendMessage("You enabled KillAura.");
                    cheatmenu.cheatMenuCommand.runTaskAura(player);
                }
                else if (cheatmenu.cheaters.containsKey(player) && cheatmenu.cheaters.get(player).killAura)
                {
                    cheatmenu.cheaters.remove(player, new Functions(aimAssist, true, speed, velocity));
                    cheatmenu.cheaters.put(player, new Functions(aimAssist, false, speed, velocity));
                    player.sendMessage("You disabled KillAura.");
                }
                else if (cheatmenu.cheaters.containsKey(player) && !cheatmenu.cheaters.get(player).killAura)
                {
                    cheatmenu.cheaters.remove(player, new Functions(aimAssist, false, speed, velocity));
                    cheatmenu.cheaters.put(player, new Functions(aimAssist, true, speed, velocity));
                    player.sendMessage("You enabled KillAura.");
                    cheatmenu.cheatMenuCommand.runTaskAura(player);
                }
                break;
            case 2:
                if (!cheatmenu.cheaters.containsKey(player))
                {
                    cheatmenu.cheaters.put(player, new Functions(false, false, true, false));
                    player.sendMessage("You enabled Speed.");
                    cheatmenu.cheatMenuCommand.runTaskSpeed(player);
                }
                else if (cheatmenu.cheaters.containsKey(player) && cheatmenu.cheaters.get(player).speed)
                {
                    cheatmenu.cheaters.remove(player, new Functions(aimAssist, killAura, true, velocity));
                    cheatmenu.cheaters.put(player, new Functions(aimAssist, killAura, false, velocity));
                    player.sendMessage("You disabled Speed.");
                }
                else if (cheatmenu.cheaters.containsKey(player) && !cheatmenu.cheaters.get(player).speed)
                {
                    cheatmenu.cheaters.remove(player, new Functions(aimAssist, killAura, false, velocity));
                    cheatmenu.cheaters.put(player, new Functions(aimAssist, killAura, true, velocity));
                    player.sendMessage("You enabled Speed.");
                    cheatmenu.cheatMenuCommand.runTaskSpeed(player);
                }
                break;
            case 3:
                if (!cheatmenu.cheaters.containsKey(player))
                {
                    cheatmenu.cheaters.put(player, new Functions(false, false, false, true));
                    player.sendMessage("You enabled Velocity.");
                }
                else if (cheatmenu.cheaters.containsKey(player) && cheatmenu.cheaters.get(player).velocity)
                {
                    cheatmenu.cheaters.remove(player, new Functions(aimAssist, killAura, speed, true));
                    cheatmenu.cheaters.put(player, new Functions(aimAssist, killAura, speed, false));
                    player.sendMessage("You disabled Velocity.");
                }
                else if (cheatmenu.cheaters.containsKey(player) && !cheatmenu.cheaters.get(player).velocity)
                {
                    cheatmenu.cheaters.remove(player, new Functions(aimAssist, killAura, speed, false));
                    cheatmenu.cheaters.put(player, new Functions(aimAssist, killAura, speed, true));
                    player.sendMessage("You enabled Velocity.");
                }
                break;
        }
    }

    @EventHandler
    public void onInventoryClick(final InventoryDragEvent e) {
        if (e.getInventory().equals(inv)) {
            e.setCancelled(true);
        }
    }
}
