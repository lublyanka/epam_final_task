// Check token expiration every minute
const token = localStorage.token;
const intervalTime = 1 * 60 * 1000; // 15 minutes in milliseconds
setInterval(function () {
    if (token != null)
        checkTokenExpiration(token)
}, intervalTime);


//Navbar activation changes
document.addEventListener('DOMContentLoaded', function () {

    if (localStorage.token != null) {
        document.getElementById('profile').style.display = 'block';
        document.getElementById('login').style.display = 'none';
    }
    else {
        document.getElementById('login').style.display = 'block';
        document.getElementById('profile').style.display = 'none';
    }

    document.getElementById('logout').addEventListener('click', function () {
        localStorage.removeItem('token');
        document.getElementById('login').style.display = 'block';
        document.getElementById('profile').style.display = 'none';
        if (localStorage.language === "es")
            M.toast({ html: 'Has cerrado sesión.', displayLength: 8000 });
        else
            M.toast({ html: 'You logged out.', displayLength: 8000 });
    })
});

//REST functions

async function getGetResponse(url, body) {
    return await fetch(url, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': localStorage.token
        },
        body: body
    });
}

async function getPostResponse(url, bodyData) {
    return await fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': localStorage.token
        },
        body: JSON.stringify(bodyData)
    });
}

async function getPutResponse(url, bodyData) {
    return await fetch(url, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': localStorage.token
        },
        body: JSON.stringify(bodyData)
    });
}

async function getJSONData(response) {
    var json = await response.json();
    const data = JSON.stringify(json);
    var jsonData = JSON.parse(data);
    return jsonData;
}


function addSortingToLink(url, sortBy, orderBy) {
    if (sortBy)
        url = url + `&sortBy=${sortBy}`;
    if (orderBy)
        url = url + `&sortOrder=${orderBy}`;
    return url;
}

//Validation functions

function isUserAdmin(decodedToken) {
    var roles = decodedToken.roles;
    var isAdmin = false;
    roles.forEach((x) => {
        if (x.authority == "ROLE_ADMIN") isAdmin = true;
    });
    return isAdmin;
}

async function isValidCreditCardNumber(cardNumber) {
    var urlCardValidation = "/api/card/isValid"

    var response = await fetch(urlCardValidation, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': localStorage.token
        },
        body: cardNumber
    });
    if (response.status === 200) {
        var isValid = await response.text();
        if (isValid === "true")
            return true
    }
    return false;
}

//Token checker functions

function isTokenExpired(token) {
    const decodedToken = getDecodedToken(token);
    const expirationTime = decodedToken.exp < 9999999999 ? decodedToken.exp * 1000 : decodedToken.exp; // check if exp is in seconds or milliseconds
    const currentTime = new Date().getTime();
    return currentTime > expirationTime;
}

function getDecodedToken(token) {
    return JSON.parse(atob(token.split('.')[1]));
}

function checkTokenExpiration(token) {
    if (isTokenExpired(token)) {
        localStorage.removeItem('token');
        document.getElementById('login').style.display = 'block';
        document.getElementById('profile').style.display = 'none';

        if (localStorage.language === "es")
            M.toast({ html: 'La sesión expiró.', displayLength: 8000 });
        else
            M.toast({ html: 'Session expired.', displayLength: 8000 });

        setTimeout(() => {
            window.location.href = "/";;
        }, 8000);
    }
}

// Data transforming functions

function transformTimestampToData(timestamp, withTime) {
    let date = new Date(timestamp);
    let formattedDate;
    if (withTime === "true")
        formattedDate = date.toLocaleDateString('en-GB') + " " + date.toLocaleTimeString('en-GB');
    else
        formattedDate = date.toLocaleDateString('en-GB');
    return formattedDate;
}

function transformDataToTimestamp(data) {
    let myDate = data.split("/");
    var newTimestamp = new Date(myDate[2], myDate[1], myDate[0]);
    return newTimestamp.getTime();
}

//Functions changing HTML 

async function insertTestErrorMessageFromResponse(response) {
    if (response.status === 500) {
        window.location.href = "/Error500";
        return;
    }

    const text = await response.text();
    insertErrorMessage(text);
}

function insertErrorMessage(text) {
    let errorElement = document.getElementById('response-message');
    errorElement.innerText = text;
    errorElement.classList.add("card-panel");
    errorElement.classList.add("orange");
    errorElement.classList.add("darken");
}

function activatePagination(jsonData, url, func) {
    const container = document.getElementById('pagination');
    if (container != null) {
        var currentPage = jsonData.number;
        var totalPages = jsonData.totalPages;
        generatePagination(currentPage + 1, totalPages, func, url, container);
    }
};

function hideElement(elem) {
    elem.setAttribute("style", "display:none");
}

function createAccountLink(id, number) {
    let link = document.createElement("a");
    link.href = "/account/" + id; // set the href of the link to the account id
    link.innerText = number; // set the text of the link to the account number
    return link;
}

function makeInputFieldValid(element) {
    if (element.nodeName === "SELECT") {
        let helper = element.parentElement.nextElementSibling.nextElementSibling
        helper.innerHTML = ""
    } else {
        element.classList.remove("invalid");
        element.classList.add("valid");
    }
}

function makeInputFieldInvalid(element, dataErrorText) {
    if (element.nodeName === "SELECT") {
        let helper = element.parentElement.nextElementSibling.nextElementSibling
        helper.innerHTML = dataErrorText;
    }
    else {
        element.classList.add("invalid");
        element.classList.remove("valid");
        element.nextElementSibling.setAttribute("data-error", dataErrorText);
    }
}

function addShowAllLink(keyLink) {
    var usersBlock = document.getElementById(keyLink);
    var h4links = usersBlock.getElementsByTagName("h4");
    Array.prototype.forEach.call(h4links, elem => {
        let link = document.createElement("a");
        link.href = `/${keyLink}`;
        link.setAttribute("data-i18n-key", "showAll");
        //link.innerText ; 
        elem.appendChild(link);
    });
}

function reloadSortingArrows(sortBy, order, element) {
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
        element.classList.remove("desc");
    }
    else {
        sortedElem.innerHTML = "arrow_upward";
        element.classList.add("desc");
        element.classList.remove("asc");
    }

    sortedElem.classList.remove("blue-grey-text");
    sortedElem.classList.remove("text-lighten-4");
}