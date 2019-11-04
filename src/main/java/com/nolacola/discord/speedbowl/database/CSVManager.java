package com.nolacola.discord.speedbowl.database;

import java.io.File;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.nolacola.discord.speedbowl.dto.Submission;
import com.nolacola.discord.speedbowl.dto.User;
import com.nolacola.discord.speedbowl.enums.JudgementState;
import com.nolacola.discord.speedbowl.enums.LoadActionEnum;

public class CSVManager implements PersistenceConnection {

	private static final String VALUE_SEPERATOR = ",";
	private static final String FILENAME = "submissions.csv";

	@Override
	public List<Submission> loadEntries(LoadActionEnum loadAction){
		try {
			List<String> entries = Files.readAllLines(new File(FILENAME).toPath());
			List<Submission> submissions = entries.stream()
					.skip(1) //first line is header line
					.map(this::mapStringEntryToSubmission)
					.collect(Collectors.toList());
			return submissions;
		} catch (Exception e) {
			// @TODO Auto-generated catch block
		}
		return null;
	}
	
	private Submission mapStringEntryToSubmission(String stringEntry){
		if(stringEntry == null || stringEntry.isEmpty()) {
			return null;
		}
		String[] entryValues = stringEntry.split(VALUE_SEPERATOR);

		Submission result = new Submission();
		
		User commander = new User();
		commander.setCmdrId(entryValues[1]);
		commander.setCmdrName(entryValues[2]);
		
		result.setId(Integer.parseInt(entryValues[0]));
		result.setBonusChallengeJudgement(JudgementState.valueOf(entryValues[9]));
		result.setCommander(commander);
		result.setHeight(Float.parseFloat(entryValues[8]));
		result.setJudgement(JudgementState.valueOf(entryValues[6]));
		result.setLink(entryValues[11]);
		result.setRawSubmissionText(entryValues[10]);
		result.setShipname(entryValues[4]);
		result.setShiptype(entryValues[3]);
		result.setSpeed(Float.parseFloat(entryValues[7]));
		
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = sdf.parse(entryValues[5]);
			result.setSubmissionTimestamp(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
		}
		
		return result;
	}

	@Override
	public void purge() {
		// TODO Auto-generated method stub
	}

	@Override
	public void judge(Submission submission, JudgementState judgement, JudgementState judgementBonus, float speed, float height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Submission> loadSubmissionsForUser(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Submission> leaderboard() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int countSubmissionsFromUser(String id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void saveSubmission(Submission submission) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Submission loadSubmission(int submissionId) {
		// TODO Auto-generated method stub
		return null;
	}
}
