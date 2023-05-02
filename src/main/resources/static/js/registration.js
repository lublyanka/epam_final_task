document.addEventListener('DOMContentLoaded', function () {
  var elems = document.querySelectorAll('.datepicker');
  var options = localStorage.language === "es" ? getDatepickerOptions('es') : getDatepickerOptions('en');
  var instances = M.Datepicker.init(elems, options);
});

async function registerUser() {
  var url = "/api/auth/registration";
  const { name, surname, middlename, email, password, phone, birthDate } = document.getElementById("registration-form");
  let timestamp = transformDataToTimestamp(birthDate.value);
  const data = {
    name: name.value,
    surname: surname.value,
    middlename: middlename.value,
    email: email.value,
    password: password.value,
    phone: phone.value,
    birthDate: timestamp
  };


  const response = await fetch(url, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(data)
  });

  if (response.status === 200) {
    M.toast({ html: 'Successfully registered!', displayLength: 2000 });
    setTimeout(() => {
      window.location.href = "/login";
    }, "2000");
  } else {
    insertPlainErrorMessage(response);
  }
}