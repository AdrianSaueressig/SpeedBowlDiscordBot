package com.nolacola.discord.speedbowl.commands;

import java.util.List;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.nolacola.discord.speedbowl.database.PersistenceConnection;
import com.nolacola.discord.speedbowl.dto.Submission;
import com.nolacola.discord.speedbowl.messages.SubmissionToTableMessageHandler;
import com.nolacola.discord.speedbowl.util.Checks;

public class MySubmissionsCommand extends Command{
	
	private PersistenceConnection persistenceConn;
	
	public MySubmissionsCommand(PersistenceConnection persistenceConnection) {
		this.help = "View your submissions";
		this.name = "mySubmissions";
		this.persistenceConn = persistenceConnection;
	}
	
	@Override
	protected void execute(CommandEvent event) {
		if(!Checks.isInPublicChannel(event.getChannel())) {
			return;
		}
		
		List<Submission> submissions = persistenceConn.loadSubmissionsForUser(event.getAuthor().getId());
		
		List<String> tables = new SubmissionToTableMessageHandler().convertToTableMessageForMySubmissions(submissions, event.getAuthor().getName());
		for (String table : tables) {
			event.reply(table);
		}
		
		event.reactSuccess();
	}
}
