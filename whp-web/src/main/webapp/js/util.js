function createAutoClosingAlert(selector, delay) {
    var alert = $(selector).alert();
    window.setTimeout(
        function() {
            $(selector).fadeTo(500, 0).slideUp(500, function(){
                $(this).remove();
            });
        },
        delay
    );
}