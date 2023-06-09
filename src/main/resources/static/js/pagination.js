var container;
var url;
var onSelectPageFunc;

function generatePagination(currentPage, totalPages, onSelectPageFunc, url, container) {
  this.container = container;
  this.url = url;

  let url2 = new URL(document.location.origin + url);
  let params = url2.searchParams;
  params.delete("page");
  url = url2.pathname + (params.toString() ? "?" + params.toString() : "");

  this.onSelectPageFunc = onSelectPageFunc;
  let paginationHtml = '';
  if (totalPages <= 1) {
    return paginationHtml;
  }

  // Previous page button
  if (currentPage > 1) {
    let scriptInline = onSelectPageFunc + "('" + url + "&page=" + (currentPage - 2) + "');";
    paginationHtml += '<li class="waves-effect"><a href="#!" onclick="event.preventDefault();' + scriptInline + '"><i class="material-icons">chevron_left</i></a></li>';
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