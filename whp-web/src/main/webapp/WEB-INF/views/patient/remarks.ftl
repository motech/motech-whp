<h3 id="Remarks">Remarks</h3>
<#if cmfAdminRemarks?size == 0 && providerRemarks?size == 0>
<label>
    No remarks added for the patient.
</label>
</#if>

<#list cmfAdminRemarks as cmfAdminRemark>
<h5>
    ${cmfAdminRemark.user} on ${cmfAdminRemark.creationTime.toString("dd/MM/yyy")}
    at ${cmfAdminRemark.creationTime.toString("hh:mm a")} says:
</h5>
<div>${cmfAdminRemark.remark}</div>
<br/>
</#list>

<#list providerRemarks as providerRemark>
<h5>
    Provider ${providerRemark.providerId()} on ${providerRemark.creationTime.toString("dd/MM/yyy")}
    at ${providerRemark.creationTime.toString("hh:mm a")} says:
</h5>
<div>
${providerRemark.remark()}
</div>
</#list>
