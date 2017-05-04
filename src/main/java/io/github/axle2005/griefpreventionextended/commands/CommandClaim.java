package io.github.axle2005.griefpreventionextended.commands;

import java.util.UUID;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.World;

import com.flowpowered.math.vector.Vector3i;

import io.github.axle2005.griefpreventionextended.GriefPreventionExtended;
import io.github.axle2005.griefpreventionextended.PluginInfo;
import me.ryanhamshire.griefprevention.api.claim.Claim;
import me.ryanhamshire.griefprevention.api.claim.ClaimManager;
import me.ryanhamshire.griefprevention.api.claim.ClaimResult;
import me.ryanhamshire.griefprevention.api.claim.ClaimType;

public class CommandClaim implements CommandExecutor {

	GriefPreventionExtended plugin;

	World world;
	Vector3i lesser;
	Vector3i greater;
	Vector3i player;
	UUID owner;

	public CommandClaim(GriefPreventionExtended plugin) {
		this.plugin = plugin;
	}

	@Override
	public CommandResult execute(CommandSource src, CommandContext arguments) throws CommandException {
		Integer args = arguments.<Integer>getOne("range").get();
		ClaimType type;
			
		if(arguments.<String>getOne("admin").isPresent() && arguments.<String>getOne("admin").get().equalsIgnoreCase("admin")){
			type = ClaimType.ADMIN;
		}
		else
		{
			type = ClaimType.BASIC;
		}
		

		if (src instanceof Player) {
			world = ((Player) src).getWorld();
			player = ((Player) src).getLocation().getBlockPosition();
			lesser = new Vector3i(player.getX() - args, world.getBlockMin().getY(), player.getZ() - args);
			greater = new Vector3i(player.getX() + args, world.getBlockMax().getY(), player.getZ() + args);
			owner = ((Player) src).getUniqueId();
		}
		final ClaimManager CLAIM_MANAGER = plugin.getGPApi().getClaimManager(world);

		ClaimResult claimResult = Claim.builder().world(world)
				.bounds(lesser, greater)
				.owner(owner)
				.type(type)
				.requiresClaimBlocks(true)
				.cause(Cause.source(plugin).build())
				.cuboid(true)
				.build();
		// Attempt to retrieve the Claim object from the result
		Claim claim = (claimResult.getClaim().isPresent()) ? claimResult.getClaim().get() : null;
		// With a vaild Claim object, add it to the DataStore using the
		// ClaimManager
		if (claim != null)
		{
			if(type.equals(ClaimType.ADMIN) && !src.hasPermission(PluginInfo.id+".admin"))
			{
				src.sendMessage(Text.of(TextColors.RED,"You do not have permission to run this: "+PluginInfo.id+".admin"));
			}
			else if(type.equals(ClaimType.BASIC) && claim.getArea()>plugin.getGPApi().getGlobalPlayerData(owner).get().getRemainingClaimBlocks() )
			{
				src.sendMessage(Text.of(TextColors.RED,"Insufficent Claimblocks"));
			}
			else
			{
				CLAIM_MANAGER.addClaim(claim, Cause.source(plugin).build());
				src.sendMessage(Text.of(TextColors.YELLOW,"Claim Created"));
				plugin.getLogger().info(src.getName() +" created a claim");
			}
			
		}
		
			
			
			
		
			
			

		return CommandResult.success();
	}

}
