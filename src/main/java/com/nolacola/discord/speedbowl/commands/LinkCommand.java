package com.nolacola.discord.speedbowl.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.nolacola.discord.speedbowl.database.PersistenceConnection;
import com.nolacola.discord.speedbowl.util.Checks;

public class LinkCommand extends Command{
	private PersistenceConnection persistenceConn;

	public LinkCommand(PersistenceConnection persistenceConnection) {
		this.help = "Get the videolink of a submission";
		this.name = "link";
		this.arguments = "<submissionid>";
		this.persistenceConn = persistenceConnection;
	}
	
	@Override
	protected void execute(CommandEvent event) {
		if(!Checks.isInPublicChannel(event.getChannel()) && !Checks.isInAdminChannel(event.getChannel())) {
			return;
		}
		
		String args = event.getArgs();
		if(args.isEmpty()) {
			event.reply("Please supply submission ID");
			event.reactError();
			return;
		}
		
		Integer submissionId = Integer.parseInt(args.trim());
		
		String link = persistenceConn.loadSubmission(submissionId).getLink();
		event.reply(link);
		event.reactSuccess();
	}

}
