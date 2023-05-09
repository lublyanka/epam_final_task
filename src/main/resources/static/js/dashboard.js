const urlProfile = "/api/auth/profile";
const urlAccountsDash = "/api/account/all?page=0&size=3";
const urlPaymentsDash = "/api/payment/all?sortBy=updatedOn&page=0&size=3";
const urlAccountsAdminDash = "/api/admin/accounts/all?page=0&size=3";
const urlPaymentsAdminDash = "/api/admin/payments/all?page=0&size=3";
const urlUsersDash = "/api/admin/users/all?page=0&size=3";

const addPaymentButton = document.getElementById("addPaymentButton");
const addAccountButton = document.getElementById("addAccountButton");

document.addEventListener('DOMContentLoaded', function () {
  loadProfile();
  addShowAllLink("accounts");
  addShowAllLink("payments");
  hideElement(addPaymentButton);
  hideElement(addAccountButton);

  var decodedToken = getDecodedToken(localStorage.token)
  if (isUserAdmin(decodedToken)) {
    addShowAllLink("users");
    loadAccounts(urlAccountsAdminDash);
    loadPayments(urlPaymentsAdminDash);
    loadUsers(urlUsersDash);
  }
  else {
    loadAccounts(urlAccountsDash);
    loadPayments(urlPaymentsDash);
  }

  var list = document.getElementsByClassName("sort-arrow");

  Array.prototype.forEach.call(list, elem => {
    hideElement(elem);
    elem.parentElement.style="pointer-events: none;"
  });
});

async function loadProfile() {
  const response = await getGetResponse(urlProfile);
  if (response.ok) {
    var jsonData = await getJSONData(response);
    insertNameAndSurname(jsonData);
  }
}

function insertNameAndSurname(jsonData) {
  var surname;
  var name;
  surname = jsonData.surname;
  name = jsonData.name;
  const nameSurnameElement = document.getElementById("name-surname");
  nameSurnameElement.innerText = `${surname} ${name}`;
}

