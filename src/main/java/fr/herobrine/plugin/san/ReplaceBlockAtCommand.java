package fr.herobrine.plugin.san;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.block.*;
import org.bukkit.World;
import org.bukkit.Location;

/**
 * Handler for the /mantra command.
 * @author Loïc Jean-Fulcrand
 */
public class ReplaceBlockAtCommand implements CommandExecutor {

    private final SanPlugin plugin;

    public ReplaceBlockAtCommand(SanPlugin plugin) {
	this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] split) {

	if (!sender.isOp()) {
	    sender.sendMessage("Operator command only");
	    return false;
	}
	if (split.length < 6) {
	    sender.sendMessage("Usage : /replaceblockat world x y z oldid newid [strike]");
	    return false;
	}
	World monde = plugin.getServer().getWorld(split[0]);
	Block blockToChange = monde.getBlockAt(Integer.valueOf(split[1]), Integer.valueOf(split[2]), Integer.valueOf(split[3]));
	if (blockToChange.getTypeId() == Integer.valueOf(split[4])) {
	    blockToChange.setTypeId(Integer.valueOf(split[5]));
	    if (split.length == 7) {
		if (Integer.valueOf(split[6]) == 1) {
		    monde.strikeLightningEffect(blockToChange.getLocation());
		}
	    }
	}
	return true;

    }
}
