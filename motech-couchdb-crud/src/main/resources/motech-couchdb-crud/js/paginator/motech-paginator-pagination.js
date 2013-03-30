var app = angular.module('whp', []);

function PaginationCtrl($scope, $http, $rootScope, $location) {

    $scope.loadPage = function () {
        $http.get($scope.buildURL($rootScope.searchCriteria, $rootScope.sortCriteria)).success(function (data) {
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
        if ($rootScope.sortCriteria)
            urlPart += $scope.id + "-sortCriteria=" + JSON.stringify($rootScope.sortCriteria);

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

    $scope.buildURL = function (searchCriteria, sortCriteria) {
        var url = $scope.contextRoot + '/page/' + $scope.entity +
            '?pageNo=' + $scope.currentPage +
            '&rowsPerPage=' + $scope.rowsPerPage;
        if (searchCriteria) {
            url += '&searchCriteria=' + JSON.stringify(searchCriteria);
        }
        if (sortCriteria) {
            url += '&sortCriteria=' + JSON.stringify(sortCriteria);
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

    $scope.hasResults = function () {
        if ($scope.data)
            return $scope.data.totalRows > 0;
        return false;
    }

    function resetSortIconsOnAllColumns() {
        $("[id^=sortIcon]").each(function () {
            $(this).removeClass("icon-arrow-up");
            $(this).removeClass("icon-arrow-down");
            $(this).addClass("icon-minus");
        });
    }

    function initializeSortOrderIfNotPresent(sortField, defaultSortOrder) {
        if (!$rootScope.sortCriteria[sortField]) {
            $rootScope.sortCriteria[sortField] = defaultSortOrder;
        }
    }

    function toggleSortOrder(sortField) {
        if ($rootScope.sortCriteria[sortField] == "DESC") {
            $rootScope.sortCriteria[sortField] = "ASC";
            $("#sortIcon_" + sortField).removeClass("icon-minus");
            $("#sortIcon_" + sortField).addClass('icon-arrow-up');
        } else {
            $rootScope.sortCriteria[sortField] = "DESC";
            $("#sortIcon_" + sortField).removeClass("icon-minus");
            $("#sortIcon_" + sortField).addClass('icon-arrow-down');
        }
    }

    function setCurrentSortCriteria(sortField) {
        var sortDirection = $rootScope.sortCriteria[sortField];
        $rootScope.sortCriteria = {};
        $rootScope.sortCriteria[sortField] = sortDirection;
    }

    $scope.sort = function (sortField, defaultSortOrder) {
        if (typeof(defaultSortOrder) === 'undefined') defaultSortOrder = "ASC";

        resetSortIconsOnAllColumns();

        if (!$rootScope.sortCriteria) {
            $rootScope.sortCriteria = {};
        }

        initializeSortOrderIfNotPresent(sortField, defaultSortOrder);
        toggleSortOrder(sortField);
        setCurrentSortCriteria(sortField);

        $scope.currentPage = 1;
        //$scope.loadPage();
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


    $rootScope.$on('filterUpdated', function (evt) {
        var searchString = JSON.stringify($rootScope.searchCriteria);
        var sortString = JSON.stringify($rootScope.sortCriteria);

        var searchParams = {}
        searchParams[$scope.id + "-searchCriteria"] = searchString;
        searchParams[$scope.id + "-sortCriteria"] = sortString;

        $location.search(searchParams);
        $scope.currentPage = 1;
        $scope.loadPage();
    });

    function setInitParams() {
        $scope.currentPage = 1;

        var paramMap = $location.search();
        if (paramMap[$scope.pagination_id + "-sortCriteria"]) {
            $rootScope.sortCriteria = JSON.parse(paramMap[$scope.pagination_id + "-sortCriteria"])

            $.each($rootScope.sortCriteria, function (sortField, sortOrder) {
                if (sortOrder == 'ASC')
                    $("#sortIcon_" + sortField).removeClass('icon-arrow-down').addClass('icon-arrow-up');
                else
                    $("#sortIcon_" + sortField).removeClass('icon-arrow-up').addClass('icon-arrow-down');

            });

        }

        var paramMap = $location.search();
        if (paramMap[$scope.id + "-pageNo"])
            $scope.currentPage = paramMap[$scope.id + "-pageNo"]

    }

    this.loadPage = function () {
        $scope.loadPage();
    }

    setInitParams();
    $scope.loadPage();
}