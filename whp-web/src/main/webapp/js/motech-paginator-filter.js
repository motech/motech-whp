var app = angular.module('paginator', []);

function FilterCtrl($scope, $http, $rootScope, $location) {
    var paramMap = $location.search();
    if(paramMap[$scope.pagination_id + "-searchCriteria"]) {
        $scope.searchCriteria =  JSON.parse(paramMap[$scope.pagination_id + "-searchCriteria"])
        $rootScope.searchCriteria =  JSON.parse(paramMap[$scope.pagination_id + "-searchCriteria"])
    }
    $scope.applyFilter = function () {
        $rootScope.$broadcast("filterUpdated");
    }

    $scope.isSelected = function(value, selected, id){
        if(value == selected){
            $('#'+ id).val(selected);
        }
    }

    $scope.selected = function(value, id){
        $('#' + id).val(value);
    }
}