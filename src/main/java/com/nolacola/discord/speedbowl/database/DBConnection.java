package com.nolacola.discord.speedbowl.database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

import java.util.List;

import com.nolacola.discord.speedbowl.NolaException;
import com.nolacola.discord.speedbowl.dto.Submission;
import com.nolacola.discord.speedbowl.enums.ErrorCodes;
import com.nolacola.discord.speedbowl.enums.JudgementState;
import com.nolacola.discord.speedbowl.enums.LoadActionEnum;

public class DBConnection implements PersistenceConnection {

	private static final Logger log = LogManager.getLogger(DBConnection.class);
	
	public SessionFactory sessionFactory;
	
	{
		setup();
	}
	
	public void setup() {
    	final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
    	        .configure() 
    	        .build();
    	try {
    	    sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
    	} catch (Exception e) {
    		log.error(e, e);
    	    StandardServiceRegistryBuilder.destroy(registry);
    	}
    }

	@Override
	public List<Submission> loadEntries(LoadActionEnum loadAction) {
		Session session = sessionFactory.openSession();
		String queryString = "FROM Submission";
		Query<Submission> query = null;
		switch(loadAction) {
			case ALL: 
				query = session.createQuery(queryString, Submission.class);
				break;
			case JUDGED: queryString += " WHERE judgement = :judgementVal or judgement = :judgementInval";
				query = session.createQuery(queryString, Submission.class)
						.setParameter("judgementVal", JudgementState.VALID)
						.setParameter("judgementInval", JudgementState.INVALID);
				break;
			case LATEST: queryString += " ORDER BY submissionTimestamp DESC";
				query = session.createQuery(queryString, Submission.class);
				break;
			case UNJUDGED: queryString += " WHERE judgement = :judgement";
				query = session.createQuery(queryString, Submission.class)
						.setParameter("judgement", JudgementState.UNJUDGED);
				break;
		}
		
		List<Submission> submissions = query.list();
		
		session.close();
		return submissions;
	}
	
	@Override
	public void purge() {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		
		try {
			session.createQuery("DELETE FROM Submission").executeUpdate();
			session.createQuery("DELETE FROM User").executeUpdate();
			transaction.commit();
		}catch(Exception e) {
			log.error(e,e);
			transaction.rollback();
		}
		
		session.close();
    }
	
	@Override
	public void judge(Submission submissionToUpdate, JudgementState judgement, JudgementState judgementBonus, float speed, float height) throws NolaException {
		Session session = sessionFactory.openSession();
		
		if(submissionToUpdate.getJudgement() != JudgementState.UNJUDGED) {
			throw new NolaException(ErrorCodes.CANT_JUDGE_ALREADY_JUDGED_SUBMISSION);
		}
		
		Transaction trans = session.beginTransaction();
		submissionToUpdate.setJudgement(judgement);
		submissionToUpdate.setSpeed(speed);
		submissionToUpdate.setHeight(height);
		if(judgementBonus != null) {
			submissionToUpdate.setBonusChallengeJudgement(judgementBonus);
		}
		session.saveOrUpdate(submissionToUpdate);
		trans.commit();
		session.close();
	}
	
	@Override
	public List<Submission> loadSubmissionsForUser(String id) {
		Session session = sessionFactory.openSession();
		String queryString = "FROM Submission WHERE cmdrId = :userId";
		
		Query<Submission> query = session.createQuery(queryString, Submission.class)
				.setParameter("userId", id);
		
		List<Submission> submissions = query.list();
		
		session.close();
		return submissions;
	}

	@Override
	public List<Submission> leaderboard() {
		Session session = sessionFactory.openSession();
		String queryString = "FROM Submission as superSub "
				+ "WHERE speed = ("
					+ "	SELECT max(speed)"
					+ "    FROM Submission as subSub"
					+ "    WHERE subSub.commander.cmdrId = superSub.commander.cmdrId AND judgement = :judgement"
					+ ") "
				+ "ORDER BY speed DESC";
		
		Query<Submission> query = session.createQuery(queryString, Submission.class)
				.setParameter("judgement", JudgementState.VALID);
		
		List<Submission> submissions = query.list();
		
		session.close();
		return submissions;
	}

	@Override
	public int countSubmissionsFromUser(String id) {
		Session session = sessionFactory.openSession();
		String queryString = "SELECT count(id) "
				+ "FROM Submission "
				+ "WHERE cmdrId = :userId";
		
		Query<Long> query = session.createQuery(queryString, Long.class)
				.setParameter("userId", id);
		
		Long amount = query.getSingleResult();
		
		session.close();
		return amount.intValue();
	}

	@Override
	public void saveSubmission(Submission submission) {
		Session session = sessionFactory.openSession();
		Transaction trans = session.beginTransaction();
		session.saveOrUpdate(submission.getCommander());
		session.saveOrUpdate(submission);
		trans.commit();
		session.close();
	}

	@Override
	public Submission loadSubmission(int submissionId) {
		Session session = sessionFactory.openSession();
		
		Submission submissionToUpdate = session.get(Submission.class, submissionId);
		
		session.close();
		return submissionToUpdate;
	}

}
