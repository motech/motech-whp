$(function() {
    $( "#district" ).combobox();
    $( "#providerId" ).combobox();
    $( "#district" ).bind( "autocompleteselected", function(event, ui) {
        $("#providerId-autocomplete").val("");
        initProviders();
    });
    $("#searchButton").click(function () {
            var districtId = $("#district-autocomplete").val();
            var providerId = $("#providerId-autocomplete").val() ? $("#providerId-autocomplete").val() : "";
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