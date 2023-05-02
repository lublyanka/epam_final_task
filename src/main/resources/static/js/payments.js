const urlPayments = "/api/payment/all";
const urlPaymentsAdmin = "/api/admin/payments/all";
const urlAccounts = "/api/account/all?isBlocked=false";
const addPaymentUrl = "/api/payment/create";

const select = document.getElementById("account");
const currencyElement = document.getElementById("currency");
const addPaymentButton = document.getElementById("addPaymentButton");


document.addEventListener('DOMContentLoaded', function () {
  var decodedToken = getDecodedToken(localStorage.token)
  if (isUserAdmin(decodedToken)) {
    loadPayments(urlPaymentsAdmin);
    hideElement(addPaymentButton);
  }
  else {
    loadPayments(urlPayments);
    loadAccountsWithStatus(urlAccounts);
    var elems = document.querySelectorAll('.modal');
    var instances = M.Modal.init(elems, options);
    // elems = document.querySelectorAll('.collapsible');
    // var collapsibles = M.Collapsible.init(elems, options);
    setTimeout(() => {
      elems = document.querySelectorAll('select');
      var selects = M.FormSelect.init(elems, options);
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
  currencyElement.labels[0].setAttribute("class", "active");
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
  } else {
    await insertPlainErrorMessage(response);
  }
}
