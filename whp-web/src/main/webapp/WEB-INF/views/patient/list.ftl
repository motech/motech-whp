<#import "/spring.ftl" as spring />
<#import "../layout/default-with-menu.ftl" as layout>
<#include "../layout/legend.ftl">
<#import "../paginator.ftl" as paginator>
<@layout.defaultLayout title="Patient List" entity="cmfadmin">
<script type="text/javascript" src="<@spring.url '/resources-${applicationVersion}/js/util.js'/>"></script>
<script type="text/javascript" src="<@spring.url '/resources-${applicationVersion}/js/autoComplete.js'/>"></script>
<script type="text/javascript" src="<@spring.url '/resources-${applicationVersion}/js/redirectOnRowClick.js'/>"></script>

<script type="text/javascript">
    $(function () {
        $('#achtung').each(function () {
            $(this).tooltip({title: "Patient Nearing Transition!"});
        })
    });
</script>
<h1>Patients Listing</h1>

<div class="row-fluid">
    <div class="well" id="search-section">
        <h3 class="search-section-header"><a id="search-section-header-link" href="#">Hide Search Pane</a></h3>

            <@paginator.filter id = "patient_list_filter"  pagination_id = "patient_listing">
            <div id="search-pane">
                    <fieldset class="filters">

                        <div class="row-fluid sel-result">
                            <div class="control-group span2">
                                <label class="control-label">Alert Type</label>
                                <div class="controls">
                                    <select id="alertType" name="alertType">
                                        <option value=""></option>
                                        <#list alertTypes.alertTypeFilters as alertType>
                                            <option value="${alertType.name()}"
                                                    ng-selected="{{isSelected('${alertType.name()}', searchCriteria.alertType, 'alertType')}}">${alertType.displayText}</option>
                                        </#list>
                                    </select>
                                </div>
                            </div>

                            <div class="control-group span2">
                                <label class="control-label">Alert Date</label>
                                <div class="controls">
                                    <select id="alertDate" name="alertDate">
                                        <option value=""></option>
                                        <#list alertDates.alertDateFilters as alertDate>
                                            <option value="${alertDate.name()}"
                                                    ng-selected="{{isSelected('${alertDate.name()}', searchCriteria.alertDate, 'alertDate')}}">${alertDate.displayText}</option>
                                        </#list>
                                    </select>
                                </div>
                            </div>

                            <div class="control-group span2">
                                <label class="control-label">Treatment Category</label>
                                <div class="controls">
                                    <select id="treatmentCategory" name="treatmentCategory">
                                        <option value=""></option>
                                        <#list treatmentCategories as category>
                                            <option value="${category.code}"
                                                    ng-selected="{{isSelected('${category.code}', searchCriteria.treatmentCategory, 'treatmentCategory')}}">${category.name}</option>
                                        </#list>
                                    </select>
                                </div>
                            </div>

                            <div class="control-group span2">
                                <label class="control-label">Provider District</label>
                                <div class="controls">
                                    <select id="providerDistrict" name="providerDistrict" data-id="providerDistrict-autocomplete">
                                        <option value=""></option>
                                        <#list districts as district>
                                            <option value="${district.name}"
                                                    ng-selected="{{isSelected('${district.name}', searchCriteria.providerDistrict, 'providerDistrict')}}">${district.name}</option>
                                        </#list>
                                    </select>
                                </div>
                            </div>

                        </div>

                        <div class="row-fluid sel-result">
                            <div class="row-fluid filters">
                                <div class="control-group span2">
                                    <label class="control-label">Provider ID</label>

                                    <div class="controls">
                                        <input type="text" name="providerId" id="providerId"  value="{{searchCriteria.providerId}}"/>
                                    </div>
                                </div>

                                <div class="control-group span2">
                                    <label class="control-label">Cumulative Missed Doses</label>
                                    <div class="controls">
                                        <input class="numbersOnly" maxlength="3" type="text" name="cumulativeMissedDoses" id="cumulativeMissedDoses"  value="{{searchCriteria.cumulativeMissedDoses}}"/>
                                    </div>
                                </div>

                                <div class="control-group span2">
                                    <label class="control-label">Adherence Missing Doses</label>
                                    <div class="controls">
                                        <input class="numbersOnly" maxlength="3" type="text" name="adherenceMissingWeeks" id="adherenceMissingWeeks"  value="{{searchCriteria.adherenceMissingWeeks}}"/>
                                    </div>
                                </div>

                            </div>
                        </div>
                    </fieldset>
                    <div class="control-group span2 pull-down">
                        <div class="controls">
                            <button type="submit" id="searchButton" class="btn btn-primary">
                                Search <i class="icon-search icon-white"></i>
                            </button>
                        </div>
                    </div>
            </div>
        </@paginator.filter>
    </div>
    <div class="row-fluid">
        <div class="results">
            <div id="patients">
                <@paginator.paginate id = "patient_listing" entity="patient_results" filterSectionId="patient_list_filter" contextRoot="/whp" rowsPerPage="5"  stylePath="/resources-${applicationVersion}/styles">
                    <table id="patientList" class="table table-striped table-bordered table-condensed" redirectOnRowClick="true">
                        <thead>
                        <tr>
                            <th>Name</th>
                            <th>Age</th>
                            <th>Gender</th>
                            <th>Patient ID</th>
                            <th>TB-ID</th>
                            <th>Provider ID</th>
                            <th>Village</th>
                            <th>Provider District</th>
                            <th>Treatment Category</th>
                            <th>TB Registration Date</th>
                            <th>Treatment Start Date</th>
                            <th>IP Treatment Progress</th>
                            <th>CP Treatment Progress</th>
                            <th>Cumulative Missed Doses &#42;</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr class="patient-listing link" ng-repeat="item in data.results"
                            id="PatientRows_{{item.patientId}}"
                            containerId="{{item.patientId}}" redirect-url="<@spring.url '/patients/show?patientId={{item.patientId}}' />">
                            <td class="name">{{item.firstName}}</td>
                            <td>{{item.age}}</td>
                            <td id="patient_{{item.patientId}}_Gender">{{item.gender}}</td>
                            <td class="patientId">{{item.patientId}}</td>
                            <td class="tbId">{{item.tbId}}</td>
                            <td id="patient_{{item.patientId}}_ProviderId">{{item.currentTreatment.providerId}}</td>
                            <td id="patient_{{item.patientId}}_Village">{{item.addressVillage}}</td>
                            <td id="patient_{{item.patientId}}_District">{{item.currentTreatment.providerDistrict}}</td>
                            <td id="patient_{{item.patientId}}_TreatmentCategory">{{item.treatmentCategoryName}}</td>
                            <td id="patient_{{item.patientId}}_TherapyCreationDate">{{item.currentTreatmentStartDate}}</td>
                            <td id="patient_{{item.patientId}}_TreatmentStartDate">{{item.therapyStartDate}}</td>
                            <td id="patient_{{item.patientId}}_IPProgress">{{item.ipProgress}}</td>
                            <td id="patient_{{item.patientId}}_CPProgress">{{item.cpProgress}}</td>
                            <td id="patient_{{item.patientId}}_MissedDoses">{{item.cumulativeMissedDoses}}</td>
                        </tr>
                        <tr type="no-results" class="hide">
                            <td class="warning text-center" colspan="14"></td>
                        </tr>
                        </tbody>
                    </table>
                </@paginator.paginate>
                <@paginator.paginationScripts jsPath="/resources-${applicationVersion}/js" loadJquery="false"/>
            </div>
        </div>
    </div>
    <div class="row-fluid ">
        <i>&#42; Cumulative missed doses shown as of ${lastSunday}</i>

        <div class="pull-right patients-list-legend">
            <@legend key1="paused" value1="Current Treatment Paused" />
        </div>
    </div>
</div>
    <@paginator.paginationScripts jsPath="/resources-${applicationVersion}/js" loadJquery="false"/>
    <script type="text/javascript" src="<@spring.url '/resources-${applicationVersion}/js/listPatients.js'/>"></script>
</@layout.defaultLayout>
