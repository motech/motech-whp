<#import "/spring.ftl" as spring />
<#import "../layout/default-with-menu.ftl" as layout>
<#import "../paginator.ftl" as paginator>
<@layout.defaultLayout title="${entity} List" entity="cmfadmin">
<script type="text/javascript" src="<@spring.url '/resources-${applicationVersion}/js/util.js'/>"></script>
<script type="text/javascript" src="<@spring.url '/resources-${applicationVersion}/js/autoComplete.js'/>"></script>
<script type="text/javascript" src="<@spring.url '/resources-${applicationVersion}/js/redirectOnRowClick.js'/>"></script>
<script type="text/javascript" src="<@spring.url '/resources-${applicationVersion}/js/jsonform/underscore.js'/>"></script>
<script type="text/javascript" src="<@spring.url '/resources-${applicationVersion}/js/jsonform/jsv.js'/>"></script>
<script type="text/javascript" src="<@spring.url '/resources-${applicationVersion}/js/jsonform/jsonform.js'/>"></script>

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
                        <td><a href="#" class = "editEntity" data-toggle="modal" data-target="#createOrEditEntityModal" onclick="editEntity(this)" entityId = "{{item._id}}">Edit</a></td>
                        <td><a href="#" class = "deleteEntity" onclick="deleteEntity(this)" entityId = "{{item._id}}">Delete</a></td>
                    </tr>
                    <tr type="no-results" class="hide">
                        <td class="warning text-center" colspan="17"></td>
                    </tr>
                    </tbody>
                </table>
            </@paginator.paginate>
        </div>
    </div>
    <a href= "#" data-toggle="modal" data-target="#createOrEditEntityModal" onclick="newEntity(this)">Add Entity</a>
</div>



<div id="createOrEditEntityModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="modalLabel" aria-hidden="true">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <h3 id="modalLabel">Create/Edit Entity</h3>
    </div>
    <div class="modal-body">
        <form id = "jsonForm"></form>
    </div>
    <#--<div class="modal-footer">-->
        <#--<button class="btn" data-dismiss="modal" aria-hidden="true">Close</button>-->
        <#--<button class="btn btn-primary">Save</button>-->
    <#--</div>-->
</div>

</div>

<@paginator.paginationScripts jsPath="/resources-${applicationVersion}/js" loadJquery="false"/>



<script type="text/javascript">

    function reloadPage(){
        angular.element($('#listing .paginator')).controller().loadPage();
    }

    function deleteEntity(elem){
        $.ajax({
            type:"GET",
            url:"<@spring.url '/crud/${entity}/delete/'/>" + $(elem).attr('entityId'),
            success:function (data) {
                reloadPage();
            }
        });
    }

    function editEntity(elem){
        $('#jsonForm').contents().remove();

        $.getJSON("<@spring.url '/crud/${entity}/get/'/>" +  $(elem).attr('entityId'), function (json) {
            $('#jsonForm').jsonForm({
                "schema":jsonSchema.properties,
                "form":[
                    "*",
                    {"key":"type", "type":"hidden"},
                    {"key":"_id", "type":"hidden"},
                    {"key":"_rev", "type":"hidden"},
                    {"type":"submit", "title":"Submit"}
                ],
                "onSubmitValid": submitValues,
                "value": json
            });
        });
    }

    function newEntity(elem){
        $('#jsonForm').contents().remove();
        $('#jsonForm').jsonForm({
            "schema":jsonSchema.properties,
            "form":[
                "*",
                {"key":"type", "type":"hidden"},
                {"key":"_id", "type":"hidden"},
                {"key":"_rev", "type":"hidden"},
                {"type":"submit", "title":"Submit"}
            ],
            "onSubmitValid": submitValues
        });

    }

    function submitValues(values){
        $.ajax("<@spring.url '/crud/${entity}/save/'/>", {
            data:JSON.stringify(values),
            contentType:'application/json',
            type:'POST'
        }).done(function () {
                    reloadPage();
                    $('#createOrEditEntityModal').modal('hide');
                });
    }

    var jsonSchema;
    $(document).ready(function(){

        $.getJSON("<@spring.url '/crud/${entity}/schema/'/>", function (json) {
            jsonSchema = json;
            jsonSchema.properties.type.default = "${entity}";
        });

//        $(".newEntity").click(function (event) {
//            event.preventDefault();
//

//            });


    });
</script>
</@layout.defaultLayout>