<#import "../layout/default.ftl" as layout>
<@layout.defaultLayout "MoTeCH-WHP">

<div>
<ul class="nav nav-list leftnav-menu-margin">
    <li class="nav-header hero-unit">
        <b>Patient Listings</b>
    </li>
    <li>
        <a id="show-patients" href="/whp/patients/list">Show all patients</a>
    </li>
    <hr>
    <li class="nav-header hero-unit">
        <b>Downloads</b>
    </li>
    <li style="color: #f7f7f7;" ">
        <a href="/whp/reports/adherence/adherenceReport.xls">Download all adherence data to excel</a>
    <li>
    <hr>
</ul>
</div>
</@layout.defaultLayout>


