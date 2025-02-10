function commitToDatabase() {
    const formData = $("#inventoryForm").serialize() + "&action=commit";
    $.post("InventoryServlet", formData, function(response) {
        alert(response);
    });
}

function updateInventory() {
    const formData = $("#inventoryForm").serialize() + "&action=update";
    $.post("InventoryServlet", formData, function(response) {
        alert(response);
    });
}

function fetchData() {
    $.post("InventoryServlet", { action: "fetch" }, function(response) {
        $("#inventoryData").text(JSON.stringify(JSON.parse(response), null, 2));
    });
}