function setUpTreatmentCardTable() {

//Tooltips to show provider information and dose status
    $('.tick-icon, .round-icon').each(function () {
        var providerId = $(this).attr('providerId');
        if (!providerId) {
            providerId = "CMF Admin";
        }
        var currentPillStatus;
        if ($(this).hasClass('tick-icon')) {
            currentPillStatus = "Taken."
        }
        else if ($(this).hasClass('round-icon')) {
            currentPillStatus = "Not Taken."
        }
        else {
            currentPillStatus = "";
        }
        $(this).tooltip({title:providerId + " gave adherence as " + currentPillStatus});
    })

//Troggle - 3 state toggling
    $('.editable').click(function () {
        if ($(this).hasClass('tick-icon')) {
            $(this).removeClass('tick-icon');
            $(this).addClass('round-icon');
            $(this).attr('currentPillStatus', '2');
            $($(this).find('div')[0]).html('O');
        }
        else if ($(this).hasClass('round-icon')) {
            $(this).removeClass('round-icon');
            $(this).addClass('tick-icon');
            $(this).attr('currentPillStatus', '1');
            $($(this).find('div')[0]).html('&#10004;');
        }
        else {
            $(this).removeClass('dash-icon');
            $(this).addClass('tick-icon');
            $(this).attr('currentPillStatus', '1');
            $($(this).find('div')[0]).html('&#10004;');
        }
        $(this).trigger('change');
    });

// setting changed attribute for cell if log is changed
    $('.editable').change(function () {
        if ($(this).attr('oldPillStatus') != $(this).attr('currentPillStatus')) {
            $(this).attr('pillStatusChanged', 'true');
        }
        else {
            $(this).attr('pillStatusChanged', 'false');
        }
    });
    colorCellsByProvider();
    createAutoClosingAlert(".dateUpdated-message-alert", 5000);

    $('#submitAdherence').click(function () {
        var dailyAdherenceRequests = [];
        $.each($('[pillStatusChanged=true]'), function () {
            dailyAdherenceRequests.push({day:$(this).attr('day'), month:$(this).attr('month'), year:$(this).attr('year'), pillStatus:$(this).attr('currentPillStatus')});
        });
        var patientId = $("#patient-id").text();
        var delta = { patientId: patientId , dailyAdherenceRequests: dailyAdherenceRequests };
        $("#delta").val(JSON.stringify(delta));
        $("#treatmentCardDeltaform").submit();
    });
}


function colorCellsByProvider() {
    var providerColorCodes = new Array("blue", "green", "orange", "brown", "purple", "rosyBrown", "olive", "salmon", "cyan", "red");
    var providerMap = {};
    var colorPos = 0;

    $('.editable').each(function () {
        if ($(this).attr('providerId') && $(this).attr('providerId') != "") {
            if (!providerMap.hasOwnProperty($(this).attr('providerId')))
                providerMap[$(this).attr('providerId')] = providerColorCodes[colorPos];
            colorPos = (colorPos + 1) % 10;
        }
    });

    for (var key in providerMap) {
        if (providerMap.hasOwnProperty(key))
            $("[providerid=" + key + "]").css('color', providerMap[key]);
    }
}

