<h3 id="Remarks">Remarks</h3>
<#if cmfAdminRemarks?size == 0>
<label>
No remarks added for the patient.
</label>
<#else>
<#list cmfAdminRemarks as cmfAdminRemark>
<h5>${cmfAdminRemark.user} on ${cmfAdminRemark.creationTime.toString("dd/MM/yyy")}
    at ${cmfAdminRemark.creationTime.toString("hh:mm a")} says:</h5>
<div>${cmfAdminRemark.remark} </div>
<br/>
</#list>
</#if>
