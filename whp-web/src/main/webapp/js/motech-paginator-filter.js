var app = angular.module('whp', []);

function FilterCtrl($scope, $http, $rootScope, $location) {

    $scope.initializeFilterForm = function () {
        var paramMap = $location.search();

        if (paramMap[$scope.pagination_id + "-searchCriteria"]) {
            $rootScope.searchCriteria = JSON.parse(paramMap[$scope.pagination_id + "-searchCriteria"])
        }

        /*Initialize the search box*/
        for (id in $rootScope.searchCriteria) {
            var element = $('#' + id);
            if (element) {
                element.val($rootScope.searchCriteria[id]);
            }
        }

        $rootScope.$broadcast("filterUpdated");
    }


    $(window).bind('hashchange', function () {
        $scope.initializeFilterForm();
    });


    $scope.applyFilter = function () {
        updateSearchCriteria();
        $rootScope.$broadcast("filterUpdated");
    }

    function updateSearchCriteria() {
        if ($scope.filterSectionId) {
            $rootScope.searchCriteria = $("#" + $scope.filterSectionId).serializeObject();
        }
        else {
            $rootScope.searchCriteria = null;
        }
    }

    $scope.isSelected = function (value, selected, id) {
        if ($rootScope.searchCriteria && $rootScope.searchCriteria[id] != null) {
            if ($rootScope.searchCriteria[id] == value)
                $('#' + id).val(value);
            return;
        }

        if (value == selected) {
            $('#' + id).val(selected);
        }
    }


    $scope.selected = function (value, id) {
        $('#' + id).val(value);
    }

    $scope.initializeFilterForm();
}

