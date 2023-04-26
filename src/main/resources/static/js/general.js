// Check token expiration every minute
const token = localStorage.token;
const intervalTime = 1 * 60 * 1000; // 15 minutes in milliseconds
setInterval(function () { checkTokenExpiration(token) }, intervalTime);


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
        if (localStorage.language == "es")
            M.toast({ html: 'Has cerrado sesión.', displayLength: 8000 });
        else
            M.toast({ html: 'You logged out.', displayLength: 8000 });
    })
});

function transformData(timestamp) {
    let date = new Date(timestamp);
    const formattedDate = date.toLocaleDateString('en-GB') + " " + date.toLocaleTimeString('en-GB');
    return formattedDate;
}


function isTokenExpired(token) {
    const decodedToken = JSON.parse(atob(token.split('.')[1]));
    const expirationTime = decodedToken.exp < 9999999999 ? decodedToken.exp * 1000 : decodedToken.exp; // check if exp is in seconds or milliseconds
    const currentTime = new Date().getTime();
    return currentTime > expirationTime;
}

function checkTokenExpiration(token) {
    if (isTokenExpired(token)) {
        localStorage.removeItem('token');
        document.getElementById('login').style.display = 'block';
        document.getElementById('profile').style.display = 'none';

        if (localStorage.language == "es")
            M.toast({ html: 'La sesión expiró.', displayLength: 8000 });
        else
            M.toast({ html: 'Session expired.', displayLength: 8000 });
    }
}

