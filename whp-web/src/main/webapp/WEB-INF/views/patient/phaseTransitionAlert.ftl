
<#if patient.currentTreatment?? && (patient.nearingPhaseTransition || patient.transitioning)>
<div class="alert alert-error black" id="transitionPatientAlert">
    <img class="pull-left" src="<@spring.url '/resources-${applicationVersion}/img/warning.png'/>"/>

    <div class="offset1-fixed">
        <#if patient.currentPhase?exists>
            Patient is nearing the end of his ${patient.currentPhase.name.name()} and only
            has ${patient.remainingDosesInCurrentPhase} more doses to completion.
        </#if>
        <#if patient.remainingDosesInCurrentPhase lte 0>
            Patient completed his ${patient.lastCompletedPhase.name.name()} on ${patient.lastCompletedPhase.endDate}
            and is past the due date for transitioning to next phase.
        </#if>
        <br/>
        Conduct the necessary Smear tests and accordingly transition the patient to the next phase.
        <br/>
        <#if patient.nextPhaseName?exists>
            Next phase is set as:
        </#if>
        <a id="EIP" href="/whp/patients/transitionPhase/${patient.patientId}?to=EIP" class="btn">EIP</a>
        <a id="CP" href="/whp/patients/transitionPhase/${patient.patientId}?to=CP" class="btn">CP</a>
        <br/>
    </div>
</div>
</#if>
