class PropertyWindow {

    constructor() {
        this.properties = new Map();
        this.updateCallbacks = new Map();
        this.parent = document.getElementById("properties-container");
    }

    addProperty(name, defaultValue) {
        this.properties.set(name, defaultValue);
        const item = document.createElement("input");
        const label = document.createElement("label");


        item.innerHTML = defaultValue;
        item.id = name;
        item.value = defaultValue;
        item.classList.add("properties-input");

        label.htmlFor = name;
        label.innerHTML = name;
        label.classList.add("properties-label");

        this.parent.appendChild(label);
        this.parent.appendChild(item);
        this.parent.appendChild(document.createElement("br"));
        this.parent.appendChild(document.createElement("br"));

        item.addEventListener("change", () => {
            this.properties.set(name, item.value)
            if (this.updateCallbacks.has(name))
                this.updateCallbacks.get(name)();
        });
    }

    getProperty(name) {
        return this.properties.get(name);
    }

    onupdate(name, callback) {
        this.updateCallbacks.set(name, callback);
    }
}

class ItemWindow {

    constructor() {
        this.parent = document.getElementById("item-container");
        this.searchBar = document.getElementById("item-search")

        this.elements = [];
        const change = val => {
            this.parent.innerHTML = "";
            this.#displayItems(this.elements, val);
        };

        this.searchBar.addEventListener("change", () => change(this.searchBar.value));
        this.searchBar.addEventListener("input", () => change(this.searchBar.value));
        this.searchBar.addEventListener("propertychangey", () => change(this.searchBar.value));
    }

    async loadItems() {
        this.elements = await (await fetch("items.json")).json();
        this.parent.innerHTML = "";
        this.#displayItems(this.elements, "");
    }

    #displayItems(items, filter) {
        const regex = new RegExp(filter, "gmi");
        for (let jsonItem of items) {
            if (!jsonItem.title.match(regex)) continue;

            const item = document.createElement("input"); //  <input type="image" src="logg.png" name="saveForm" class="btTxt submit" id="saveForm" />
            item.type = "image";
            item.src = jsonItem.icon;
            item.name = jsonItem.title;
            item.alt = jsonItem.title;
            item.title = jsonItem.title;
            item.id = jsonItem.title.toLowerCase().replace(" ", "_");

            item.addEventListener("dragstart", e => e.dataTransfer.setData("text/plain", item.id));
            this.parent.appendChild(item);
        }
    }
}

class Gui {
    constructor(properties) {
        this.parent = document.getElementById("gui-slots");
        this.properties = properties;
    }

    generateGui() {
        return this.parseQueryGui()
            .then(content => {
                this.content = content;
            })
            .catch(err => console.log("Couldn't read gui from query"))
            .finally(() => {
                if (this.content)
                    return;
                this.content = {
                    guiTitleTranslation: "none",
                    rows: 6,
                    items: []
                };
            });
    }

    parseQueryGui() {
        return new Promise((resolve, reject) => {
            const params = new Proxy(new URLSearchParams(window.location.search), {
                get: (searchParams, prop) => searchParams.get(prop),
            });
             resolve(JSON.parse(atob(params.gui)));
        });
    }

    renderGui() {
        this.parent.innerHTML = "";
        const rows = Math.min(6, Math.max(1, parseInt(this.properties.getProperty("Rows"))));
        for (let i = 0; i < rows; i++) {
            if (!this.content.items[i])
                this.content.items.push([]);

            for (let j = 0; j < 9; j++) {
                if (!this.content.items[i][j])
                    this.content.items[i].push({
                        defaultState: {
                            id: "air",
                            translationId: "none",
                        }
                    });

                const background = document.createElement("img");
                const reference = document.getElementById(this.content.items[i][j].defaultState.id);
                background.src = reference.src;
                background.classList.add("slot");
                background.classList.add("unselectable");

                background.addEventListener("dragenter", cancelDefault);
                background.addEventListener("dragover", cancelDefault);
                background.addEventListener("drop", e => {
                    cancelDefault(e);
                    const item = document.getElementById(e.dataTransfer.getData("text/plain"));
                    background.src = item.src;
                    this.content.items[i][j].defaultState.id = item.id;
                });

                this.parent.appendChild(background);
            }

            const breakElm = document.createElement("div");
            breakElm.classList.add("break");
            this.parent.appendChild(breakElm);
        }
    }

    toBase64() {
        return btoa(JSON.stringify(this.content));
    }
}

function addExportListener() {
    document.getElementById("export-gui-button").addEventListener("click", () => {
        navigator.clipboard.writeText("/f editor apply " + gui.toBase64())
            .then(r => Toast.show("Copied command into clipboard", "success"))
            .catch(e => {
                console.error(e);
                Toast.show("Error occurred while coping to clipboard", "error")
            });
    });

    document.getElementById("share-gui-button").addEventListener("click", () => {
        navigator.clipboard.writeText(window.location.href.split('?')[0] + "?gui=" + gui.toBase64())
            .then(r => Toast.show("Copied link into clipboard", "success"))
            .catch(e => {
                console.error(e);
                Toast.show("Error occurred while coping to clipboard", "error")
            });
        gui.toBase64();
    });
}

function cancelDefault(e) {
    e.preventDefault();
    e.stopPropagation();
    return false;
}

const properties = new PropertyWindow();
const itemWindow = new ItemWindow();
const gui = new Gui(properties);
gui.generateGui()
    .then(() => {
        addExportListener();

        properties.addProperty("Title", "&eTitle");
        properties.addProperty("Rows", 6);

        properties.onupdate("Rows", () => {
            gui.renderGui();
        })

        itemWindow.loadItems()
            .then(() => gui.renderGui());
    });
