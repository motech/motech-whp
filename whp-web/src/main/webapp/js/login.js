$(document).ready(function() {

    $("#loginForm").submit(function() {

        var username = $("#j_username").val();
        $("#j_username").val(username.toLowerCase());
        return true;
    });


});
