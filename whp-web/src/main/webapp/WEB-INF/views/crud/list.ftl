<#import "/spring.ftl" as spring />
<#import "../layout/default-with-menu.ftl" as layout>
<#import "../paginator.ftl" as paginator>
<@layout.defaultLayout title="${entity} List" entity="cmfadmin">
<script type="text/javascript" src="<@spring.url '/resources-${applicationVersion}/js/util.js'/>"></script>
<script type="text/javascript" src="<@spring.url '/resources-${applicationVersion}/js/autoComplete.js'/>"></script>
<script type="text/javascript"
        src="<@spring.url '/resources-${applicationVersion}/js/redirectOnRowClick.js'/>"></script>

<h1>${entity} Listing</h1>

<div class="row-fluid">
    <@paginator.filter id = "filter"  pagination_id = "listing">
        <div class="well" id="search-section">
            <div id="search-pane">
                <fieldset class="filters">
                <div class="row-fluid sel-result">
                    <#list filterFields as filterField>

                        <div class="control-group span2">
                            <label class="control-label">${filterField}</label>

                            <div class="controls">
                                <input type="text" name="${filterField}" id="${filterField}"
                                       value="{{searchCriteria.${filterField}}}"/>
                            </div>
                        </div>

                    </#list>
                </div>
                </fieldset>
                <div class="control-group buttons-group row-fluid">
                    <div class="controls pull-right">
                        <button id="clearFilter" ng-click="clearFormFieldsAndSearchCriteria()" type="reset"
                                class="btn "><i class="icon-remove"></i> Clear All
                        </button>
                        <button type="submit" id="searchButton" class="btn btn-primary">
                            Search <i class="icon-search icon-white"></i>
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </@paginator.filter>
</div>
<div class="row-fluid">
    <div class="results">
        <div id="${entity}s">
            <@paginator.paginate id = "listing" entity="${entity}" filterSectionId="filter" contextRoot="/whp" rowsPerPage="20"  stylePath="/resources-${applicationVersion}/styles">
                <table id="${entity}List" class="table table-striped table-bordered table-condensed"
                       redirectOnRowClick="true">
                    <thead>
                    <tr>
                        <#list displayFields as displayField>
                        <th>${displayField}</th>
                        </#list>
                        <th>Edit</th>
                        <th>Delete</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr ng-repeat="item in data.results">
                        <#list displayFields as displayField>
                            <td id="${entity}_{{item.${displayField}}}">{{item.${displayField}}}</td>
                        </#list>
                        <td><a href="#" class = "editEntity" entityId = "{{item._id}}">Edit</a></td>
                        <td><a href="#" class = "deleteEntity" entityId = "{{item._id}}">Delete</a></td>
                    </tr>
                    <tr type="no-results" class="hide">
                        <td class="warning text-center" colspan="17"></td>
                    </tr>
                    </tbody>
                </table>
            </@paginator.paginate>
        </div>
    </div>
</div>
</div>
<@paginator.paginationScripts jsPath="/resources-${applicationVersion}/js" loadJquery="false"/>

<script type="text/javascript">
    $(document).ready(function(){
        $(".deleteEntity").click(function (event) {
            event.preventDefault();

            $.ajax({
                type:"GET",
                url:"<@spring.url '/crud/${entity}/delete/'/>" + $(elem).attr('entityId'),
                success:function (data) {
                    angular.element($('#${entity} .paginator')).controller().loadPage();
                }
            });
        });
    }
</script>
</@layout.defaultLayout>