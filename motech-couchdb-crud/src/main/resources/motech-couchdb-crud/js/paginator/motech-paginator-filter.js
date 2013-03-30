var app = angular.module('whp', []);

function FilterCtrl($scope, $http, $rootScope, $location) {

    $scope.initializeFilterForm = function () {

        $scope.resetFormFields();

        var paramMap = $location.search();
        if (paramMap[$scope.pagination_id + "-searchCriteria"]) {
            $rootScope.searchCriteria = JSON.parse(paramMap[$scope.pagination_id + "-searchCriteria"])
        } else {
            $rootScope.searchCriteria = {}
        }

        if (paramMap[$scope.pagination_id + "-sortCriteria"]) {
            $rootScope.sortCriteria = JSON.parse(paramMap[$scope.pagination_id + "-sortCriteria"])

            resetSortIconsOnAllColumns();
            $.each($rootScope.sortCriteria, function(sortField, sortOrder) {
                if (sortOrder == "DESC") {
                    $("#sortIcon_" + sortField).removeClass("icon-minus");
                    $("#sortIcon_" + sortField).removeClass('icon-arrow-up');
                    $("#sortIcon_" + sortField).addClass('icon-arrow-down');
                } else {
                    $("#sortIcon_" + sortField).removeClass("icon-minus");
                    $("#sortIcon_" + sortField).removeClass('icon-arrow-down');
                    $("#sortIcon_" + sortField).addClass('icon-arrow-up');
                }
            });
        } else {
            $rootScope.sortCriteria = {}
        }



        /*Initialize the search box*/
        for (key in $rootScope.searchCriteria) {
            var element = $('#' + key);
            if (element) {
                element.val($rootScope.searchCriteria[key]);
                if(element.data("id")){ //if data-id (alternate element) is present, populate value into that.
                                        //use data-id attribute in cases where the element is rendered by UI widgets
                                        //like combobox that create another input element
                    $('#' + element.data("id")).val($rootScope.searchCriteria[key]);
                }
            }
        }

        $rootScope.$broadcast("filterUpdated");
    }

    $scope.resetFormFields = function () {
        var formElement = $('#' + $scope.filter_id);
        $(formElement).find("input").each(function(index, element){
                $(element).val("");
        });
        $(formElement).find("select").each(function(index, element){
            $(element).val("");
        });
    }

    function resetSortIconsOnAllColumns() {
        $("[id^=sortIcon]").each(function () {
            $(this).removeClass("icon-arrow-up");
            $(this).removeClass("icon-arrow-down");
            $(this).addClass("icon-minus");
        });
    }

    $scope.clearFormFieldsAndSearchCriteria = function() {
        $scope.resetFormFields();
        $rootScope.searchCriteria = {};
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

            for (key in $rootScope.searchCriteria) {
                var element = $('#' + key);
                if(element.data("id")){ //if data-id (alternate element) is present, take value from that.
                    $rootScope.searchCriteria[key] = $('#' + element.data("id")).val();
                }
            }

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

