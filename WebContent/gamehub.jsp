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
<html>
<head>
  <meta id="viewport" name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
  <link rel="stylesheet" type="text/css" href="css/bootstrap.css" media="screen"  charset="utf-8">
  <link rel="stylesheet" type="text/css" href="css/lie_party_styles.css" media="screen"  charset="utf-8">
<title>Insert title here</title>
</head>
<body class="homepage">
<jsp:include page="global-header.jsp" flush="true"/>
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
		GameSession gSession = (GameSession) session.getAttribute("gameSession");
		String code = gSession.getCode();
		List results = null;

		//make server call
		Transaction tx = null;
 		try {
			   tx = hSession.beginTransaction();
			   // do some work
			   String hql = "FROM Player p WHERE p.roomCode = '" + code + "'";
			   Query query = hSession.createQuery(hql);
			   results = query.list();
			   //System.out.println(results.toString() + ", this is the code " + code);
			   tx.commit();
			}
			catch (Exception e) {
			   if (tx!=null) tx.rollback();
			   e.printStackTrace();
			}finally {
			   hSession.close();
			}
       %>
      <div class="row playerList">
		<%
		//building up the player names
		//System.out.println("does it come until here?");
		if(results != null){
			for(int i = 0; i< results.size(); i++){
				%>
	   		<div class="col-xs-12 col-sm-6">
	            <div class="playerName">

	            <%= ((Player) results.get(i)).getName()%>
	            </div>
	        </div>
	            <%
			}
		}
		 %>

        </div>
      </div>
    </div>
    <div class="row">
      <div class="col-sm-4 hidden-xs">&#160;</div>
      <div class="col-xs-12 col-sm-4">
        <form action="">
          <a id="start-game" type="submit" class="btn btn-yellow fill-width">Start Game</a>
        </form>
      </div>
    </div>

  </div>

<jsp:include page="global-footer.jsp" flush="true"/>
</body>
<script type="text/javascript">
  var page_id = "gamehub";
</script>
<script type="text/javascript" src="js/jquery.js"></script>
<script type="text/javascript" src="js/bootstrap.min.js"></script>
<script type="text/javascript" src="js/lie_party.js"></script>
</html>
