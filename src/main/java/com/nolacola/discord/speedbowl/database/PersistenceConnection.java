package com.nolacola.discord.speedbowl.database;

import java.util.List;

import com.nolacola.discord.speedbowl.NolaException;
import com.nolacola.discord.speedbowl.dto.Submission;
import com.nolacola.discord.speedbowl.enums.JudgementState;
import com.nolacola.discord.speedbowl.enums.LoadActionEnum;

public interface PersistenceConnection {
	
	public void purge();

	public List<Submission> loadEntries(LoadActionEnum loadAction);

	public void judge(Submission submission, JudgementState judgement, JudgementState judgementBonus, float speed, float height) throws NolaException;

	public List<Submission> loadSubmissionsForUser(String id);

	public List<Submission> leaderboard();

	public int countSubmissionsFromUser(String id);

	public void saveSubmission(Submission submission);

	public Submission loadSubmission(int submissionId);
}
