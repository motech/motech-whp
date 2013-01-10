$(function () {
    initializeCollapsiblePane('#search-section', '#search-section-header-link', "Show Search Pane", "Hide Search Pane");

    $('#containerIssuedDateFrom').datepicker({dateFormat:'dd/mm/yy'});
    $('#containerIssuedDateTo').datepicker({dateFormat:'dd/mm/yy'});
    $("#district").combobox();

    $("#district").bind("autocomplete-selected", function (event, ui) {
        $("#district").val($(this).val());
    });

    $(".show-date-button").click(function(){
        $(this).parent().find(".dates").focus();
    });

    $(".clear-date-button").click(function () {
        $(this).parent().find(".dates").val("");
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
});
