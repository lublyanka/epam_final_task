async function registerUser() {
  var url = "/api/auth/registration";
  var name = document.getElementById("name").value;
  var surname = document.getElementById("surname").value;
  var email = document.getElementById("email").value;
  var password = document.getElementById("password").value;
  var phone = document.getElementById("phone").value;
  var data = {
    name: name,
    surname: surname,
    password: password,
    email: email,
    password: password,
    phone: phone
  };

  const response = await fetch(url, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(data)
  });

  if (response.status === 200) {
    M.toast({html: 'Successfully registered!', displayLength: 2000});
    setTimeout(() => {
      window.location.href = "/login";
    }, "2000");
  } else {
    const text = await response.text();
    console.log(text);
    document.getElementById('response-message').innerText = text;
  }
}