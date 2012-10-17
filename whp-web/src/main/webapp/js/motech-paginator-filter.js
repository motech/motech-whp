var app = angular.module('paginator', []);

function FilterCtrl($scope, $http, $rootScope, $location) {
    var paramMap = $location.search();
    if(paramMap[$scope.pagination_id + "-searchCriteria"]) {
        $rootScope.searchCriteria =  JSON.parse(paramMap[$scope.pagination_id + "-searchCriteria"])
    }

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

    $scope.isSelected = function(value, selected, id){
        if($rootScope.searchCriteria && $rootScope.searchCriteria[id] != null) {
            if($rootScope.searchCriteria[id] == value)
                $('#'+ id).val(value);
            return;
        }

        if(value == selected){
            $('#'+ id).val(selected);
        }
    }


    $scope.selected = function(value, id){
        $('#' + id).val(value);
    }
}