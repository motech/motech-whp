<#import "layout/default.ftl" as layout>
<@layout.defaultLayout "MoTeCH-TB">
    <div class="row" id="welcomeMessage">
        Welcome ${Session.loggedInUser.username}!
    </div>
</@layout.defaultLayout>
