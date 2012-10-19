<#ftl encoding="ISO-8859-5">
<form class="modal hide fade" id="setReason" method="POST"
      action="<@spring.url '/sputum-tracking/in-treatment/close-container'/>"
      xmlns="http://www.w3.org/1999/html">
    <input id="containerId" type="hidden" name="containerId" value=""/>


    <div class="modal-header">
        <button class="close" data-dismiss="modal">x</button>
        <h3>
            Reason For Closure [<span id="containerIdDisplay" class="containerIdDisplay" name="containerIdDisplay"></span>]
        </h3>
    </div>
    <div class="modal-body">
        <div class="control-group">
            <table id="reasonUpdateTable" class="table table-bordered table-condensed">
                <tbody>
                <tr>
                    <td>
                        <div class="control-group">
                            <div class="controls">
                                <label class="control-label">Reason*</label>
                                <select id="reason" name="reason" validate="required:true">
                                    <option value=""></option>
                                <#list reasons as reason>
                                    <option value="${reason.code}">
                                        ${reason.name}
                                    </option>
                                </#list>
                                </select>
                            </div>
                        </div>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
    <div class="modal-footer">
        <button class="btn btn-group" data-dismiss="modal" id="close">Cancel</button>
        <button type="submit" class="btn btn-group btn-primary" id="saveReason">Save</button>
    </div>
</form>