function removeActivateColumnIfAllAreActive() {
    if ($('a[type=activate-link]').length == 0) {
        $('[type=activate-provider]').remove();
    }
}

function removeResetPasswordColumnIfAllAreInActive() {
    if ($('a[type=reset-password-link]').length == 0) {
        $('[type=reset-password]').remove();
    }
}

$(function () {
    $("#district").combobox();
    initializeCollapsiblePane('#search-section', '#search-section-header-link', "Show Search Pane", "Hide Search Pane");

    removeActivateColumnIfAllAreActive();
    removeResetPasswordColumnIfAllAreInActive();

    $('a[type=activate-link]').click(function () {
        var providerId = $(this).closest('tr').attr('providerId');
        $('#activateProviderUserNameLabel').text(providerId);
    });
    $('a[type=reset-password-link]').click(function () {
        var providerId = $(this).closest('tr').attr('providerId');
        $('#resetPasswordModal .user-name').text(providerId);
    });

    $('#activateProviderModal').bind('activateProviderSuccess', function (event, userName) {
        $('tr[providerId=' + userName + '] td[type=activate-provider] a').remove();
        $('tr[providerId=' + userName + '] td[type=status]').text('Active');
        $('tr[providerId=' + userName + ']').effect("highlight", {}, 6000);
        removeActivateColumnIfAllAreActive();
    });
    $('#resetPasswordModal').bind('resetPasswordSuccess', function (event, userName) {
        $('tr[providerId=' + userName + ']').effect("highlight", {}, 6000);
        removeResetPasswordColumnIfAllAreInActive();
    });
});
