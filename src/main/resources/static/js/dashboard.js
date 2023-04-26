const urlProfile = "/api/auth/profile";
const urlAccountsDash = "/api/account/all?page=0&size=3";
const urlPayments = "/api/payment/all";
var surname;
var name;

document.addEventListener('DOMContentLoaded', function () {
  loadProfile();
  loadAccounts(urlAccountsDash);
  loadPayments();
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

async function loadPayments() {
  const response = await fetch(urlPayments, {
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
    var table = document.getElementById("payments");
    table.removeAttribute("style");
    var ul = document.getElementById("no-payments");
    ul.setAttribute("style", "display:none");
    insertPayments(jsonData);
  }

  function insertPayments(jsonData) {
    // Loop through the JSON data and create table rows
    jsonData.forEach((item) => {
      let tr = document.createElement("tr");
      var payment = [];
      payment.push(item.number);
      payment.push(item.amount + " " + item.currency);
      payment.push(item.description);
      payment.push(item.updated_on);
      payment.push(item.status);

      // Loop through the values and create table cells
      payment.forEach((item) => {
        let td = document.createElement("td");
        td.innerText = item; // Set the value as the text of the table cell
        tr.appendChild(td); // Append the table cell to the table row
      });
      table.getElementsByTagName("tbody")[0].appendChild(tr); // Append the table row to the table
    });
  }}




function insertNameAndSurname() {
  const nameSurnameElement = document.getElementById("name-surname");
  nameSurnameElement.innerText = `${surname} ${name}`;
}

