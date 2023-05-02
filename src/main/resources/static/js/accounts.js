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
    var modals = M.Modal.init(elems, options);
    setTimeout(() => {
      elems = document.querySelectorAll('select');
      var selects = M.FormSelect.init(elems, options);
    }, "1000");
  }
});


async function addAccount() {

  const addAccountUrl = "/api/account/create";
  var name = document.getElementById("name").value;
  var number = document.getElementById("number").value;
  var currency = document.getElementById("currency").value;
  var data = {
    name: name,
    number: number,
    currency: currency,
  };

  const response = await fetch(addAccountUrl, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': localStorage.token
    },
    body: JSON.stringify(data)
  });

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
    await insertPlainErrorMessage(response);
  }
}