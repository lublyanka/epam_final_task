const urlProfile = "/api/auth/profile";
const userId = window.location.pathname.split("/")[2];
const urlUser = `/api/admin/users/${userId}`;
const urlBlock = `/api/admin/users/${userId}/block`;
const urlUnblock = `/api/admin/users/${userId}/unblock`;
var decodedToken;

let blockUserButton;
let unblockUserButton;

document.addEventListener('DOMContentLoaded', function () {
  decodedToken = getDecodedToken(localStorage.token);
  if (isUserAdmin(decodedToken))
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

function insertFields(obj) {
  const fields = [
    { id: "name", prop: "name" },
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
    let value = obj;
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
      element.labels[0].setAttribute("class", "active");
      if (field.transform) {
        if (field.withTime)
          element.setAttribute("value", field.transform(value, field.withTime));
        else element.setAttribute("value", field.transform(value));
      }
    }
  });
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
      insertPlainErrorMessage(response);
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
      insertPlainErrorMessage(response);
    }
  }
}