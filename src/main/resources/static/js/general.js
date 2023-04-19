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
    })
});
