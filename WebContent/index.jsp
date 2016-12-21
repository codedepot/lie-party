<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
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
        <h1 class="text-center homepage-header">Welcome to Lie Party!</h1>
      </div>
    </div>

    <div class="row homepage-buttons">
      <div class="col-xs-12 col-sm-6 text-center">
        <button type="button" name="make-room" class="btn btn-yellow"  data-toggle="collapse" data-target="#homepage-make-room" >Make Room</button>
      </div>
      <div class="col-xs-12 col-sm-6 text-center">
        <button type="button" name="join-room" class="btn btn-yellow " data-toggle="collapse" data-target="#homepage-join-room">Join Room</button>

      </div>

    </div>
    <div id="homepage-make-room" class="row collapse">
      <div class="col-xs-12 col-sm-4">
        <input type="text" id="userNameMake" placeholder="user name">
      </div>
      <div class="col-xs-12 col-sm-2">
        <button id="make-room-confirm" type="button" class="btn btn-yellow submit" name="make-room-confirm">Make room</button>
      </div>

    </div>
    <div id="homepage-join-room" class="row collapse">
      <div class="col-xs-12 col-sm-4">
        <input type="text" id="userNameJoin" placeholder="user name">
      </div>
      <div class="col-xs-12 col-sm-4">
        <input type="text" id="roomCode" placeholder="room code">
      </div>
      <div class="col-xs-12 col-sm-2">
        <button id="join-room-confirm" type="button" class="btn btn-yellow submit" name="join-room-confirm">Join</button>
      </div>

    </div>
  </div>


</body>
<script type="text/javascript">
  var page_id = "index";
</script>
<script type="text/javascript" src="js/jquery.js"></script>
<script type="text/javascript" src="js/bootstrap.min.js"></script>
<script type="text/javascript" src="js/lie_party.js"></script>

</html>
