function hideActivateColumnIfAllAreActive() {
    $('[type=activate-provider]').show();
    if ($('a[type=activate-link]:visible').length == 0) {
        $('[type=activate-provider]').hide();
    }
}

function hideResetPasswordColumnIfAllAreInActive() {
    $('[type=reset-password]').show();
    if ($('a[type=reset-password-link]:visible').length == 0) {
        $('[type=reset-password]').hide();
    }
}


function addResetPasswordLink(userName) {
    $('tr[providerId=' + userName + '] td[type=reset-password]').html(
        "<a type='reset-password-link' data-toggle='modal' href='#resetPasswordModal'>Reset Password</a>");
    $('[type=reset-password]').show();
    hideActivateColumnIfAllAreActive();
}

function highlightProviderRow(userName) {
    $('tr[providerId=' + userName + '] td:visible').effect("highlight", {}, 6000);
}
function setProviderAsActive(userName) {
    $('tr[providerId=' + userName + '] td[type=activate-provider] a').remove();
    $('tr[providerId=' + userName + '] td[type=status]').text('Active');
}

$(function () {
    $('#provider_pagination').bind('pageLoadSuccess', function () {
        if ($('#providerList tbody tr').length == 1) {
            var noResultsMessage = "No Patients found for District '" + $('[name=selectedDistrict]').val() + "'";

            if ($('[name=providerId]').val() != '') {
                noResultsMessage += " with provider ID: '" + $('[name=providerId]').val() + "'";
            }
            $('[type=no-results] td').html(noResultsMessage);
            $('[type=no-results]').show();
        }
        else {
            $('[type=no-results]').hide();
        }
        hideActivateColumnIfAllAreActive();
        hideResetPasswordColumnIfAllAreInActive();
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
        hideResetPasswordColumnIfAllAreInActive();
    });
});

