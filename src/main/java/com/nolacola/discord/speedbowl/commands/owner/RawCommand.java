package com.nolacola.discord.speedbowl.commands.owner;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.nolacola.discord.speedbowl.database.PersistenceConnection;
import com.nolacola.discord.speedbowl.util.Checks;

public class RawCommand extends Command {

	private PersistenceConnection persistenceConn;

	public RawCommand(PersistenceConnection persistenceConnection) {
		this.help = "Get the raw submission text for a submission";
		this.name = "raw";
		this.arguments = "<submissionid>";
		this.ownerCommand = true;
		this.persistenceConn = persistenceConnection;
	}
	
	@Override
	protected void execute(CommandEvent event) {
		if(!Checks.isInAdminChannel(event.getChannel())) {
			return;
		}
		
		String args = event.getArgs();
		if(args.isEmpty()) {
			event.reply("Please supply submission ID");
			event.reactError();
			return;
		}
		
		Integer submissionId = Integer.parseInt(args.trim());
		
		String raw = persistenceConn.loadSubmission(submissionId).getRawSubmissionText();
		event.reply(raw);
		event.reactSuccess();
	}

}
