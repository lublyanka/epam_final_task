async function login() {
  event.preventDefault();
  var url = "/api/auth/login";
  var username = document.getElementById('username').value;
  var password = document.getElementById('password').value;
  var data = {
    "email": username,
    "password": password
  };

  if (username && password) {
    const response = await fetch(url, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(data)
    })

    console.log(response);

    if (response.ok) {
      const token = await response.text();
      localStorage.setItem("token", token);
      window.location.href='/dashboard';
    } else {
      const text = await response.text();
      console.log(text);
      document.getElementById('response-message').innerText = text;
    }
  }
  else {
    document.getElementById('response-message').innerText = "Please fill in both fields.";
    return;
  };


};