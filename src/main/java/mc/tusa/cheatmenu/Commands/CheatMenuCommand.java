package mc.tusa.cheatmenu.Commands;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import mc.tusa.cheatmenu.CalculateDamage;
import mc.tusa.cheatmenu.CheatMenu;
import mc.tusa.cheatmenu.Listeners.CheatGUI;
import mc.tusa.cheatmenu.TeleportUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheatMenuCommand implements CommandExecutor {

    CheatMenu cheatmenu;
    //Vector prevVelocity;

    public CheatMenuCommand(CheatMenu cheatmenu)
    {
        this.cheatmenu = cheatmenu;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player)
        {
            CheatGUI cheatGUI = new CheatGUI(cheatmenu);
            Bukkit.getServer().getPluginManager().registerEvents(cheatGUI, cheatmenu);
            cheatGUI.openInventory(((Player) sender));
        }
        return true;
    }

    public void runTaskAim(Player player)
    {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!cheatmenu.cheaters.containsKey(player))
                    cancel();
                if (!cheatmenu.cheaters.get(player).aimAssist)
                    cancel();
                if (player.getHealth() <= 0)
                    return;
                if (!player.isOnline()) {
                    cancel();
                    cheatmenu.cheaters.remove(player);
                }
                List<Entity> near = player.getNearbyEntities(5.0D, 5.0D, 5.0D);
                for (Entity entity : near) {
                    if (entity instanceof Player) {
                        if (entity == player)
                            continue;
                        Player nearPlayer = (Player) entity;
                        Location centerLocation = nearPlayer.getEyeLocation();
                        centerLocation.setY(nearPlayer.getEyeLocation().getY() - 0.5);
                        ProtocolManager manager = ProtocolLibrary.getProtocolManager();
                        final PacketContainer packet = manager.createPacket(PacketType.Play.Server.ENTITY_LOOK);
                        packet.getModifier().writeDefaults();
                        packet.getIntegers().write(0, player.getEntityId());
                        packet.getBytes().write(0, (byte)getYaw(player, centerLocation)).write(1, (byte)getPitch(player, centerLocation));
                        try {
                            manager.sendServerPacket(nearPlayer, packet);
                        } catch (InvocationTargetException e) {
                            throw new RuntimeException(e);
                        }
                        final PacketContainer packet2 = manager.createPacket(PacketType.Play.Server.ENTITY_HEAD_ROTATION);
                        packet2.getIntegers().write(0, player.getEntityId());
                        packet2.getModifier().writeDefaults();
                        byte yRot = (byte) ((getYaw(player, centerLocation) / 360.0) * 256);
                        packet2.getBytes().write(0, yRot);
                        try {
                            manager.sendServerPacket(nearPlayer, packet2);
                        } catch (InvocationTargetException e) {
                            throw new RuntimeException(e);
                        }
                        try {
                            TeleportUtils.teleport(player, centerLocation);
                        } catch (InvocationTargetException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }.runTaskTimer(cheatmenu, 0, 1);
    }

    public void runTaskSpeed(Player player)
    {
        new BukkitRunnable() {
            int ticks = 0;
            @Override
            public void run() {
                ticks++;
                boolean cancelled = false;
                if (!cheatmenu.cheaters.containsKey(player))
                    cancelled = true;
                if (!cheatmenu.cheaters.get(player).speed)
                    cancelled = true;
                if (player.getHealth() <= 0)
                    return;
                if (!player.isOnline()) {
                    cancel();
                    cheatmenu.cheaters.remove(player);
                    return;
                }
                if (cancelled)
                {
                    cancel();
                    player.setWalkSpeed(0.2f);
                    player.setFlySpeed(0.1f);
                    return;
                }
                player.setWalkSpeed(0.3f);
                player.setFlySpeed(0.15f);
                if (player.getLocation().add(0, 0.001, 0).getBlock().getBlockData().getMaterial() == Material.WATER || player.getLocation().add(0, 0.001, 0).getBlock().getBlockData().getAsString(true).contains("waterlogged")) {
                    player.setVelocity(new Vector(player.getVelocity().getX(), 0.09f, player.getVelocity().getZ()));
                    player.setWalkSpeed(0.5f);
                    player.setVelocity(player.getLocation().getDirection().multiply(0.8).setY(0.045));
                    player.setFallDistance(0.0f);
                }
            }
        }.runTaskTimer(cheatmenu, 1L, 1L);
    }

    public void runTaskAura(Player player) {
        new BukkitRunnable() {
            float ticks;
            @Override
            public void run() {
                ticks++;
                if (!cheatmenu.cheaters.containsKey(player))
                    cancel();
                if (!cheatmenu.cheaters.get(player).killAura)
                    cancel();
                if (player.getHealth() <= 0)
                    return;
                if (!player.isOnline()) {
                    cancel();
                    cheatmenu.cheaters.remove(player);
                }
                List<Entity> near = player.getNearbyEntities(5.0D, 5.0D, 5.0D);
                for (Entity entity : near) {
                    if (entity instanceof Player) {
                        if (entity == player)
                            continue;
                        Player nearPlayer = (Player) entity;
                        Location centerLocation = nearPlayer.getEyeLocation();
                        centerLocation.setY(nearPlayer.getEyeLocation().getY() - 0.5);
                        ProtocolManager manager = ProtocolLibrary.getProtocolManager();
                        final PacketContainer packet = manager.createPacket(PacketType.Play.Server.ENTITY_LOOK);
                        packet.getModifier().writeDefaults();
                        packet.getIntegers().write(0, player.getEntityId());
                        packet.getBytes().write(0, (byte) getYaw(player, centerLocation)).write(1, (byte) getPitch(player, centerLocation));
                        try {
                            manager.sendServerPacket(nearPlayer, packet);
                        } catch (InvocationTargetException e) {
                            throw new RuntimeException(e);
                        }
                        final PacketContainer packet2 = manager.createPacket(PacketType.Play.Server.ENTITY_HEAD_ROTATION);
                        packet2.getIntegers().write(0, player.getEntityId());
                        packet2.getModifier().writeDefaults();
                        byte yRot = (byte) ((getYaw(player, centerLocation) / 360.0) * 256);
                        packet2.getBytes().write(0, yRot);
                        try {
                            manager.sendServerPacket(nearPlayer, packet2);
                        } catch (InvocationTargetException e) {
                            throw new RuntimeException(e);
                        }
                        float hasteAmplifier = 0;
                        if (player.getPotionEffect(PotionEffectType.FAST_DIGGING) != null)
                        {
                            hasteAmplifier += player.getPotionEffect(PotionEffectType.FAST_DIGGING).getAmplifier() * 0.1f;
                        }
                        if (player.getPotionEffect(PotionEffectType.SLOW_DIGGING) != null)
                        {
                            hasteAmplifier -= player.getPotionEffect(PotionEffectType.SLOW_DIGGING).getAmplifier() * 0.1f;
                        }
                        if (player.getAttackCooldown() + hasteAmplifier >= 0.9) {//ticks >= getDelay(player.getInventory().getItemInMainHand())) {
                            player.swingMainHand();
                            player.resetCooldown();
                            CalculateDamage.CalculateItemAttackDamage(player, nearPlayer, player.getInventory().getItemInMainHand());
                            ticks = 0;
                        }
                    }
                }
            }
        }.runTaskTimer(cheatmenu, 0, 1);
    }

    public static boolean canCrit(Player player)
    {
        return player.getVelocity().getY() < 0 && !player.isOnGround() && !player.getLocation().subtract(0, 0.1, 0).getBlock().getType().equals(Material.LADDER) && !player.getLocation().subtract(0, 0.1, 0).getBlock().getType().equals(Material.VINE) && !player.getLocation().subtract(0, 0.1, 0).getBlock().getType().equals(Material.WATER) && player.getPotionEffect(PotionEffectType.BLINDNESS) == null && player.getPotionEffect(PotionEffectType.SLOW_FALLING) == null && player.getVehicle() == null && !player.isSprinting() && !player.isFlying();
    }

    @Deprecated
    public int getDelay(ItemStack itemStack)
    {
        double attackSpeed;
        if (itemStack.getType().equals(Material.NETHERITE_SWORD)) {
            attackSpeed = 1.6;
        } else if (itemStack.getType().equals(Material.DIAMOND_SWORD)) {
            attackSpeed = 1.6;
        } else if (itemStack.getType().equals(Material.IRON_SWORD)) {
            attackSpeed = 1.6;
        } else if (itemStack.getType().equals(Material.STONE_SWORD)) {
            attackSpeed = 1.6;
        } else if (itemStack.getType().equals(Material.GOLDEN_SWORD)) {
            attackSpeed = 1.6;
        } else if (itemStack.getType().equals(Material.WOODEN_SWORD)) {
            attackSpeed = 1.6;
        }
        else if (itemStack.getType().equals(Material.NETHERITE_AXE)) {
            attackSpeed = 1;
        } else if (itemStack.getType().equals(Material.DIAMOND_AXE)) {
            attackSpeed = 1;
        } else if (itemStack.getType().equals(Material.IRON_AXE)) {
            attackSpeed = 0.9;
        } else if (itemStack.getType().equals(Material.STONE_AXE)) {
            attackSpeed = 0.8;
        } else if (itemStack.getType().equals(Material.GOLDEN_AXE)) {
            attackSpeed = 1;
        } else if (itemStack.getType().equals(Material.WOODEN_AXE)) {
            attackSpeed = 0.8;
        } else if (itemStack.getType().equals(Material.NETHERITE_PICKAXE)) {
            attackSpeed = 1.2;
        } else if (itemStack.getType().equals(Material.DIAMOND_PICKAXE)) {
            attackSpeed = 1.2;
        } else if (itemStack.getType().equals(Material.IRON_PICKAXE)) {
            attackSpeed = 1.2;
        } else if (itemStack.getType().equals(Material.STONE_PICKAXE)) {
            attackSpeed = 1.2;
        } else if (itemStack.getType().equals(Material.GOLDEN_PICKAXE)) {
            attackSpeed = 1.2;
        } else if (itemStack.getType().equals(Material.WOODEN_PICKAXE)) {
            attackSpeed = 1.2;
        } else if (itemStack.getType().equals(Material.NETHERITE_SHOVEL)) {
            attackSpeed = 1;
        } else if (itemStack.getType().equals(Material.DIAMOND_SHOVEL)) {
            attackSpeed = 1;
        } else if (itemStack.getType().equals(Material.IRON_SHOVEL)) {
            attackSpeed = 1;
        } else if (itemStack.getType().equals(Material.STONE_SHOVEL)) {
            attackSpeed = 1;
        } else if (itemStack.getType().equals(Material.GOLDEN_SHOVEL)) {
            attackSpeed = 1;
        } else if (itemStack.getType().equals(Material.WOODEN_SHOVEL)) {
            attackSpeed = 1;
        } else if (itemStack.getType().equals(Material.TRIDENT)) {
            attackSpeed = 1.1;
        }
        else {
            attackSpeed = 4;
        }

        return (int) Math.round((1 / attackSpeed) * 20);
    }

    public float getYaw(Player p, Location point) {
        Location npcLoc = p.getEyeLocation();

        double xDiff = point.getX() - npcLoc.getX();
        //double yDiff = point.getY() - npcLoc.getY();
        double zDiff = point.getZ() - npcLoc.getZ();

        double DistanceXZ = Math.sqrt(xDiff * xDiff + zDiff * zDiff);
        //double DistanceY = Math.sqrt(DistanceXZ * DistanceXZ + yDiff * yDiff);
        double newYaw = Math.acos(xDiff / DistanceXZ) * 180 / Math.PI;
        //double newPitch = Math.acos(yDiff / DistanceY) * 180 / Math.PI - 90;
        if (zDiff < 0.0)
            newYaw = newYaw + Math.abs(180 - newYaw) * 2;
        newYaw = (newYaw - 90);
        return (float)newYaw;
    }

    public float getPitch(Player p, Location point) {
        Location npcLoc = p.getEyeLocation();

        double xDiff = point.getX() - npcLoc.getX();
        double yDiff = point.getY() - npcLoc.getY();
        double zDiff = point.getZ() - npcLoc.getZ();

        double DistanceXZ = Math.sqrt(xDiff * xDiff + zDiff * zDiff);
        double DistanceY = Math.sqrt(DistanceXZ * DistanceXZ + yDiff * yDiff);
        //double newYaw = Math.acos(xDiff / DistanceXZ) * 180 / Math.PI;
        double newPitch = Math.acos(yDiff / DistanceY) * 180 / Math.PI - 90;
        //if (zDiff < 0.0)
            //newYaw = newYaw + Math.abs(180 - newYaw) * 2;
        //newYaw = (newYaw - 90);
        return (float)newPitch;
    }
}
