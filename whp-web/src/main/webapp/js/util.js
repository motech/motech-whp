$(function () {
    $('form[submitOnEnterKey=true] input').keypress(function (e) {
        if ((e.which && e.which == 13) || (e.keyCode && e.keyCode == 13)) {
            $(this).closest('form').submit();
            return false;
        } else {
            return true;
        }
    });

    $.validator.addMethod(
        "regex",
        function (value, element, regexp) {
            var re = new RegExp(regexp);
            return this.optional(element) || re.test(value);
        },
        "Please check your input."
    );
});

function createAutoClosingAlert(selector, delay) {
    var alert = $(selector).alert();
    window.setTimeout(
        function () {
            $(selector).fadeTo(500, 0).slideUp(500, function () {
                $(this).remove();
            });
        },
        delay
    );
}

function initializeCollapsiblePane(collapseSectionSelector, headerLinkSelector, showText, hideText) {
    var icons = {
        header:"ui-icon-circle-arrow-e",
        headerSelected:"ui-icon-circle-arrow-s"
    };
    $(collapseSectionSelector).accordion({
        collapsible:true,
        icons:icons,
        heightStyle: "content",
        change:function (event, ui) {
            if(ui.newHeader.length === 0){
                $(headerLinkSelector).text(showText);
            }else{
                $(headerLinkSelector).text(hideText);
            }
        }
    });
}

