<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

    <%@ page import="org.hibernate.SessionFactory" %>
    <%@ page import="org.hibernate.cfg.Configuration" %>
    <%@ page import="org.hibernate.Transaction" %>
    <%@ page import="org.hibernate.Session" %>
    <%@ page import="org.hibernate.Query" %>
    <%@ page import="com.lie_party.*" %>
    <%@ page import="java.util.List" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%	
	String roomCode = (String) session.getAttribute("gameCode");		
	SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
	Session hSession = sessionFactory.openSession();
   	Transaction tx = null;
   	List playersList = null;
   	try{
   		tx = hSession.beginTransaction();
	   	String hql = "FROM Player p WHERE p.roomCode = '" + roomCode + "'";
   		Query query = hSession.createQuery(hql);
	   	List results = query.list();
	   	tx.commit();
	   	playersList = results;
   	
   	}catch(Exception e){
   		e.printStackTrace();
	}finally{
		//hSession.close();
	}
	//System.out.println("The roomCode is " + roomCode + " and the size is " + playersList.size());    	
 %>
<nav class="navbar navbar-default">
  <div class="container-fluid">
    <!-- Brand and toggle get grouped for better mobile display -->
    <div class="navbar-header">
      <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
        <span class="sr-only">Toggle navigation</span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </button>
      <a class="navbar-brand" href="#">Lie Party</a>
    </div>

    <!-- Collect the nav links, forms, and other content for toggling -->
    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
      <ul class="nav navbar-nav">
        <li class="dropdown players-dropdown">
          <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">Players <span class="caret"></span></a>
          <ul class="dropdown-menu">
          	<%
          	int topScore = 0;
          	String topPlayer = "";
          	if(playersList == null){
          	}else{
          	
          		for(int i = 0; i<playersList.size(); i++){
          			Player player = ((Player) playersList.get(i));
          			if(player.getScore() > topScore){
          				topScore = player.getScore();
          				topPlayer = player.getName();
          			}
          			%><li class="players-dropdown-item"><span class="player-name-nav"> <%= player.getName() %> </span> 
          				<span class="pull-right score-nav"><%= player.getScore() %></span>
          			</a></li><%
          			
          		}
          	} 
          	GameSession gSession = null;
          	int round = 0;
          	try{
          		tx = hSession.beginTransaction();
          		String hql2 = "FROM GameSession gs WHERE gs.code = '" + roomCode +"'";
          		Query query2 = hSession.createQuery(hql2);
          		tx.commit();
          		List<GameSession> results2 = query2.list();
          		if(results2.size()!= 0){
          			round = results2.get(0).getRound();
          		}
          	
          	}catch(Exception e){
          	
          	}finally{
          		hSession.close();
          	}
          	
          	
          	%>

          </ul>
        </li>
      </ul>
      <div class="header-status inline">
        <ul class="list-inline">
          <li>Round: <%=round %></li>
          <li>|</li>
          <li>Top Player: <%=topPlayer %> </li>
          <li></li>
        </ul>
      </div>
      <ul class="nav navbar-nav navbar-right">
        <!-- <li><a href="#">Link</a></li> -->
        <li class="dropdown">
          <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">Options <span class="caret"></span></a>
          <ul class="dropdown-menu">
            <li><a href="#">Action</a></li>
            <li><a href="#">Another action</a></li>
            <li><a href="#">Skip Question</a></li>
            <li role="separator" class="divider"></li>
            <li><a href="#">End Game</a></li>
          </ul>
        </li>
      </ul>
    </div><!-- /.navbar-collapse -->
  </div><!-- /.container-fluid -->
</nav>
<!-- 
<link rel="stylesheet" type="text/css" href="css/bootstrap.css" media="screen"  charset="utf-8">
<link rel="stylesheet" type="text/css" href="css/lie_party_styles.css" media="screen"  charset="utf-8">
<script type="text/javascript" src="js/jquery.js"></script>
<script type="text/javascript" src="js/bootstrap.min.js"></script> -->
