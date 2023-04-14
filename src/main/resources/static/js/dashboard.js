const surname = response.surname;
const name = response.name;

function insertNameAndSurname() {
  const nameSurnameElement = document.getElementById("name-surname");
  nameSurnameElement.innerText = `${surname} ${name}`;
}

function load() {
  // make a fetch request to load
  // ...

  // if successfully
  insertNameAndSurname();
}