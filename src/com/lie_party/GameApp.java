package com.lie_party;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class GameApp {

	/**
	 * @param args
	 */
	
	public static final int ANSWER_NUM_OPTIONS = 6;
	
	public static final int CORRECT_SCORE = 500;
	
	public static final int LIE_SCORE = 500;
	
	public static final int MAX_ROUNDS = 8;
	
	public GameApp(){
//		   AbstractApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");
//
//		      GameSession gSession = (GameSession) context.getBean("gameSession");
//		      SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
//		      Session session = sessionFactory.openSession();
//		      Transaction tx = null;
//		      try {
//		         tx = session.beginTransaction();
//		         // do some work
//		         session.save(gSession);
//		         tx.commit();
//		      }
//		      catch (Exception e) {
//		         if (tx!=null) tx.rollback();
//		         e.printStackTrace(); 
//		      }finally {
//		         session.close();
//		      }
//		      System.out.println(gSession.getCode());
	}
	public static void main(String[] args) {
//		   AbstractApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");
//
//		      GameSession gSession = (GameSession) context.getBean("gameSession");
//		      SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
//		      Session session = sessionFactory.openSession();
//		      
//		      Transaction tx = null;
//		      try {
//		         tx = session.beginTransaction();
//		         // do some work
//		         session.save(gSession);
//		         tx.commit();
//		      }
//		      catch (Exception e) {
//		         if (tx!=null) tx.rollback();
//		         e.printStackTrace(); 
//		      }finally {
//		         session.close();
//		      }
//		      System.out.println(gSession.getCode());
	}

}
