const urlProfile = "/api/auth/profile";
const urlAccounts = "/api/account/all";
var surname;
var name;

document.addEventListener('DOMContentLoaded', function () {
  loadProfile();
  loadAccounts();
});

async function loadProfile() {
  const response = await fetch(urlProfile, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': localStorage.token
    },
  })
  if (response.ok) {
    var json = await response.json();
    const data = JSON.stringify(json);
    var obj = JSON.parse(data);
    surname = obj.surname;
    name = obj.name;
    insertNameAndSurname();
  }
}

async function loadAccounts() {
  const response = await fetch(urlAccounts, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': localStorage.token
    },
  })
  if (response.ok) {
    var json = await response.json();
    const data = JSON.stringify(json);
    var jsonData = JSON.parse(data);
    var table = document.getElementById("accounts");
    table.removeAttribute("style");
    var ul = document.getElementById("no-accounts");
    ul.setAttribute("style", "display:none");
    insertAccounts(jsonData);
  }

  function insertAccounts(jsonData) {
    // Loop through the JSON data and create table rows
    jsonData.forEach((item) => {
      let tr = document.createElement("tr");
      var account = [];
      account.push(item.number);
      account.push(item.name);
      account.push(item.currentBalance + " " + item.currency);
      account.push((item.blocked == false) ? 'active' : 'blocked');

      // Loop through the values and create table cells
      account.forEach((item) => {
        let td = document.createElement("td");
        td.innerText = item; // Set the value as the text of the table cell
        tr.appendChild(td); // Append the table cell to the table row
      });
      table.getElementsByTagName("tbody")[0].appendChild(tr); // Append the table row to the table
    });
  }
}


function insertNameAndSurname() {
  const nameSurnameElement = document.getElementById("name-surname");
  nameSurnameElement.innerText = `${surname} ${name}`;
}

