package com.nolacola.discord.speedbowl.commands.owner;

import java.io.File;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.nolacola.discord.speedbowl.util.Checks;

public class GetLogCommand extends Command{
	
	public GetLogCommand() {
		this.help="fetches logfile";
		this.name="log";
		this.ownerCommand = true;
	}
	
	@Override
	protected void execute(CommandEvent event) {
		if(!Checks.isInAdminChannel(event.getChannel())) {
			return;
		}
		
		event.reactSuccess();
		event.replyInDm("Here's the logfile you ordered.", new File("log/SpeedBowl3DiscordBot.log"), "SpeedBowl3.log");
	}
}