package com.nolacola.discord.speedbowl.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.nolacola.discord.speedbowl.enums.JudgementState;

@Entity
@Table(name = "submissions")
public class Submission {
	
	private static final String NEW_LINE = " \n";

	private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public Submission() {
		this.judgement = JudgementState.UNJUDGED;
		this.bonusChallengeJudgement = JudgementState.UNJUDGED;
		this.speed = 0;
		this.height = 0;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne
    @JoinColumn(name = "cmdrId")
	private User commander;
	
	private String shiptype;
	
	private String shipname;
	
	private Date submissionTimestamp;
	
    @Column(length = 10)
    @Enumerated(EnumType.STRING)
	private JudgementState judgement;
	
	private float speed;
	
	private float height;
	
    @Column(length = 10)
    @Enumerated(EnumType.STRING)
	private JudgementState bonusChallengeJudgement;
	
	private String rawSubmissionText;
	
	private String link;
	
	public int getId() {
		return id;
	}

	public String getShiptype() {
		return shiptype;
	}
	
	public void setShiptype(String shiptype) {
		this.shiptype = shiptype;
	}
	
	public String getShipname() {
		return shipname;
	}
	
	public void setShipname(String shipname) {
		this.shipname = shipname;
	}
	
	public Date getSubmissionTimestamp() {
		return submissionTimestamp;
	}
	
	public void setSubmissionTimestamp(Date submissionTimestamp) {
		this.submissionTimestamp = submissionTimestamp;
	}
	
	public JudgementState getJudgement() {
		return judgement;
	}
	
	public void setJudgement(JudgementState judgement) {
		this.judgement = judgement;
	}
	
	public float getSpeed() {
		return speed;
	}
	
	public void setSpeed(float speed) {
		this.speed = speed;
	}
	
	public float getHeight() {
		return height;
	}
	
	public void setHeight(float height) {
		this.height = height;
	}
	
	public JudgementState getBonusChallengeJudgement() {
		return bonusChallengeJudgement;
	}
	
	public void setBonusChallengeJudgement(JudgementState bonusChallengeJudgement) {
		this.bonusChallengeJudgement = bonusChallengeJudgement;
	}
	
	public String getRawSubmissionText() {
		return rawSubmissionText;
	}
	
	public void setRawSubmissionText(String rawSubmissionText) {
		this.rawSubmissionText = rawSubmissionText;
	}
	
	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public User getCommander() {
		return commander;
	}

	public void setCommander(User commander) {
		this.commander = commander;
	}

	public void setId(int id) {
		this.id=id;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("id = ")
				.append(id + NEW_LINE)
				.append(" user = ")
				.append(commander.toString() + NEW_LINE)
				.append(" shiptype = ")
				.append(shiptype + NEW_LINE)
				.append(" shipname = ")
				.append(shipname + NEW_LINE)
				.append(" timestamp = ")
				.append(SDF.format(submissionTimestamp) + NEW_LINE)
				.append(" judgement = ")
				.append(judgement + NEW_LINE)
				.append(" speed = ")
				.append(speed + NEW_LINE)
				.append(" height = ")
				.append(height + NEW_LINE)
				.append(" bonusChallengeJudgement = ")
				.append(bonusChallengeJudgement + NEW_LINE)
				.append(" link = ")
				.append(link + NEW_LINE)
				.append(" rawSubmissionText = ")
				.append(rawSubmissionText + NEW_LINE);
		return builder.toString();
	}
}
