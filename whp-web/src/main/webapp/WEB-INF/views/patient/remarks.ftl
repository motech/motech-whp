<h3 class="remarks underline">Previous Remarks</h3>
<div id="remarks-content">
    <#if cmfAdminRemarks?size == 0 && providerRemarks?size == 0>
        <p>
            No remarks added for the patient.
        </p>
    </#if>

    <#if cmfAdminRemarks?size!=0>
        <ul id="cmf-admin-remarks">
            <#list cmfAdminRemarks as cmfAdminRemark>
                <li class="cmf-admin-remark">
                    <h5> <i class="icon-user"></i>
                    ${cmfAdminRemark.user} <span>on <em>${cmfAdminRemark.creationTime.toString("dd/MM/yyy")} </em>
                        at <em>${cmfAdminRemark.creationTime.toString("hh:mm a")}</em> says : </span>
                    </h5>

                    <div class="remark-desc">
                        <pre class="inherit-style"><#transform html_escape>${cmfAdminRemark.remark}</#transform></pre>
                    </div>
                    <br/>
                </li>
            </#list>
        </ul>
        <hr />
    </#if>

    <#if providerRemarks?size!=0>
        <ul id="provider-remarks">
            <#list providerRemarks as providerRemark>
                <li class="provider-remark">
                    <h5> <i class="icon-user"></i>
                        Provider ${providerRemark.providerId()} <span>on <em>${providerRemark.creationTime.toString("dd/MM/yyy")}</em>
                        at <em>${providerRemark.creationTime.toString("hh:mm a")}</em> says :</span>
                    </h5>

                    <div class="remark-desc">
                        <pre class="inherit-style"><#transform html_escape>${providerRemark.remark()}</#transform></pre>
                    </div>
                    <br/>
                </li>
            </#list>
        </ul>
    </#if>
</div>
