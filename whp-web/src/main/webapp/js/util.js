$(function() {
    $('form[submitOnEnterKey=true] input').keypress(function (e) {
        if ((e.which && e.which == 13) || (e.keyCode && e.keyCode == 13)) {
            $('form[submitOnEnterKey=true]').submit();
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
        function() {
            $(selector).fadeTo(500, 0).slideUp(500, function(){
                $(this).remove();
            });
        },
        delay
    );
}

