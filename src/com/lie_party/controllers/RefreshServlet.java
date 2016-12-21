package com.lie_party.controllers;

import java.io.IOException;
import java.util.Enumeration;
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

import com.lie_party.Player;
import com.lie_party.UserSessions;


/**
 * Servlet implementation class RefreshServlet
 */
//@WebServlet("/RefreshServlet")
public class RefreshServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RefreshServlet() {
        super();

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
		
		HttpSession curSession = request.getSession();
		Transaction tx = null;
		
		if(page.equals("gamehub") ){
			try{
				tx = hSession.beginTransaction();
				//System.out.println(curSession.hashCode());
				//String sql = "FROM UserSessions us WHERE us.hashCode = " + curSession.hashCode();
				String sql = "FROM UserSessions us WHERE us.hashCode = " + curSession.hashCode();
				//System.out.println(sql);
				Query query = hSession.createQuery(sql);
				//List<UserSessions> results = query.list();
				List<UserSessions> results = query.list();
				if(!tx.wasCommitted()){
					tx.commit();
				}
				//tx.commit();
				//System.out.println("Checking to see if it has this hashcode " + curSession.hashCode() + ", for " + results);
				if(results.size() != 0){
					UserSessions userSession = results.get(0);
					tx = hSession.beginTransaction();
					if(userSession.getRedirect()){
						userSession.setRedirect(false);
						hSession.saveOrUpdate(userSession);
						tx.commit();
						response.setHeader("redirect", "true");
					}else if(userSession.getRefresh()){
						userSession.setRefresh(false);
						hSession.saveOrUpdate(userSession);
						tx.commit();
						response.setHeader("refresh", "true");
					}
				}
				
			}catch(Exception e){
			   //if (tx!=null) tx.rollback();
			   e.printStackTrace(); 
			}finally{
				hSession.close();
			}
		}else if( page.equals("question_page")){
			try{
				tx = hSession.beginTransaction();
				//System.out.println(curSession.hashCode());
				//String sql = "FROM UserSessions us WHERE us.hashCode = " + curSession.hashCode();
				String sql = "FROM UserSessions us WHERE us.hashCode = " + curSession.hashCode();
				//System.out.println(sql);
				Query query = hSession.createQuery(sql);
				//List<UserSessions> results = query.list();
				List<UserSessions> results = query.list();
				if(!tx.wasCommitted()){
					tx.commit();
				}
				//tx.commit();
				//System.out.println("Checking to see if it has this hashcode " + curSession.hashCode() + ", for " + results);
				if(results.size() != 0){
					UserSessions userSession = results.get(0);
					tx = hSession.beginTransaction();
					if(userSession.getRefresh()){
						userSession.setRefresh(false);
						hSession.saveOrUpdate(userSession);
						
						response.setHeader("refresh", "true");
					}
					if(userSession.getRedirect()){
						System.out.println("redirect was true!!! " +  userSession);
						userSession.setRedirect(false);
						hSession.saveOrUpdate(userSession);
						
						response.setHeader("redirect", "true");
					}
					tx.commit();
				}
				
			}catch(Exception e){
			   //if (tx!=null) tx.rollback();
			   e.printStackTrace(); 
			}finally{
				hSession.close();
			}
		}else if(page.equals("answer_page")){
			
			
			try{
				tx = hSession.beginTransaction();
				String sql = "FROM Player p WHERE p.standBy = false AND p.roomCode ='" +curSession.getAttribute("gameCode") +  "'";
				Query query = hSession.createQuery(sql);
				List<Player> results = query.list();
				
				tx.rollback();
				if(results.size() == 0){
					tx = hSession.beginTransaction();
					//System.out.println(curSession.hashCode());
					sql = "FROM UserSessions us WHERE us.roomCode = '" + curSession.getAttribute("gameCode")  + "'";
					query = hSession.createQuery(sql);
					List<UserSessions> userSessions = query.list();
					for(UserSessions us : userSessions){
						us.setRefresh(true);
						hSession.update(us);
					}
					tx.commit();
				}
			}catch(Exception e){
				   if (tx!=null) tx.rollback();
				   e.printStackTrace(); 
			}finally{
				
			}
			
			try{
				tx = hSession.beginTransaction();
				//System.out.println(curSession.hashCode());
				String sql = "FROM UserSessions us WHERE us.hashCode = " + curSession.hashCode();
				Query query = hSession.createQuery(sql);
				List<UserSessions> results = query.list();
				//System.out.println("Our hash is: " + curSession.hashCode());
				
				if(results.size()>0){
					UserSessions userSession = results.get(0);
					if(userSession.getRefresh()){
						userSession.setRefresh(false);
						hSession.save(userSession);
						tx.commit();
						response.setHeader("refresh", "true");
					}
				}
				
			}catch(Exception e){
			   if (tx!=null) tx.rollback();
			   e.printStackTrace(); 
			}finally{
				hSession.close();
			}
			
		}
		
	}
	
	


	

}
