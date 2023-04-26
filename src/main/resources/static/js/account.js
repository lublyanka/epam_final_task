const accountId = window.location.pathname.split("/")[2];
const url = "/api/account/";
const urlAccount = url + accountId

document.addEventListener('DOMContentLoaded', function () {
  loadAccount(urlAccount);
  loadCards(urlAccount + "/cards");
  var elems = document.querySelectorAll('.modal');
  var modals = M.Modal.init(elems, options);
  elems = document.querySelectorAll('.collapsible');
  var collapsibles = M.Collapsible.init(elems, options);
});

async function loadAccount(url) {
  const response = await fetch(url, {
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
    insertAccount(jsonData);
  }
  if (response.status === 404) {
    window.location.href = "/404";
  }
}

function insertAccount(jsonData) {
  const fields = ["name", "number", "updatedOn"];
  fields.forEach(field => {
    const element = document.getElementById(field);
    field === "updatedOn" ? element.innerHTML = transformData(jsonData[field]) : element.innerHTML = jsonData[field]
  });
  var element = document.getElementById("c-balance");
  element.innerHTML = jsonData.currentBalance + " " + jsonData.currency;
  if (jsonData.blocked == true) {
    var elem = document.getElementById("unblockAccount");
    elem.removeAttribute("style");
    elem = document.getElementById("blockAccount");
    elem.setAttribute("style", "display:none");
    if (jsonData.requested == true) {
      elem = document.getElementById("unblockAccount").firstElementChild.firstElementChild;
      elem.getAttribute("class");
      elem.setAttribute("class", elem.getAttribute("class") + " disabled");
    }
  }
}

async function loadCards(url) {
  const response = await fetch(url, {
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
    var table = document.getElementById("cards");
    table.removeAttribute("style");
    var elem = document.getElementById("no-cards");
    elem.setAttribute("style", "display:none");
    elem = document.getElementById("addCard")
    elem.setAttribute("style", "display:none");
    insertCards(jsonData);
  }

}

function insertCards(jsonData) {
  jsonData.forEach((item) => {
    var element = document.getElementById("cardTitle");
    element.innerHTML = item.cardTitle;
    var element = document.getElementById("cardHolder");
    element.innerHTML = item.cardHolder;
    element = document.getElementById("cardNumber");
    element.innerHTML = "••••" + item.cardNumber.substr(- 4);
    element = document.getElementById("cardType");
    element.innerHTML = item.cardType;
    element = document.getElementById("dueDate");
    element.innerHTML = item.month + "/" + item.year;
  });
}

async function addCard() {
  const addCardUrl = "/api/account/addCard";
  var cardNumber = document.getElementById("cardNumberReg").value;
  var cardType = document.getElementById("cardTypeReg").value;
  var month = document.getElementById("monthReg").value;
  var year = document.getElementById("yearReg").value;
  var cardHolder = document.getElementById("cardHolderReg").value;
  var cardTitle = document.getElementById("cardTitleReg").value;
  var data = {
    cardNumber: cardNumber,
    accountId: accountId,
    month: month,
    year: year,
    cardHolder: cardHolder,
    cardType: cardType,
    cardTitle: cardTitle
  };

  const response = await fetch(addCardUrl, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': localStorage.token
    },
    body: JSON.stringify(data)
  });

  if (response.status === 200) {
    if (localStorage.language == "es")
      M.toast({ html: 'Tarjeta añadida!', displayLength: 3000 });
    else
      M.toast({ html: 'Card added!', displayLength: 3000 });
    /*setTimeout(() => {
      window.location.href = "#!";
    }, "2000");*/
    M.Modal.getInstance(document.getElementById('modal1')).close()
    loadCards(urlAccount + "/cards");
  } else {
    const text = await response.text();
    document.getElementById('response-message').innerText = text;
  }
}

async function blockAccount() {
  let url = urlAccount + "/block";

  const response = await fetch(url, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': localStorage.token
    },
  });

  if (response.status === 204) {
    if (localStorage.language == "es")
      M.toast({ html: 'Cuenta bloqueada!', displayLength: 3000 });
    else
      M.toast({ html: 'Account blocked!', displayLength: 3000 });
    /*setTimeout(() => {
      window.location.href = "#!";
    }, "2000");*/
    M.Modal.getInstance(document.getElementById('block')).close()
    var elem = document.getElementById("unblockAccount");
    elem.removeAttribute("style");
    elem = document.getElementById("blockAccount");
    elem.setAttribute("style", "display:none");
  } else {
    const text = await response.text();
    document.getElementById('response-message').innerText = text;
  }
}



async function unblockAccount() {
  let url = urlAccount + "/unblock";

  const response = await fetch(url, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': localStorage.token
    },
  });

  if (response.status === 204) {
    if (localStorage.language == "es")
      M.toast({ html: 'Se envía la solicitud de desbloqueo de cuenta.', displayLength: 3000 });
    else
      M.toast({ html: 'Account unblocked request is submitted.', displayLength: 3000 });
    /*setTimeout(() => {
      window.location.href = "#!";
    }, "2000");*/

    let element = document.getElementById("unblockAccount").firstElementChild.firstElementChild;
    element.getAttribute("class");
    element.setAttribute("class", element.getAttribute("class") + " disabled");
  } else {
    const text = await response.text();
    document.getElementById('response-message').innerText = text;
  }
}

async function refill() {
  let url = urlAccount + "/refill";
let amount = document.getElementById("refillsum").value;
  var data = {
    amount: amount,
  };

  const response = await fetch(url, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': localStorage.token
    },
    body: document.getElementById("refillsum").value
  });

  if (response.status === 200) {
    var json = await response.json();
    const data = JSON.stringify(json);
    var jsonData = JSON.parse(data);
    insertAccount(jsonData);
    var instance = M.Collapsible.getInstance(document.getElementById("collapsible"));
    instance.close();
    if (localStorage.language == "es")
      M.toast({ html: 'Cuenta recargada!', displayLength: 3000 });
    else
      M.toast({ html: 'Account refilled!', displayLength: 3000 });
    /*setTimeout(() => {
      window.location.href = "#!";
    }, "2000");*/
  } else {
    const text = await response.text();
    M.toast({ html: text, displayLength: 3000 });
  }
}
