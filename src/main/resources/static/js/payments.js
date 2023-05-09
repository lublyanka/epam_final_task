const urlPayments = "/api/payment/all?size=10";
const urlPaymentsAdmin = "/api/admin/payments/all?size=10";
const urlAccounts = "/api/account/all?isBlocked=false";
const addPaymentUrl = "/api/payment/create";

const select = document.getElementById("account");
const currencyElement = document.getElementById("currency");
const addPaymentButton = document.getElementById("addPaymentButton");
var decodedToken;

document.addEventListener('DOMContentLoaded', function () {
  decodedToken = getDecodedToken(localStorage.token)
  if (isUserAdmin(decodedToken)) {
    loadPayments(urlPaymentsAdmin);
    hideElement(addPaymentButton);
  }
  else {
    loadPayments(urlPayments);
    loadAccountsWithStatus(urlAccounts);
    var elems = document.querySelectorAll('.modal');
    var instances = M.Modal.init(elems);
    setTimeout(() => {
      elems = document.querySelectorAll('select');
      var selects = M.FormSelect.init(elems);
    }, "1000");
  }
});

select.addEventListener("change", () => {
  const selectedOption = select.options[select.selectedIndex];
  const accountId = selectedOption.value;
  let currency;
  accountJSONData.content.forEach((item) => {
    if (item.id === accountId) {
      currency = item.currency;
      return;
    }
  });
  currencyElement.value = currency;
  currencyElement.labels[0].classList.add("active");
});


async function addPayment() {
  var accountId = document.getElementById("account").value
  var desc = document.getElementById("desc").value;
  var sum = document.getElementById("sum").value;
  var number = document.getElementById("number").value;
  var currency = document.getElementById("currency").value;
  var data = {
    accountId: accountId,
    description: desc,
    amount: sum,
    number: number,
    currency: currency,
  };

  const response = await getPutResponse(addPaymentUrl, data);

  if (response.status === 200) {
    if (localStorage.language === "es")
      M.toast({ html: 'Pago añadido!', displayLength: 3000 });
    else
      M.toast({ html: 'Payment added!', displayLength: 3000 });
    setTimeout(() => {
      window.location.href = "#!";
    }, "2000");
    loadPayments(urlPayments);
    M.Modal.getInstance(document.getElementById("addPayment")).close();
    //document.getElementById("payment-creation").reset();
  } else {
    await insertTestErrorMessageFromResponse(response);
  }
}


function check() {
  const { account, sum, number, currency } = document.getElementById("payment-creation");

  var isValidSum = /\d{1,8}\.{0,1}\d{1,2}$/.test(sum.value);
  var isValidNumber = /^\d{5,20}$/.test(number.value);
  var isAccountSelected = account.value

  if (!sum.value) {
    makeInputFieldInvalid(sum, translations["fieldRequiredError"]);
  } else if (!isValidSum) {
    makeInputFieldInvalid(sum, translations["sumNumberFormatError"]);
  }
  else {
    makeInputFieldValid(sum);
  }

  if (!number.value) {
    makeInputFieldInvalid(number, translations["fieldRequiredError"]);
  } else if (!isValidNumber) {
    makeInputFieldInvalid(number, translations["onlyNumbersError5-20"]);
  }
  else {
    makeInputFieldValid(number);
  }

  setTimeout(() => {
    var isValidCurrency = /^[A-Z]{3}$/.test(currency.value);
    if (!currency.value) {
      makeInputFieldInvalid(currency, translations["fieldRequiredError"]);
    } else if (!isValidCurrency) {
      makeInputFieldInvalid(currency, translations["notCurrencyError"]);
    }
    else {
      makeInputFieldValid(currency);
    }

    const submitButton = document.getElementById('saveButton');
    if (!(isAccountSelected && isValidNumber && isValidSum && isValidCurrency))
      submitButton.classList.add("disabled");
    else submitButton.classList.remove("disabled");

  }, "400");


}

function sort(element) {
  var sortByElement = element.parentElement.id;
  var sortByColumn;
  switch (sortByElement) {
    case 'numberH':
      sortByColumn = "number";
      break;
    case 'sumH': sortByColumn = "amount";
      break;
    case 'dateH': sortByColumn = "updatedOn";
      break;
  }

  var order = element.classList.contains("asc") ? "DESC" : "ASC";
  if (isUserAdmin(decodedToken))
    loadPayments(urlPaymentsAdmin, sortByColumn, order);
  else
    loadPayments(urlPayments, sortByColumn, order);

  var sortBy;
  switch (sortByColumn) {
    case 'number':
      sortBy = "numberH";
      break;
    case 'amount': sortBy = "sumH";
      break;
    case 'updatedOn': sortBy = "dateH";
      break;
  }

  var list = document.getElementsByClassName("sort-arrow");

  Array.prototype.forEach.call(list, elem => {
    elem.innerHTML = "arrow_downward";
    elem.classList.add("blue-grey-text");
    elem.classList.add("text-lighten-4");
  });

  var sortedElem = document.getElementById(sortBy);
  sortedElem = sortedElem.getElementsByClassName("sort-arrow")[0];

  if (order === "ASC") {
    sortedElem.innerHTML = "arrow_downward";
    element.classList.add("asc");
    element.classList.remove("desc")
  }
  else {
    sortedElem.innerHTML = "arrow_upward";
    element.classList.add("desc");
    element.classList.remove("asc");
  }

  sortedElem.classList.remove("blue-grey-text")
  sortedElem.classList.remove("text-lighten-4")

}