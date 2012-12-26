<#import "/spring.ftl" as spring />
<#import "../layout/default-with-menu.ftl" as layout>
<@layout.defaultLayout title="Provider Reminder Configuration" entity="itadmin">
    <div class="row well form-container offset1-fixed">
    <h1>Schedule Reminder</h1>
        <form id="provider-reminder-form"  autocomplete="off" action="<@spring.url '/providerreminder/update'/>" input method="POST" submitOnEnterKey="true" class="form-horizontal">
            <div class="form-element">
                <div class="control-group">
                    <label class="control-label">Reminder Type</label>
                    <div class="controls">
                        <@spring.bind "providerReminderConfiguration.reminderType" />
                        <input type="text" id="${spring.status.expression}" name="${spring.status.expression}" value="${spring.status.value?default("")}" readonly=""/>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">Day Of Week</label>
                    <div class="controls">
                        <@spring.bind "providerReminderConfiguration.dayOfWeek" />
                        <select id="dayOfWeek" name="${spring.status.expression}" validate="required:true">
                            <#list ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"] as dayOfWeek>
                                <option value="${dayOfWeek}" <#if spring.status.value?default("") == dayOfWeek>selected="true"</#if>>${dayOfWeek}</option>
                            </#list>
                        </select>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">Hour</label>
                    <div class="controls">
                        <@spring.bind "providerReminderConfiguration.hour" />
                        <select id="hour" name="${spring.status.expression}" validate="required:true">
                            <#list 1..24 as hour>
                                <#assign hourValue = spring.status.value?number>
                                <option value="${hour}" <#if hourValue == hour>selected="true"</#if>>${hour}</option>
                            </#list>
                        </select>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">Minute</label>
                    <div class="controls">
                        <@spring.bind "providerReminderConfiguration.minute" />
                        <select id="minute" name="${spring.status.expression}" validate="required:true">
                            <#list 0..59 as minute>
                                <#assign minuteValue = spring.status.value?number>
                                <option value="${minute}" <#if minuteValue == minute>selected="true"</#if>>${minute}</option>
                            </#list>
                        </select>
                    </div>
                </div>
                <div class="form-buttons control-group">
                    <div class="controls">
                        <button type="submit" id="registerButton" class="btn btn-primary">
                            <i class="icon-ok icon-white"></i> Schedule Reminder
                        </button>
                        <a id="back" class="btn padding-right" href="<@spring.url ''/>"><i class="icon-remove"></i> Cancel</a>
                    </div>
                </div>
            </div>
        </form>
    </div>
</@layout.defaultLayout>
