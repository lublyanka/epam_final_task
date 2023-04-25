let options = {
    alignment: 'left',
    autoFocus: true,
    constrainWidth: true,
    container: null,
    coverTrigger: true,
    closeOnClick: true,
    hover: true,
    inDuration: 150,
    outDuration: 250,
    onOpenStart: null,
    onOpenEnd: null,
    onCloseStart: null,
    onCloseEnd: null,
    onItemClick: null
};

// Gets filled with active locale translations
let translations = {};


document.addEventListener('DOMContentLoaded', function () {
    var elems = document.querySelectorAll('.dropdown-trigger');
    /* var options = new Map;
    options.set('alignment','right'); */
    var instances = M.Dropdown.init(elems, options);
    if (!localStorage.language != null)
        setLocale(localStorage.language);
});

// Load translations for the given locale and translate
// the page to this locale
async function setLocale(language) {
    /* if (localStorage.language == language)
        return; */
    if (['es', 'en'].includes(language)) {
        let lang = ':lang(' + language + ')';
        let hide = '[lang]:not(' + lang + ')';
        document.getElementsByTagName("html")[0].lang = language;
        document.querySelectorAll(hide).forEach(function (node) {
            node.style.display = 'none';
        });
        let show = '[lang]' + lang;
        document.querySelectorAll(show).forEach(function (node) {
            node.style.display = 'unset';
        });
        localStorage.language = language;
        const newTranslations =
            await fetchTranslationsFor(language);
        translations = newTranslations;
        translatePage();
    }
}

// Retrieve translations JSON object for the given
// locale over the network
async function fetchTranslationsFor(language) {
    const response = await fetch(`/lang/${language}.json`);
    return await response.json();
}

// Replace the inner text of each element that has a
// data-i18n-key attribute with the translation corresponding
// to its data-i18n-key
function translatePage() {
    document
        .querySelectorAll("[data-i18n-key]")
        .forEach(translateElement);
}
// Replace the inner text of the given HTML element
// with the translation in the active locale,
// corresponding to the element's data-i18n-key
function translateElement(element) {
    const key = element.getAttribute("data-i18n-key");
    const translation = translations[key];
    let children = Object.assign({},element.children);
    if (key.startsWith("ph"))
        element.setAttribute("placeholder", translation);
    else {
        element.innerHTML = "";
        if(!(Object.keys(children).length === 0))
            element.appendChild(children[0]);
        element.appendChild(document.createTextNode(translation));

    }
}

