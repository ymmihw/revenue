function Dialog(id) {
    this.id = id;
}

Dialog.prototype = {
    hide : function() {
        $('#' + this.id).modal('hide');
    }
}

function inherit(proto) {
    function F() {
    }
    F.prototype = proto;
    return new F;
}

function ConfirmDialog(id, message, callback) {
    Dialog.apply(this, arguments);
    this.message = message;
    this.callback = callback;
}

ConfirmDialog.prototype = inherit(Dialog.prototype);
ConfirmDialog.prototype.show = function() {
    if ($('#' + this.id).length == 0) {
        var divHtml = 
            ' <div class="modal fade" id="' + this.id + '" role="dialog">' 
            + '  <div class="vertical-alignment-helper">'
            + '   <div class="modal-dialog modal-lg vertical-align-center">' 
            + '    <div class="modal-content">'                
            + '     <div class="modal-body">' 
            + '      <p id="' + this.id + 'Message">' + this.message + '</p>' 
            + '     </div>' 
            + '     <div class="modal-footer">'
            + '      <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>' 
            + '      <button id="' + this.id + 'Confirm"type="button" class="btn btn-primary">确定</button>' 
            + '     </div>' 
            + '    </div>' 
            + '   </div>' 
            + '  </div>' 
            + ' </div>';
        $('body').append(divHtml);
    } else {
        $('#' + this.id + 'Message').text(this.message);
    }
    $('#' + this.id + 'Confirm').unbind("click");
    $('#' + this.id + 'Confirm').click(this.callback);
    $('#' + this.id).modal();
}

function MessageDialog(id, message) {
    Dialog.apply(this, arguments);
    this.message = message;
}
MessageDialog.prototype = inherit(Dialog.prototype);
MessageDialog.prototype.show = function() {
    if ($('#' + this.id).length == 0) {
        var divHtml = 
            ' <div class="modal fade" id="' + this.id + '" role="dialog">' 
            + '  <div class="vertical-alignment-helper">'
            + '   <div class="modal-dialog modal-lg vertical-align-center">' 
            + '    <div class="modal-content">'                
            + '     <div class="modal-body">' 
            + '      <p id="' + this.id + 'Message">' + this.message + '</p>' 
            + '     </div>' 
            + '     <div class="modal-footer">'
            + '      <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>' 
            + '     </div>' 
            + '    </div>' 
            + '   </div>' 
            + '  </div>' 
            + ' </div>';
        $('body').append(divHtml);
    } else {
        $('#' + this.id + 'Message').text(this.message);
    }
    $('#' + this.id).modal();
}