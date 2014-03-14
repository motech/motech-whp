<#if patient??>
<h4>Patients Details</h4>
<div>
    <label class="tc-label">Patient ID</label>
    <label class="tc-value">${patient.patientId}</label>
</div>

<div>
    <label class="tc-label">First Name</label>
    <label class="tc-value">${patient.firstName}</label>
</div>

<div>
    <label class="tc-label">Last Name</label>
    <label class="tc-value">${patient.lastName}</label>
</div>

<div>
    <label class="tc-label">Age</label>
    <label class="tc-value">${patient.age}</label>
</div>

<div>
    <label class="tc-label">Gender</label>
    <label class="tc-value">${patient.gender}</label>
</div>

<div>
    <label class="tc-label">District</label>
    <label class="tc-value">${patient.addressDistrict}</label>
</div>


<h4>Treatment Details</h4>
<div>

            <label class="tc-label">Most Recent tb Id</label>
            <label class="tc-value">${currentTreatmentDetail}</label>

        <h5>Previous TB IDS</h5>

           <#foreach item in previousTreatmentDetails>
                <label class="tc-label">previous tb Id</label>
                <label class="tc-value">${item}</label>
          </#foreach>
</div>


<div class="controls pull-right">
    <button id="deletePatient" class="btn btn-primary btn-danger">
        Delete <i class="icon-trash icon-white"></i>
    </button>
</div>
<#else>
<h4>No Patient Found</h4>
</#if>