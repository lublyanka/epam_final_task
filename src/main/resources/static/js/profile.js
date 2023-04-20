const urlProfile = "/api/auth/profile";

document.addEventListener('DOMContentLoaded', function () {
  loadProfile();
});

async function loadProfile() {
  const response = await fetch(urlProfile, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': localStorage.token
    },
  })
  if (response.status == 200) {
    var json = await response.json();
    const data = JSON.stringify(json);
    var obj = JSON.parse(data);
    insertFields(obj);
  }
}

  function insertFields(obj) {
    const fields = [
      { id: "name", prop: "name" },
      { id: "surname", prop: "surname" },
      { id: "middlename", prop: "middlename" },
      { id: "email", prop: "email" },
      { id: "phone", prop: "phone" },
      { id: "address1", prop: ["userAddress","address1" ]},
      { id: "address2", prop: ["userAddress","address2"] },
      { id: "country", prop: ["userAddress", "country", "name"] },
      { id: "state", prop: ["userAddress","state" ]},
      { id: "city", prop: ["userAddress","city"] },
      { id: "code", prop: ["userAddress","code"] },
      { id: "role", prop: "role", transform: getRoleName }
    ];

    fields.forEach(field => {
      let value = obj;
      const props = Array.isArray(field.prop) ? field.prop : [field.prop];
      for (const prop of props) {
        if (value == null) {
          break;
        }
        value = value[prop];
      }
      if (value != null) {
        const element = document.getElementById(field.id);
        element.setAttribute("value", value);
        element.labels[0].setAttribute("class", "active");
        if (field.transform) {
          element.setAttribute("value", field.transform(value));
        }
      }
    });
  }

function getRoleName(role) {
  if (role === 'user') {
    if (localStorage.language == "es") {
      return 'Usuario';
    } else {
      return 'User';
    }
  };
  if (role === 'admin') {
    if (localStorage.language == "es") {
      return 'Administrador';
    } else {
      return 'Admin';
    }
  }
}