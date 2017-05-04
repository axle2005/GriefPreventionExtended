package io.github.axle2005.griefpreventionextended.commands;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Chunk;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.biome.BiomeTypes;

import com.flowpowered.math.vector.Vector3i;

import io.github.axle2005.griefpreventionextended.GriefPreventionExtended;
import me.ryanhamshire.griefprevention.api.claim.Claim;

public class CommandBiome implements CommandExecutor {

	GriefPreventionExtended plugin;
	public CommandBiome(GriefPreventionExtended plugin){
		this.plugin = plugin;
	}
	@Override
	public CommandResult execute(CommandSource src, CommandContext arguments) throws CommandException {
		
		
		String biome = arguments.<String>getOne("biome").get();
		String size = arguments.<String>getOne("island|chunk|block").get();
		
		if(!(src instanceof Player)){
			src.sendMessage(Text.of(TextColors.RED, "Must be run by a player"));
			return CommandResult.empty();
		}
		else
		{
			Player player =(Player) src;
			World world = player.getWorld();
			Claim claim = plugin.getGPApi().getClaimManager(world).getClaimAt(player.getLocation(), true);
			if(!(claim.getOwnerUniqueId().equals(player.getUniqueId()))){
				src.sendMessage(Text.of(TextColors.RED, "Must be run by claim owners"));
				return CommandResult.empty();
			}
			else
			{
				if(size.equals("island")){
					for(Chunk chunk : claim.getChunks()){
						chunk.setBiome(chunk.getPosition(), BiomeTypes.MESA_PLATEAU);
					}
					return CommandResult.success();
				}
				else if(size.equalsIgnoreCase("chunk"))
				{
					Vector3i loc = player.getLocation().getChunkPosition();
					Chunk chunk = Sponge.getServer().getWorld(world.getName()).get().getChunk(loc).get();
					chunk.setBiome(loc, BiomeTypes.DESERT);
					return CommandResult.success();
				}
				else 
				{
					Vector3i loc = player.getLocation().getBlockPosition();
					Sponge.getServer().getWorld(world.getName()).get().setBiome(loc.getX(), loc.getY()-2, loc.getZ(), BiomeTypes.MEGA_TAIGA);
					
					return CommandResult.success();
				}
				
	 		}
		}

	}

}
