var app=angular.module('myApp', []);

function PaginationCtrl($scope, $http) {
    $scope.currentPage = 0;
    $scope.pageSize = 5;

    $scope.loadPage = function (){
        $http.get($scope.contextRoot + '/page/' + $scope.entity + '?pageNo=' + $scope.currentPage).success(function(data) {
            $scope.data = data;
            $scope.numberOfPages=function(){
                return Math.ceil($scope.data.results.length/$scope.pageSize);
            }
        });
    }

    $scope.loadPage();

    $scope.prevPage = function(){
        $scope.currentPage--;
        $scope.loadPage();
    }

    $scope.nextPage = function(){
        $scope.currentPage++;
        $scope.loadPage();
    }
}

//We already have a limitTo filter built-in to angular,
//let's make a startFrom filter
app.filter('startFrom', function() {
    return function(input, start) {
        start = +start; //parse to int
        return input.slice(start);
    }
});

