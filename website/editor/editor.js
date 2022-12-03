function createUI() {
    return new Promise(async resolve => {
        await gui.generateGui();
        await itemWindow.loadItems();

        createListeners();
        gui.renderGui();
        gui.renderStateProperties();
        resolve();
    });
}

function createListeners() {
    document.getElementById("export-gui-button")
        .addEventListener("click", () => {
            navigator.clipboard.writeText("/f editor apply " + gui.toBase64())
                .then(r => Toast.show("Copied command into clipboard", "success"))
                .catch(e => {
                    console.error(e);
                    Toast.show("Error occurred while coping to clipboard", "error")
                });
        });

    document.getElementById("share-gui-button")
        .addEventListener("click", () => {
            navigator.clipboard.writeText(window.location.href.split('?')[0] + "?gui=" + gui.toBase64())
                .then(r => Toast.show("Copied link into clipboard", "success"))
                .catch(e => {
                    console.error(e);
                    Toast.show("Error occurred while coping to clipboard", "error")
                });
            gui.toBase64();
        });
}

function htmlToElement(html) {
    var template = document.createElement('template');
    html = html.trim(); // Never return a text node of whitespace as the result
    template.innerHTML = html;
    return template.content.firstChild;
}

function capitalizeFirstLetter(string) {
    return string.charAt(0).toUpperCase() + string.slice(1);
}

const rowElement = document.getElementById("rows");

const itemWindow = new ItemWindow();
const gui = new Gui();

createUI()
    .then(() => Toast.show("Editor loaded successfully", "success"))
    .catch(() => Toast.show("Editor didn't load correctly", "error"));
