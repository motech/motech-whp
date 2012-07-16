<#macro categoryDrugs dosesPerWeek=3, drugs=["H","R"]>
<div>${dosesPerWeek} times/week</div>
<table class="text-center line-height-normal drugs">
    <tr class="table-bordered sharp">
        <#list drugs as drug>
            <td class="">&#10004;</td>
        </#list>
    </tr>
    <tr>
        <#list drugs as drug>
            <td class="">${drug}</td>
        </#list>
    </tr>
</table>
</#macro>