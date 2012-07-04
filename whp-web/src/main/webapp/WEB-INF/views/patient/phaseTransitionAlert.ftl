
<#if patient.currentTreatment?? && (patient.nearingPhaseTransition || patient.transitioning)>
    <div class="alert alert-error black" id="transitionPatientAlert">
        <img id="achtung" class="pull-left" src="<@spring.url '/resources-${applicationVersion}/img/warning.png'/>"/>
        <div class="offset1-fixed">
            <#if !patient.nextPhaseName?exists>
                <#if patient.currentPhase?exists>
                    Patient is nearing the end of his ${patient.currentPhase.name.name()} and only has ${patient.remainingDosesInCurrentPhase} more doses to completion.
                </#if>
                <#if patient.remainingDosesInCurrentPhase lte 0>
                    Patient completed his ${patient.lastCompletedPhase.name.name()} on ${patient.lastCompletedPhase.endDate} and is past the due date for transitioning to next phase.
                </#if>
                <br/>
                Contact provider to remind him/her that patient requires a sputum retest now.  Tell provider that patient should come for a free consultation with the sputum test result.
                <br/>
            </#if>

            <#if patient.nextPhaseName?exists>
                Next phase is set as:
            <#else>
                Please select the treatment decision ordered by the CMF Doctor:
            </#if>

            <a id="EIP" href="/whp/patients/transitionPhase/${patient.patientId}?to=EIP" class="btn">Initiate Extended IP</a>
            <a id="CP" href="/whp/patients/transitionPhase/${patient.patientId}?to=CP" class="btn">Start CP</a>
            <br/>
        </div>
    </div>
</#if>
<div id="patientCurrentPhase">
    Patient's Current Phase: <#if patient.currentPhase??>${patient.currentPhase.name.name()}</#if>
</div>
