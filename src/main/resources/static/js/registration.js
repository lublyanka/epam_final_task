const dateOptions = {
  minDate: new Date('1900-01-01T00:00:00'),
  maxDate: new Date(new Date().setFullYear(new Date().getFullYear() - 14)),
  defaultDate: new Date(new Date().setFullYear(new Date().getFullYear() - 14)),
  yearRange: 70
};

document.addEventListener('DOMContentLoaded', function () {
  var elems = document.querySelectorAll('.datepicker');
  var optionsLang = localStorage.language === "es" ? getDatepickerOptions('es') : getDatepickerOptions('en');
  Object.assign(dateOptions, optionsLang)
  var instances = M.Datepicker.init(elems, dateOptions);
});

async function registerUser() {
  var url = "/api/auth/registration";
  const { firstname, surname, email, password, phone, birthDate } = document.getElementById("registration-form");
  let timestamp = transformDataToTimestamp(birthDate.value);
  const data = {
    name: firstname.value.trim(),
    surname: surname.value.trim(),
    email: email.value.trim(),
    password: password.value.trim(),
    phone: phone.value.trim(),
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
    insertTestErrorMessageFromResponse(response);
  }
}

function check() {
  const { firstname, surname, email, password, phone, birthDate } = document.getElementById("registration-form");

  function isValidTextString(str) {
    // Allow letters, spaces, apostrophes, and hyphens, as well as Cyrillic and Spanish characters
    return String(str).match("^[a-zA-Z\\u00C0-\\u024F\\u0400-\\u04FF\\u0500-\\u052F\\u1E00-\\u1EFF\\s'’\\-]+$");
  }

  const passPattern = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z]{8,}$/;
  const isValidFirstName = isValidTextString(firstname.value);
  const isValidSurname = isValidTextString(surname.value);
  const isValidName = isValidFirstName && isValidSurname;
  //const isValidEmail = /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email.value);
  const isValidPassword = passPattern.test(password.value);
  const isValidBirthDate = /^(\d{2})\/(\d{2})\/(\d{4})$/.test(birthDate.value);
  const isValidPhone = /^\d{5,20}$/.test(phone.value);


  if (!firstname.value) {
    makeInputFieldInvalid(firstname, translations["fieldRequiredError"]);
  } else if (!isValidFirstName) {
    makeInputFieldInvalid(firstname, translations["onlyTextError"]);
  }
  else {
    makeInputFieldValid(firstname);
  }

  if (!surname.value) {
    makeInputFieldInvalid(surname, translations["fieldRequiredError"]);
  } else if (!isValidSurname) {
    makeInputFieldInvalid(surname, translations["onlyTextError"]);
  }
  else {
    makeInputFieldValid(surname);
  }

  if (!email.value) {
    makeInputFieldInvalid(email, translations["fieldRequiredError"]);
  }

  if (!password.value) {
    makeInputFieldInvalid(password, translations["fieldRequiredError"]);
  } else if (!isValidPassword) {
    makeInputFieldInvalid(password, translations["passwordFormatError"]);
  }
  else {
    makeInputFieldValid(password);
  }

  if (!birthDate.value) {
    makeInputFieldInvalid(birthDate, translations["fieldRequiredError"]);
  } else if (!isValidBirthDate) {
    makeInputFieldInvalid(birthDate, translations["dateFormatError"]);
  }
  else {
    makeInputFieldValid(birthDate);
  }

  if (!phone.value) {
    makeInputFieldInvalid(phone, translations["fieldRequiredError"]);
  } else if (!isValidPhone) {
    makeInputFieldInvalid(phone, translations["onlyNumbersError5-20"]);
  }
  else {
    makeInputFieldValid(phone);
  }

  const submitButton = document.getElementById('registerButton');
  submitButton.disabled = !(isValidName && isValidPassword && isValidBirthDate && isValidPhone);
}

