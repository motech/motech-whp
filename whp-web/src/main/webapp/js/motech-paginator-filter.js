var app = angular.module('whp', []);

app.directive('updateFilter', function($rootScope) {
    return function(scope,elm,attrs) {
        $rootScope.$on("hashChanged", function() {
            $(elm).find("input").each(function(index, element){
                $(element).val("");
            })
            $(elm).find("select").each(function(index, element){
                $(element).val("");
            })
        });
    };
});

function FilterCtrl($scope, $http, $rootScope, $location) {

    $scope.initializeFilterForm = function () {
        var paramMap = $location.search();

        if (paramMap[$scope.pagination_id + "-searchCriteria"]) {
            $rootScope.searchCriteria = JSON.parse(paramMap[$scope.pagination_id + "-searchCriteria"])
        }

        /*Initialize the search box*/
        for (key in $rootScope.searchCriteria) {
            var element = $('#' + key);
            if (element) {
                element.val($rootScope.searchCriteria[key]);
            }
        }

        $rootScope.$broadcast("filterUpdated");
    }


    $(window).bind('hashchange', function () {
        $scope.initializeFilterForm();
        $rootScope.$broadcast("hashChanged");
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

