package io.github.axle2005.griefpreventionextended;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.world.World;

import io.github.axle2005.griefpreventionextended.commands.CommandRegister;
import me.ryanhamshire.griefprevention.GriefPrevention;
import me.ryanhamshire.griefprevention.api.GriefPreventionApi;
import me.ryanhamshire.griefprevention.api.claim.Claim;
import me.ryanhamshire.griefprevention.api.claim.ClaimResultType;

@Plugin(id=PluginInfo.id, name = PluginInfo.name, version = PluginInfo.version, description = PluginInfo.description, dependencies = @Dependency(id="griefprevention", optional = false))
public class GriefPreventionExtended {
	private GriefPreventionApi gpApi;
	
	@Inject
	private Logger logger;
	
	List<File> files = new ArrayList<File>();
	String owner;
	UUID claimUUID;
	
	
	@Listener
	public void preInitialization(GamePreInitializationEvent event) {
		//config = new Config(this, defaultConfig, configManager);
		//events = new ListenersRegister(this);
	}
	
	@Listener
	public void initialization(GameInitializationEvent event) {
		new CommandRegister(this);
	}
	@Listener(order = Order.LATE)
	public void onServerStart(GameStartedServerEvent event) throws Exception {
		Optional<PluginContainer> optGpContainer = Sponge.getPluginManager().getPlugin("griefprevention");
		if (optGpContainer.isPresent()) {
			this.gpApi = GriefPrevention.getApi();
			logger.info("Loaded GP API");
			logger.info("Starting GP Claim Expiration");
			removeOldLogs("plugins/config/griefprevention/GlobalPlayerData",30);
			long l = System.currentTimeMillis();
			for(File f : files)
			{
				f.setLastModified(System.currentTimeMillis());
				claimExpiration(UUID.fromString(f.getName()));
			}
			long l2 = System.currentTimeMillis();
			logger.info("Completed GP Claim Expiration in: "+ (l2-l)+"ms");
			
			
					
		}
		
		

	}
	
	
	
	public Logger getLogger(){
		return logger;
	}
	public GriefPreventionApi getGPApi()
	{
		return gpApi;
	}
	public Cause getCause(){
		
		return Cause.source(this).build();
	}
	
	private void removeOldLogs(String folder, int days) {

		String path = folder.replaceAll("/n/", File.separator + "");
		// String path = Sponge.getGame().getGameDirectory() + File.separator +
		// folder;
		
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, -days);

		File location = new File(path);
		if (location.isDirectory()) {
			String[] entries = location.list();
			for (String s : entries) {
				File currentFile = new File(location.getPath(), s);
				if (currentFile.lastModified() > c.getTimeInMillis()) {
					files.add(currentFile);
					//currentFile.delete();
					//currentFile.setLastModified(System.currentTimeMillis());
					//claimExpiration(UUID.fromString(currentFile.getName()));
				}

			}

		}

	}
	
	private void claimExpiration(UUID uuid)
	{
		
			World world = Sponge.getServer().getWorld("world").get();
			//logger.info(uuid+"");
			for(Claim claim :gpApi.getClaimManager(world).getPlayerClaims(uuid) ) {
				if(claim.isBasicClaim())
				{
					owner = claim.getOwnerName();
					claimUUID= claim.getUniqueId();
					if(gpApi.getClaimManager(world).deleteClaim(claim, getCause()).getResultType().equals(ClaimResultType.SUCCESS)){
						logger.debug("Deleted Claim "+claimUUID +"of: " +owner);
					}
					
				}
				
				
			
		}
	}
	
	private void regionExpiration()
	{
		for(World world: Sponge.getServer().getWorlds())
		{
			
		}
	}
	
	
	
	
	

}
