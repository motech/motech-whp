<#macro paginate entity>

    <link rel="stylesheet" type="text/css" href="<@spring.url '/resources-${applicationVersion}/styles/pagination.css'/>"/>

    <div ng-init="entity='${entity}'">
        <div class="paginator"  ng-controller="PaginationCtrl" >
            <div class="paginator-content">
                <#nested>
            </div>
            <button ng-disabled="currentPage == 0" ng-click="prevPage()">Previous</button>
                {{currentPage+1}}/{{numberOfPages()}}
            <button ng-disabled="currentPage >= data.results.length/pageSize - 1" ng-click="nextPage()">Next</button>
        </div>
    </div>

    <script type="text/javascript" src="<@spring.url '/resources-${applicationVersion}/js/angular/angular-1.0.1.js'/>"></script>
    <script type="text/javascript" src="<@spring.url '/resources-${applicationVersion}/js/angular/pagination.js'/>"></script>
</#macro>