<#import "/spring.ftl" as spring />
<#import "../layout/default-with-menu.ftl" as layout>
<@layout.defaultLayout title="Schedule Configuration" entity="itadmin">
    <div class="modal hide" id="confirm">
        <div class="modal-header">
            <button class="close" data-dismiss="modal">x</button>
            <h3>Confirm Reminder</h3>
        </div>
        <div class="modal-body">
            This will execute immediately. Do you still want to continue?
        </div>
        <div class="modal-footer">
            <button class="btn btn-primary"  data-dismiss="modal" id="confirmReminder">OK</button>
            <button class="btn" data-dismiss="modal">Cancel</button>
        </div>
    </div>
    <div class="row well form-container offset1-fixed" style="position:relative">
    <div id="alert">
        <#if message?exists>
            <div id="update-alert" class="alert row alert-non-intrusive alert-success fade in"><button class="close" data-dismiss="alert">&times;</button>Successfully updated</div>
            <script type="text/javascript">
                createAutoClosingAlert("#update-alert", 5000);
            </script>
        </#if>
    </div>
    <h1>
        Schedule
        <#if scheduleConfiguration.scheduled == true>
            <div class="pull-right controls">
                <button type="button" id="manualTrigger" class="btn btn-danger" data-toggle="modal" href="#confirm"><i class="icon-play icon-white"></i> Remind now</button>
                <button type="submit" id="unScheduleButton" class="btn btn-primary"><i class="icon-trash icon-white"></i> Unschedule</button>
            </div>
        </#if>
    </h1>
        <form id="provider-reminder-form"  autocomplete="off" action="<@spring.url '/schedule/update'/>" input method="POST" submitOnEnterKey="true" class="form-horizontal">
            <div class="form-element">
                <div class="control-group">
                    <label class="control-label">Type</label>
                    <div class="controls">
                        <@spring.bind "scheduleConfiguration.scheduleType" />
                        <input type="hidden" id="${spring.status.expression}" name="${spring.status.expression}" value="${spring.status.value?default("")}" readonly=""/>
                        <div class="span8 text-value"><strong>${spring.status.value?default("")?replace("_"," ")}</strong></div>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">Day Of Week</label>
                    <div class="controls">
                        <@spring.bind "scheduleConfiguration.dayOfWeek" />
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
                        <@spring.bind "scheduleConfiguration.hour" />
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
                        <@spring.bind "scheduleConfiguration.minute" />
                        <select id="minute" name="${spring.status.expression}" validate="required:true">
                            <#list 0..59 as minute>
                                <#assign minuteValue = spring.status.value?number>
                                <option value="${minute}" <#if minuteValue == minute>selected="true"</#if>>${minute}</option>
                            </#list>
                        </select>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">Message ID</label>
                    <div class="controls">
                        <@spring.bind "scheduleConfiguration.messageId" />
                        <input type="text" name="${spring.status.expression}" value="${spring.status.value?default("")}"/>
                    </div>
                </div>
                <div class="form-buttons control-group">
                    <div class="controls">
                        <button type="submit" id="registerButton" class="btn btn-primary">
                            <i class="icon-ok icon-white"></i>
                            <#if scheduleConfiguration.scheduled == true>Reschedule
                            <#else>
                                Schedule
                             </#if>
                        </button>
                        <a id="back" class="btn padding-right" href="<@spring.url ''/>"><i class="icon-remove"></i> Cancel</a>
                    </div>
                </div>
            </div>
        </form>

        <script type="text/javascript">
        $('#unScheduleButton').click(function(){
           var action =  $('#provider-reminder-form').attr('action');
           $('#provider-reminder-form').attr('action',action+"/unschedule");
           $('#provider-reminder-form').submit();
        })
        $('#confirmReminder').click(function(){
           var messageId = encodeURIComponent('${scheduleConfiguration.messageId}')
           $.get('/whp/schedule/execute?type=${scheduleConfiguration.scheduleType}&messageId=' + messageId, function(data){
                $("#alert").html('<div id="update-alert" class="alert row alert-non-intrusive alert-success fade in"><button class="close" data-dismiss="alert">&times;</button>' + data + '</div>');
                createAutoClosingAlert("#update-alert", 5000);
           });
        })
        </script>

    </div>
</@layout.defaultLayout>
