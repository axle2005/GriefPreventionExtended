package io.github.axle2005.griefpreventionextended.commands;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;


import io.github.axle2005.griefpreventionextended.GriefPreventionExtended;
import io.github.axle2005.griefpreventionextended.PluginInfo;

public class CommandRegister {

	public CommandRegister(GriefPreventionExtended plugin){
		

		CommandSpec biome = CommandSpec.builder().permission(PluginInfo.id+".base").arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("biome"))),
				GenericArguments.onlyOne(GenericArguments.string(Text.of("island|chunk|block"))))
				.description(Text.of("Provides current stats of server")).executor(new CommandBiome(plugin)).build();
		
		
		CommandSpec claim = CommandSpec.builder().permission(PluginInfo.id+".command.claim").arguments(GenericArguments.onlyOne(GenericArguments.integer(Text.of("range"))),
				GenericArguments.optional(GenericArguments.string(Text.of("admin"))))
				.description(Text.of("Provides current stats of server")).executor(new CommandClaim(plugin)).build();
		
		Sponge.getCommandManager().register(plugin, biome, "issetbiome");
		Sponge.getCommandManager().register(plugin, claim, "claim");
	}
}
