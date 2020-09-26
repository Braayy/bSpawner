package braayy.spawner;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class Spawner extends JavaPlugin {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return true;

        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /spawner <mob>");
            sender.sendMessage(ChatColor.GOLD + "Available mobs:");

            StringBuilder builder = new StringBuilder();

            for (EntityType type : EntityType.values()) {
                if (type.isAlive() && type != EntityType.PLAYER) {
                    builder.append(getFancyName(type)).append(", ");
                }
            }

            String mobs = builder.toString();
            mobs = mobs.substring(0, mobs.length() - 2);

            sender.sendMessage(ChatColor.GOLD + mobs);

            return true;
        }

        EntityType type;

        try {
            type = EntityType.valueOf(args[0].toUpperCase());

            if (!type.isAlive() || type == EntityType.PLAYER) throw new Exception();
        } catch (Exception exception) {
            sender.sendMessage(ChatColor.RED + "Invalid mob name");

            return true;
        }

        Player player = (Player) sender;

        ItemStack stack = new ItemStack(Material.MOB_SPAWNER);
        BlockStateMeta meta = (BlockStateMeta) stack.getItemMeta();

        CreatureSpawner spawner = (CreatureSpawner) meta.getBlockState();
        spawner.setSpawnedType(type);

        meta.setBlockState(spawner);

        stack.setItemMeta(meta);

        if (player.getInventory().addItem(stack).size() != 0) {
            player.sendMessage(ChatColor.RED + "Your inventory is full");
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> list = new ArrayList<>();

        for (EntityType type : EntityType.values()) {
            if (type.isAlive() && type != EntityType.PLAYER && type.name().toLowerCase().startsWith(args[0].toLowerCase())) {
                list.add(type.name().toLowerCase());
            }
        }

        return list;
    }

    private static String getFancyName(EntityType type) {
        String name = type.name();
        return Character.toUpperCase(name.charAt(0)) + name.substring(1).toLowerCase();
    }
}