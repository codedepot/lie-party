<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

    <%@ page import="org.hibernate.SessionFactory" %>
    <%@ page import="org.hibernate.cfg.Configuration" %>
    <%@ page import="org.hibernate.Transaction" %>
    <%@ page import="org.hibernate.Session" %>
    <%@ page import="org.hibernate.Query" %>
    <%@ page import="com.lie_party.*" %>
    <%@ page import="java.util.List" %>
    <%@ page import="java.util.*" %>
    <%@ page import="com.lie_party.*" %>
    <%@ page import="com.lie_party.controllers.*" %>

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
        <h1 class="text-center homepage-header">Lie Party</h1>
      </div>
    </div>
    <h2 class="text-center color-white">Round Results:</h2>
    <div class="row">
      <div class="hidden-xs col-sm-2"> &#160;</div>
      <div id="results-container" class="col-xs-12 col-sm-8">
      <%
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		Session hSession = sessionFactory.openSession();
		GameSession gSession = (GameSession) session.getAttribute("gameSession");
		String code = gSession.getCode();
		TreeSet<Player> ts = null;


		//make server call


		try{

			Transaction tx = null;
			tx = hSession.beginTransaction();
			String hql = "FROM Player p WHERE p.roomCode = '" + code + "'";
			Query query = hSession.createQuery(hql);
			List<Player> results= query.list();
			tx.commit();

			ts = new TreeSet<Player>(new Comparator<Player>(){
				@Override
				public int compare(Player p1, Player p2){
					if(p1.getScore() - p2.getScore() <0){
						return -1;
					}else if(p1.getScore() - p2.getScore() >0){
						return 1;
					}else{
						return 0;
					}
					
					
				}
			});
			ts.addAll(results);

		}catch(Exception e){
			e.printStackTrace();
		}finally{
			hSession.close();
		}
		
		%>
        <table id="game-results">
          <thead>
            <tr>
              <th>Name</th>
              <th>Score</th>
              <th>Likes</th>
            </tr>
          </thead>
          <tbody>
          <%
          	Iterator<Player> iter = ts.descendingIterator();
          	while(iter.hasNext()){
          		Player temp = iter.next();
          		%>
		            <tr>
		              <td class="resultsPlayerName"><span><%=temp.getName() %></span></td>
		              <td><span><%=temp.getScore() %></span></td>
		              <td><span><%=temp.getLikes() %></span></td>
		            </tr>          			
          		<%
          	}
           %>
          </tbody>
        </table>

      </div>
    </div>
    <div class="row mar-top-30 mar-bottom-30">
      <div class="col-sm-3 hidden-xs">&#160;</div>
      <div class="col-xs-12 col-sm-3">
         <a id="newGame" href="#" class="btn btn-yellow fill-width" >New Game</a>
      </div>
      <div class="col-xs-12 col-sm-3">
         <a id="endGame" href="#" class="btn btn-yellow fill-width" >End Game</a>
      </div>
    </div>

  </div>

<jsp:include page="global-footer.jsp" flush="true"/>
</body>
<script type="text/javascript">
  var page_id = "game_results";
</script>
<script type="text/javascript" src="js/jquery.js"></script>
<script type="text/javascript" src="js/bootstrap.min.js"></script>
<script type="text/javascript" src="js/lie_party.js"></script>
</html>
