const urlProfile = "/api/auth/profile";
const urlAccountsDash = "/api/account/all?page=0&size=3";
const urlPaymentsDash = "/api/payment/all?page=0&size=3";
const urlAccountsAdminDash = "/api/admin/accounts/all?page=0&size=3";
const urlPaymentsAdminDash = "/api/admin/payments/all?page=0&size=3";
const urlUsersDash = "/api/admin/users/all?page=0&size=3";

const addPaymentButton = document.getElementById("addPaymentButton");

document.addEventListener('DOMContentLoaded', function () {
  loadProfile();
  addShowAllLink("users");
  addShowAllLink("payments");
  hideElement(addPaymentButton);

  var decodedToken = getDecodedToken(localStorage.token)
  if (isUserAdmin(decodedToken)) {
    loadAccounts(urlAccountsAdminDash);
    loadPayments(urlPaymentsAdminDash);
    loadUsers(urlUsersDash);
  }
  else {
    loadAccounts(urlAccountsDash);
    loadPayments(urlPaymentsDash);
  }
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

