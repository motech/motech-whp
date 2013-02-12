function addLink(value, openInNewTab) {
    $(value).addClass('no-padding');
    var shouldOpenNewTab = openInNewTab ? " target='_blank'" : "";
    var clsName = 'table-condensed-padding';
    if($(value).hasClass('alert-cell')) {
        clsName = 'alert-count';
    }
    $(value).html("<a class='inherit-color' redirect-link=true" + shouldOpenNewTab + " style='display:inline-block; width: 100%; height: 100%' href='" + $(value).closest('tr').attr('redirect-url') + "'>" +
        " <div class="+ clsName +">" + $(value).html() + "</div>" +
        "</a>");
}
function removeLink(value) {
    $(value).removeClass('no-padding');
    $(value).html($(value).find('a div').html());
}

$(function () {
    var isDragging = false;
    $("[ redirectOnRowClick=true] tr").each(function (index, value) {
        if($(value).attr('redirect-url') != null)
        {
            var openInNewTab = $(value).attr('open-in-new-tab');
            $(value).find('td').not('.row-click-exclude').each(function(pos, element){
                addLink(element, openInNewTab);
            });
        }
    });

    $("[ redirectOnRowClick=true] td").not(".row-click-exclude")
        .mouseover(function (event) {
            $(this).closest('tr').find('td').not(".row-click-exclude").each(function (index, value) {
                $(value).find('a[redirect-link=true] div').addClass('hover');
            });
        })
        .mouseout(function (event) {
            $(this).closest('tr').find('td').not(".row-click-exclude").each(function (index, value) {
                $(value).find('a[redirect-link=true] div').removeClass('hover');
            });
        })
});
