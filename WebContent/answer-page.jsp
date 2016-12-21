	<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
	<%@ page import="org.hibernate.*" %>
    <%@ page import="org.hibernate.SessionFactory" %>
    <%@ page import="org.hibernate.cfg.Configuration" %>
    <%@ page import="org.hibernate.Transaction" %>
    <%@ page import="org.hibernate.Session" %>
    <%@ page import="org.hibernate.Query" %>
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
  <link rel="stylesheet" type="text/css" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.6.3/css/font-awesome.min.css" media="screen">
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
   		GameSession gSession = (GameSession) session.getAttribute("gameSession");
   		String roomCode = gSession.getCode();
   		Question quest = Question.readByteArray(gSession.getCurrentQuestion());
   		quest.getQuestion();
   		//System.out.println("what the fuck is going on?");


   		List answerList = (List) session.getAttribute("answerList");

     %>
    <div class="row question-row">
     <div class="col-xs-12">
 		<%=quest.getQuestion() %>

      </div>
    </div>

  	<div class="answer-section">
    	<%
    		if(answerList == null){
    			//System.out.println("Inside the if statement " + (answerList == null) + " Even though it is:" + answerList);
				SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
				Session hSession = sessionFactory.openSession();
		    	Transaction tx = null;

		    	try{
					tx = hSession.beginTransaction();
				   // do some work
				   String hql = "FROM Player pl WHERE pl.roomCode = '" + roomCode + "'";
				   Query query = hSession.createQuery(hql);
				   List results = query.list();

				   //tx.commit();
					String[] suggestions = quest.getSuggestions();
					int limit = Math.min(suggestions.length/2, GameApp.ANSWER_NUM_OPTIONS);
					

				   answerList = Question.insertRandomlyListP(Arrays.copyOfRange(quest.getSuggestions(),0,limit+1), results);

	    			for(int i = 0; i<answerList.size(); i++){
			    		%>
			    		<div class="answer-item-container">
				    		<a href="#" class="item"><%=answerList.get(i)%></a>
				    		<a href="#" class="like-btn"><i class="fa fa-thumbs-up" aria-hidden="true"></i><span class="sr-only">Like button</span></a>
			    		</div>
			    	<%
			    	}
		    	}catch(Exception e){
		    		e.printStackTrace();
		    	}finally{
		    		hSession.close();
		    	}
    		}else{
	    		for(int i = 0; i<answerList.size(); i++){
			    		%>
			    		<div class="answer-item-container">
				    		<a href="#" class="item"><%=answerList.get(i)%></a>
				    		<a href="#" class="like-btn"><i class="fa fa-thumbs-up" aria-hidden="true"></i><span class="sr-only">Like button</span></a>
			    		</div>
			    	<%
			    	}
    		}

	    	%>


		</div>
		<div id="feedback-section" class="row hidden">
			<div class="col-xs-12">
				Answer Entered!
			</div>
		</div>

  </div>

  <jsp:include page="global-footer.jsp" flush="true"/>
</body>
<script type="text/javascript">
	page_id="answer_page";
</script>
<script type="text/javascript" src="js/jquery.js"></script>
<script type="text/javascript" src="js/bootstrap.min.js"></script>
<script type="text/javascript" src="js/lie_party.js"></script>
</html>
