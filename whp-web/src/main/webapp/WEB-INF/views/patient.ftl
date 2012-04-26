<#import "layout/default.ftl" as layout>
<@layout.defaultLayout "MoTeCH-TB">
    <div class="row">
        Welcome ${Session.loggedInUser.username}!
    </div>
</@layout.defaultLayout>
