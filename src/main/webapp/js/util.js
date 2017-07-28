function toggleRow(checkbox, itemId) {
    var disabled = !checkbox.checked;
    $("#id_" + itemId).prop('disabled', disabled);
    var inputField = $("#quantity_" + itemId);
    inputField.prop('disabled', disabled);
    if (disabled) {
        inputField.val("0");
    }
}

function addErrorHighlight(field) {
    $(field).parent().addClass("has-error");
}

function removeErrorHighlight(field) {
    $(field).parent().removeClass('has-error');
}

function showErrorMessage(message) {
    $('[id=error_message]').html('<div class="alert alert-dismissible alert-danger">' +
        '<button type="button" class="close" data-dismiss="alert">&times;</button>' +
        '<strong>' + message + '</strong></div>');
}

function validate() {
    var rows = $('[id^=coffeerow_]');
    var totalOrderQuantity = 0;
    for (var i = 0; i < rows.length; i++) {
        var row = $(rows[i]);
        var idField = row.find('[id^=id_]');
        var checkbox = row.find('[id^=selected_]');
        var quantityField = row.find('[id^=quantity_]');

        if (!idField.prop('disabled') && !quantityField.prop('disabled') && checkbox.prop('checked')) {
            var quantity = parseInt(quantityField.val());
            if (isNaN(quantity)) {
                addErrorHighlight(quantityField);
                quantityField.focus();
                showErrorMessage('Please enter correct quantity and try again.');
                return false;
            }
            totalOrderQuantity += quantity;
        }
    }
    if (totalOrderQuantity == 0) {
        showErrorMessage('Your order is empty. Please enter quantity and try again.');
        return false;
    }
}
