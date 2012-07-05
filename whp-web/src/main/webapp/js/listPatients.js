function initializeSearchPane() {
    var icons = {
        header:"ui-icon-circle-arrow-e",
        headerSelected:"ui-icon-circle-arrow-s"
    };
    $('#search-section').accordion({
        collapsible:true,
        icons:icons,
        change:function (event, ui) {
            if (ui.options.active == 0)
                $('#search-section-header').text("Show Search Pane");
            else
                $('#search-section-header').text("Hide Search Pane");
        }
    });
    $('#search-section').accordion("option", "active", true);

}
$(function () {
    initializeSearchPane();

    $("#district").combobox();
    $("#providerId").combobox();
    $("#district").bind("autocomplete-selected", function (event, ui) {
        $("#providerId-autocomplete").val("");
        initProviders();
    });
    $('#district').bind('keypress', function (event, e) {
        submitOnEnter(e);
    });
    $('#providerId').bind('keypress', function (event, e) {
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

    $("#district").bind("invalid-value", function () {
        $("#district-autocomplete").val("");
        $("#providerId-autocomplete").val("");
        $("#providerId").html("");
        $("#providerId").data('combobox').destroy();
        $("#providerId").combobox();
    });
    $("#providerId").bind("invalid-value", function () {
        $("#providerId-autocomplete").val("");
    });

    $('#searchButton').click(function () {
        $("#searchForm").submit();
    });
    $("#searchForm").submit(function (event) {
        event.preventDefault();

        if ($('#district-autocomplete').val() == "")
            return;

        var districtId = $("#district-autocomplete").val();
        var providerId = $("#providerId-autocomplete").val();
        var data = {
            "selectedDistrict":districtId,
            "selectedProvider":providerId
        };
        $.post('/whp/patients/search', data, function (response) {
            $('#patients').html(response);
        })
    });

    var initProviders = function () {
        $.get("/whp/providers/byDistrict/" + $("#district").val(), function (response) {
            $("#providerId").html(response);
        });
    }
    initProviders();
});