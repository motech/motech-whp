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

function addResetPasswordLink(userName) {
    $('tr[providerId=' + userName + '] td[type=reset-password]').html(
        "<a type='reset-password-link' data-toggle='modal' href='#resetPasswordModal'>Reset Password</a>");
    removeActivateColumnIfAllAreActive();
}

function setProviderAsActive(userName) {
    $('tr[providerId=' + userName + '] td[type=activate-provider] a').remove();
    $('tr[providerId=' + userName + '] td[type=status]').text('Active');
    $('tr[providerId=' + userName + ']').effect("highlight", {}, 6000);
}

$(function () {
    $("#district").combobox();
    initializeCollapsiblePane('#search-section', '#search-section-header-link', "Show Search Pane", "Hide Search Pane");

    removeActivateColumnIfAllAreActive();
    removeResetPasswordColumnIfAllAreInActive();

    $('td').on('click', 'a[type=activate-link]', function () {
        var providerId = $(this).closest('tr').attr('providerId');
        $('#activateProviderUserNameLabel').text(providerId);
    });
    $('td').on('click', 'a[type=reset-password-link]', function () {
        var providerId = $(this).closest('tr').attr('providerId');
        $('#resetPasswordModal .user-name').text(providerId);
    });

    $('#activateProviderModal').bind('activateProviderSuccess', function (event, userName) {
        setProviderAsActive(userName);
        addResetPasswordLink(userName);
    });
    $('#resetPasswordModal').bind('resetPasswordSuccess', function (event, userName) {
        $('tr[providerId=' + userName + ']').effect("highlight", {}, 6000);
        removeResetPasswordColumnIfAllAreInActive();
    });
});

