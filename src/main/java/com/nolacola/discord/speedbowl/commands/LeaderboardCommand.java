package com.nolacola.discord.speedbowl.commands;

import java.util.List;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.nolacola.discord.speedbowl.database.PersistenceConnection;
import com.nolacola.discord.speedbowl.dto.Submission;
import com.nolacola.discord.speedbowl.messages.SubmissionToTableMessageHandler;
import com.nolacola.discord.speedbowl.util.Checks;

public class LeaderboardCommand extends Command {
	private PersistenceConnection persistenceConn;
	
	public LeaderboardCommand(PersistenceConnection persistenceConnection) {
		this.help = "View the Leaderboard";
		this.name = "leaderboard";
		this.persistenceConn = persistenceConnection;
	}
	
	@Override
	protected void execute(CommandEvent event) {
		if(!Checks.isInPublicChannel(event.getChannel())) {
			return;
		}
		
		List<Submission> submissions = persistenceConn.leaderboard();

		event.reply("This is the preliminary Leaderboard.");
		
		List<String> tables = new SubmissionToTableMessageHandler().convertToTableMessageForLeaderboard(submissions);
		for (String table : tables) {
			event.reply(table);
		}
		
		event.reactSuccess();
	}

}
