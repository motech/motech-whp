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

    $('#patient_listing').bind('pageLoadSuccess', function () {
        if ($('#patientList tbody tr').length == 1) {
            var noResultsMessage = "No Patients found.";

            $('[type=no-results] td').html(noResultsMessage);
            $('[type=no-results]').show();
        }
        else {
            $('[type=no-results]').hide();
        }

        $('#patientList tbody tr').each(function () {
            if ($(this).attr("paused-treatment") === "true") {
                $(this).addClass("paused");
            }
        });

        $('#achtung').each(function () {
            $(this).attr("title", "Patient Nearing Transition!");
        })
    });

    $('#patient_listing').bind('click', function(event){

        var target = event.target;
        if($(target).hasClass("flagImage")){
            $.ajax({
                type:"GET",
                url:$(target).attr("endpoint"),
                success:function (data) {
                    if(data === "success") {
                        var imageSrc = $(target).attr('src');
                        var endpoint = $(target).attr('endpoint');
                        var flagImageCurrentValue = $(target).attr("flagValue");
                        var flagImageNextValue = (flagImageCurrentValue == "true") ? "false" : "true";

                        $(target).attr('src', imageSrc.replace(flagImageCurrentValue + "-star", flagImageNextValue + "-star"));
                        $(target).attr('endpoint', endpoint.replace("value=" + flagImageNextValue, "value=" + flagImageCurrentValue));
                        $(target).attr("flagValue", flagImageNextValue);
                    }
                }
            });
        }
    });

    $('.numbersOnly').keyup(function () {
        if (this.value != this.value.replace(/[^0-9\.]/g, '')) {
            this.value = this.value.replace(/[^0-9\.]/g, '');
        }
    });

    initSearchPane();
    initializeCollapsiblePane('#search-section', '#search-section-header-link', "Show Search Pane", "Hide Search Pane");

});