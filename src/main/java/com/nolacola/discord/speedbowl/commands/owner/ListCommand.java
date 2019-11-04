package com.nolacola.discord.speedbowl.commands.owner;

import java.util.List;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.nolacola.discord.speedbowl.database.PersistenceConnection;
import com.nolacola.discord.speedbowl.dto.Submission;
import com.nolacola.discord.speedbowl.enums.LoadActionEnum;
import com.nolacola.discord.speedbowl.messages.SubmissionToTableMessageHandler;
import com.nolacola.discord.speedbowl.util.Checks;

public class ListCommand extends Command{
	
	private PersistenceConnection persistenceConn;

	public ListCommand(PersistenceConnection persistenceConnection) {
		this.help = "List database entries";
		this.name = "list";
		this.arguments = "<all/latest/unjudged/judged>";
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
			event.reply("Please supply which kind of entries you'd like to list. " + this.getArguments());
			event.reactError();
			return;
		}
		args = args.toUpperCase();
		
		LoadActionEnum action = LoadActionEnum.valueOf(args);
		List<Submission> submissionList = persistenceConn.loadEntries(action);
		List<String> tables = new SubmissionToTableMessageHandler().convertToTableMessageForList(submissionList);
		for (String table : tables) {
			event.reply(table);
		}
		
		event.reactSuccess();
	}

}
