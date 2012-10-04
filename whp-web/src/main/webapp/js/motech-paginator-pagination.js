var app = angular.module('paginator', []);

function PaginationCtrl($scope, $http, $rootScope, $location) {

    $scope.loadPage = function () {
        $http.get($scope.buildURL($rootScope.searchCriteria)).success(function (data) {
            $scope.data = data;
            $scope.numberOfPages = function () {
                return Math.ceil($scope.data.totalRows / $scope.rowsPerPage);
            }

            $scope.firstRowCount = ($scope.currentPage - 1) * ($scope.rowsPerPage) + 1;

            $scope.lastRowCount = $scope.currentPage * ($scope.rowsPerPage);
            if ($scope.lastRowCount > $scope.data.totalRows) {
                $scope.lastRowCount = $scope.data.totalRows;
            }

            setPaginationLinkUrls();
        });
    }
    function setPaginationLinkUrls() {
        var urlPart = $location.path() + "#?";
        if ($rootScope.searchCriteria)
            urlPart += $scope.id + "-searchCriteria=" + JSON.stringify($rootScope.searchCriteria);

        urlPart += "&" + $scope.id + "-rowsPerPage=" + $scope.rowsPerPage + "&" + $scope.id + "-pageNo=";
        var currentPage = Number($scope.currentPage);

        $('#' + $scope.id + " [link-type=firstPage]").attr('href', urlPart + "1");
        $('#' + $scope.id + " [link-type=prevPage]").attr('href', urlPart + (currentPage - 1));
        $('#' + $scope.id + " [link-type=nextPage]").attr('href', urlPart + (1 + currentPage));
        $('#' + $scope.id + " [link-type=lastPage]").attr('href', urlPart + $scope.numberOfPages());
    }


    $scope.prevPage = function () {
        $scope.currentPage--;
        $scope.loadPage();
    }

    $scope.buildURL = function (searchCriteria) {
        var url = $scope.contextRoot + '/page/' + $scope.entity +
            '?pageNo=' + $scope.currentPage +
            '&rowsPerPage=' + $scope.rowsPerPage;
        if (searchCriteria) {
            url += '&searchCriteria=' + JSON.stringify(searchCriteria);
        }
        return url;
    }

    $scope.nextPage = function () {
        $scope.currentPage++;
        $scope.loadPage();
    }

    $scope.firstPage = function () {
        $scope.currentPage = 1;
        $scope.loadPage();
    }

    $scope.lastPage = function () {
        $scope.currentPage = $scope.numberOfPages();
        $scope.loadPage();
    }

    $.fn.serializeObject = function () {
        var o = {};
        var a = this.serializeArray();
        $.each(a, function () {
            if (o[this.name] !== undefined) {
                if (!o[this.name].push) {
                    o[this.name] = [o[this.name]];
                }
                o[this.name].push(this.value || '');
            } else {
                o[this.name] = this.value || '';
            }
        });
        return o;
    };

    $("#" + $scope.id + ' .current-page').keypress(function (e) {
        var keyCode = e.which;

        //is entered key a number or not printable character
        var isSpecialCharacterPressed = keyCode == 8 || e.ctrlKey || e.metaKey || e.altKey;
        if (keyCode < 127 && (keyCode > 47 && keyCode < 58 || isSpecialCharacterPressed || keyCode == 0)) {
            return true;
        }

        if (keyCode == 13) {
            if ($(this).val() > 0 && $scope.numberOfPages() >= $(this).val()) {
                $scope.currentPage = $(this).val();
                $scope.loadPage();
            }
            else {
                $("#" + $scope.id + ' .current-page').val('1');
                $scope.currentPage = 1;
                $scope.loadPage();
            }
            return true;
        }
        return false;
    });


    function setSearchCriteria() {
        if ($scope.filterSectionId) {
            $rootScope.searchCriteria = $("#" + $scope.filterSectionId).serializeObject();
        }
        else
            $rootScope.searchCriteria = null;
    }

    $rootScope.$on('filterUpdated', function (evt, searchCriteria) {
        setSearchCriteria();
        var searchString = JSON.stringify($rootScope.searchCriteria);
        var searchParams = {}
        searchParams[$scope.id + "-searchCriteria"] = searchString;
        $location.search(searchParams);
        $scope.currentPage = 1;
        $scope.loadPage();
    });

    function setInitParams() {
        $scope.currentPage = 1;

        var paramMap = $location.search();
        if(paramMap[$scope.id + "-pageNo"])
            $scope.currentPage = paramMap[$scope.id + "-pageNo"]

    }

    setInitParams();
    //setSearchCriteria();
    $scope.loadPage();
}