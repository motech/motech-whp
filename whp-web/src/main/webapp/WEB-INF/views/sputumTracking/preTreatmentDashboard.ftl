<#import "/spring.ftl" as spring />
<#import "../layout/default-with-menu.ftl" as layout>
<#import "../paginator.ftl" as paginator>
<@layout.defaultLayout title="Pre Treatment Containers" entity="cmfadmin">
    <#if !pageNo??>
        <#assign pageNo=1/>
    </#if>
    <#include "preTreatmentReasonForClosure.ftl"/>
<div>
    <@paginator.filter id = "sputum_tracking_filter"  pagination_id = "sputum_tracking_pagination">
      <h1>Pre Treatment Sputum Tracking Dashboard</h1>
      <div class="well" id="search-section">
           <h3 class="search-section-header"><a id="search-section-header-link" href="#">Hide Search Pane</a></h3>
           <div id="search-pane">
              <fieldset class="span12">
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
                             <input type = "text" id="providerId" name="providerId" ng-model = "searchCriteria.providerId">
                       </div>
                   </div>
                  <div class="control-group span2 ">
                      <label class="control-label ">Diagnosis</label>
                      <div class="controls">
                          <select id="diagnosis" name="diagnosis">
                              <option value=""></option>
                              <#list diagnosisList as diagnosis>
                                  <option value="${diagnosis}" ng-selected="{{isSelected('${diagnosis}', searchCriteria.diagnosis, 'diagnosis')}}">${diagnosis}</option>
                              </#list>
                          </select>
                      </div>
                  </div>

                  <div class="control-group  span4 ">
                      <label class="control-group   date-field">Consultation Date</label>
                      <div class="controls">
                          <div class="input-append input-prepend">
                              <span class="add-on show-date-button"><i class ="icon-calendar"></i></span>
                              <input class = "dates" type="text" placeholder="From: dd/mm/yyyy" id="consultationDateFrom" name="consultationDateFrom" ng-model = "searchCriteria.consultationDateFrom">
                              <span class="add-on clear-date-button"><i class ="icon-remove-sign"></i></span>

                          </div>
                          <div class="input-append input-prepend">
                              <span class="add-on show-date-button"><i class ="icon-calendar"></i></span>
                              <input class = "dates" type="text" placeholder="To: dd/mm/yyyy" id="consultationDateTo" name="consultationDateTo"  ng-model = "searchCriteria.consultationDateTo">
                              <span class="add-on clear-date-button"><i class ="icon-remove-sign"></i></span>

                          </div>
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

                   <div class="control-group  span2">
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


                   <div class="control-group buttons-group span2 pull-down">
                       <div class="controls">
                           <button type="submit" id="search" class="btn btn-primary">Search <i class="icon-search icon-white"></i></button>
                           <button id="clearFilter" type="reset" class="btn ">Clear All</button>
                       </div>
                   </div>

                   </div>
               </fieldset>

           </div>
      </div>
    </@paginator.filter>
</div>

<div class="results">
    <@paginator.paginate id="sputum_tracking_pagination" filterSectionId="sputum_tracking_filter" entity="pre_treatment_container_tracking_dashboard_row" rowsPerPage="20" contextRoot="/whp" stylePath="/resources-${applicationVersion}/styles">
        <table class="table table-striped table-bordered" id="sputumTrackingDashboardRowsList" endpoint="<@spring.url '/sputum-tracking/pre-treatment'/>"/>
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
                <td class="containerId">{{item.containerId}}</td>
                <td>{{item.containerIssuedOn}}</td>
                <td>{{item.labName}}</td>
                <td>{{item.consultationOneResult}}</td>
                <td>{{item.consultationOneDate}}</td>
                <td>{{item.consultationTwoResult}}</td>
                <td>{{item.consultationTwoDate}}</td>
                <td>{{item.consultation}}</td>
                <td id="diagnosisValue">{{item.diagnosis}}</td>
                <td><a href="<@spring.url '/patients/show?patientId={{item.patientId}}' />"  target="_new">{{item.patientId}}</a></td>
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
</div>
    <@paginator.paginationScripts jsPath="/resources-${applicationVersion}/js" loadJquery="false"/>
    <script type="text/javascript" src="<@spring.url '/resources-${applicationVersion}/js/listSputumTrackingDashboardRows.js'/>"></script>
    <script type="text/javascript" src="<@spring.url '/resources-${applicationVersion}/js/util.js'/>"></script>
    <script type="text/javascript" src="<@spring.url '/resources-${applicationVersion}/js/sputumTrackingDashboard.js'/>"></script>
    <script type="text/javascript">
    $(function () {
        $('#consultationDateFrom').datepicker({dateFormat:'dd/mm/yy'});
        $('#consultationDateTo').datepicker({dateFormat:'dd/mm/yy'});
    });
    </script>
</@layout.defaultLayout>
