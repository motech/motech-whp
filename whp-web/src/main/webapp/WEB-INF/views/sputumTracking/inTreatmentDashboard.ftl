<#import "/spring.ftl" as spring />
<#import "../layout/default-with-menu.ftl" as layout>
<#import "../paginator.ftl" as paginator>
<@layout.defaultLayout title="MoTeCH-WHP" entity="cmfadmin">
    <#if !pageNo??>
        <#assign pageNo=1/>
    </#if>
    <#if errors??>
    <div id="container-tracking-error" class="container-tracking-message-alert row alert alert-error fade in">
        <button class="close" data-dismiss="alert">&times;</button>
        <#list errors as error>
            <div><@spring.messageArgs code=error.code args=error.parameters/></div>
        </#list>
    </div>
    </#if>
    <#include "inTreatmentReasonForClosure.ftl"/>
<div>
    <@paginator.filter id = "sputum_tracking_filter" pagination_id = "sputum_tracking_pagination">
       <h1>In Treatment Sputum Tracking Dashboard</h1>
        <div class="well" id="search-section">
            <h3 class="search-section-header"><a id="search-section-header-link" href="#">Hide Search Pane</a></h3>
            <div id="search-pane">
                <fieldset class="filters">
                    <div class="row-fluid sel-provider">

                        <div class="control-group span2">
                            <label class="control-label">Provider District</label>
                            <div class="controls">
                                <select id="district" name="district">
                                    <option value=""></option>
                                    <#list districts as district>
                                        <option value="${district.name}" ng-selected="{{isSelected('${district.name}', searchCriteria.district, 'district')}}">${district.name}</option>
                                    </#list>
                                </select>
                            </div>
                        </div>
                        <div class="control-group span2">
                            <label class="control-label">Provider ID</label>
                            <div class="controls">
                                <input type="text" id="providerId" name="providerId" value="{{searchCriteria.providerId}}">
                            </div>
                        </div>
                        <div class="control-group span3 ">
                            <label class="control-label ">Instance</label>
                            <div class="controls">
                                <select id="mappingInstance" name="mappingInstance">
                                    <option value=""></option>
                                    <#list instances as instance>
                                        <option value="${instance.searchString}" ng-selected="{{isSelected('${instance}', searchCriteria.mappingInstance, 'mappingInstance')}}">${instance.displayText}</option>
                                    </#list>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="row-fluid sel-result">

                        <div class="control-group  span2 ">
                            <label class="control-label ">Cumulative Lab Result</label>
                            <div class="controls ">
                                <select id="cumulativeResult" name="cumulativeResult">
                                    <option value=""></option>
                                    <#list labResults as labResult>
                                        <option value="${labResult}" ng-selected="{{isSelected('${labResult}', searchCriteria.cumulativeResult, 'cumulativeResult')}}">${labResult}</option>
                                    </#list>
                                </select>
                            </div>
                        </div>

                        <div class="control-group  span2 ">
                            <label class="control-label ">Container Status</label>
                            <div class="controls ">
                                <select id="containerStatus" name="containerStatus">
                                    <option value=""></option>
                                    <#list containerStatusList as containerStatus>
                                        <option value="${containerStatus}" ng-selected="{{isSelected('${containerStatus}', searchCriteria.containerStatus, 'containerStatus')}}">${containerStatus}</option>
                                    </#list>
                                </select>
                            </div>
                        </div>

                        <div class="control-group  span3">
                            <label class="control-label ">Reason for Closure</label>
                            <div class="controls ">
                                <select id="reasonForClosure" name="reasonForClosure">
                                    <option value=""></option>
                                    <#list reasonsForFilter as reason>
                                        <option value="${reason.code}" ng-selected="{{isSelected('${reason.code}', searchCriteria.reasonForClosure, 'reasonForClosure')}}">${reason.name}</option>
                                    </#list>
                                </select>
                            </div>
                        </div>


                        <div class="control-group span4 ">
                            <label class="control-label  date-field">Container Issue Date</label>
                            <div class="controls">
                                <div class="input-append input-prepend">
                                    <span class="add-on show-date-button"><i class ="icon-calendar"></i></span>
                                    <input class = "dates" type="text" placeholder="From: dd/mm/yyyy"  data-date-format="dd/mm/yyyy" id="containerIssuedDateFrom" name="containerIssuedDateFrom"  ng-model = "searchCriteria.containerIssuedDateFrom">
                                    <span class="add-on clear-date-button"><i class ="icon-remove-sign"></i></span>
                                </div>
                                <div class="input-append input-prepend">
                                    <span class="add-on show-date-button"><i class ="icon-calendar"></i></span>
                                    <input class = "dates" type="text" placeholder="To: dd/mm/yyyy"  data-date-format="dd/mm/yyyy" id="containerIssuedDateTo" name="containerIssuedDateTo" ng-model = "searchCriteria.containerIssuedDateTo">
                                    <span class="add-on clear-date-button"><i class ="icon-remove-sign"></i></span>
                                </div>
                            </div>
                        </div>




                    </div>
                </fieldset>
                <div class="control-group buttons-group row-fluid">
                    <div class="controls pull-right">

                        <button id="clearFilter"  ng-click="resetFormFields()" type="reset" class="btn">Clear All</button>
                        <button type="submit" id="search" class="btn btn-primary">Search <i class="icon-search icon-white"></i></button>
                    </div>
                </div>

            </div>
        </div>
    </@paginator.filter>
</div>



<div class="results">
    <@paginator.paginate id="sputum_tracking_pagination" filterSectionId="sputum_tracking_filter" entity="in_treatment_container_tracking_dashboard_row" rowsPerPage="20" contextRoot="/whp" stylePath="/resources-${applicationVersion}/styles">
        <table class="table table-striped table-bordered table-condensed" id="sputumTrackingDashboardRowsList" endpoint="<@spring.url '/sputum-tracking/in-treatment'/>">
            <thead>
            <tr>
                <th>Container Id</th>
                <th>Container Issued On  <a href="#" ng-click = "sort('containerIssuedDate')"><i id = "sortIcon_containerIssuedDate" class="icon-arrow-up"></i></a></th>
                <th>Lab Name</th>
                <th>Result 1</th>
                <th>Date Of Test 1</th>
                <th>Result 2</th>
                <th>Date Of Test 2</th>
                <th>Instance</th>
                <th>Patient ID</th>
                <th>District</th>
                <th>Provider Id</th>
                <th>Status</th>
                <th>Action</th>
                <th>Reason for Closure</th>
            </tr>
            </thead>
            <tbody>
            <tr class="sputum-tracking-dashboard-row" ng-repeat="item in data.results"
                id="dashboardRows_{{item.containerId}}"
                containerId="{{item.containerId}}">
                <td>{{item.containerId}}</td>
                <td>{{item.containerIssuedOn}}</td>
                <td>{{item.labName}}</td>
                <td>{{item.consultationOneResult}}</td>
                <td>{{item.consultationOneDate}}</td>
                <td>{{item.consultationTwoResult}}</td>
                <td>{{item.consultationTwoDate}}</td>
                <td>{{item.mappingInstance}}</td>
                <td><a href="<@spring.url '/patients/show?patientId={{item.patientId}}'/>" target="_new">{{item.patientId}}</a></td>
                <td>{{item.district}}</td>
                <td>{{item.providerId}}</td>
                <td>{{item.containerStatus}}</td>
                <td class="{{item.action}}">
                    <div class="closeContainer">
                        <a class="closeContainer" data-toggle="modal" href="#setReason">Close</a>
                    </div>
                    <div class="openContainer">
                        <a class="openContainerLink" href="#">Open</a>
                    </div>
                </td>
                <td>
                    <span class="ellipsis" rel="tooltip" title="{{item.reasonForClosure}}">{{item.reasonForClosure}} </span>
                </td>
            </tr>
            <tr type="no-results" class="hide">
                <td class="warning text-center" colspan="15"></td>
            </tr>
            </tbody>
        </table>
    </@paginator.paginate>
    <@paginator.paginationScripts jsPath="/resources-${applicationVersion}/js" loadJquery="false"/>
    <script type="text/javascript" src="<@spring.url '/resources-${applicationVersion}/js/listSputumTrackingDashboardRows.js'/>"></script>
    <script type="text/javascript" src="<@spring.url '/resources-${applicationVersion}/js/util.js'/>"></script>
    <script type="text/javascript" src="<@spring.url '/resources-${applicationVersion}/js/sputumTrackingDashboard.js'/>"></script>
</@layout.defaultLayout>
