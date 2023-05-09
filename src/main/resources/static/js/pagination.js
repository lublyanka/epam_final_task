var container;
var url;
var onSelectPageFunc;

function generatePagination(currentPage, totalPages, onSelectPageFunc, url, container) {
  this.container = container;
  this.url = url;

//TODO remove page param from URL

  this.onSelectPageFunc = onSelectPageFunc;
  let paginationHtml = '';
  if (totalPages <= 1) {
    return paginationHtml;
  }

  // Previous page button
  if (currentPage > 1) {
    let scriptInline = onSelectPageFunc + "('" + url + "&page=" + (currentPage - 2) + "');";
    paginationHtml += '<li class="waves-effect"><a href="#!" onclick="event.preventDefault();' + scriptInline + '"><i class="material-icons">chevron_left</i></a></li>';
    //paginationHtml += '<li class="waves-effect"><a href="#!" onclick="event.preventDefault(); onSelectPage(' + (currentPage - 1) + ', ' + totalPages + ');"><i class="material-icons">chevron_left</i></a></li>';
  } else {
    paginationHtml += '<li class="disabled"><a href="#!"><i class="material-icons">chevron_left</i></a></li>';
  }

  // Page numbers
  for (let i = 1; i <= totalPages; i++) {
    if (i === currentPage) {
      paginationHtml += '<li class="active blue darken-1"><a href="#!">' + i + '</a></li>';
    } else {
      let scriptInline = onSelectPageFunc + "('" + url + "&page=" + (i - 1) + "');";
      paginationHtml += '<li class="waves-effect"><a href="#!" onclick="event.preventDefault();' + scriptInline + '">' + i + '</a></li>';
    }
  }

  // Next page button
  if (currentPage < totalPages) {
    let scriptInline = onSelectPageFunc + "('" + url + "&page=" + (currentPage) + "');";
    paginationHtml += '<li class="waves-effect"><a href="#!" onclick="event.preventDefault();' + scriptInline + '"><i class="material-icons">chevron_right</i></a></li>';
  } else {
    paginationHtml += '<li class="disabled"><a href="#!"><i class="material-icons">chevron_right</i></a></li>';
  }

  // Append pagination HTML to container element
  container.innerHTML = '<ul class="pagination">' + paginationHtml + '</ul>';
}