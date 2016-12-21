package com.lie_party.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.lie_party.*;

/**
 * Servlet implementation class QuestionServlet
 */
//@WebServlet("/QuestionServlet")
public class QuestionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public QuestionServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//QuestionLib.
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//GameSession gSession = (GameSession) request.getAttribute("gSession");
		//System.out.println("Game Session is " + gSession);
		
		//gSession.setCurrentQuestion(QuestionsLib.getRandomQuestion().toByteArray());
		HttpSession curSession = request.getSession();
		String roomCode = (String) curSession.getAttribute("gameCode");
		
		//hibernate init stuff
		SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
		Session hSession = sessionFactory.openSession();
		Transaction tx = null;
		

		
		String type = request.getParameter("type");
		String answer = request.getParameter("answer");
		//System.out.println("this is the answer entered: " + answer);
		
		
		switch (type){
		case "questionSubmit":
			
			
			try{
				Player player = (Player) request.getSession().getAttribute("user");
				
				player.setAnswer(answer);
				Question quest = (Question) request.getSession().getAttribute("currentQuestion");
				
				tx = hSession.beginTransaction();					
				
				//System.out.println("answer was correct...");
				if(quest.checkAnswer(answer)){
					player.adjustScore(GameApp.CORRECT_SCORE);
				}
				player.setStandBy(true);
				//do other stuff that is correct
				//System.out.println("The player is " + player.getName() + " with in-memory score of " + player.getScore());
				hSession.saveOrUpdate(player);
				tx.commit();
				
				//can configure for other players to refresh
				
				//check if all other players are on standby too
				tx = hSession.beginTransaction();
				String sql = "FROM Player pl WHERE pl.standBy = false and pl.roomCode = '" + roomCode + "'";
				Query query = hSession.createQuery(sql);
				List<UserSessions> results = query.list();
				
				//if no other player is on standby
				//set all player stand by to false and set usersession refresh to true
				if(results.size() ==0){
					String hql = "UPDATE Player set standBy = :standBy "  + 
				             "WHERE  roomCode = :roomCode";
					query = hSession.createQuery(hql);
					query.setParameter("standBy", false);
					query.setParameter("roomCode", roomCode);
					int rowsEffected = query.executeUpdate();
					
					System.out.println("Rows affected: " + rowsEffected);
					
					//hql = "UPDATE UserSessions set redirect = true WHERE roomCode = '" + roomCode + "'";
					hql = "UPDATE UserSessions set redirect = :redirect WHERE roomCode = :roomCode";
					query = hSession.createQuery(hql);
					query.setParameter("redirect", true);
					query.setParameter("roomCode", roomCode);
					
					int rowsEffRedirect = query.executeUpdate();
					System.out.println("the redirected rows have been changed " + rowsEffRedirect);
					tx.commit();
				}
					
					
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				hSession.close();
			}
			
			
			
			break;
		
		case "answerPicked":
			
//			@SuppressWarnings("unchecked")
//			Hashtable<String, ArrayList<String>> answerReview = (Hashtable<String, ArrayList<String>>) request.getSession().getAttribute("answerReview");
//			if(answerReview ==null){
//				answerReview = new Hashtable<String, ArrayList<String>>();
//			}
//			
//			Hashtable<String, ArrayList<String>> answerReviewSug = (Hashtable<String, ArrayList<String>>) request.getSession().getAttribute("answerReviewSug");
//			if(answerReviewSug ==null){
//				answerReviewSug = new Hashtable<String, ArrayList<String>>();
//			}
			//initializing
			Hashtable<String, ArrayList<String>> answerReview = null, answerReviewSug = null;
			GameSession gameSession = (GameSession) request.getSession().getAttribute("gameSession");
			
			Player curPlayer = (Player) request.getSession().getAttribute("user");
			try{
				//get the suggestions from the database
				tx = hSession.beginTransaction();
				String hql = "From GameSession gs WHERE gs.code = '" + roomCode + "'" ;
				Query query = hSession.createQuery(hql);
				List<GameSession> gameResults = query.list();
				if(gameResults.size() != 0){
					gameSession = gameResults.get(0); 
					if(gameSession.getAnswerReview() == null){
						 answerReview = new Hashtable<String, ArrayList<String>>();
					}else{
						answerReview = GameSession.readByteArray(gameSession.getAnswerReview());
					}
					
					if(gameSession.getAnswerReviewSug() == null){
						answerReviewSug = new Hashtable<String, ArrayList<String>>();
					}else{
						answerReviewSug = GameSession.readByteArray(gameSession.getAnswerReviewSug());
					}
					
				}
				tx.commit();
				
				
				//find players who had the answer and give them points and also make a list to use for presentation
				tx = hSession.beginTransaction();
				hql = "FROM Player p WHERE p.roomCode = '" + roomCode + "' AND p.answer = '" + answer + "'";
				query = hSession.createQuery(hql);
				List<Player> results = query.list();
				for(int i = 0; i< results.size(); i++){
					Player currentP =results.get(i);
					if(currentP.getAnswer().equals(answer)){
						currentP.setScore(currentP.getScore() + GameApp.LIE_SCORE);
						hSession.update(currentP);				
						ArrayList<String> al;
						//update current player status
						hql = "FROM Player p WHERE p.id = "  + curPlayer.getId();
						query = hSession.createQuery(hql);
						Player currentPlayer = (Player) query.list().get(0);
						
						
						currentPlayer.setStandBy(true);
						
						
						hSession.update(currentPlayer);
						request.getSession().setAttribute("user", currentPlayer);
						if(answerReview.get(answer) == null){
							al = new ArrayList<String>();
							al.add(currentP.getName());
							al.add(curPlayer.getName());
							answerReview.put(answer, al);
						}else{
							al = answerReview.get(answer);
							al.add(curPlayer.getName());
							
						}
						
					}
				}
				System.out.println("The results is " + results.size() + " the object " + results);
				//if answer entered is not from other players
				if(results.size() == 0){
					ArrayList<String> al;
					//update current player status
					hql = "FROM Player p WHERE p.id = "  + curPlayer.getId();
					query = hSession.createQuery(hql);
					Player currentPlayer = (Player) query.list().get(0);
					
					
					currentPlayer.setStandBy(true);
					
					
					hSession.update(currentPlayer);
					request.getSession().setAttribute("user", currentPlayer);
					if(answerReviewSug.get(answer) == null){
						al = new ArrayList<String>();
						al.add(curPlayer.getName());
						answerReviewSug.put(answer, al);
					}else{
						al = answerReviewSug.get(answer);
						al.add(curPlayer.getName());
					}
					System.out.println("our array list is: " + al);
						
				}
				

				
				//putting the suggestions back
				gameSession.setAnswerReview(GameSession.toByteArray(answerReview));
				gameSession.setAnswerReviewSug(GameSession.toByteArray(answerReviewSug));
				hSession.saveOrUpdate(gameSession);
				
				
				tx.commit();
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				hSession.close();				
			}
			
			//if all other players are on standby, 
			boolean standby = HibernateUtil.checkIfAllOnStandby(roomCode);
			System.out.println("the standby factor is " + standby);
			if(standby){
				//gameSession.setCurrentQuestion(QuestionsLib.getRandomQuestion().toByteArray());
				HibernateUtil.newQuestionForSession(roomCode);
				//HibernateUtil.clearAllStandby(roomCode);
			}
			
			break;
			
			
		case "likePick":
			try{
				tx = hSession.beginTransaction();
				// do some work
				String hql = "FROM Player "  + 
				"WHERE answer = '" + answer + "'";
				Query query = hSession.createQuery(hql);
				List results = query.list();
				if (results.size() > 0) {
					Player player = (Player) results.get(0);
					player.setLikes(player.getLikes() +1);
					//hSession.save(player);
					hSession.update(player);
					tx.commit();
				}

				
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				hSession.close();
			}
			break;
	
		}
		

    	
		//response.sendRedirect("question-page.jsp");
	}

}
