package com.lie_party.controllers;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.lie_party.*;

/**
 * Servlet implementation class HomePageServlet
 */
//@WebServlet("/HomePageServlet")
public class HomePageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public HomePageServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		//System.out.println(request.getParameter("type") + " whaaat?");
		//System.out.println(request.getHeader("type") + " header?");
		//response.addHeader("GameSessionCode", "from data "+ request.getParameter("type") + ", from header " + request.getHeader("type"));
		String type = request.getParameter("type");
		System.out.println("our hash is: " + request.getSession().hashCode());

		SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
		Session session = sessionFactory.openSession();
		
		HttpSession curSession = request.getSession();
		UserSessions userSesh = null;
		Transaction tx = null;
		
		
		switch (type){
		
		case "makeServer":
			//database access stuff, works now

			ApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");
			GameSession gSession = (GameSession) context.getBean("gameSession");
			System.out.println("see this name?" + request.getParameter("userName"));
			Player host = new Player(request.getParameter("userName"));
			host.setIsHost(true);
			host.setRoomCode(gSession.getCode());
			
			
			userSesh = new UserSessions(curSession.hashCode(), gSession.getCode());
			try {
			   tx = session.beginTransaction();
			   // do some work
			   session.save(gSession);
			   session.save(host);
			   session.save(userSesh);
			   tx.commit();
			}
			catch (Exception e) {
			   if (tx!=null) tx.rollback();
			   e.printStackTrace(); 
			}finally {
			   session.close();
			}
			request.getSession().setAttribute("gameId", gSession.getId());
			request.getSession().setAttribute("gameCode", gSession.getCode());
			request.getSession().setAttribute("gameSession", gSession);
			request.getSession().setAttribute("userId", host.getId());
			request.getSession().setAttribute("user", host);
			request.getSession().setAttribute("isHost", "true");


			//RefreshServlet.addSession(request.getSession(), gSession.getCode(), "false");
			break;
		
		case "joinServer":
			String code = request.getParameter("code");
			String playerName = request.getParameter("userName");
			Player player = new Player(playerName);
			player.setRoomCode(code);
			
			userSesh = new UserSessions(curSession.hashCode(), code);
			
			session.save(player);
			session.save(userSesh);
			
			//check if game session exists in table
			try {
			   tx = session.beginTransaction();
			   // do some work
			   String hql = "FROM GameSession gs WHERE gs.code = '" + code + "'";
			   Query query = session.createQuery(hql);
			   List results = query.list();
			   tx.commit();
			   
			   if(results.size() >0){
				   //success
				   response.setHeader("codeResponse", "success");
				   request.getSession().setAttribute("gameCode", code);
				   request.getSession().setAttribute("gameSession", (GameSession) results.get(0));
				   request.getSession().setAttribute("userId", player.getId());
				   request.getSession().setAttribute("user", player);
			   }
			}
			catch (Exception e) {
			   if (tx!=null) tx.rollback();
			   e.printStackTrace(); 
			}finally {
			   //session.close();
			}
			
			//set all refresh of userSessions to true
			try{
				tx = session.beginTransaction();
				
				String hql = "FROM UserSessions us where us.roomCode = '" + code +"'";
				Query query = session.createQuery(hql);
				List<UserSessions> results = query.list();
				for(int i = 0; i< results.size(); i++){
					results.get(i).setRefresh(true);
					//results.get(i)
					System.out.println("Chaning the item " + results.get(i).getHashCode() + ", and the state is " + results.get(i).getRefresh());
					session.saveOrUpdate(results.get(i));
				}
				tx.commit();
				
			}catch(Exception e){
				   if (tx!=null) tx.rollback();
				   e.printStackTrace(); 
			}finally {
				   session.close();
			}
				
			
		default:
			break;
		
		
		}


	}

}
