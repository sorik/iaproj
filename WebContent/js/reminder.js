
var reminder = function(email) {
	console.log("POST reminder");
	$.ajax({
	    type: "POST",
	    url: "/proj1/Reminder",
	    data: {"email": email},
	    dataType: "json",
	    success: function(response){
	    	var result = response.result;
	    	console.log(result);
	    	// normal operation should be "sent_reminder"
	        window.location = "notification.html" + "?type=reminder&result=" + result;
	        console.log("aaa");
	    },
	    failure: function(errMsg) {
	    	console.log(errMsg);
	    	window.location = "notification.html" + "?type=reminder&result=unknown";
	    }
	});
};

$(document).ready(function(){

  $("#reminder-btn").click(function(){
	  var email = $('#reminder-email').val();

	  var validation = validateInput(email, null);
	  if (validation.length > 0) {
		  $('#reminder-error').text(validation);
		  $("#reminder-error").css("display", "block");
		  return;
	  };
	  
	  reminder(email);
  });

});
  
  