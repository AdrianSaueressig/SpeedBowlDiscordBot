package com.nolacola.discord.speedbowl.commands;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.nolacola.discord.speedbowl.database.PersistenceConnection;
import com.nolacola.discord.speedbowl.dto.Submission;
import com.nolacola.discord.speedbowl.dto.User;
import com.nolacola.discord.speedbowl.util.Checks;

public class SubmitCommand extends Command {
	
	private static final Logger log = LogManager.getLogger(SubmitCommand.class);
	private PersistenceConnection persistenceConn;
	
	public SubmitCommand(PersistenceConnection persistenceConnection) {
		this.help = "Submit your runs!";
		this.name = "submit";
		this.arguments = "<cmdrname> - <shiptype> - <shipname> - <link>[ - <freetext>]";
		this.persistenceConn = persistenceConnection;
	}
	
	@Override
	protected void execute(CommandEvent event) {
		if(!Checks.isInPublicChannel(event.getChannel())) {
			return;
		}
		
		if(!Checks.isInRacePeriod()) {
			event.reactError();
			event.reply("You can't submit runs outside of the race period");
			return;
		}
		
		String args = event.getArgs();
		if(args.isEmpty()) {
			reject(event, "Please enter the details of your submission" + this.getArguments());
			return;
		}
		
		String[] splitArgs = args.split(" - ");
		List<String> argsAsList = Arrays.stream(splitArgs).map(s -> s.trim()).collect(Collectors.toList());
		
		if(argsAsList.size() < 4) {
			reject(event, "You need to supply all 4 mandatory arguments." + this.getArguments());
			return;
		}

		int amountOfSubmissions = persistenceConn.countSubmissionsFromUser(event.getAuthor().getId());
		if(amountOfSubmissions >= 3) {
			reject(event, "Submission denied. You can't submit more than 3 runs.");
			return;
		}
		
		Submission submission = new Submission();
		User user = new User();
		user.setCmdrId(event.getAuthor().getId());
		user.setCmdrName(argsAsList.get(0));
		submission.setCommander(user);
		submission.setShiptype(argsAsList.get(1));
		submission.setShipname(argsAsList.get(2));
		submission.setLink(argsAsList.get(3));
		submission.setRawSubmissionText(args);
		
		try {
			persistenceConn.saveSubmission(submission);
			event.reactSuccess();
		}catch(Exception e) {
			log.error(e,e);
			event.reactError();;
		}
		
	}

	private void reject(CommandEvent event, String message) {
		event.reply(message);
		event.reactError();
	}
	
}
