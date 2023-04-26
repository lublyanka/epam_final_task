async function loadAccounts(url) {
  const response = await fetch(url, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': localStorage.token
    },
  })
  if (response.status === 200) {
    var json = await response.json();
    const data = JSON.stringify(json);
    var jsonData = JSON.parse(data);
    var table = document.getElementById("accounts");
    table.removeAttribute("style");
    var ul = document.getElementById("no-accounts");
    ul.setAttribute("style", "display:none");
    insertAccounts(jsonData);
    activatePagination(jsonData);
  }
  if (response.status === 500)
  {
    window.location.href = "/500";
  }

  function insertAccounts(jsonData) {

    table.getElementsByTagName("tbody")[0].innerHTML = '';
    // Loop through the JSON data and create table rows
    jsonData.content.forEach((item) => {
      let tr = document.createElement("tr");
      var account = [];

      var id = item.id; // get the id of the account
      var number = item.number; // get the account number

      // create the link element
      let link = document.createElement("a");
      link.href = "/account/" + id; // set the href of the link to the account id
      link.innerText = number; // set the text of the link to the account number

      // add the link to the account array
      account.push(link);
      account.push(item.name);
      account.push(item.currentBalance + " " + item.currency);
      account.push((item.blocked == false) ? 'active' : 'blocked');

      // Loop through the values and create table cells
      account.forEach((item) => {
        let td = document.createElement("td");
        if (!(typeof item === "string"))
          td.appendChild(item); // Set the value as the text of the table cell
        else
          td.innerText = item; // Set the value as the text of the table cell
        tr.appendChild(td); // Append the table cell to the table row
      });
      table.getElementsByTagName("tbody")[0].appendChild(tr); // Append the table row to the table
    });
  }
}

function activatePagination(jsonData) {
  const container = document.getElementById('pagination');
  if (container != null) {
    var currentPage = jsonData.number;
    var totalPages = jsonData.totalPages;
    generatePagination(currentPage + 1, totalPages, 'loadAccounts', '/api/account/all?size=10', container);
  }
};