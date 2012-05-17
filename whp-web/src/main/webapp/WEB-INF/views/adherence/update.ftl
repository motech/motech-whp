<#import "/spring.ftl" as spring />
<#import "../layout/default.ftl" as layout>
<#include "../layout/legend.ftl">
<@layout.defaultLayout "Update Adherence">

<div class="row">
    <#if readOnly > <div class="alert alert-error"> Please contact the CMF admin to update adherence. </div> </#if>
    <h4 class="page-header form-header">Update last week's adherence</h4>
</div>

<@legend key1="paused" value1="Treatment Paused on Date" />

<div class="row">
    <form id="weeklyAdherenceForm" action="<@spring.url '/adherence/update/' + adherence.patientId/>" method="POST">
        <input type="hidden" name="patientId" value="${adherence.patientId}"/>
        <input type="hidden" name="treatmentId" value="${adherence.treatmentId}"/>
        <table class="table table-bordered">
            <thead>
                <tr>
                    <th>Day of week</th>
                    <th>Date</th>
                    <th>Taken</th>
                    <th>Not taken</th>
                </tr>
            </thead>
            <tbody>
            <input type="hidden" name="referenceDateString" value="${referenceDate}"/>
                <#list adherence.adherenceList as adherenceLog>
                <tr id="adherenceLogFor_${adherenceLog.pillDay}" class="adherenceRow <#if adherenceLog.treatmentInterrupted>paused</#if>">
                    <td>
                    ${adherenceLog.pillDay}
                        <input type="hidden" name="adherenceList[${adherenceLog_index}].pillDay" class="pillDay"
                               value="${adherenceLog.pillDay}"/>
                    </td>
                    <td>
                    ${adherenceLog.pillDateString}
                        <input type="hidden" name="adherenceList[${adherenceLog_index}].pillDateString"
                               value="${adherenceLog.pillDateString}"/>
                    </td>
                    <td>
                        <input type="radio" name="adherenceList[${adherenceLog_index}].pillStatus"
                               class="pillStatusTaken"
                               value="Taken" <#if adherenceLog.isTaken> checked </#if> <#if readOnly > disabled </#if>/>
                    </td>
                    <td>
                        <input type="radio" name="adherenceList[${adherenceLog_index}].pillStatus"
                               class="pillStatusNotTaken"
                               value="NotTaken" <#if adherenceLog.isNotTaken> checked </#if> <#if readOnly >
                               disabled </#if>/>
                    </td>
                </tr>
                </#list>
            </tbody>
        </table>
        <div class="control-group">
            <div class="controls pull-right">
                <#if !readOnly>
                    <input type="submit" class="btn btn-primary login-btn" value="Submit"/>
                </#if>
            </div>
        </div>
    </form>
</div>
</@layout.defaultLayout>
