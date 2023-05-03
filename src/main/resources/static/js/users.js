const urlUsers = "/api/admin/users/all";


document.addEventListener('DOMContentLoaded', function () {
    loadUsers(urlUsers);
    var elems = document.querySelectorAll('.modal');
    var modals = M.Modal.init(elems, options);
    var h4links = document.getElementsByTagName("h4");
    Array.prototype.forEach.call(h4links, elem => {
        elem.getElementsByTagName("a")[0].remove()
      });
});

