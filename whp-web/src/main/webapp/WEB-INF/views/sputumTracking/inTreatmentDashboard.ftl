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
       <h3>In Treatment Sputum Tracking Dashboard</h3>
       <div class="row-fluid well" id="search-section">
            <h3 class="search-section-header"><a id="search-section-header-link" href="#">Hide Search Pane</a></h3>
            <div id="search-pane">
               <fieldset class="inline-align span3">
                   <legend><small>Provider</small></legend>
                    <div class="control-group">
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
                    <div class="control-group">
                        <label class="control-label">Provider ID</label>
                        <div class="controls">
                              <input type = "text" id="providerId" name="providerId" ng-model = "searchCriteria.providerId">
                        </div>
                    </div>
                </fieldset>
                <fieldset class="inline-align span9">
                    <legend><small>Container</small></legend>
                    <div class="control-group inline-align">
                        <label class="control-label span4">Instance</label>
                        <div class="controls inline-align">
                            <select id="mappingInstance" name="mappingInstance">
                                <option value=""></option>
                                <#list instances as instance>
                                    <option value="${instance}" ng-selected="{{isSelected('${instance}', searchCriteria.mappingInstance, 'mappingInstance')}}">${instance.displayText}</option>
                                </#list>
                            </select>
                        </div>
                    </div>
                    <div class="control-group inline-align">
                        <label class="control-label span4">Cumulative Lab Result</label>
                        <div class="controls inline-align">
                            <select id="cumulativeResult" name="cumulativeResult">
                                <option value=""></option>
                                <#list labResults as labResult>
                                    <option value="${labResult}" ng-selected="{{isSelected('${labResult}', searchCriteria.cumulativeResult, 'cumulativeResult')}}">${labResult}</option>
                                </#list>
                            </select>
                        </div>
                    </div>
                    <div class="control-group inline-align">
                        <label class="control-label span4">Container Status</label>
                        <div class="controls inline-align">
                            <select id="containerStatus" name="containerStatus">
                                <option value=""></option>
                                <#list containerStatusList as containerStatus>
                                    <option value="${containerStatus}" ng-selected="{{isSelected('${containerStatus}', searchCriteria.containerStatus, 'containerStatus')}}">${containerStatus}</option>
                                </#list>
                            </select>
                        </div>
                    </div>
                    <div class="control-group inline-align">
                        <label class="control-label span4">Reason for Closure</label>
                        <div class="controls inline-align">
                            <select id="reasonForClosure" name="reasonForClosure">
                                <option value=""></option>
                                <#list reasons as reason>
                                    <option value="${reason.code}" ng-selected="{{isSelected('${reason.code}', searchCriteria.reasonForClosure, 'reasonForClosure')}}">${reason.name}</option>
                                </#list>
                            </select>
                        </div>
                    </div>
                    <div class="control-group inline-align span7 date-field">
                        <label class="control-label span4">Container Issue Date</label>
                        <div class="controls">
                            <div class="input-append inline">
                                <input class = "dates" type="text" data-date-format="dd/mm/yyyy" id="containerIssuedDateFrom" name="containerIssuedDateFrom"  ng-model = "searchCriteria.containerIssuedDateFrom">
                                <button class="btn btn-danger clear-date-button" type="button">x</button>
                            </div>
                            <div class="input-append inline">
                                <input class = "dates" type="text" data-date-format="dd/mm/yyyy" id="containerIssuedDateTo" name="containerIssuedDateTo" ng-model = "searchCriteria.containerIssuedDateTo">
                                <button class="btn btn-danger clear-date-button" type="button">x</button>
                            </div>
                        </div>
                    </div>
                </fieldset>
                <div id="container-filter-buttons" class="span12 pull-left pull-down padding-left">
                    <div class="controls">
                        <button type="submit" id="search" class="btn btn-primary form-button-center">Search</button>
                        <button id="clearFilter" type="reset" class="btn btn-primary form-button-center">Clear</button>
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
                <th>Container Issued On</th>
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
                <td><a href="<@spring.url '/patients/show?patientId={{item.patientId}}'  target="_new"/>">{{item.patientId}}</a></td>
                <td>{{item.district}}</td>
                <td>{{item.providerId}}</td>
                <td>{{item.containerStatus}}</td>
                <td class="{{item.containerStatus}}">
                    <div class="closeContainer">
                        <a class="closeContainer" data-toggle="modal" href="#setReason">Close</a>
                    </div>
                    <div class="openContainer">
                        <a class="openContainerLink" href="#">Open</a>
                    </div>
                </td>
                <td>{{item.reasonForClosure}}</td>
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
