<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

    <%@ page import="org.hibernate.SessionFactory" %>
    <%@ page import="org.hibernate.cfg.Configuration" %>
    <%@ page import="org.hibernate.Transaction" %>
    <%@ page import="org.hibernate.Session" %>
    <%@ page import="org.hibernate.Query" %>
    <%@ page import="com.lie_party.*" %>
    <%@ page import="com.lie_party.controllers.*" %>
    <%@ page import="java.util.List" %>
    <%@ page import="org.springframework.context.*" %>
    <%@ page import="org.springframework.context.support.ClassPathXmlApplicationContext" %>
    <%@ page import="javax.json.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <meta id="viewport" name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
  <link rel="stylesheet" type="text/css" href="css/bootstrap.css" media="screen"  charset="utf-8">
  <link rel="stylesheet" type="text/css" href="css/lie_party_styles.css" media="screen"  charset="utf-8">
<title>Insert title here</title>
</head>
<body class="homepage">
  <div class="container ">
    <div class="row">
      <div class="col-xs-12">
        <h1 class="text-center homepage-header">Welcome to room <%= session.getAttribute("gameCode") %></h1>
      </div>
    </div>
    <h2 class="text-center color-white">Players in game:</h2>
    <div class="row">
      <div class="hidden-xs col-sm-2"> &#160;</div>
      <div class="col-xs-12 col-sm-8">
      <%
		SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
		Session hSession = sessionFactory.openSession();
	    ApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");
		GameSession gSession = (GameSession) context.getBean("gameSession");
		gSession.setCurrentQuestion(QuestionsLib.getTestQuestion().toByteArray());
		//QuestionsLib ql = new QuestionsLib();
		//System.out.println(QuestionsLib.getRandomQuestion().toString());
		HttpSession curSession = request.getSession();
		
		
		
		int num = 5;
		UserSessions[] userSesh = new UserSessions[num];
		Transaction tx = null;
		
		
	    //create a bunch of users:
	    Player[] players = new Player[num];
	    String[] names = new String[] {"Bob", "Rachael", "Newt", "Gabriel", "Stan"};
	    int[] scores  = new int[]{500, 3200, 2500, 1500, 3000};
	    
	    for(int i = 0; i< players.length; i++){
      		players[i] = new Player(names[i]);
	      	players[i].setRoomCode(gSession.getCode());
	      	userSesh[i] = new UserSessions(curSession.hashCode(), gSession.getCode());
	      	//code for answer page START
			players[i].setAnswer("dummy answer " + i);
			//code for answer page END
			//score
			players[i].setScore(scores[i]);
			
	    }
	    players[0].setIsHost(true);
//	    players[1].setStandBy(true);
//	    players[2].setStandBy(true);
		players[0].setLikes(2);
	    
	    
		//remove session variables
		session.setAttribute("answerReview", null);
		session.setAttribute("answerReviewSug", null);
		
		//empty all the tables
		try {
		   tx = hSession.beginTransaction();
		   // do some work
			String hql = "DELETE FROM Player "  + 
             "WHERE id >= 0";
			Query query = hSession.createQuery(hql);
			int result = query.executeUpdate();
			
			 hql = "DELETE FROM GameSession "  + 
             "WHERE id >= 0";
			 query = hSession.createQuery(hql);
			 result = query.executeUpdate();
			
			hql = "DELETE FROM UserSessions "  + 
             "WHERE id >= 0";
			 query = hSession.createQuery(hql);
			 result = query.executeUpdate();
			
		   hSession.save(gSession);
		   //hSession.save(userSesh);
		   tx.commit();
		}
		catch (Exception e) {
		   if (tx!=null) tx.rollback();
		   e.printStackTrace(); 
		}finally {
		   //hSession.close();
		}
		
		//populate the tables 
		try {
		   tx = hSession.beginTransaction();
		   // do some work
		   for(int i = 0; i< players.length; i++){
		   	hSession.save(players[i]);
		   	hSession.save(userSesh[i]);
		   }
		   hSession.save(gSession);
		   //hSession.save(userSesh);
		   tx.commit();
		}
		catch (Exception e) {
		   if (tx!=null) tx.rollback();
		   e.printStackTrace(); 
		}finally {
		   hSession.close();
		}
		request.getSession().setAttribute("gameId", gSession.getId());
		request.getSession().setAttribute("gameCode", gSession.getCode());
		request.getSession().setAttribute("gameSession", gSession);
		request.getSession().setAttribute("userId", players[0].getId());
		request.getSession().setAttribute("user", players[0]);
		

		
       %>
       <script>
      	 //window.location.replace("question-page.jsp?answer=cat_piss");
      	 window.location.replace("game-results.jsp");
       </script>

      </div>
    </div>
    <div class="row">
      <div class="col-sm-4 hidden-xs">&#160;</div>
      <div class="col-xs-12 col-sm-4">
        <form action="">
          <input type="submit" class="btn btn-yellow fill-width" value="Start Game"/>
        </form>
      </div>
    </div>

  </div>


</body>
<script type="text/javascript">
  var page_id = "temp_redirect";
</script>
<script type="text/javascript" src="js/jquery.js"></script>
<script type="text/javascript" src="js/bootstrap.min.js"></script>
<script type="text/javascript" src="js/lie_party.js"></script>
</html>
