const accountId = window.location.pathname.split("/")[2];
const urlAccount = `/api/account/${accountId}`;
var urlPayments = urlAccount + "/payments";
const urlCard = urlAccount + "/cards";
var urlUnblock = urlAccount + "/unblock";
var urlBlock = urlAccount + "/block";
const urlRefill = urlAccount + "/refill";

var decodedToken;
let blockAccountButton;
let unblockAccountButton;
let collapsibleRefill;
let cardActionsDiv;
let addCardDiv;


document.addEventListener('DOMContentLoaded', function () {
  decodedToken = getDecodedToken(localStorage.token);
  blockAccountButton = document.getElementById("blockAccount");
  unblockAccountButton = document.getElementById("unblockAccount");
  collapsibleRefill = document.getElementById("collapsible");
  cardActionsDiv = document.getElementById("card-actions");
  addCardDiv = document.getElementById("addCard")
  loadAccount(urlAccount);
  loadCards(urlCard);
  loadPayments(urlPayments)

  var elems = document.querySelectorAll('.modal');
  var modals = M.Modal.init(elems, options);
  elems = document.querySelectorAll('.collapsible');
  var collapsibles = M.Collapsible.init(elems, options);

  if (isUserAdmin(decodedToken)) {
    hideElement(collapsibleRefill);
    hideElement(cardActionsDiv);
    hideElement(addCardDiv);
    //urlBlock = `/api/admin/accounts/${accountId}/block`;
    urlUnblock = `/api/admin/accounts/${accountId}/unblock`;
    urlPayments = `/api/admin/accounts/${accountId}/payments`;
  }
});

async function loadAccount(url) {
  const response = await getGetResponse(url);
  if (response.status === 200) {
    var jsonData = await getJSONData(response);
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
    field === "updatedOn" ? element.innerHTML = transformTimestampToData(jsonData[field], "true") : element.innerHTML = jsonData[field]
  });
  var element = document.getElementById("c-balance");
  element.innerHTML = jsonData.currentBalance + " " + jsonData.currency;
  if (jsonData.blocked === true) {
    var elem = unblockAccountButton;
    elem.removeAttribute("style");
    elem = blockAccountButton;
    hideElement(elem);;

    if (jsonData.requested === true && !isUserAdmin(decodedToken)) {
      elem = unblockAccountButton.firstElementChild.firstElementChild;
      elem.getAttribute("class");
      elem.setAttribute("class", elem.getAttribute("class") + " disabled");
      elem.firstElementChild.innerHTML = "access_time";
      document.getElementById("requestSent").removeAttribute("style");
    }
    if (jsonData.requested === true)
      document.getElementById("requestSent").removeAttribute("style");
  }
}

async function loadCards(url) {
  const response = await await getGetResponse(url);
  if (response.status === 200) {
    var jsonData = await getJSONData(response);
    document.getElementById("cards").removeAttribute("style");
    hideElement(document.getElementById("no-cards"));
    hideElement(addCardDiv)
    insertCards(jsonData);
  }
}

function insertCards(jsonData) {
  jsonData.forEach(item => {
    document.getElementById("cardTitle").innerHTML = item.cardTitle;
    document.getElementById("cardHolder").innerHTML = item.cardHolder;
    document.getElementById("cardNumber").innerHTML = `••••${item.cardNumber.substr(-4)}`;
    document.getElementById("cardType").innerHTML = item.cardType;
    document.getElementById("dueDate").innerHTML = `${item.month}/${item.year}`;
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
    if (localStorage.language === "es")
      M.toast({ html: 'Tarjeta añadida!', displayLength: 3000 });
    else
      M.toast({ html: 'Card added!', displayLength: 3000 });
    /*setTimeout(() => {
      window.location.href = "#!";
    }, "2000");*/
    M.Modal.getInstance(document.getElementById('modal1')).close()
    loadCards(urlAccount + "/cards");
  } else {
    await insertPlainErrorMessage(response);
  }
}

async function blockAccount() {
  const response = await getPutResponse(urlBlock);

  if (response.status === 204) {
    if (localStorage.language === "es")
      M.toast({ html: 'Cuenta bloqueada!', displayLength: 3000 });
    else
      M.toast({ html: 'Account blocked!', displayLength: 3000 });
    /*setTimeout(() => {
      window.location.href = "#!";
    }, "2000");*/
    M.Modal.getInstance(document.getElementById('block')).close()
    unblockAccountButton.removeAttribute("style");
    hideElement(blockAccountButton);
  } else {
    insertPlainErrorMessage(response);
  }
}

async function unblockAccount() {
  const response = await getPutResponse(urlUnblock)
  if (isUserAdmin(decodedToken)) {
    if (response.status === 204) {
      if (localStorage.language === "es")
        M.toast({ html: 'Cuenta desbloqueda.', displayLength: 3000 });
      else
        M.toast({ html: 'Account unblocked.', displayLength: 3000 });
      blockAccountButton.removeAttribute("style");
      hideElement(unblockAccountButton)
    } else {
      insertPlainErrorMessage(response);
    }
  }
  else {
    if (response.status === 204) {
      if (localStorage.language === "es")
        M.toast({ html: 'Se envía la solicitud de desbloqueo de cuenta.', displayLength: 3000 });
      else
        M.toast({ html: 'Account unblocked request is submitted.', displayLength: 3000 });
      let element = document.getElementById("unblockAccount").firstElementChild.firstElementChild;
      element.setAttribute("class", element.getAttribute("class") + " disabled");
    } else {
      insertPlainErrorMessage(response);
    }
  }
}

async function refill() {
  let amount = document.getElementById("refillsum").value;

  const response = await fetch(urlRefill, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': localStorage.token
    },
    body: amount
  });

  if (response.status === 200) {
    var jsonData = await getJSONData(response);
    insertAccount(jsonData);
    M.Collapsible.getInstance(collapsibleRefill).close();
    if (localStorage.language === "es")
      M.toast({ html: 'Cuenta recargada!', displayLength: 3000 });
    else
      M.toast({ html: 'Account refilled!', displayLength: 3000 });
  } else {
    const text = await response.text();
    M.toast({ html: text, displayLength: 3000 });
  }
}
