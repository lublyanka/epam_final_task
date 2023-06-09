var accountJSONData;

async function loadAccounts(url, sortBy, orderBy) {
  url = addSortingToLink(url, sortBy, orderBy);
  const response = await getGetResponse(url);
  if (response.status === 200) {
    accountJSONData = await getJSONData(response);
    var table = document.getElementById("accountsTable");
    table.removeAttribute("style");
    var ul = document.getElementById("no-accounts");
    hideElement(ul);
    insertAccounts(accountJSONData);
    activatePagination(accountJSONData, url, 'loadAccounts');
  } else if(!(response.status === 204)) insertTestErrorMessageFromResponse(response);

  function insertAccounts(jsonData) {
    table.getElementsByTagName("tbody")[0].innerHTML = '';
    // Loop through the JSON data and create table rows
    jsonData.content.forEach((item) => {
      let tr = document.createElement("tr");
      var account = [];

      var id = item.id; // get the id of the account
      var number = item.number; // get the account number

      // create the link element
      let link = createAccountLink(id, number); // set the text of the link to the account number
      // add the link to the account array
      account.push(link);
      account.push(item.name);
      account.push(item.currentBalance + " " + item.currency);
      account.push((item.blocked) ? 'blocked' : 'active');

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

async function loadAccountsWithStatus(url) {
  const response = await getGetResponse(url);
  if (response.status === 200) {
    accountJSONData = await getJSONData(response);
    // var ul = document.getElementById("no-accounts");
    // ul.setAttribute("style", "display:none");
    insertAccountsSelector(accountJSONData);
  }
}

function insertAccountsSelector(jsonData) {
  var select = document.getElementById("account");
  jsonData.content.forEach((item) => {
    let option = document.createElement("option");
    let b = document.createElement("b");
    b.innerHTML = item.number;
    let div = document.createElement("div");
    div.classList.add("left");
    div.classList.add("truncate");
    div.classList.add("valign-wrapper");
    div.appendChild(b);
    option.innerText = `(${item.name})`;
    option.appendChild(div);
    option.setAttribute("value", item.id)
    select.appendChild(option)
  });

  elems = document.querySelectorAll('select');
  var selects = M.FormSelect.init(elems);
}