<#import "/spring.ftl" as spring />
<#import "../layout/default.ftl" as layout>
<@layout.defaultLayout "Error occurred">
   <div class="row well form-horizontal">
    <h2><@spring.message code="message.error"/></h2>
    <h3><@spring.message code="message.error.description"/></h3>
   </div>
</@layout.defaultLayout>