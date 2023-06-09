const urlAccounts = "/api/account/all?size=10";
const urlAccountsAdmin = "/api/admin/accounts/all?size=10";
var decodedToken;

document.addEventListener('DOMContentLoaded', function () {
  decodedToken = getDecodedToken(localStorage.token)
  if (isUserAdmin(decodedToken)) {
    loadAccounts(urlAccountsAdmin);
    hideElement(document.getElementById("addAccountButton"));
  }
  else {
    loadAccounts(urlAccounts);
    loadCurrencies();
    var elems = document.querySelectorAll('.modal');
    var modals = M.Modal.init(elems);
    setTimeout(() => {
      elems = document.querySelectorAll('select');
      var selects = M.FormSelect.init(elems);
    }, "1000");
  }
});


async function addAccount() {
  const addAccountUrl = "/api/account/create";
  var name = document.getElementById("accountName").value.trim();
  var number = document.getElementById("number").value.trim();
  var currency = document.getElementById("currencySelect").value.trim();
  var data = {
    name: name,
    number: number,
    currency: currency,
  };

  const response = await getPutResponse(addAccountUrl, data);

  if (response.status === 200) {
    if (localStorage.language === "es")
      M.toast({ html: 'Cuenta añadida!', displayLength: 3000 });
    else
      M.toast({ html: 'Account added!', displayLength: 3000 });
    setTimeout(() => {
      window.location.href = "#!";
    }, "2000");
    loadAccounts(urlAccounts);
  } else if(response.status != 204){
    await insertTestErrorMessageFromResponse(response);
  }
}

function check() {
  const { accountName, number, currencySelect } = document.getElementById("account-creation");
  function isValidTextString(str) {
    // Allow letters, spaces, apostrophes, and hyphens, as well as Cyrillic and Spanish characters
    return String(str).match("^[a-zA-Z\\u00C0-\\u024F\\u0400-\\u04FF\\u0500-\\u052F\\u1E00-\\u1EFF\\s'’\\-]+$") != null;
  }

  const isValidName = isValidTextString(accountName.value);
  const isValidNumber = /^\d{5,20}$/.test(number.value);
  const isValidCurrency = /^[A-Z]{3}$/.test(currencySelect.value);


  if (!accountName.value) {
    makeInputFieldInvalid(accountName, translations["fieldRequiredError"]);
  } else if (!isValidName) {
    makeInputFieldInvalid(accountName, translations["onlyTextError"]);
  }
  else {
    makeInputFieldValid(accountName);
  }

  if (!number.value) {
    makeInputFieldInvalid(number, translations["fieldRequiredError"]);
  } else if (!isValidNumber) {
    makeInputFieldInvalid(number, translations["onlyNumbersError5-20"]);
  }
  else {
    makeInputFieldValid(number);
  }

  if (!currencySelect.value) {
    makeInputFieldInvalid(currencySelect, translations["fieldRequiredError"]);
  } else if (!isValidCurrency) {
    makeInputFieldInvalid(currencySelect, translations["notCurrencyError"]);
  }
  else {
    makeInputFieldValid(currencySelect);
  }

  const submitButton = document.getElementById('saveButton');
  if (!(isValidName && isValidNumber && isValidCurrency))
    submitButton.classList.add("disabled");
  else submitButton.classList.remove("disabled");
}

function sort(element) {
  var sortByElement = element.parentElement.id;
  var sortByColumn;
  switch (sortByElement) {
    case 'accountNumberT':
      sortByColumn = "number";
      break;
    case 'accountNameT': sortByColumn = "name";
      break;
    case 'accountCurrentBalanceT': sortByColumn = "currentBalance";
      break;
  }

  var order = element.classList.contains("asc") ? "DESC" : "ASC";
  if (isUserAdmin(decodedToken))
    loadAccounts(urlAccountsAdmin, sortByColumn, order);
  else
    loadAccounts(urlAccounts, sortByColumn, order);

  var sortBy;
  switch (sortByColumn) {
    case 'number':
      sortBy = "accountNumberT";
      break;
    case 'name': sortBy = "accountNameT";
      break;
    case 'currentBalance': sortBy = "accountCurrentBalanceT";
      break;
  }

  reloadSortingArrows(sortBy, order, element);

}