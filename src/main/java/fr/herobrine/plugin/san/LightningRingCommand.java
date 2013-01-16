package fr.herobrine.plugin.san;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.block.*;
import org.bukkit.entity.Player;
import org.bukkit.World;
import org.bukkit.Location;

/**
 * Handler for the /mantra command.
 * @author Loïc Jean-Fulcrand
 */
public class LightningRingCommand implements CommandExecutor {

    private final SanPlugin plugin;

    public LightningRingCommand(SanPlugin plugin) {
	this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] split) {

	double angle = 0;
	double centerX = 0;
	double centerZ = 0;
	double tempX = 0;
	double tempZ = 0;
	double radius = 0;
	double amount = 0;

	if (!sender.isOp()) {
	    sender.sendMessage("Operator command only");
	    return false;
	}
	if (split.length > 3 && split.length < 6) {
	    sender.sendMessage("Usage : /lightningring [world x y z / playername] [diameter] [amount]");
	    return false;
	}

	if (split.length >= 0 && split.length <= 3) {

	    Player player = null;

	    if (sender instanceof Player) {

		if (split.length > 0) {//si il y a au moins 1 argument, c'est le nom du joueur
		    player = plugin.getServer().getPlayer(split[0]);
		    if (player == null) {
			sender.sendMessage(split[0] + " n'est pas en ligne !");
			return false;
		    }
		} else {//sinon, c'est le joueur par defaut
		    player = (Player) sender;
		}
		World monde = player.getWorld();
		Location temploc = player.getLocation();
		centerX = temploc.getX();
		centerZ = temploc.getZ();

		if (split.length > 1) {
		    radius = Integer.valueOf(split[1]);
		    if (radius <= 0) {
			radius = 5;
		    } else if (radius > 200) {
			radius = 200;
		    }

		} else {
		    radius = 5;
		}

		if (split.length > 2) {
		    amount = Integer.valueOf(split[2]);
		    if (amount <= 0) {
			amount = 8;
		    } else if (amount > 64) {
			amount = 64;
		    }
		} else {
		    amount = 8;
		}

		for (angle = 0; angle < 2 * Math.PI; angle += 2 * Math.PI / amount) {
		    tempX = Math.round(Math.cos(angle) * radius);
		    tempZ = Math.round(Math.sin(angle) * radius);
		    temploc.setX(centerX + tempX);
		    temploc.setZ(centerZ + tempZ);
		    monde.strikeLightningEffect(temploc);
		    try {
			Thread.currentThread().sleep(10);//sleep for 1000 ms
		    } catch (Exception ex) {
			plugin.notifyError(ex.getClass().getName() + " : " + ex.getMessage());
			sender.sendMessage("Erreur dans le delais d'attente du thread de dessin de cercle d'eclairs.");
			return true;
		    }
		}

		return true;
	    }

	} else if (split.length >= 4 && split.length <= 6) {

	    Location temploc = null;
	    World monde = null;

	    monde = plugin.getServer().getWorld(split[0]);
	    Block blockToChange = monde.getBlockAt(Integer.valueOf(split[1]), Integer.valueOf(split[2]), Integer.valueOf(split[3]));
	    temploc = blockToChange.getLocation();
	    centerX = temploc.getX();
	    centerZ = temploc.getZ();

	    if (split.length > 1) {
		radius = Integer.valueOf(split[1]);
		if (radius <= 0) {
		    radius = 5;
		} else if (radius > 100) {
		    radius = 100;
		}

	    } else {
		radius = 5;
	    }

	    if (split.length > 2) {
		amount = Integer.valueOf(split[2]);
		if (amount <= 0) {
		    amount = 8;
		} else if (amount > 64) {
		    amount = 64;
		}
	    } else {
		amount = 8;
	    }

	    for (angle = 0; angle < 2 * Math.PI; angle += 2 * Math.PI / amount) {
		tempX = Math.round(Math.cos(angle) * radius);
		tempZ = Math.round(Math.sin(angle) * radius);
		temploc.setX(centerX + tempX);
		temploc.setZ(centerZ + tempZ);
		monde.strikeLightningEffect(temploc);
		try {
		    Thread.currentThread().sleep(10);//sleep for 1000 ms
		} catch (Exception ex) {
		    plugin.notifyError(ex.getClass().getName() + " : " + ex.getMessage());
		    sender.sendMessage("Erreur dans le delais d'attente du thread de dessin de cercle d'eclairs.");
		    return true;
		}
	    }

	    return true;

	}
	return true;

    }
}
