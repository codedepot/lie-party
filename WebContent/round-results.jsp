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
		

		//make server call
		
		//get attributes from hibernate
		Hashtable<String, ArrayList<String>> answerReviewSug = (Hashtable<String, ArrayList<String>>) session.getAttribute("answerReviewSug");
		Hashtable<String, ArrayList<String>> answerReview = (Hashtable<String, ArrayList<String>>) session.getAttribute("answerReview");
		try{

			Transaction tx = null;
			tx = hSession.beginTransaction();
			String hql = "FROM GameSession gs WHERE code = '" + code + "'";
			Query query = hSession.createQuery(hql);
			List<GameSession> results= query.list();
			tx.commit();
			
			if(results.size()!=0){
				gSession = results.get(0);
				answerReview = GameSession.readByteArray(gSession.getAnswerReview());
				answerReviewSug = GameSession.readByteArray(gSession.getAnswerReviewSug());
			}
		}catch(Exception e){
		}finally{
			hSession.close();
		}
		
		Enumeration<String> answerReviewSugKeys = answerReviewSug.keys();
       	Enumeration<String> answerReviewKeys = answerReview.keys();
		
		while(answerReviewSugKeys.hasMoreElements()){		
			String sugKey = answerReviewSugKeys.nextElement();
			ArrayList<String> alSug = answerReviewSug.get(sugKey);
		%>  
      	<div class="answer-results">
         <div class="answer-item-container">
          <div class="item">
		<%=sugKey%>
          </div>
          <% for(int i = 0; i< alSug.size(); i++){
          		%><div class="voter-name" style="height:0px"><span> <%=alSug.get(i) %></span></div><%		
          	} %>
          <div class="answer-author" style="height:0px"><span>Our Lie </span></div>
         </div>
       </div>
       <%
       }
       

		while(answerReviewKeys.hasMoreElements()){
			String key = answerReviewKeys.nextElement();
			ArrayList<String> al = answerReview.get(key);
		%>  
      	<div class="answer-results">
         <div class="answer-item-container">
          <div class="item">
		<%=key%>
          </div>
          <% for(int i = 1; i< al.size(); i++){
          		%><div class="voter-name" style="height:0px"><span> <%=al.get(i) %></span></div><%		
          	} %>
          	<div class="answer-author" style="height:0px"><span> <%=al.get(0) %></span></div>
         </div>
       </div>
       <%
		}
		%>
       

      </div>
    </div>
    <div class="row hidden">
      <div class="col-sm-4 hidden-xs">&#160;</div>
      <div class="col-xs-12 col-sm-4">
        <form action="">
          <input type="submit" class="btn btn-yellow fill-width" value="Start Game"/>
        </form>
      </div>
    </div>

  </div>

<jsp:include page="global-footer.jsp" flush="true"/>
</body>
<script type="text/javascript">
  var page_id = "round_results";
</script>
<script type="text/javascript" src="js/jquery.js"></script>
<script type="text/javascript" src="js/bootstrap.min.js"></script>
<script type="text/javascript" src="js/lie_party.js"></script>
</html>
