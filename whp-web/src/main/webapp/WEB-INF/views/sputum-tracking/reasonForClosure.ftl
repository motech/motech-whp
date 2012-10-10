<form class="modal hide fade" id="setReason" method="POST" action="<@spring.url '/sputum-tracking/updateReasonForClosure'/>"
      xmlns="http://www.w3.org/1999/html">
    <input type="hidden" id="containerId" name="containerId" value="${item.containerId}"/>

    <div class="modal-header">
        <button class="close" data-dismiss="modal">x</button>
        <h3>Reason For Closure</h3>
    </div>
    <div class="modal-body">
        <div class="control-group">
            <table id="reasonUpdateTable" class="table table-bordered table-condensed">
                <tbody>
                <tr>
                    <td>
                        <div class="control-group">
                            <label class="control-label">Reason*</label>

                            <div class="controls">
                                <select id="reason" name="selectedReason">
                                <#list reasons as reason>
                                    <option  value="${reason.name}">${reason.name}</option>
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
        <button class="btn btn-group" data-dismiss="modal">Close</button>
        <input type="button" class="btn btn-group" id="clearFields" value="Clear All"/>
        <button type="submit" class="btn btn-group btn-primary" id="saveTheDate">Save</button>
    </div>
</form>