class Gui {
    constructor() {
        this.parent = document.getElementById("gui-slots");
        this.properties = document.getElementById("item-meta");
        this.selectedSlot = null;

        this.properties.style.visibility = "hidden";
        document.getElementById("properties-window").style.overflowY = "hidden";

        document.getElementById("rows").addEventListener("change", () => this.renderGui());
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
                    guiId: "gui-0",
                    rows: 6,
                    states: ["defaultState"],
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

    renderStateProperties() {
        for (let state of this.content.states) {
            const stateInput = document.createElement("input");
            stateInput.type = "image";
            stateInput.name = state;
            stateInput.src = "res/slot.png";
            stateInput.classList.add("properties-input");
            stateInput.id = "item-state-" + state;

            const label = document.createElement("label");
            label.textContent = state;
            label.classList.add("properties-label");

            const itemTranslationLabel = htmlToElement(`<label for="item-translation-id-${state}" class="properties-label">Item Translation Id</label>`);
            const itemTranslationInput = htmlToElement(`<input id="item-translation-id-${state}" type="text" name="Item translation id" value="item-0" class="properties-input">`);
            itemTranslationInput.addEventListener("change", () => {
               this.selectedSlot[state].translationId = itemTranslationInput.value;
            });


            stateInput.addEventListener("dragenter", cancelDefault);
            stateInput.addEventListener("dragover", cancelDefault);
            stateInput.addEventListener("drop", e => {
                cancelDefault(e);
                const item = document.getElementById(e.dataTransfer.getData("text/plain"));
                stateInput.src = item.src;
                this.selectedSlot[state].id = item.id;
                this.renderGui();
            });
            stateInput.addEventListener("dragstart", e => e.dataTransfer.setData("text/plain",
                this.selectedSlot[state].id));


            this.properties.appendChild(htmlToElement(`<h4 class="properties-states">${capitalizeFirstLetter(state)}</h4>`));
            this.properties.appendChild(itemTranslationLabel);
            this.properties.appendChild(itemTranslationInput);
            this.properties.appendChild(document.createElement("br"));
            this.properties.appendChild(document.createElement("br"));
            this.properties.appendChild(label);
            this.properties.appendChild(stateInput);
        }
    }

    renderGui() {
        this.parent.innerHTML = "";
        document.getElementById("rows").value = this.content.rows;
        document.getElementById("title").value = this.content.title;
        const rows = Math.min(6, Math.max(1, parseInt(rowElement.value)));
        for (let i = 0; i < rows; i++) {
            if (!this.content.items[i])
                this.content.items.push([]);

            for (let j = 0; j < 9; j++) {
                if (!this.content.items[i][j]) {
                    const json = {};
                    for (let state of this.content.states) {
                        json[state] = {
                            id: "air",
                            translationId: "none",
                            customData: null
                        };
                    }
                    this.content.items[i].push(json);
                }

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
                    document.getElementById("item-state-defaultState").src = item.src;
                });
                background.addEventListener("dragstart", e => e.dataTransfer.setData("text/plain",
                    this.content.items[i][j].defaultState.id));

                background.addEventListener("click", () => {
                    this.selectedSlot = this.content.items[i][j];
                    this.updateSelected();
                });

                this.parent.appendChild(background);
            }

            const breakElm = document.createElement("div");
            breakElm.classList.add("break");
            this.parent.appendChild(breakElm);
        }
    }

    updateSelected() {
        document.getElementById("properties-window").style.overflowY = "scroll";
        this.properties.style.visibility = "visible";
        for (let state of this.content.states) {
            const stateJson = this.selectedSlot[state];
            const stateItem = document.getElementById("item-state-" + state);
            const stateTranslationId = document.getElementById(`item-translation-id-${state}`);
            stateItem.src = document.getElementById(stateJson.id).src;
            stateTranslationId.value = stateJson.translationId;
        }
    }

    toBase64() {
        return btoa(JSON.stringify(this.content));
    }
}