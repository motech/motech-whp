<#import "/spring.ftl" as spring />
<#import "../layout/default.ftl" as layout>
<@layout.defaultLayout "403">
   <div class="row well form-horizontal">
    <h2><@spring.message code="message.access.denied"/></h2>
    <h3><@spring.message code="message.access.denied.description"/></h3>
    <h3><@spring.message code="message.access.denied.help"/></h3>
   </div>
</@layout.defaultLayout>
