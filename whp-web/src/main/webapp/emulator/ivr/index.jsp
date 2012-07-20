<%@page import="org.springframework.context.ApplicationContext" %>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@page import="java.util.Properties" %>
<html>
<head>
<%
    ApplicationContext appCtx = WebApplicationContextUtils.getWebApplicationContext(config.getServletContext());
    Properties whpProperties = appCtx.getBean("whpProperties", Properties.class);
    String appVersion = whpProperties.getProperty("application.version");
%>

<%
    String url = application.getContextPath() + "/kookoo/ivr?tree=adherenceCapture";
%>

<script src="http://localhost:8080/whp/js/jquery/jquery-1.7.2.js"></script>
<script type="text/javascript">
    $(document).ready(function () {
        $('#missedCallButton').click(function () {
            dojo.xhrPost({
                url:'<%= url %>&external_id=' + $('#missedCall').val() + "&call_type=Outbox",
                content:{ 'phone_no':$('#phone').val(), 'status':'ring', 'sid':callId},
                load:function () {
                    alert('Posted missed call');
                }
            })
        });
    });
</script>
<script type="text/javascript">
    /**
     * jQuery Cookie plugin
     *
     * Copyright (c) 2010 Klaus Hartl (stilbuero.de)
     * Dual licensed under the MIT and GPL licenses:
     * http://www.opensource.org/licenses/mit-license.php
     * http://www.gnu.org/licenses/gpl.html
     *
     */
    jQuery.cookie = function (key, value, options) {

        // key and at least value given, set cookie...
        if (arguments.length > 1 && String(value) !== "[object Object]") {
            options = jQuery.extend({}, options);

            if (value === null || value === undefined) {
                options.expires = -1;
            }

            if (typeof options.expires === 'number') {
                var days = options.expires, t = options.expires = new Date();
                t.setDate(t.getDate() + days);
            }

            value = String(value);

            return (document.cookie = [
                encodeURIComponent(key), '=',
                options.raw ? value : encodeURIComponent(value),
                options.expires ? '; expires=' + options.expires.toUTCString() : '', // use expires attribute, max-age is not supported by IE
                options.path ? '; path=' + options.path : '',
                options.domain ? '; domain=' + options.domain : '',
                options.secure ? '; secure' : ''
            ].join(''));
        }

        // key and possibly options given, get cookie...
        options = value || {};
        var result, decode = options.raw ? function (s) {
            return s;
        } : decodeURIComponent;
        return (result = new RegExp('(?:^|; )' + encodeURIComponent(key) + '=([^;]*)').exec(document.cookie)) ? decode(result[1]) : null;
    };
</script>

<script>
    var contextRoot = "<%= url %>";
    var collectdtmf = 1;
    var dtmf = "";
    var callId;

    function deleteCookie() {
        var d = new Date();
        document.cookie = "v0=1;expires=" + d.toGMTString() + ";" + "path=<%=application.getContextPath() %>/ivr/;";

        alert(document.cookie);
    }
    function pollCall() {
        if ($('#poll_call').is(':checked'))
            $.getJSON("<%=application.getContextPath() %>/emulator/calls.jsp?phone=" + $('#phone').val(), function (data) {
                if (data.phone_no) {
                    if (confirm("Incoming call.. Receive?")) {
                        $('#phone').val(data.phone_no);
                        $('#call_id').val(data.call_id);
                        $('#retry_interval').val(data.retry_interval);
                        $('#is_outbound_call').attr('checked', (data.is_outbound_call === "true"));
                        $('#outbox_call').attr('checked', (data.outbox_call === "true"));
                    }
                }
                setTimeout(pollCall, 1000);
            });
    }

    $(function () {
        setTimeout(pollCall, 500);
    });
    $('#missedCallButton').click(function () {
        dojo.xhrPost({
            url:'<%= url %>&external_id=' + $('#missedCall').val() + "&call_type=Outbox",
            content:{ 'phone_no':$('#phone').val(), 'status':'ring', 'sid':callId},
            load:function () {
                alert('Posted missed call');
            }
        })
    });


    function playfile(id) {
        if (!$('#mute').is(":checked"))
            document.getElementById(id).play();
        //console.log(new Date());
    }

    function playAll(idx) {
        var duration = 0;
        var audioslist = $('.audio');
        var i = idx;
        if (typeof idx == "undefined") i = 0;
        for (; i < audioslist.length; i++) {
            var ad = audioslist[i].id;
            window.setTimeout("playfile('" + ad + "');", (1 + duration * 1000));
            if (isNaN(audioslist[i].duration)) {
                setTimeout("playAll(" + i + ")", 500);
                return;
            }
            else {
                duration += audioslist[i].duration;
            }
        }
    }


    function call(path) {
        try {

            $.ajax({url:path,
                crossDomain:true,
                beforeSend:function (xhr) {
                    //xhr.setRequestHeader('Cookie', "");
                    //xhr.setRequestHeader("X-Set-Cookie", "foo2=quux");
                },
                success:function (data) {
                    //alert(data);
                    $('#response').val(data);
                    if ($(data).length == 1 && $(data)[0].childElementCount == 0) {
                        send('');
                        return;
                        $('#missedCallButton').click(function () {
                            dojo.xhrPost({
                                url:'<%= url %>&external_id=' + $('#missedCall').val() + "&call_type=Outbox",
                                content:{ 'phone_no':$('#phone').val(), 'status':'ring', 'sid':callId},
                                load:function () {
                                    alert('Posted missed call');
                                }
                            })
                        });
                    }
                    var html = "";//'<audio controls="controls" id="message" autoplay="autoplay">';
                    //var msg = ""
                    collectdtmf = $(data).find('collectdtmf').attr('l');
                    $(data).find('playaudio').each(function () {
                        var filename = $(this).text();//.replace(/.*\//, "");
                        var text = filename;//.replace(/.wav/,"");
                        html += '<audio src="' + filename + '" autostart=false width=1 height=1 id="' + filename + '" enablejavascript="true" class="audio"/>' +
                                '<button id="' + filename + '" onclick="play(\'' + filename + '\');">&raquo;' + text + ' </button>';
                        html += '<source src="' + filename + '" type="audio/wave" />';
                        //msg+=text + " ";
                    });
                    $(data).find('playtext').each(function () {
                        html += "<div>" + $(this).text() + "</div>"
                    });
                    $('#missedCallButton').click(function () {
                        dojo.xhrPost({
                            url:'<%= url %>&external_id=' + $('#missedCall').val() + "&call_type=Outbox",
                            content:{ 'phone_no':$('#phone').val(), 'status':'ring', 'sid':callId},
                            load:function () {
                                alert('Posted missed call');
                            }
                        })
                    });
                    //html += "</audio>";
                    //html ='<button  onclick="play(\'message\');">&raquo;'+msg+' </button><span style="float:right;">&nbsp;'+ html + '</span>';
                    if ($(data).find('playaudio').length > 0 && $(data).find('collectdtmf').length == 0) html += '<button onclick="send()">Continue</button>'
                    if($(data).find('gotourl').length > 0){
                        contextRoot = $(data).find('gotourl').text();
                    }
                    $('#result').html(html);

                    playAll();
                },
                error:function (x, status, err) {
                    alert('error ' + status + err);
                }
            });
        } catch (e) {
            alert(e);
        }
    }

    function newCall() {
        callId = Math.floor(Math.random() * 10000000000);
        var phone = $('#phone').val();
        var symptoms_reporting_option = ($('#symptoms_reporting').is(":checked") ? "&symptoms_reporting=true" : "");
        call(contextRoot + '&event=NewCall&cid=' + phone + '&sid=' + callId + symptoms_reporting_option);
    }
    function endCall() {
        var phone = $('#phone').val();
        var symptoms_reporting_option = ($('#symptoms_reporting').is(":checked") ? "&symptoms_reporting=true" : "");
        deleteCookie();
        call(contextRoot + '&event=Hangup&cid=' + phone + '&sid=' + callId + symptoms_reporting_option);
        $.cookie('current_decision_tree_position', null, {'path':'<%=application.getContextPath() %>/ivr/'});
        $.cookie('preferred_lang_code', null, {'path':'<%=application.getContextPath() %>/ivr/'});
        $.cookie('call_id', null, {'path':'<%=application.getContextPath() %>/ivr/'});
        $.cookie('call_detail_record_id', null, {'path':'<%=application.getContextPath() %>/ivr/'});
        $.cookie('LastCompletedTree', null, {'path':'<%=application.getContextPath() %>/ivr/'});
        $.cookie('LastPlayedVoiceMessageID', null, {'path':'<%=application.getContextPath() %>/ivr/'});
        $.cookie('outboxCompleted', null, {'path':'<%=application.getContextPath() %>/ivr/'});
        $.cookie('lastPlayedHealthTip', null, {'path':'<%=application.getContextPath() %>/ivr/'});
        $.cookie('healthTipsPlayedCount', null, {'path':'<%=application.getContextPath() %>/ivr/'});
        $.cookie('switch_to_dial_state', null, {'path':'<%=application.getContextPath() %>/ivr/'});
        $.cookie('number_of_clinicians_called', null, {'path':'<%=application.getContextPath() %>/ivr/'});
    }

    function login() {
        var phone = $('#phone').val();
        var call_id = $('#call_id').val();
        var is_outbound_call = $('#is_outbound_call').is(":checked") ? "true" : "";
        var dataMap = "";
        if (is_outbound_call == 'true')
            dataMap = ($('#symptoms_reporting').is(":checked")) ? "" : ('&dataMap={%27dosage_id%27:%27' + dosageId + '%27, %27regimen_id%27:%27' + regimen_id + '%27, %27times_sent%27:%27' + times_sent + '%27, %27retry_interval%27:%27' + retry_interval + '%27, %27total_times_to_send%27:%27' + total + '%27, %27call_id%27:%27' + call_id + '%27, %27outbox_call%27:%27' + outbox_call + '%27, %27is_outbound_call%27:%27' + is_outbound_call + '%27}');
        call(contextRoot + '&event=GotDTMF&cid=' + phone + '&data=' + pin + '&sid=' + callId + dataMap + symptoms_reporting_option);
    }

    function send(i) {
        if (i === '') dtmf = i;
        else {
            dtmf += i;
            if (typeof collectdtmf != "undefined" && collectdtmf > dtmf.length) {
                return;
            }
        }


        var phone = $('#phone').val();
        var is_outbound_call = $('#is_outbound_call').is(":checked") ? "true" : "";
        var dataMap = "";
        var event = "event=GotDTMF&";
        if (collectdtmf) event = "event=GotDTMF&" + '&data=' + dtmf + "&";
        call(contextRoot + '&' + event + 'cid=' + phone + '&sid=' + callId);
        dtmf = "";
    }
    function play(i) {
        var el = document.getElementById(i);
        el.play();
    }

</script>
<style>
    .optional {
        display: none;
    }
</style>
</head>
<body>
<div>
    <div id="result">

    </div>
    <br/>
    <button onclick="newCall();">New Call</button>
    <button onclick="login();">Login</button>
    <button onclick="endCall();">End Call</button>
    <a href="playFiles.jsp">Play Files</a>
    <table>
        <tr>
            <td>
                <table>
                    <tr>
                        <td>
                            <button onclick="send(1);">1</button>
                        </td>
                        <td>
                            <button onclick="send(2);">2</button>
                        </td>
                        <td>
                            <button onclick="send(3);">3</button>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <button onclick="send(4);">4</button>
                        </td>
                        <td>
                            <button onclick="send(5);">5</button>
                        </td>
                        <td>
                            <button onclick="send(6);">6</button>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <button onclick="send(7);">7</button>
                        </td>
                        <td>
                            <button onclick="send(8);">8</button>
                        </td>
                        <td>
                            <button onclick="send(9);">9</button>
                            domain
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <button onclick="send(0);">0</button>
                        </td>
                        <td>
                            <button onclick="send('');">blank</button>
                        </td>
                    </tr>
                </table>
            </td>
            <td style="width:450px;"></td>
        </tr>
    </table>
    <br><br>
    <label for="missedCall">Patient Doc Id </label><input id="missedCall" type="text"/>
    <button id="missedCallButton">Missed Call</button>
    <br><br>

    <div><input type="checkbox" id="mute"></input> Mute audio <span><input type="checkbox"
                                                                           id="symptoms_reporting"></input> Symptoms Reporting call
<input type="checkbox" id="poll_call" checked="true"></input> Accept incoming call
</span></div>
    <button onclick="$('.optional').toggle(600);">Show / Hide Params</button>
    <table id="params">
        <tr>
            <td>Phone Number</td>
            <td><input type="text" id="phone" value="1234567899"/></td>
            <td>(From Tama patient profile)</td>
        </tr>
        <tr class="optional">
            <td>call id</td>
            <td><input type="text" id="call_id" value=""/></td>
        </tr>
        <tr class="optional">
            <td>outbound call?</td>
            <td><input type="checkbox" id="is_outbound_call" value="false"/></td>
        </tr>
        <tr>
            <td colspan="2"><textarea rows="10" cols="100" id="response"></textarea></td>
        </tr>
    </table>
</div>
</body>
</html>