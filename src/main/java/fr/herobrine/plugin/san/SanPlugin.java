package fr.herobrine.plugin.san;

import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.List;
import org.bukkit.World;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * San plugin for Bukkit
 * @author Sébastien Mériot
 */
public class SanPlugin extends JavaPlugin {

	private Logger log = getLogger();

	public void onDisable() {
		if (log != null) {
			log.info("SanPlugin is shutting off.");
		}
	}

	public void onEnable() {	
		// Register our commands
		getCommand("san").setExecutor(new SanCommand(this));
		getCommand("setblockat").setExecutor(new SetBlockAtCommand(this));
		getCommand("replaceblockat").setExecutor(new ReplaceBlockAtCommand(this));
		getCommand("mantra").setExecutor(new MantraCommand(this));
		getCommand("lightningring").setExecutor(new LightningRingCommand(this));

		if (log != null) {
			PluginDescriptionFile pdfFile = this.getDescription();
			log.info(pdfFile.getName() + " (v. " + pdfFile.getVersion() + ") successfully loaded.");
		}
	}

	public void notifyError(String msg) {

		if (log == null) {
			return;
		}

		log.log(Level.SEVERE, msg);
	}
}
