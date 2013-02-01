function submitOnEnter(key) {
    if ((key.which && key.which == 13) || (key.keyCode && key.keyCode == 13)) {
        $('#searchForm').submit();
        return false;
    } else {
        return true;
    }
}


function submitFormOnEnterKey() {
    $('#providerDistrict').bind('keypress', function (event, e) {
        submitOnEnter(e);
    });
    $('#providerDistrict').bind('keypress', function (event, e) {
        submitOnEnter(e);
    });
}

function resetFieldsOnInvalidValue() {
    $("#providerDistrict").bind("invalid-value", function () {
        $("#providerDistrict-autocomplete").val("");
    });
}

function initSearchPane() {
    $("#providerDistrict").combobox();
    submitFormOnEnterKey();
    resetFieldsOnInvalidValue();
}

$(function () {
    initSearchPane();
    initializeCollapsiblePane('#search-section', '#search-section-header-link', "Show Search Pane", "Hide Search Pane");
//
//    $('#searchButton').click(function () {
//        $("#searchForm").submit();
//    });
});