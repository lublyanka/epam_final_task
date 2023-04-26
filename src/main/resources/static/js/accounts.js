const urlAccounts = "/api/account/all";


document.addEventListener('DOMContentLoaded', function () {
  loadAccounts(urlAccounts);
  var elems = document.querySelectorAll('.modal');
  var instances = M.Modal.init(elems, options);
});


async function addAccount() {

  const addAccountUrl="/api/account/create";
  var name = document.getElementById("name").value;
  var number = document.getElementById("number").value;
  var currency = document.getElementById("currency").value;
  var data = {
    name: name,
    number: number,
    currency: currency,
  };

  const response = await fetch(addAccountUrl, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': localStorage.token
    },
    body: JSON.stringify(data)
  });

  if (response.status === 200) {
    if(localStorage.language=="es")
    M.toast({html: 'Cuenta añadida!', displayLength: 3000});
    else
    M.toast({html: 'Account added!', displayLength: 3000});
    setTimeout(() => {
      window.location.href = "#!";
    }, "2000");
    loadAccounts(urlAccounts);
  } else {
    const text = await response.text();
    document.getElementById('response-message').innerText = text;
  }
}