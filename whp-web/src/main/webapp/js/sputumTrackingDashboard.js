$(function () {
    initializeCollapsiblePane('#search-section', '#search-section-header-link', "Show Search Pane", "Hide Search Pane");

    $('#containerIssuedDateFrom').datepicker({dateFormat:'dd/mm/yy'});
    $('#containerIssuedDateTo').datepicker({dateFormat:'dd/mm/yy'});
    $("#district").combobox();

    $("#district").bind("autocomplete-selected", function (event, ui) {
        $("#district").val($(this).val());
        $("#providerId-autocomplete").val("");
        initProvidersList();
    });

    $("#district").bind("invalid-value", function (event, ui) {
        resetProvidersList();
    });

    $(".show-date-button").click(function(){
        $(this).parent().find(".dates").focus();
    });

    $(".clear-date-button").click(function () {
        $(this).parents(".input-append").children("input").val("");
    });

    $("#providerId").bind("autocomplete-changed", function (event, ui, data) {
        $("#providerId").val($(data.item).val());
    });

    function resetProvidersList() {
        $("#providerId-autocomplete").val("");
        $("#providerId").html("");
        initProvidersList();
    }

    function initProvidersList() {
        if ($("#providerId").data('combobox')) {
            $("#providerId").val("");
            $("#providerId").data('combobox').destroy();
        }
        $.get("/whp/providers/byDistrict/" + $("#district").val(), function (response) {
            $("#providerId").html(response);
        });
        $("#providerId").combobox();
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

    initProvidersList();
});
