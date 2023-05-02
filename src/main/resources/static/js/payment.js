const paymentId = window.location.pathname.split("/")[2];
const url = "/api/payment/";
const urlPayment = url + paymentId;
let checkbox;
let sendPaymentDiv;
let button;
var decodedToken 


document.addEventListener('DOMContentLoaded', function () {
  checkbox = document.getElementById("agreeCheck");
  sendPaymentDiv = document.getElementById("sendPayment");
  button = document.getElementById("sendPaymentButton");
  decodedToken = getDecodedToken(localStorage.token);
  checkbox.addEventListener("change", () => {
    checkbox.checked ? button.classList.remove("disabled") : button.classList.add("disabled");
  });
  loadPayment(urlPayment);
});

async function loadPayment(url) {
  const response = await getGetResponse(url);
  if (response.status === 200) {
    var jsonData = await getJSONData(response);
    insertPayment(jsonData);
  }
  if (response.status === 404) {
    window.location.href = "/404";
  }
};

function insertPayment(jsonData) {
  const fields = ["number", "updatedOn", "status"];
  fields.forEach(field => {
    const element = document.getElementById(field);
    field === "updatedOn" ? element.innerHTML = transformTimestampToData(jsonData[field]) : element.innerHTML = jsonData[field];
  });
  var element = document.getElementById("sum");
  element.innerHTML = jsonData.amount + " " + jsonData.currency;
  element = document.getElementById("account");
  let link = createAccountLink(jsonData.account.id, jsonData.account.number);
  element.appendChild(link);
  if (jsonData.status === "SENT") {
    hideElement(sendPaymentDiv);
    var elem = document.getElementById("status");
    elem.setAttribute("class", "blue-text text-darken-4")
  }

  if (isUserAdmin(decodedToken)) {
    hideElement(sendPaymentDiv);
    hideElement(button);
    hideElement(checkbox);
  }
};


async function sendPayment() {
  let url = urlPayment + "/send";
  const response = await getPutResponse(url);
  if (response.status === 200) {
    var jsonData = await getJSONData(response);
    insertPayment(jsonData);
    if (languageGlobal === "es")
      M.toast({ html: 'Pago enviado!', displayLength: 3000 });
    else
      M.toast({ html: 'Payment sent!', displayLength: 3000 });
  }
  else {
    await insertPlainErrorMessage(response);
  }
}


