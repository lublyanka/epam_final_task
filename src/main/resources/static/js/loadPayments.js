async function loadPayments(url, sortBy, orderBy) {
    url = addSortingToLink(url, sortBy, orderBy);
    const response = await getGetResponse(url);
    if (response.status === 200) {
        var jsonData = await getJSONData(response);
        var table = document.getElementById("paymentsTable");
        table.removeAttribute("style");
        var ul = document.getElementById("no-payments");
        hideElement(ul);
        insertPayments(jsonData);
        activatePagination(jsonData, url, 'loadPayments');
    } else if(!(response.status === 204)) insertTestErrorMessageFromResponse(response);

    function insertPayments(jsonData) {
        table.getElementsByTagName("tbody")[0].innerHTML = '';
        // Loop through the JSON data and create table rows
        jsonData.content.forEach((item) => {
            let tr = document.createElement("tr");
            var payment = [];

            var id = item.id; // get the id of the payment
            var number = item.number; // get the payment number

            // create the link element
            let link = document.createElement("a");
            link.href = "/payment/" + id; // set the href of the link to the payment id
            link.innerText = number; // set the text of the link to the payment number
            // add the link to the payment array
            payment.push(link);

            payment.push(item.amount + " " + item.currency);
            payment.push(transformTimestampToData(item.updatedOn, "true"));
            payment.push(item.description);
            payment.push(item.status);

            // Loop through the values and create table cells
            payment.forEach((item) => {
                let td = document.createElement("td");
                if (!((typeof item === "string") || (typeof item === "number")))
                    td.appendChild(item); // Set the value as the text of the table cell
                else
                    td.innerText = item; // Set the value as the text of the table cell
                tr.appendChild(td); // Append the table cell to the table row
            });
            table.getElementsByTagName("tbody")[0].appendChild(tr); // Append the table row to the table
        });
    }
}