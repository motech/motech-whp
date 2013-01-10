function submitOnEnter(key) {
    if ((key.which && key.which == 13) || (key.keyCode && key.keyCode == 13)) {
        $('#searchForm').submit();
        return false;
    } else {
        return true;
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
    });
}

function initSearchPane() {
    $("#district").combobox();
    submitFormOnEnterKey();
    resetFieldsOnInvalidValue();
}

$(function () {
    initSearchPane();

    initializeCollapsiblePane('#search-section', '#search-section-header-link', "Show Search Pane", "Hide Search Pane");

    $('#searchButton').click(function () {
        $("#searchForm").submit();
    });

    $("#searchForm").submit(function (event) {
        event.preventDefault();
        var districtId = $("#district-autocomplete").val();
        var providerId = $("#providerId").val();
        var data = {
            "selectedDistrict":districtId,
            "selectedProvider":providerId
        };
        $.post('/whp/patients/search', data, function (response) {
            $('#patients').html(response);
        })
    });
});