function submitOnEnter(key) {
    if ((key.which && key.which == 13) || (key.keyCode && key.keyCode == 13)) {
        $('#searchForm').submit();
        return false;
    } else {
        return true;
    }
}

function setSelectedProviderIfInSession() {
    if($('#providerId').attr('selectedProviderInSession') != null)  {
        $('#providerId').val($('#providerId').attr('selectedProviderInSession'));
        $("#providerId-autocomplete").val($('#providerId').attr('selectedProviderInSession'));
    }
}

function submitFormOnEnterKey() {
    $('#district').bind('keypress', function (event, e) {
        submitOnEnter(e);
    });
    $('#providerId').bind('keypress', function (event, e) {
        submitOnEnter(e);
    });
}

function resetFieldsOnInvalidValue() {
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
}

function initProvidersList() {
    $.get("/whp/providers/byDistrict/" + $("#district").val(), function (response) {
        $("#providerId").html(response);
    });
}

function initSearchPane() {
    $("#district").combobox();
    $("#providerId").combobox();
    $("#district").bind("autocomplete-selected", function (event, ui) {
        $("#providerId-autocomplete").val("");
        initProvidersList();
    });
    initProvidersList();
    setSelectedProviderIfInSession();

    submitFormOnEnterKey();
    resetFieldsOnInvalidValue();
}

$(function () {
    initializeCollapsiblePane('#search-section', '#search-section-header-link', "Show Search Pane", "Hide Search Pane");

    initSearchPane();

    $('#searchButton').click(function () {
        $("#searchForm").submit();
    });

    $("#searchForm").submit(function (event) {
        event.preventDefault();
        if ($('#district-autocomplete').val() == "" || $('#district').val() != $('#district-autocomplete').val())
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
});