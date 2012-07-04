$(function() {

    $( "#district" ).combobox();
    $( "#providerId" ).combobox();
    $( "#district" ).bind( "autocomplete-selected", function(event, ui) {
        $("#providerId-autocomplete").val("");
        initProviders();
    });
    $('#district').bind('keypress',function(event,e){
        submitOnEnter(e);
    });
    $('#providerId').bind('keypress',function(event,e){
        submitOnEnter(e);
    });

    function submitOnEnter(key) {
        if ((key.which && key.which == 13) || (key.keyCode && key.keyCode == 13)) {
            $('#searchForm').submit();
            return false;
        } else {
            return true;
        }
    }
    $( "#district" ).bind( "invalid-value", function() {
           $("#district-autocomplete").val("");
           $("#providerId-autocomplete").val("");
           $("#providerId").html("");
           $("#providerId").data('combobox').destroy();
           $("#providerId").combobox();
    });
    $( "#providerId" ).bind( "invalid-value", function() {
        $("#providerId-autocomplete").val("");
    });

    $('#searchButton').click(function(){
        $("#searchForm").submit();
    });
    $("#searchForm").submit(function (event) {
            event.preventDefault();

            if($('#district').val()== "")
                return;

            var districtId = $("#district").val();
            var providerId = $("#providerId").val() ? $("#providerId-autocomplete").val() : "";
            var data = {
                "selectedDistrict":districtId,
                "selectedProvider":providerId
            };
            $.post('/whp/patients/search', data, function(response) {
                $('#patients').html(response);
            })
    });

    var initProviders = function(){
       $.get("/whp/providers/byDistrict/" + $("#district").val(), function(response){
                   $("#providerId").html(response);
       });
    }
    initProviders();
});