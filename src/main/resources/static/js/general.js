// Check token expiration every minute
const token = localStorage.token;
const intervalTime = 1 * 60 * 1000; // 15 minutes in milliseconds
setInterval(function () {
    if (token != null)
        checkTokenExpiration(token)
}, intervalTime);

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

async function getGetResponse(url) {
    return await fetch(url, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': localStorage.token
        }
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
    let myDate = data.split(".");
    var newTimestamp = new Date(myDate[2], myDate[1], myDate[0]);
    return newTimestamp.getTime();
}

function isUserAdmin(decodedToken) {
    var roles = decodedToken.roles;
    var isAdmin = false;
    roles.forEach((x) => {
        if (x.authority == "ROLE_ADMIN") isAdmin=  true; 
    });
    return isAdmin;
}

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

async function insertPlainErrorMessage(response) {
    const text = await response.text();
    let errorElement = document.getElementById('response-message');
    errorElement.innerText = text;
    errorElement.classList.add("card-panel");
    errorElement.classList.add("orange");
    errorElement.classList.add("darken");
}

function hideElement(elem) {
    elem.setAttribute("style", "display:none");
  }

  function createAccountLink(id, number) {
    let link = document.createElement("a");
    link.href = "/account/" + id; // set the href of the link to the account id
    link.innerText = number; // set the text of the link to the account number
    return link;
  }