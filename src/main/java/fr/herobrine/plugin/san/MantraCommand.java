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
 * Handler for the /mantra command.
 * @author Loïc Jean-Fulcrand
 */
public class MantraCommand implements CommandExecutor {

    public final static String API_URL = "http://www.herobrine.fr/api.php";
    public final static String API_KEY = "totofaitdelamoto";
    private final SanPlugin plugin;

    public MantraCommand(SanPlugin plugin) {
	this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] split) {

	Player player = null;
	String playername = null;
	String mantra = null;

	if (sender instanceof Player) {
	    player = (Player) sender;
	}

	// Sent from command
	if (player == null) {
	    sender.sendMessage("Une erreur est survenue en tentant de prononcer le mantra. L'administrateur a été notifié, veuillez essayer dans quelques instants.");
	    return true;
	} else {
	    playername = player.getName();
	}

	if (split.length < 1) {
	    sender.sendMessage("Usage : /mantra VOTRE MANTRA ICI");
	    return true;	    
	}
	
	StringBuffer buf = new StringBuffer();
	for (int i = 0; i < split.length; i++) {
	    buf.append(split[i]);
	    if (i != split.length - 1) {
		buf.append("%20");
	    }
	}

	mantra = buf.toString();

	// Build API Url to call
	Map<String, String> params = Maps.newHashMap();
	params.put("key", API_KEY);
	params.put("api", "mantra");
	params.put("player", playername);
	params.put("mantra", mantra);
	String url = Utils.BuildFullUrl(API_URL, params);

	// Consume webservice
	JsonNode result = null;
	try {
	    result = consumeMantraWS(url);
	} catch (Exception ex) {
	    plugin.notifyError(ex.getClass().getName() + " : " + ex.getMessage());
	    sender.sendMessage("Une erreur est survenue, rien ne se passe (err 1)." + url);
	    return true;
	}

	if (result.get("effect").asInt() == 1) {
	    sender.sendMessage("§eVous connaissez déjà ce mantra." + url);
	    
	} else if (result.get("effect").asInt() == 2) {
	    sender.sendMessage("§eNouveau mantra recopié !");
	    
	} else if (result.get("effect").asInt() == 3) {
	    sender.sendMessage("§eCe mantra ne semble pas exister." + url);
	    
	} else if (result.get("effect").asInt() == 0) {
	    sender.sendMessage("§eUne erreur est survenue, rien ne se passe (err 2)." + url);
	}

	return true;
    }

    /**
     * Send a GET request to web service (json expected)
     * @param url Url of the WS
     * @return JsonNode representing web service response
     * @throws Exception
     */
    private JsonNode consumeMantraWS(String url) throws Exception {

	JsonNode values = null;

	// Call API then parse JSON result
	ObjectMapper mapper = new ObjectMapper();
	JsonNode mantraArray = mapper.readTree(new URL(url));

	// Get values and check everything's ok
	values = mantraArray.get("mantra");
	if (values == null) {
	    throw new Exception(mantraArray.get("error").toString());
	}

	return values;
    }
}
