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
        this.#displayItems(this.elements, "s");
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

            this.parent.appendChild(item);
        }
    }
}

class Gui {
    constructor(properties) {
        this.parent = document.getElementById("gui-slots");
        this.properties = properties;
    }

    renderGui() {
        this.parent.innerHTML = "";
        const rows = Math.min(6, Math.max(1, parseInt(this.properties.getProperty("Rows"))));
        for (let i = 0; i < rows; i++) {
            const container = document.createElement("div");
            this.parent.appendChild(container);
            for (let j = 0; j < 9; j++) {
                const background = document.createElement("img");
                background.src = "res/slot.png";
                background.classList.add("slot");
                background.classList.add("unselectable");

                background.addEventListener("drop", e => {
                    cancelDefault(e);
                    background.src = e.dataTransfer.getData("text/plain");
                });
                background.addEventListener("dragenter", cancelDefault);
                background.addEventListener("dragover", cancelDefault);


                container.appendChild(background);
            }

            const breakElm = document.createElement("div");
            breakElm.classList.add("break");
            this.parent.appendChild(breakElm);
        }
    }
}

function cancelDefault(e) {
    e.preventDefault();
    e.stopPropagation();
    return false;
}

const properties = new PropertyWindow();
const itemWindow = new ItemWindow();
const gui = new Gui(properties);


properties.addProperty("Title", "&eTitle");
properties.addProperty("Rows", 5);

properties.onupdate("Rows", () => {
    gui.renderGui();
})

itemWindow.loadItems();
gui.renderGui();