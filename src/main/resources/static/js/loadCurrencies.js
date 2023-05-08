async function loadCurrencies() {
    const url = "/api/dict/currencies";
    const response = await getGetResponse(url);
    if (response.status === 200) {
        var jsonData = await getJSONData(response);
        // var ul = document.getElementById("no-accounts");
        // ul.setAttribute("style", "display:none");
        insertCurrency(jsonData);
    }
}

function insertCurrency(jsonData) {
    var select = document.getElementById("currencySelect");
    jsonData.forEach((item) => {
        let option = document.createElement("option");
        option.innerText = item.name;
        option.setAttribute("value", item.code)
        select.appendChild(option)
    });
}