var languageGlobal = localStorage.language;

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
    if (!languageGlobal != null)
        setLocale(languageGlobal);
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
        languageGlobal = language;
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
    let children = Object.assign({}, element.children);
    if (key.startsWith("ph"))
        element.setAttribute("placeholder", translation);
    else {
        element.innerHTML = "";
        if (!(Object.keys(children).length === 0)){
            for(let i = 0; i < Object.keys(children).length; i++)
                element.appendChild(children[i]);}
        element.appendChild(document.createTextNode(translation));

    }
}


function getDatepickerOptions(language) {
    var options = {
        format: 'dd.mm.yyyy',
        firstDay: 1,
        minDate: new Date('1900-01-01T00:00:00'),
        maxDate: new Date(new Date().setFullYear(new Date().getFullYear() - 14)),
        yearRange:70,
        i18n: {
            cancel: language === 'en' ? 'Cancel' : 'Annuler',
            clear: language === 'en' ? 'Clear' : 'Effacer',
            done: language === 'en' ? 'Done' : 'Terminé',
            months: ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'],
            monthsShort: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
            weekdays: ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'],
            weekdaysShort: ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'],
            weekdaysAbbrev: ['S', 'M', 'T', 'W', 'T', 'F', 'S']
        }
    };

    if (language === 'es') {
        options.i18n.cancel = 'Cancelar';
        options.i18n.clear = 'Borrar';
        options.i18n.done = 'Aceptar';
        options.i18n.months = ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'];
        options.i18n.monthsShort = ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun', 'Jul', 'Ago', 'Sep', 'Oct', 'Nov', 'Dic'];
        options.i18n.weekdays = ['Domingo', 'Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes', 'Sábado'];
        options.i18n.weekdaysShort = ['Dom', 'Lun', 'Mar', 'Mié', 'Jue', 'Vie', 'Sáb'];
        options.i18n.weekdaysAbbrev = ['D', 'L', 'M', 'X', 'J', 'V', 'S'];
    }
    return options;
}