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
public class SetBlockAtCommand implements CommandExecutor {

    private final SanPlugin plugin;

    public SetBlockAtCommand(SanPlugin plugin) {
	this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] split) {

	if (!sender.isOp()) {
	    sender.sendMessage("Operator command only");
	    return false;
	}
	if (split.length < 5) {
	    sender.sendMessage("Usage : /setblockat world x y z newid [strike]");
	    return false;
	}
	World monde = plugin.getServer().getWorld(split[0]);
	Block blockToChange = monde.getBlockAt(Integer.valueOf(split[1]), Integer.valueOf(split[2]), Integer.valueOf(split[3]));
	blockToChange.setTypeId(Integer.valueOf(split[4]));
	if (split.length == 6) {
	    if (Integer.valueOf(split[5]) == 1) {
		monde.strikeLightningEffect(blockToChange.getLocation());
	    }
	}
	return true;

    }
}
