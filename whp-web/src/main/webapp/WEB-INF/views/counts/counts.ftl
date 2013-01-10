<#import "/spring.ftl" as spring />
<#import "../layout/default-with-menu.ftl" as layout>
<@layout.defaultLayout entity="itadmin" title="Count of all records" >
    <h1>Count of all records</h1>
    <div class="row well">
        <table class="table table-bordered table-condensed">
            <thead>
                <tr>
                    <th>Name</th>
                    <th>Count</th>
                </tr>
            </thead>
            <tbody>
                <#list counts as count>
                    <tr>
                        <td>${count.name}</td>
                        <td>${count.count}</td>
                    </tr>
                </#list>
            <tbody>
        </table>
    </div>
</@layout.defaultLayout>
