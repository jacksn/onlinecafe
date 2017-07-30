function addErrorHighlight(field) {
    $(field).parent().addClass("has-error");
}

function removeErrorHighlight(field) {
    $(field).parent().removeClass('has-error');
}

function showErrorMessage(message) {
    $.notify({
            icon: 'glyphicon glyphicon-warning-sign',
            message: message
        }, {
            type: "danger",
            placement: {
                from: "bottom",
                align: "right"
            },
            mouse_over: 'pause'
        }
    );
}
