$(function () {
    initializeCollapsiblePane('#search-section', '#search-section-header-link', "Show Search Pane", "Hide Search Pane");

    $('#containerIssuedDateFrom').datepicker({dateFormat:'dd/mm/yy'});
    $('#containerIssuedDateTo').datepicker({dateFormat:'dd/mm/yy'});
    $("#district").combobox();
    var providerId = $("#providerId").val();
    $("#providerId").combobox();

    $("#district").bind("autocomplete-selected", function (event, ui) {
        initProvidersList();
        $("#district").val($(this).val());
        $("#providerId-autocomplete").val("");
    });

    $("#providerId").bind("autocomplete-selected", function (event, ui) {
        $("#providerId").val($("#providerId-autocomplete").val());
    });

    initProvidersList();

    function initProvidersList() {
        $.get("/whp/providers/byDistrict/" + $("#district").val(), function (response) {
            $("#providerId").html(response);
            $("#providerId-autocomplete").val(providerId);
        });
    }
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
});