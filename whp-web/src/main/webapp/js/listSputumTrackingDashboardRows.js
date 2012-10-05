$(function () {
    $('#sputum_tracking_pagination').bind('pageLoadSuccess', function () {
        if ($('#sputumTrackingDashboardRowsList tbody tr').length == 1) {
            var noResultsMessage = "No Containers found";

            $('[type=no-results] td').html(noResultsMessage);
            $('[type=no-results]').show();
        }
        else {
            $('[type=no-results]').hide();
        }
    });
});

