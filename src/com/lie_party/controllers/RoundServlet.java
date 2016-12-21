package com.lie_party.controllers;

import java.io.IOException;
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

import com.lie_party.GameApp;
import com.lie_party.GameSession;
import com.lie_party.UserSessions;

/**
 * Servlet implementation class RoundServlet
 */
//@WebServlet("/RoundServlet")
public class RoundServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RoundServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String page = request.getParameter("page");
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		Session hSession = sessionFactory.openSession();
		String gameCode = (String) request.getSession().getAttribute("gameCode");
		String isHost = (String) request.getSession().getAttribute("isHost");
		HttpSession session = (HttpSession) request.getSession();
		Transaction tx = null;
		switch (page){
			case "gamehub":
				try{
					tx = hSession.beginTransaction();
					String sql = "FROM GameSession gs WHERE gs.code = '" + gameCode +"'";
					Query query = hSession.createQuery(sql);
					List<GameSession> results = query.list();
					if(!tx.wasCommitted()){
						tx.commit();
					}
					GameSession gSession = results.get(0);
					
					tx = hSession.beginTransaction();
					gSession.setRound(1);
					gSession.setCurrentQuestion(QuestionsLib.getRandomQuestion().toByteArray());
					hSession.saveOrUpdate(gSession);
					request.getSession().setAttribute("gameSession", gSession);
					tx.commit();
					//String sql
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					//hSession.close();
				}
				
				//make it so other users refresh
				try{
					tx = hSession.beginTransaction();
					
					String sql = "FROM UserSessions us WHERE us.roomCode = '" + gameCode +"'";
					Query query = hSession.createQuery(sql);
					List<UserSessions> results = query.list();
					if(!tx.wasCommitted()){
						tx.commit();
					}
					
					tx = hSession.beginTransaction();
					//GameSession gSession = results.get(0);
					for(int i = 0; i<results.size(); i++){
						UserSessions tempSesh = results.get(i);
						if(tempSesh.getHashCode() != session.hashCode()){
							tempSesh.setRedirect(true);
							hSession.saveOrUpdate(tempSesh);
						}
						
					}					
					tx.commit();
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					hSession.close();
				}
				
				response.setHeader("redirectAddress", "question-page.jsp");
				break;
			case "round_results":
				int round = 0;
				try{
					tx = hSession.beginTransaction();
					String sql = "FROM GameSession gs WHERE gs.code = '" + gameCode +"'";
					Query query = hSession.createQuery(sql);
					List<GameSession> results = query.list();
					if(!tx.wasCommitted()){
						tx.commit();
					}
					GameSession gSession = results.get(0);
					gSession.setAnswerReview(null);
					gSession.setAnswerReviewSug(null);
					tx = hSession.beginTransaction();
					if(isHost == "true"){
						gSession.setRound(gSession.getRound() + 1);
					}
					round = gSession.getRound();
					
					//reset round variables
//					gSession.setAnswerReview(null);
//					gSession.setAnswerReviewSug(null);
//					gSession.setCurrentQuestion(QuestionsLib.getRandomQuestion().toByteArray());
//					hSession.saveOrUpdate(gSession);
//					request.getSession().setAttribute("gameSession", gSession);
					tx.commit();
					//String sql
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					hSession.close();
				}
				HibernateUtil.clearAllStandby(gameCode);
				if(round > GameApp.MAX_ROUNDS){//if more than maximum rounds
					response.setHeader("redirectAddress", "game-results.jsp");
				}else{
					response.setHeader("redirectAddress", "question-page.jsp");
				}
				
				
				break;
		}
	}

}
