package com.nolacola.discord.speedbowl.messages;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;

import com.jagrosh.jdautilities.commons.utils.TableBuilder;
import com.jagrosh.jdautilities.commons.utils.TableBuilder.Borders;
import com.nolacola.discord.speedbowl.dto.Submission;

public class SubmissionToTableMessageHandler {
	
	private static final int CHUNKSIZE = 3;

	private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss z";
	
	private String[] headersLeaderboard = new String[] {
			"cmdrName", "shiptype", "shipname", "time submitted",
			"speed", "height", "bonusChallenge", "link"};
	
	private String[] headersList = new String[] {
			"cmdrName", "shiptype", "shipname", "time submitted",
			"judgement", "speed", "height", "bonusChallenge", "link"};
	
	private String[] headersMySubmissions = new String[] {
			"shiptype", "shipname", "time submitted", "judgement",
			"speed", "height", "bonusChallenge"};
	
	String rowDelimiter = "\u2500";
	String columnDelimiter= "\u2502";
	String crossDelimiter = "\u253C";
	String leftIntersection = "\u251C";
	String rightIntersection = "\u2524";
	String upperIntersection=  "\u252C";
	String lowerIntersection=  "\u2534";
	String upLeftCorner= "\u250C";
	String upRightCorner= "\u2510";
	String lowLeftCorner= "\u2514";
	String lowRightCorner= "\u2518";
	String headerDelimiter= "\u2550";
	String headerCrossDelimiter="\u256A";
	String headerLeftIntersection= "\u255E";
	String headerRightIntersection="\u2561";
	String firstColumnDelimiter="\u2551";
	String firstColumnCrossDelimiter="\u256B";
	String firstColumnUpperIntersection="\u2565";
	String firstColumnLowerIntersection="\u2568";
	String headerColumnCrossDelimiter="\u256C";
	String horizontalOutline="\u2500";
	String verticalOutline="\u2502";
	
	private Borders bordersToUse = Borders.newHeaderRowNamesFrameBorders(rowDelimiter, columnDelimiter, crossDelimiter, leftIntersection, rightIntersection, upperIntersection, lowerIntersection, upLeftCorner, upRightCorner, lowLeftCorner, lowRightCorner, headerDelimiter, headerCrossDelimiter, headerLeftIntersection, headerRightIntersection, firstColumnDelimiter, firstColumnCrossDelimiter, firstColumnUpperIntersection, firstColumnLowerIntersection, headerColumnCrossDelimiter, horizontalOutline, verticalOutline);
	
	public List<String> convertToTableMessageForList(List<Submission> submissions) {
		if(CollectionUtils.isEmpty(submissions)) {
			return Arrays.asList("No Submissions");
		}
		
		List<String> tables = new ArrayList<>();
		List<List<Submission>> submissionsSplitIntoChunks = ListUtils.partition(submissions, CHUNKSIZE);
	
		for (List<Submission> list : submissionsSplitIntoChunks) {
			tables.add(
					new TableBuilder()
					.addHeaders(headersList) // setting headers
					.setValues(convertSubmissionsForList(list))
					.addRowNames(list.stream().map(s -> Integer.toString(s.getId())).toArray(String[]::new)) // setting row names
					.setName("Submissions") // the name (displayed in the top left corner)
					.frame(true)
					.setBorders(bordersToUse)// activating framing
					.codeblock(true)
					.build()
					);
		}
		return tables;
	}
	
	public List<String> convertToTableMessageForMySubmissions(List<Submission> submissions, String cmdrName) {
		if(CollectionUtils.isEmpty(submissions)) {
			return Arrays.asList("No Submissions");
		}
		
		List<String> tables = new ArrayList<>();
		List<List<Submission>> submissionsSplitIntoChunks = ListUtils.partition(submissions, CHUNKSIZE);
	
		for (List<Submission> list : submissionsSplitIntoChunks) {
			tables.add(
					new TableBuilder()
					.addHeaders(headersMySubmissions) // setting headers
					.setValues(convertSubmissionsForMySubmissions(list))
					.addRowNames(list.stream().map(s -> Integer.toString(s.getId())).toArray(String[]::new)) // setting row names
					.setName(cmdrName) // the name (displayed in the top left corner)
					.frame(true)
					.setBorders(bordersToUse)// activating framing
					.codeblock(true)
					.build()
					);
		}
		return tables;
	}

	public List<String> convertToTableMessageForLeaderboard(List<Submission> submissions) {
		if(CollectionUtils.isEmpty(submissions)) {
			return Arrays.asList("No Submissions");
		}
		
		List<String> tables = new ArrayList<>();
		List<List<Submission>> submissionsSplitIntoChunks = ListUtils.partition(submissions, CHUNKSIZE);
	
		for (List<Submission> list : submissionsSplitIntoChunks) {
			tables.add(
					new TableBuilder()
					.addHeaders(headersLeaderboard) // setting headers
					.setValues(convertSubmissionsForLeaderboard(list))
					.addRowNames(list.stream().map(s -> Integer.toString(submissions.indexOf(s)+1)).toArray(String[]::new)) // setting row names
					.setName("Leaderboard") // the name (displayed in the top left corner)
					.frame(true)
					.setBorders(bordersToUse)// activating framing
					.codeblock(true)
					.build()
					);
		}
		return tables;
	}
	
	private String[][] convertSubmissionsForList(List<Submission> submissions) {
		String[][] submissionsArray = new String[submissions.size()][headersList.length];
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		
		for (int i = 0; i < submissions.size(); i++) {
			Submission submission = submissions.get(i);
			submissionsArray[i] = new String[] {
					submission.getCommander().getCmdrName(),
					submission.getShiptype(),
					submission.getShipname(),
					sdf.format(submission.getSubmissionTimestamp()),
					submission.getJudgement().name(),
					Float.toString(submission.getSpeed()),
					Float.toString(submission.getHeight()),
					submission.getBonusChallengeJudgement().name(),
					submission.getLink()
			};
		}
		
		return submissionsArray;
	}
	
	private String[][] convertSubmissionsForMySubmissions(List<Submission> submissions) {
		String[][] submissionsArray = new String[submissions.size()][headersMySubmissions.length];
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		
		for (int i = 0; i < submissions.size(); i++) {
			Submission submission = submissions.get(i);
			submissionsArray[i] = new String[] {
					submission.getShiptype(),
					submission.getShipname(),
					sdf.format(submission.getSubmissionTimestamp()),
					submission.getJudgement().name(),
					Float.toString(submission.getSpeed()),
					Float.toString(submission.getHeight()),
					submission.getBonusChallengeJudgement().name()
			};
		}
		
		return submissionsArray;
	}
	
	private String[][] convertSubmissionsForLeaderboard(List<Submission> submissions) {
		String[][] submissionsArray = new String[submissions.size()][headersLeaderboard.length];
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		
		for (int i = 0; i < submissions.size(); i++) {
			Submission submission = submissions.get(i);
			submissionsArray[i] = new String[] {
					submission.getCommander().getCmdrName(),
					submission.getShiptype(),
					submission.getShipname(),
					sdf.format(submission.getSubmissionTimestamp()),
					Float.toString(submission.getSpeed()),
					Float.toString(submission.getHeight()),
					submission.getBonusChallengeJudgement().name(),
					submission.getLink()
			};
		}
		
		return submissionsArray;
	}
}
