package mc.tusa.cheatmenu;
//import com.comphenix.packetwrapper.wrappers.play.clientbound.WrapperPlayServerLookAt;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.utility.MinecraftReflection;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

public class TeleportUtils {
    public enum Anchor {
        FEET,
        EYES
    }

    public static void teleport(Player player, Location location) throws InvocationTargetException {

        PacketContainer container = new PacketContainer(PacketType.Play.Server.LOOK_AT);
        container.getDoubles().write(0, location.getX());
        container.getDoubles().write(1, location.getY());
        container.getDoubles().write(2, location.getZ());
        container.getEnumModifier(Anchor.class, MinecraftReflection.getMinecraftClass("ArgumentAnchor$Anchor")).writeSafely(0, Anchor.EYES);
        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        protocolManager.sendServerPacket(player, container);

        /*WrapperPlayServerLookAt wrapperPlayServerLookAt = new WrapperPlayServerLookAt();
        wrapperPlayServerLookAt.setX(location.getX());
        wrapperPlayServerLookAt.setY(location.getY());
        wrapperPlayServerLookAt.setZ(location.getZ());
        wrapperPlayServerLookAt.setFromAnchor(WrapperPlayServerLookAt.Anchor.EYES);
        wrapperPlayServerLookAt.sendPacket(player);*/

    }
}
