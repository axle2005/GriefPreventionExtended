package io.github.axle2005.griefpreventionextended;

import java.util.Optional;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;

import io.github.axle2005.griefpreventionextended.commands.CommandRegister;
import me.ryanhamshire.griefprevention.GriefPrevention;
import me.ryanhamshire.griefprevention.api.GriefPreventionApi;

@Plugin(id=PluginInfo.id, name = PluginInfo.name, version = PluginInfo.version, description = PluginInfo.description, dependencies = @Dependency(id="griefprevention", optional = false))
public class GriefPreventionExtended {
	private GriefPreventionApi gpApi;
	
	@Inject
	private Logger logger;
	
	
	@Listener
	public void preInitialization(GamePreInitializationEvent event) {
		//config = new Config(this, defaultConfig, configManager);
		//events = new ListenersRegister(this);
	}
	
	@Listener
	public void initialization(GameInitializationEvent event) {
		new CommandRegister(this);
	}
	@Listener
	public void onServerStart(GameStartedServerEvent event) throws Exception {
		Optional<PluginContainer> optGpContainer = Sponge.getPluginManager().getPlugin("griefprevention");
		if (optGpContainer.isPresent()) {
			this.gpApi = GriefPrevention.getApi();
			logger.info("Loaded GP API");
		}
		
		

	}
	
	
	
	public Logger getLogger(){
		return logger;
	}
	public GriefPreventionApi getGPApi()
	{
		return gpApi;
	}
	
	
	

}
