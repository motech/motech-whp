<#import "/spring.ftl" as spring />
<#import "../layout/default-with-menu.ftl" as layout>
<#include "../layout/legend.ftl">
<@layout.defaultLayout title="Update Adherence" entity="provider">
<div class="row">
    <#if adherence.warningMessage != ''>
        <div id="adherenceWarning" class="alert alert-error">
        ${adherence.warningMessage}
        </div>
    </#if>
    <h4 class="page-header form-header" id="adherenceCaption">Update adherence for Patient ID: ${adherence.patientId} from ${weekStartDate} to ${weekEndDate}</h4>
</div>

<form id="weeklyAdherenceForm" class="row well form-horizontal" action="<@spring.url '/adherence/update/' + adherence.patientId/>" method="POST">
    <input type="hidden" name="patientId" value="${adherence.patientId}"/>
    <input type="hidden" name="referenceDateString" value="${adherence.referenceDateString}"/>
    <div id="numberSelect" class="control-group">
        <label class="control-label"><b>How many doses did the patient take in the last week?</b></label>
        <div class="controls">
            <select id="dosesTaken" name="numberOfDosesTaken" <#if readOnly> disabled </#if>>
                <#list 0..totalDoses as number>
                    <option <#if adherence.numberOfDosesTaken == number> selected </#if> value="${number}">${number}</option>
                </#list>
            </select>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label"><b>Remarks:</b></label>
        <div class="controls">
            <textarea id="remarks" name="remarks" <#if readOnly> disabled class="disable-remarks"</#if>></textarea>
        </div>
    </div>
    <div class="control-group">
        <a href="<@spring.url ''/>" class="btn">Cancel</a>
        <#if !readOnly>
            <input type="submit" id="submit" class="btn btn-primary login-btn" value="Submit"/>
        </#if>
    </div>
</form>
</@layout.defaultLayout>
