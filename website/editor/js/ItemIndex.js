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
        this.elements = await (await fetch("res/items.json")).json();
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

function cancelDefault(e) {
    e.preventDefault();
    e.stopPropagation();
    return false;
}

let currentTab = "item-tab";
function openTab(tabId) {
    document.getElementById(currentTab).style.visibility = "hidden";
    document.getElementById(tabId).style.visibility = "visible";

    currentTab = tabId;
}