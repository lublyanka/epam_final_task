const urlAccounts = "/api/account/all";
const urlAccountsAdmin = "/api/admin/accounts/all";


document.addEventListener('DOMContentLoaded', function () {
  var decodedToken = getDecodedToken(localStorage.token)
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
  var name = document.getElementById("accountName").value;
  var number = document.getElementById("number").value;
  var currency = document.getElementById("currency").value;
  var data = {
    name: name,
    number: number,
    currency: currency,
  };

  const response = getPutResponse(addAccountUrl, JSON.stringify(data));

  if (response.status === 200) {
    if (language === "es")
      M.toast({ html: 'Cuenta añadida!', displayLength: 3000 });
    else
      M.toast({ html: 'Account added!', displayLength: 3000 });
    setTimeout(() => {
      window.location.href = "#!";
    }, "2000");
    loadAccounts(urlAccounts);
  } else {
    await insertTestErrorMessageFromResponse(response);
  }
}

function check() {
  const { accountName, number, currency } = document.getElementById("account-creation");

  function isValidTextString(str) {
    // Allow letters, spaces, apostrophes, and hyphens, as well as Cyrillic and Spanish characters
    return String(str).match("^[a-zA-Z\\u00C0-\\u024F\\u0400-\\u04FF\\u0500-\\u052F\\u1E00-\\u1EFF\\s'’\\-]+$") != null;
  }

  const isValidName = isValidTextString(accountName.value);
  const isValidNumber = /^\d{5,20}$/.test(number.value);
  const isValidCurrency = /^[A-Z]{3}$/.test(currency.value);


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

  if (!currency.value) {
    makeInputFieldInvalid(currency, translations["fieldRequiredError"]);
  } else if (!isValidCurrency) {
    makeInputFieldInvalid(currency, translations["notCurrencyError"]);
  }
  else {
    makeInputFieldValid(currency);
  }

  const submitButton = document.getElementById('saveButton');
  if (!(isValidName && isValidNumber && isValidCurrency))
    submitButton.classList.add("disabled");
  else submitButton.classList.remove("disabled");
}