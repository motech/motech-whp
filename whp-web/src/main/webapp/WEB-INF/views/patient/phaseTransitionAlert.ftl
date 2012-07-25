<#if patient.currentTreatment?? && patient.showAlert>
    <div class="alert alert-error black" id="transitionPatientAlert">
        <#if !patient.nextPhaseName?exists>
            <img id="achtung" class="pull-left" src="<@spring.url '/resources-${applicationVersion}/img/warning.png'/>"/>
        </#if>
        <div <#if !patient.nextPhaseName?exists>class="offset1-fixed"</#if>>
            <#if !patient.nextPhaseName?exists>
                <#if patient.currentPhase?exists>
                    Patient is nearing the end of ${patient.currentPhase.name.name()} and only has ${patient.remainingDosesInCurrentPhase} more doses to completion.
                </#if>
                <#if patient.remainingDosesInCurrentPhase lte 0 && patient.lastCompletedPhase??>
                    Patient has completed ${patient.lastCompletedPhase.name.name()} and is past the due date for transitioning to next phase.
                </#if>
                <br/>
                Contact provider to remind that patient requires a sputum retest now. Tell provider that patient should come for a free consultation with the sputum test result.
                <br/>
            </#if>

            <#if patient.nextPhaseName?exists>
                Next phase is set as:
            <#else>
                Please select the treatment decision ordered by the CMF Doctor:
            </#if>

            <a id="EIP" href="/whp/patients/transitionPhase/${patient.patientId}?to=EIP" class="btn"><#if patient.nextPhaseName?exists>EIP<#else>Initiate Extended IP</#if></a>
            <a id="CP" href="/whp/patients/transitionPhase/${patient.patientId}?to=CP" class="btn"><#if patient.nextPhaseName?exists>CP<#else>Start CP</#if></a>
            <br/>
        </div>
    </div>
</#if>
