<#import "/spring.ftl" as spring />
<#import "../layout/default.ftl" as layout>
<@layout.defaultLayout "MoTeCH-WHP">
<#if message?exists && (message?length>0)>
<div class="message-alert row text-center alert alert-info fade in">
    <button class="close" data-dismiss="alert">&times;</button>
${message}
    <#assign message=""/>
</div>
</#if>
<script type="text/javascript">
    createAutoClosingAlert(".message-alert", 5000)
</script>
<a href="<@spring.url '/cmfAdmin/create'/>"><b>Create CMF Admin</b></a>
<br>
<a id="list-providers" href="<@spring.url '/providers/list'/>"><b>Search Providers</b></a>
</@layout.defaultLayout>
