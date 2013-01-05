package fr.herobrine.plugin.san;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.google.common.collect.Maps;

/**
 * Handler for the /san command.
 * @author Sébastien Mériot
 */
public class SanCommand implements CommandExecutor {

	public final static String API_URL = "http://www.herobrine.fr/api.php";
	public final static String API_KEY = "totofaitdelamoto";

	private final SanPlugin plugin;

	public SanCommand(SanPlugin plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] split) {

		Player player = null;
		String playername = null;

		if (sender instanceof Player) {
			player = (Player) sender;
		}

		// Sent from command
		if (player == null) {

			if (split.length != 1) {
				sender.sendMessage("Need playername !");
				return false;
			}

			playername = split[0];
		}
		// Sent by a player in game
		else {
			playername = player.getName();
		}

		// Build API Url to call
		Map<String, String> params = Maps.newHashMap();
		params.put("key", API_KEY);
		params.put("api", "san");
		params.put("player", playername);
		String url = Utils.BuildFullUrl(API_URL, params);

		try {
			// Call API then parse JSON result
			ObjectMapper mapper = new ObjectMapper();
			JsonNode sanArray = mapper.readTree(new URL(url));

			// Get values and check everything's ok
			JsonNode values = sanArray.get("san");
			if (values == null) {
				sender.sendMessage("Une erreur est survenue en récupérant la santé mentale de \"" + playername + "\". L'administrateur a été notifié, veuillez essayer dans quelques instants.");
				plugin.notifyError(sanArray.get("error").toString());
			} else {

				StringBuffer representation = new StringBuffer();
				int current = values.get("cursan").asInt();
				int max = values.get("maxsan").asInt();
				int dead = values.get("deadsan").asInt();

				for (int i = 0; i < current; i++)
					representation.append("§f0");
				for (int i = 0; i < max - current - dead; i++)
					representation.append("§8-");
				for (int i = 0; i < dead; i++)
					representation.append("§cX");

				sender.sendMessage("§eIl vous reste " + current + " points de santé mentale sur un total de " + max + ". [" + representation + "§e]");
			}
		} catch (MalformedURLException ex) {
			sender.sendMessage("Une erreur est survenue en récupérant la santé mentale de \"" + playername + "\". L'administrateur a été notifié, veuillez essayer dans quelques instants.");
			plugin.notifyError(ex.getClass().getName() + " : " + ex.getMessage());
		} catch (JsonProcessingException ex) {
			sender.sendMessage("Une erreur est survenue en récupérant la santé mentale de \"" + playername + "\". L'administrateur a été notifié, veuillez essayer dans quelques instants.");
			plugin.notifyError(ex.getClass().getName() + " : " + ex.getMessage());
		} catch (IOException ex) {
			sender.sendMessage("Une erreur est survenue en récupérant la santé mentale de \"" + playername + "\". L'administrateur a été notifié, veuillez essayer dans quelques instants.");
			plugin.notifyError(ex.getClass().getName() + " : " + ex.getMessage());
		}

		return true;
	}
}
