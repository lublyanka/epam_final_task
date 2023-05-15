const urlProfile = "/api/auth/profile";
const userId = window.location.pathname.split("/")[2];
const urlUser = `/api/admin/users/${userId}`;
const urlBlock = `/api/admin/users/${userId}/block`;
const urlUnblock = `/api/admin/users/${userId}/unblock`;
const urlProfileUpdate = urlProfile + "/update"
var decodedToken;

let blockUserButton;
let unblockUserButton;

document.addEventListener('DOMContentLoaded', function () {
  decodedToken = getDecodedToken(localStorage.token);
  if (isUserAdmin(decodedToken) && window.location.pathname.split("/")[1] === "users")
    loadProfile(urlUser);
  else
    loadProfile(urlProfile);
  var elems = document.querySelectorAll('.datepicker');
  var options = localStorage.language === "es" ? getDatepickerOptions('es') : getDatepickerOptions('en');
  var instances = M.Datepicker.init(elems, options);
  elems = document.querySelectorAll('.modal');
  M.Modal.init(elems, options);
  blockUserButton = document.getElementById("blockUser");
  unblockUserButton = document.getElementById("unblockUser");
});

async function loadProfile(url) {
  const response = await getGetResponse(url);
  if (response.status === 200) {
    var jsonData = await getJSONData(response);
    insertFields(jsonData);
  }
}

function insertFields(jsonData) {
  const fields = [
    { id: "id", prop: "id" },
    { id: "firstname", prop: "name" },
    { id: "surname", prop: "surname" },
    { id: "middlename", prop: "middlename" },
    { id: "email", prop: "email" },
    { id: "phone", prop: "phone" },
    { id: "address", prop: "address" },
    // { id: "address1", prop: ["userAddress","address1" ]},
    // { id: "address2", prop: ["userAddress","address2"] },
    // { id: "country", prop: ["userAddress", "country", "name"] },
    // { id: "state", prop: ["userAddress","state" ]},
    // { id: "city", prop: ["userAddress","city"] },
    // { id: "code", prop: ["userAddress","code"] },
    { id: "birthDate", prop: "birthDate", transform: transformTimestampToData, withTime: "false" },
    { id: "role", prop: "role", transform: getRoleName }
  ];

  fields.forEach(field => {
    let value = jsonData;
    const props = Array.isArray(field.prop) ? field.prop : [field.prop];
    for (const prop of props) {
      if (value === null) {
        break;
      }
      value = value[prop];
    }
    if (value != null) {
      const element = document.getElementById(field.id);
      element.setAttribute("value", value);
      if (element.labels.length != 0)
        element.labels[0].classList.add("active");
      if (field.transform) {
        if (field.withTime)
          element.setAttribute("value", field.transform(value, field.withTime));
        else element.setAttribute("value", field.transform(value));
      }
    }
  });
  if (isUserAdmin(decodedToken)) {
    if (jsonData.blocked) {
      unblockUserButton.removeAttribute("style");
      hideElement(blockUserButton);
    }
  }

}

function getRoleName(role) {
  if (role === 'USER') {
    if (localStorage.language === "es") {
      return 'Usuario';
    } else {
      return 'User';
    }
  };
  if (role === 'ADMIN') {
    if (localStorage.language === "es") {
      return 'Administrador';
    } else {
      return 'Admin';
    }
  }
}

async function blockUser() {
  if (isUserAdmin(decodedToken)) {
    const response = await getPutResponse(urlBlock);

    if (response.status === 204) {
      if (localStorage.language === "es")
        M.toast({ html: 'Usuario bloqueado!', displayLength: 3000 });
      else
        M.toast({ html: 'User blocked!', displayLength: 3000 });
      /*setTimeout(() => {
        window.location.href = "#!";
      }, "2000");*/
      M.Modal.getInstance(document.getElementById('block')).close()
      unblockUserButton.removeAttribute("style");
      hideElement(blockUserButton);
    } else {
      insertTestErrorMessageFromResponse(response);
    }
  }
}

async function unblockUser() {
  if (isUserAdmin(decodedToken)) {
    const response = await getPutResponse(urlUnblock)
    if (response.status === 204) {
      if (localStorage.language === "es")
        M.toast({ html: 'Usuario desbloquedo.', displayLength: 3000 });
      else
        M.toast({ html: 'User unblocked.', displayLength: 3000 });
      blockUserButton.removeAttribute("style");
      hideElement(unblockUserButton)
    } else {
      insertTestErrorMessageFromResponse(response);
    }
  }
}

async function updateProfile() {
  const { id, firstname, surname, middlename, email, phone, birthDate, address } = document.getElementById("userProfileForm");
  let timestamp = transformDataToTimestamp(birthDate.value);
  const body = {
    id: id.value,
    name: firstname.value.trim(),
    surname: surname.value.trim(),
    middlename: middlename.value.trim(),
    email: email.value.trim(),
    phone: phone.value.trim(),
    address: address.value.trim(),
    birthDate: timestamp
  };

  const response = await getPostResponse(urlProfileUpdate, body);
  if (response.status === 200) {
    M.toast({ html: 'Successfully updated!', displayLength: 2000 });
    var jsonData = await getJSONData(response);
    insertFields(jsonData);
  } else {
    insertTestErrorMessageFromResponse(response);
  }
}



function check() {
  const { firstname, surname, middlename, email, phone, birthDate } = document.getElementById("userProfileForm");

  function isValidTextString(str) {
    // Allow letters, spaces, apostrophes, and hyphens, as well as Cyrillic and Spanish characters
    return String(str).match("^[a-zA-Z\\u00C0-\\u024F\\u0400-\\u04FF\\u0500-\\u052F\\u1E00-\\u1EFF\\s'’\\-]+$");
  }

  const passPattern = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z]{8,}$/;
  const isValidFirstName = isValidTextString(firstname.value);
  const isValidSurname = isValidTextString(surname.value);
  const isValidMiddlename = middlename.value ? isValidTextString(middlename.value) : true;
  const isValidName = isValidFirstName && isValidSurname && isValidMiddlename;
  //const isValidEmail = /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email.value);
  const isValidBirthDate = /^(\d{2})\/(\d{2})\/(\d{4})$/.test(birthDate.value);
  const isValidPhone = /^\d{5,20}$/.test(phone.value);
  const isValidAddress = address.value ? true : true;


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

  if (!isValidMiddlename) {
    makeInputFieldInvalid(middlename, translations["onlyTextError"]);
  }
  else {
    makeInputFieldValid(middlename);
  }

  if (!email.value) {
    makeInputFieldInvalid(email, translations["fieldRequiredError"]);
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

  if (!isValidAddress) {
    makeInputFieldInvalid(address, translations["fieldRequiredError"]);
  }
  else {
    makeInputFieldValid(address);
  }

  const submitButton = document.getElementById('saveProfile');
  if (!(isValidName && isValidBirthDate && isValidPhone && isValidAddress))
    submitButton.classList.add("disabled");
  else submitButton.classList.remove("disabled");
}
