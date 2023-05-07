async function login() {
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

    if (response.status===200) {
      const token = await response.text();
      localStorage.setItem("token", token);
      M.toast({html: 'Successfully logged in!', displayLength: 2000});
      setTimeout(() => {
            window.location.href = "/dashboard";
          }, "2000");
    }     
    else {
      await insertTestErrorMessageFromResponse(response);
    }
  }
  else {
    insertErrorMessage(translations["fieldsNotFilled"]);
    return;
  };
};