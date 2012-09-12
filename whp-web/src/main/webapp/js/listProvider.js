function addResetPasswordLink(userName) {
    $('tr[providerId=' + userName + '] td[type=action-on-provider]').html(
        "<a type='reset-password-link' data-toggle='modal' href='#resetPasswordModal'>Reset Password</a>");
}

function highlightProviderRow(userName) {
    $('tr[providerId=' + userName + '] td:visible').effect("highlight", {}, 6000);
}
function setProviderAsActive(userName) {
    $('tr[providerId=' + userName + '] td[type=status]').text('Active');
}

$(function () {
    $('#provider_pagination').bind('pageLoadSuccess', function () {
        if ($('#providerList tbody tr').length == 1) {
            var noResultsMessage = "No Providers found for District '" + $('[name=selectedDistrict]').val() + "'";

            if ($('[name=selectedProvider]').val() != '') {
                noResultsMessage += " with provider ID: '" + $('[name=selectedProvider]').val() + "'";
            }
            $('[type=no-results] td').html(noResultsMessage);
            $('[type=no-results]').show();
        }
        else {
            $('[type=no-results]').hide();
        }
    });

    $("#district").combobox();
    initializeCollapsiblePane('#search-section', '#search-section-header-link', "Show Search Pane", "Hide Search Pane");

    $('#providerList').on('click', 'a[type=activate-link]', function () {
        var providerId = $(this).closest('tr').attr('providerId');
        $('#activateProviderUserNameLabel').text(providerId);
    });
    $('#providerList').on('click', 'a[type=reset-password-link]', function () {
        var providerId = $(this).closest('tr').attr('providerId');
        $('#resetPasswordModal .user-name').text(providerId);
    });

    $('#activateProviderModal').bind('activateProviderSuccess', function (event, userName) {
        setProviderAsActive(userName);
        addResetPasswordLink(userName);
        highlightProviderRow(userName);
    });
    $('#resetPasswordModal').bind('resetPasswordSuccess', function (event, userName) {
        highlightProviderRow(userName);
    });
});

