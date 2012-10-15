<#import "/spring.ftl" as spring />
<#import "../layout/default-with-sidebar.ftl" as layout>
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

    <#include "reasonForClosure.ftl"/>

<div>
    <@paginator.filter id = "sputum_tracking_filter"  pagination_id = "sputum_tracking_pagination">
        Filter section
        <table>
            <tr>
                <td>
                    <div class="control-group">
                        <label class="control-label">Provider ID</label>

                        <div class="controls">
                            <input type="text" id="providerId" name="providerId">
                        </div>
                    </div>
                </td>
                <td>
                    <div class="control-group">
                        <label class="control-label">Container Issue Date</label>

                        <div class="controls">
                            <input type="text" data-date-format="dd/mm/yyyy" id="containerIssuedDateFrom" name="containerIssuedDateFrom">
                            <input type="text" data-date-format="dd/mm/yyyy" id="containerIssuedDateTo" name="containerIssuedDateTo">
                        </div>
                    </div>
                </td>
                <td>
                    <div class="control-group">
                        <label class="control-label">Cumulative Lab Result</label>

                        <div class="controls">
                            <select id="cumulativeResult" name="cumulativeResult">
                                <option value=""></option>
                                <#list labResults as labResult>
                                    <option value="${labResult}">${labResult}</option>
                                </#list>
                            </select>
                        </div>
                    </div>
                </td>
            </tr>
            <tr>
                <td>
                    <div class="control-group">
                        <label class="control-label">Provider District</label>

                        <div class="controls">
                            <select id="district" name="district">
                                <option value=""></option>
                                <#list districts as district>
                                    <option value="${district.name}">${district.name}</option>
                                </#list>
                            </select>
                        </div>
                    </div>
                </td>
                <td>
                    <div class="control-group">
                        <label class="control-label">Container Status</label>

                        <div class="controls">
                            <select id="containerStatus" name="containerStatus">
                                <option value=""></option>
                                <#list containerStatusList as containerStatus>
                                    <option value="${containerStatus}">${containerStatus}</option>
                                </#list>
                            </select>
                        </div>
                    </div>
                </td>
                <td>
                    <div class="control-group">
                        <label class="control-label">Diagnosis</label>

                        <div class="controls">
                            <select id="diagnosis" name="diagnosis">
                                <option value=""></option>
                                <#list diagnosisList as diagnosis>
                                    <option value="${diagnosis}">${diagnosis}</option>
                                </#list>
                            </select>
                        </div>
                    </div>
                </td>
            </tr>
            <tr>
                <td>
                    <div class="control-group">
                        <label class="control-label">Consultation Date</label>

                        <div class="controls">
                            <input type="text" id="consultationDateFrom" name="consultationDateFrom">
                            <input type="text" id="consultationDateTo" name="consultationDateTo">
                        </div>
                    </div>
                </td>
                <td>
                    <div class="control-group">
                        <label class="control-label">Reason for Closure</label>

                        <div class="controls">
                            <select id="reasonForClosure" name="reasonForClosure">
                                <option value=""></option>
                                <#list reasons as reason>
                                    <option value="${reason.code}">${reason.name}</option>
                                </#list>
                            </select>
                        </div>
                    </div>
                </td>
            </tr>
            <tr>
                <td>
                    <div class="control-group pull-down padding-left">
                        <div class="controls">
                            <button type="submit" id="search" class="btn btn-primary form-button-center">Search</button>
                        </div>
                    </div>
                </td>
            </tr>
        </table>
    </@paginator.filter>
</div>

<div class="results">
    <@paginator.paginate id="sputum_tracking_pagination" filterSectionId="sputum_tracking_filter" entity="container_tracking_dashboard_row" rowsPerPage="20" contextRoot="/whp" stylePath="/resources-${applicationVersion}/styles">
        <table class="table table-striped table-bordered" id="sputumTrackingDashboardRowsList">
            <thead>
            <tr>
                <th>Container Id</th>
                <th>Container Issued On</th>
                <th>Lab Name</th>
                <th>Result 1</th>
                <th>Date Of Test 1</th>
                <th>Result 2</th>
                <th>Date Of Test 2</th>
                <th>Consulted On</th>
                <th>Diagnosis</th>
                <th>Patient ID</th>
                <th>Provider District</th>
                <th>Provider Id</th>
                <th>Container Status</th>
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
                <td>{{item.consultation}}</td>
                <td id="diagnosis">{{item.diagnosis}}</td>
                <td><a href="<@spring.url '/patients/show?patientId={{item.patientId}}' />">{{item.patientId}}</a></td>
                <td>{{item.district}}</td>
                <td>{{item.providerId}}</td>
                <td>{{item.containerStatus}}</td>
                <td class="{{item.containerStatus}}">
                    <div class="closeContainer">
                        <a class="closeContainer" data-toggle="modal" href="#setReason">Close</a>
                    </div>
                    <div class="openContainer">
                        <a data-toggle="modal" href="<@spring.url '/sputum-tracking/open-container?containerId={{item.containerId}}' />">Open</a>
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
    <script type="text/javascript"
            src="<@spring.url '/resources-${applicationVersion}/js/listSputumTrackingDashboardRows.js'/>"></script>
    <script type="text/javascript">
        $('#containerIssuedDateFrom').datepicker({dateFormat:'dd/mm/yy'});
        $('#containerIssuedDateTo').datepicker({dateFormat:'dd/mm/yy'});
        $('#consultationDateFrom').datepicker({dateFormat:'dd/mm/yy'});
        $('#consultationDateTo').datepicker({dateFormat:'dd/mm/yy'});
    </script>
</@layout.defaultLayout>