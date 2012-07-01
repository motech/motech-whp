<#list providerList as provider>
    <option <#if provider_index == 0> selected </#if> value="${provider.providerId}">${provider.providerId}</option>
</#list>
