package com.nolacola.discord.speedbowl.commands.owner;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.nolacola.discord.speedbowl.NolaException;
import com.nolacola.discord.speedbowl.TheBot;
import com.nolacola.discord.speedbowl.database.PersistenceConnection;
import com.nolacola.discord.speedbowl.dto.Submission;
import com.nolacola.discord.speedbowl.enums.JudgementState;
import com.nolacola.discord.speedbowl.util.Checks;

import net.dv8tion.jda.api.entities.User;

public class JudgeCommand extends Command {
	
	private static final Logger log = LogManager.getLogger(TheBot.class);
	private PersistenceConnection persistenceConn;

	public JudgeCommand(PersistenceConnection persistenceConnection) {
		this.help = "Judge a submission";
		this.name = "judge";
		this.arguments = "<submissionid> <valid/invalid> for <speed>m/s at <height>km [- <freetext>] [(bonusChallenge:)<valid/invalid>]";
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
			reject(event, "Please supply submission ID & judgement " + this.getArguments());
			return;
		}
		
		args = args.toUpperCase();
		String[] argsSplit = args.split(" ");
		
		int submissionId = Integer.parseInt(argsSplit[0]);
		JudgementState judgement = JudgementState.valueOf(argsSplit[1]);
		JudgementState judgementBonus = null; 
		try{
			judgementBonus = JudgementState.valueOf(args.substring(args.lastIndexOf(" ")+1));
		}catch(Exception e) {
			// no Bonus judgement
		}
		
		if(judgement == JudgementState.UNJUDGED || judgementBonus == JudgementState.UNJUDGED) {
			reject(event, "UNJUDGED is not a valid judgement " + this.getArguments());
			return;
		}
		
		float speed = determineSpeed(args), height = determineHeight(args);
		if(speed == 0f || height == 0f) {
			reject(event, "Couldn't determine speed or height");
			return;
		}
		
		try {
			Submission submission = persistenceConn.loadSubmission(submissionId);
			if(submission == null) {
				event.reactError();
				event.reply("No Submission with provided id found.");
				return;
			}
			persistenceConn.judge(submission, judgement, judgementBonus, speed, height);

			event.reactSuccess();
			
			User userThatWasJudged = event.getJDA().getUserById(submission.getCommander().getCmdrId());
			userThatWasJudged.openPrivateChannel().queue(pc -> pc.sendMessage("Your submission #" + submission.getId() + " was just judged as " + judgement + "\r\n").queue());
		} catch (NolaException e) {
			log.error(e,e);
			event.reactError();
		}
	}

	private float determineHeight(String args) {
		//Matches all kinds of variantions. Either with or without m/s, with/without space, with or without decimals
		Pattern p = Pattern.compile(".*AT (\\d*\\.?\\d*) ?(KM)?.*"); 
		Matcher m = p.matcher(args);
		if(m.matches()) {
			return Float.parseFloat(m.group(1));
		}
		return 0f;
	}
	private float determineSpeed(String args) {
		//Matches all kinds of variantions. Either with or without km, with/without space, with or without decimals
		Pattern p = Pattern.compile(".*FOR (\\d*\\.?\\d*) ?(M\\/S)? AT.*"); 
		Matcher m = p.matcher(args);
		if(m.matches()) {
			return Float.parseFloat(m.group(1));
		}
		return 0f;
	}
	
	private void reject(CommandEvent event, String message) {
		event.reply(message);
		event.reactError();
	}

}
