<#if treatment??>
<h4>Treatment Details</h4>
<div>
    <label class="tc-label">TB ID</label>
    <label class="tc-value">${treatment.tbId}</label>
</div>
<div>
    <label class="tc-label">Provider ID</label>
    <label class="tc-value">${treatment.providerId}</label>
</div>
<div>
    <label class="tc-label">Creation Date</label>
    <label class="tc-value">${treatment.creationDate!}</label>
</div>
<div>
    <label class="tc-label">Start Date</label>
    <label class="tc-value">${treatment.startDate!}</label>
</div>
<div>
    <label class="tc-label">End Date</label>
    <label class="tc-value">${treatment.endDate!}</label>
</div>
<div>
    <label class="tc-label">Treatment Outcome</label>
    <label class="tc-value">
    <#if treatment.treatmentOutcome??>
        ${treatment.treatmentOutcome.outcome!}
    </#if>
    </label>
</div>
<div>
    <label class="tc-label">TB Registration Number</label>
    <label class="tc-value">${treatment.tbRegistrationNumber!}</label>
</div>
<div>
    <label class="tc-label">Close Treatment Remarks</label>
    <label class="tc-value">${treatment.closeTreatmentRemarks!}</label>
</div>

<div class="controls pull-right">
    <button id="deleteTreatment" class="btn btn-primary btn-danger">
        Delete <i class="icon-trash icon-white"></i>
    </button>
</div>
<#else>
<h4>No Treatment Found</h4>
</#if>