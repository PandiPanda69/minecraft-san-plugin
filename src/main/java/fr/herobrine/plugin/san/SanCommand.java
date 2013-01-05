package fr.herobrine.plugin.san;

import java.net.URL;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.codehaus.jackson.JsonNode;
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

		// Consume webservice
		JsonNode result = null;
		try {
			result = consumeSanWS(url);
		} catch (Exception ex) {
			plugin.notifyError(ex.getClass().getName() + " : " + ex.getMessage());
			sender.sendMessage("Une erreur est survenue en récupérant la santé mentale. L'administrateur a été notifié, veuillez essayer dans quelques instants.");
			return true;
		}

		// Get values and send them to player
		int current = result.get("cursan").asInt();
		int max = result.get("maxsan").asInt();
		int dead = result.get("deadsan").asInt();

		String representation = buildRepresentation(current, max, dead);
		sender.sendMessage("§eIl vous reste " + current + " points de santé mentale sur un total de " + max + ". [" + representation + "§e]");

		return true;
	}

	/**
	 * Send a GET request to web service (json expected)
	 * @param url Url of the WS
	 * @return JsonNode representing web service response
	 * @throws Exception
	 */
	private JsonNode consumeSanWS(String url) throws Exception {

		JsonNode values = null;

		// Call API then parse JSON result
		ObjectMapper mapper = new ObjectMapper();
		JsonNode sanArray = mapper.readTree(new URL(url));

		// Get values and check everything's ok
		values = sanArray.get("san");
		if (values == null) {
			throw new Exception(sanArray.get("error").toString());
		}

		return values;
	}

	/**
	 * Build a string representation using colored "0"
	 * @param current Current value
	 * @param max Max value
	 * @param dead Dead value
	 * @return String representation
	 */
	private String buildRepresentation(int current, int max, int dead) {

		StringBuffer representation = new StringBuffer();

		for (int i = 0; i < current; i++)
			representation.append("§f0");
		for (int i = 0; i < max - current - dead; i++)
			representation.append("§80");
		for (int i = 0; i < dead; i++)
			representation.append("§c0");

		return representation.toString();
	}
}
