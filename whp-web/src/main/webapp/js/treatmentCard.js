$(function () {
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
            $($(this).find('div')[0]).html('&#10003;');
        }
        else {
            $(this).removeClass('dash-icon');
            $(this).addClass('tick-icon');
            $(this).attr('currentPillStatus', '1');
            $($(this).find('div')[0]).html('&#10003;');
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


});

