var ENTER_KEY = 13;

$(document).ready(function(){
  if(page_id ==="index"){
    GameSessionWidget.homepageInit();
  }else if(page_id =="gamehub"){
    GlobalWidget.refreshListener(1000, page_id);
    GameSessionWidget.gamehubInit();
  }else if(page_id =="question_page"){
    GameSessionWidget.questionPageInit();
    GlobalWidget.refreshListener(1000, page_id);
    GlobalWidget.progressBarInit("answer-page.jsp", 30000, 1000);
  }else if(page_id =="answer_page"){
    GameSessionWidget.answerPageInit();
  }else if(page_id =="round_results"){
    GamePresentationWidget.resultsInit();
  }else if(page_id =="game_results"){
    GameSessionWidget.gameResultsInit();
  }

  DebugHelper.init();

});

var GameSessionWidget = {
  homepageInit: function(){
    $("#make-room-confirm").on("click", function(){
      var playerName = $("#userNameMake").val().trim();
      $.ajax({
        type: 'POST',
        url: 'HomePageServlet',
        data: {"type":"makeServer", 'userName': playerName},
        success: function(data, textStatus, request){
            window.location.replace("gamehub.jsp?code=" +request.getResponseHeader('gameSessionCode'));
       },
      }).done(function(data){
        console.log(data);
      });
    });
    $("#join-room-confirm").on("click", function(){
      var code = $(this).closest(".row").find("#roomCode").val().trim();
      var playerName = $("#userNameJoin").val().trim();
      if( code !==""){
        //find if room exists
        $.ajax({
          type: 'POST',
          url: 'HomePageServlet',
          data: {"type":"joinServer", "code": code, 'userName': playerName},
          success: function(data, textStatus, request){
              if(request.getResponseHeader('CodeResponse')=== 'success'){
                window.location.replace("gamehub.jsp?code=" +code);
              }
         },
        }).done(function(data){
          //console.log(data);
        });
      }
    });

    $("#userNameMake, #userNameJoin, #roomCode").on("keydown", function(e){
      if(e.keyCode == ENTER_KEY){
        $(this).closest(".row").find(".submit").click();
      }
    });

  },
  questionPageInit: function(){//question page event handlers
    $("#submit-answer-btn").on("click", function(e){
      e.preventDefault();
      if(($("#submit-answer-input")).val() !== ""){
        $.ajax({
          type: "POST",
          url: "QuestionServlet",
          data:{type: "questionSubmit", answer: $("#submit-answer-input").val()},
          success: function(){
            $("#question-form").hide();
            $(".question-row").hide();
            $(".content-header").html("Answer Submitted");
            console.log("it went through");
            //initiate stanby

          }
        });
      }
    });

  },
  answerPageInit: function(){//question and answer pages
    $(".answer-section .item").on("click", function(e){//user picks the answer
      e.preventDefault();
      var answer = $(this).html();
      $.ajax({
        type: 'POST',
        url: 'QuestionServlet',
        data: {'type': 'answerPicked', 'answer': answer },
        success: function(){
          $(".answer-section").hide();
          $("#feedback-section").removeClass("hidden");
          GlobalWidget.refreshListener(1000, page_id);
        }
      });
      console.log("clicked!");
    });
    $(".answer-section .like-btn").on("click", function(e){//user picks likek
      e.preventDefault();
      var answer = $(this).siblings(".item").html();
      $(this).addClass("hide");

      $.ajax({
        type: 'POST',
        url: 'QuestionServlet',
        data: {'type': 'likePick', 'answer': answer },
        success: function(){

        }
      });
      console.log("clicked!");
    });
  },
  gamehubInit: function(){
    $("#start-game").on("click", function(){
      //window.location.href="question-page.jsp?round=1";
      $.ajax({
        type: "POST",
        url: "RoundServlet",
        data: {"page": "gamehub"},
        success: function(data, textStatus, request){
          window.location.href = request.getResponseHeader("redirectAddress");
        }
      });
    });
  },
  gameResultsInit : function(){
    $("#newGame").on("click", function(){
      window.location.href = "index.jsp";
    });
  }
};

var GlobalWidget = {
  refreshListener: function(refreshRate, pageid){
    setInterval(function(){
      $.ajax({
        type:'POST',
        url: 'RefreshServlet',
        data: {'page': pageid},
        success: function(data, textStatus, request){
          if(request.getResponseHeader('redirect') === 'true'){
            if(page_id == "gamehub"){
              window.location.replace('question-page.jsp');
            }else if(page_id == "question_page"){
              window.location.replace('answer-page.jsp');
            }
          }else if(request.getResponseHeader('refresh') === 'true'){
            if(page_id == "answer_page"){
              window.location.replace('round-results.jsp');
            }else if(page_id == "gamehub"){
              window.location.replace('gamehub.jsp');

            }

            console.log("running gamehub stuff");
          }
        }
      });
    }, refreshRate);
  },
  progressBarInit: function(redirectPage, totalTime, update){
    var progress = 0;

    setInterval(function(){
      if(progress >= totalTime){
        $.ajax({
          type: "POST",
          url: "RoundServlet",
          data: {"page": "gamehub"},
          success: function(data, textStatus, request){
            window.location.href = request.getResponseHeader('redirectAddress');
          }
        });
      }else{
        //progress
        var percent = 100*progress/totalTime;
        $(".progress-bar").css("width", percent + "%");
        $(".progress-bar").html("" + Math.round((progress/1000)) + " Seconds");
        progress= progress + update;
      }
    }, update);
  }
};

var EventHandlers = {
  init: function(){


  }
};

var GamePresentationWidget = {
  resultsInit: function(){
    var containers = $(".answer-item-container");
    $(containers[0]).css("transform", "scale(2.0)");
    containers.addClass("blur");
    $(containers[0]).removeClass("blur");
    setTimeout(function(){

      GamePresentationWidget.animateItemContainer(containers, 0, 0);
    }, 1000 );
  },

  animateItemContainer: function(containers, index, innerIndex){
    var references = $(containers[index]).find(".voter-name, .answer-author");
    if(innerIndex >= references.length){//case that it has animated all of refs
      if(index +1 >= containers.length){//case that it has animated all
        $(containers[index]).css("transform", "scale(1.0)");
        references.collapse("toggle");
        containers.removeClass("blur");
        //animation done, redirect now
        setTimeout(function(){
          //window.location.href="question-page.jsp"; //put an ajax call here so new question is recieved, and set standby to 0
          $.ajax({
            type: "POST",
            url: "RoundServlet",
            data: {"page": page_id},
            success: function(data, textStatus, request){
              window.location.href = request.getResponseHeader('redirectAddress');
            }
          });
        }, 4000);
        return true;//function is finished animating
      }else{
        $(containers[index]).css("transform", "scale(1.0)");//reset the old one
        references.collapse("toggle");//collapse the old ones
        $(containers[index+1]).css("transform", "scale(2.0)");
        containers.addClass("blur");
        $(containers[index+1]).removeClass("blur");
        setTimeout(function(){
          GamePresentationWidget.animateItemContainer(containers, index+1, 0);
        }, 1000);
      }
    }
    else {
      //animate ref
      $(references[innerIndex]).collapse("toggle");
      setTimeout(function(){
          GamePresentationWidget.animateItemContainer(containers, index, innerIndex+1);
      }, 1000);
      return;
    }


  }
};

var DebugHelper = {
  init: function(){
    var url = window.location.href;
    if(url.indexOf("answer=") >0){
      var argument = url.substring(url.indexOf("answer=") +"answer=".length, url.length);
      if(argument !==""){
        $("#submit-answer-input").val(argument.replace("_", " "));
        $("#submit-answer-btn").trigger("click");
      }
    }

  }
};
