	<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
	<%@ page import="org.hibernate.*" %>
    <%@ page import="org.hibernate.SessionFactory.*" %>
    <%@ page import="org.hibernate.cfg.Configuration.*" %>
    <%@ page import="org.hibernate.Transaction.*" %>
    <%@ page import="org.hibernate.Session.*" %>
    <%@ page import="org.hibernate.Query.*" %>
    <%@ page import="com.lie_party.*" %>
    <%@ page import="com.lie_party.controllers.*" %>
    <%@ page import="java.util.List" %>
    <%@ page import="java.util.Arrays" %>
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
    <h2 class="content-header">Question:</h2>
    <%
    	//init stuff
   		GameSession gSession = null;

   		//get latest version of gSession
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		Session hSession = sessionFactory.openSession();
   		String gameCode = (String) session.getAttribute("gameCode");
   		Transaction tx = null;
   		try{
	   		tx = hSession.beginTransaction();
	   		String hql = "FROM GameSession gs WHERE gs.code = '" +gameCode + "'";
	   		Query query = hSession.createQuery(hql);
	   		List<GameSession> results = query.list();
	   		if(results.size() != 0){
	   			gSession = results.get(0);
	   			session.setAttribute("gameSession", gSession);
	   		}

   		}catch(Exception e){
   			e.printStackTrace();
   		}
   		//System.out.println("print your gSession" + gSession);
    	Question quest = Question.readByteArray(gSession.getCurrentQuestion());
    	quest.getQuestion();
    	session.setAttribute("currentQuestion", quest);

    	//empty review variables
    	session.setAttribute("answerReviewSug", null);
		session.setAttribute("answerReview", null);
     %>
    <div class="row question-row">
     <div class="col-xs-12">
 		<%=quest.getQuestion() %>

      </div>
    </div>
     <form id="question-form" action="">
	    <div class="row">
	      <div class="col-sm-3 hidden-xs">&#160;</div>
	      <div class="col-xs-12 col-sm-6">

	          <input id="submit-answer-input" type="text" class="inp-flat"/>

	      </div>
	      <div class="col-xs-12 col-sm-3">
	      <a id="submit-answer-btn" type="submit" class="btn btn-yellow fill-width">Submit </a>
	      </div>
	    </div>
	    <div class="row mar-top-30"	>
	      <div class="col-sm-3 hidden-xs">&#160;</div>

	      <div class="col-sm-6 hidden-xs">
	      	<button type="button" class="btn btn-yellow-thin"  data-toggle="collapse" data-target="#lie-for-me" >Lie For Me</button>
	      </div>
	      <div class="col-xs-12 col-sm-3">&#160;</div>
	    </div>
			<div id="lie-for-me" class="suggestion-section collapse">
				<%
				String[] suggestions = quest.getSuggestions();
				int limit = Math.min(suggestions.length/2, GameApp.ANSWER_NUM_OPTIONS);
				int start = suggestions.length - limit;
				//int limit = suggestions.length/2;
				for(int i=start; i<quest.getSuggestions().length; i++){
					%><a href="#" class="item"> <%=quest.getSuggestions()[i] %></a>

				<%} %>
			</div>
     </form>
  	 <div class="progress mar-top-30">
	    <div class="progress-bar progress-bar-striped active" role="progressbar" aria-valuenow="40" aria-valuemin="0" aria-valuemax="100" style="width:0%">

	    </div>
	  </div>
		 <%-- <h2 class="submition-feedback">Answer Submited</h2> --%>



  </div>

  <jsp:include page="global-footer.jsp" flush="true"/>
</body>
<script type="text/javascript">
	page_id="question_page";
</script>
<script type="text/javascript" src="js/jquery.js"></script>
<script type="text/javascript" src="js/bootstrap.min.js"></script>
<script type="text/javascript" src="js/lie_party.js"></script>
</html>
