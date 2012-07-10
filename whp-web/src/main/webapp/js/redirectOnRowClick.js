$(function () {
    var isDragging = false;
    $("[ redirectOnRowClick=true] td").not(".row-click-exclude")
        .mouseover(function(event) {
          $(this).closest('tr').addClass('link');
        })
        .mouseout(function(event) {
            $(this).closest('tr').removeClass('link');
        })
        .mousedown(function (event) {
            if (event.which != 1)
                return;
            $(window).mousemove(function () {
                isDragging = true;
                $(window).unbind("mousemove");
            });
        })
        .mouseup(function (event) {
            if (event.which != 1)
                return;
            var wasDragging = isDragging;
            isDragging = false;
            $(window).unbind("mousemove");
            if (!wasDragging) { //was clicking
                if ($(this).parent().attr('redirect-url') != null)
                    window.location = $(this).parent().attr('redirect-url');
            }
        });

});
