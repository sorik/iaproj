
var doDelete = function() {

	$("#delete-form").submit();

};

$(document).ready(function(){

    $( "#dialog-confirm" ).dialog({
        autoOpen: false,
        resizable: false,
        height:140,
        modal: true,
        buttons: {
            "Delete": function() {
                $( this ).dialog( "close" );
                doDelete();
            },
            Cancel: function() {
                $( this ).dialog( "close" );
            }
        }
    });
    
    
	$( "#delete-btn-dialog" ).button().click(function() {
		    $( "#dialog-confirm" ).dialog( "open" );
    });
	  

});
