var table;

async function loadUsers(url) {
    const response = await getGetResponse(url);
    if (response.status === 200) {
        var jsonData = await getJSONData(response);
        table = document.getElementById("users");
        table.removeAttribute("style");
        insertUsers(jsonData);
        activatePagination(jsonData);
    }
    if (response.status === 500) {
        window.location.href = "/Error500";
    }
}

function insertUsers(jsonData) {
    table = document.getElementById("usersTable");
    table.removeAttribute("style");
    table.getElementsByTagName("tbody")[0].innerHTML = '';
    // Loop through the JSON data and create table rows
    jsonData.content.forEach((item) => {
        let tr = document.createElement("tr");
        var user = [];

        var id = item.id;
        var name = item.name + " " + item.surname; // get the id of the payment

        // create the link element
        let link = document.createElement("a");
        link.href = "/users/" + id;
        link.innerText = name;
        user.push(link);
        user.push(item.email);
        user.push(item.role);
        user.push(item.lastLogin != null ? transformTimestampToData(item.lastLogin, "true") : "");
        user.push((item.enabled === true) ? 'active' : 'blocked');

        // Loop through the values and create table cells
        user.forEach((item) => {
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

function activatePagination(jsonData) {
    const container = document.getElementById('pagination');
    if (container != null) {
        var currentPage = jsonData.number;
        var totalPages = jsonData.totalPages;
        generatePagination(currentPage + 1, totalPages, 'loadUsers', '/api/admin/users/all?size=10', container);
    }
};