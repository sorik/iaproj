

var success_of_login = "main.html";
var notification_of_login_fail = "notification.html" + "?type=login&result=fail";
var login = function(email, pwd) {
	$.ajax({
	    type: "POST",
	    url: "/proj1/Login",
	    data: {"email": email, "password": pwd},
	    dataType: "json",
	    success: function(response){
	    	var result = response.result;
	    	// normal operation should be "logged-in"
	    	if ("logged-in" == result) {
	    		window.location = "main.html";
	    	} else {
	    		window.location = "notification.html" + "?type=login&result=" + result;
	    	}
	    },
	    failure: function(errMsg) {
	    	console.log(errMsg);
	    	window.location = "notification.html" + "?type=login&result=unknown";
	    }
	});
};


var register = function(email) {
	console.log("POST register");
	$.ajax({
	    type: "POST",
	    url: "/proj1/Register",
	    data: {"email": email},
	    dataType: "json",
	    success: function(response){
	    	var result = response.result;
	    	// normal operation should be "sent_register"
	        window.location = "notification.html" + "?type=register&result=" + result;
	    },
	    failure: function(errMsg) {
	    	console.log(errMsg);
	    	window.location = "notification.html" + "?type=register&result=unknown";
	    }
	});
};

$(document).ready(function(){

  $("#signin-btn").click(function(){
	  var email = $('#signin-email').val();
	  var pwd = $("#signin-password").val();
	  var validation = validateInput(email, pwd);
	  if (validation.length > 0) {
		  $('#signin-error').text(validation);
		  $("#signin-error").css("display", "block");
		  return;
	  };
	  
	  login(email, pwd);
  });

  $("#register-btn").click(function(){
	  var email = $('#register-email').val();
	  var validation = validateInput(email, null);
	  if (validation.length > 0) {
		  $('#register-error').text(validation);
		  $("#register-error").css("display", "block");
		  return;
	  };

	  
	  register(email);
  });

});
  
  