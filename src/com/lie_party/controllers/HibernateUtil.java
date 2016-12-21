package com.lie_party.controllers;

import java.io.FileReader;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonStructure;
import javax.json.JsonValue;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.lie_party.GameSession;
import com.lie_party.Player;


public class HibernateUtil {

    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
            return new Configuration().configure().buildSessionFactory();
        }
        catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
    


    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    
    
    public static GameSession getCurrentGameSession(String gameCode){
    	try{
        	SessionFactory sessionFactory = buildSessionFactory();
        	Session hSession = sessionFactory.openSession();
        	Transaction tx = hSession.beginTransaction();
        	String hql = "FROM GameSession gs WHERE gs.code = '" + gameCode + "'";
        	Query query = hSession.createQuery(hql);
        	List<GameSession> results = query.list();
        	tx.commit();
        	hSession.close();
        	if(results.size() == 0){
        		return null;	
        	}else{
        		return results.get(0);
        	}
    	}catch(Exception e){
    		e.printStackTrace();
    		return null;
    	}

    }
    
    public static Question getCurrentQuestion(String gameCode){
    	GameSession gs = getCurrentGameSession(gameCode);
    	if(gs == null){
    		return null;
    	}else{
    		
    	}
    	return null;
    }
    
    
    public static boolean clearAllStandby(String gameCode){
    	try{
        	SessionFactory sessionFactory = buildSessionFactory();
        	Session hSession = sessionFactory.openSession();
        	Transaction tx = hSession.beginTransaction();
			String hql = "UPDATE Player set standBy = :standBy "  + 
		             "WHERE  roomCode = :roomCode";
			Query query = hSession.createQuery(hql);
			query.setParameter("standBy", false);
			query.setParameter("roomCode", gameCode);
			int rowsEffected = query.executeUpdate();
        	tx.commit();
        	hSession.close();
        	return true;
    	}catch(Exception e){
    		e.printStackTrace();
    		return false;
    	}
    }
    
    public static boolean checkIfAllOnStandby(String gameCode){
    	boolean allStand = false;
    	try{
        	SessionFactory sessionFactory = buildSessionFactory();
        	Session hSession = sessionFactory.openSession();
        	Transaction tx = hSession.beginTransaction();
			String hql = "FROM Player p WHERE p.roomCode = '" + gameCode + "' AND p.standBy = false";
			Query query = hSession.createQuery(hql);
			List<Player> results = query.list();
			tx.commit();
			if(results.size() == 0){
				allStand = true;
			}
        	
        	hSession.close();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return allStand;
    }
    
    public static boolean newQuestionForSession(String gameCode){
    	boolean found = true;
    	try{
        	SessionFactory sessionFactory = buildSessionFactory();
        	Session hSession = sessionFactory.openSession();
        	Transaction tx = hSession.beginTransaction();
        	String hql = "FROM GameSession gs WHERE gs.code = '" + gameCode + "'";
        	Query query = hSession.createQuery(hql);
        	List<GameSession> results = query.list();
        	if(results.size() ==0){
        		found = false;
        	}else{
        		GameSession gSession = results.get(0);
        		gSession.setCurrentQuestion(QuestionsLib.getRandomQuestion().toByteArray());
        		hSession.update(gSession);
        	}
        	
        	tx.commit();
        	hSession.close();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return found;
    }
    

}