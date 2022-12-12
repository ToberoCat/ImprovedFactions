class Gui {
    constructor() {
        this.parent = document.getElementById("gui-slots");
        this.properties = document.getElementById("item-meta");
        this.selectedSlot = null;
        this.rowsElement = document.getElementById("rows");
        this.showState = "defaultState";

        this.properties.style.visibility = "hidden";
        document.getElementById("properties-window").style.overflowY = "hidden";

        this.rowsElement.addEventListener("change", () => {
            this.content.rows = this.rowsElement.value;
            this.renderGui();
        });

        const stateSelector = document.getElementById("display-states");
        stateSelector.addEventListener("change", e => {
            this.showState = stateSelector.value;
            this.renderGui();
            if (this.selectedSlot) this.updateSelected();
        })
    }

    generateGui() {
        return this.parseQueryGui()
            .then(content => {
                this.content = content;
            })
            .catch(err => {
                /*                const currentUrl = window.location.href.split(/(?<=editor)\//gm)[0];
                                location.href = `${currentUrl}/select-file.html`;*/ // ToDo: Open the file seleciton
            })
            .finally(() => {
                if (!this.content)
                    this.content = {
                        guiId: "gui-0",
                        rows: 6,
                        states: ["defaultState"],
                        flags: [{"name": "Sample Flag", "id": 0}],
                        items: []
                    };
                document.getElementById("gui-id").innerText = capitalizeBySpace(this.content.guiId.replaceAll("-", " "));
                this.rowsElement.value = this.content.rows;
                const stateSelector = document.getElementById("display-states");
                for (let state of this.content.states) stateSelector.appendChild(htmlToElement(
                    `<option value="${state}">${capitalizeFirstLetter(state)}</option>`));
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
            label.textContent = "Item";
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
                const parts = e.dataTransfer.getData("text/plain").split(";;;");
                const id = parts[0];
                const isHead = parts[1] === "true";
                const customData = parts[2];

                const item = document.getElementById(id);
                stateInput.src = item.src;
                this.selectedSlot[state].id = isHead ? "player_head" : item.id;
                this.selectedSlot[state].customData = customData;
                this.renderGui();
            });

            makeDraggableItem(stateInput, false,
                () => this.selectedSlot[state].id,
                () => this.selectedSlot[state].customData
            );

            this.properties.appendChild(htmlToElement(`<h4 class="properties-states">${capitalizeFirstLetter(state)}</h4>`));
            this.properties.appendChild(itemTranslationLabel);
            this.properties.appendChild(itemTranslationInput);
            this.properties.appendChild(document.createElement("br"));
            this.properties.appendChild(document.createElement("br"));
            this.properties.appendChild(label);
            this.properties.appendChild(stateInput);

            for (let flag of this.content.flags) {
                this.properties.appendChild(document.createElement("br"));
                this.properties.appendChild(document.createElement("br"));

                const flagLabel = htmlToElement(`<label for='flag-${state}-${flag.id}' class="properties-label">${flag.name}</label>`);
                const flagBox = htmlToElement(`<input type="checkbox" id='flag-${state}-${flag.id}' name="${flag.name}" value=false class="properties-input">`);
                flagBox.addEventListener("change", e => {
                    console.log("Change");
                    const flags = this.selectedSlot[state].flags;
                    if (!e.target.value) {
                        const index = flags.indexOf(flag.id);
                        if (index <= -1)
                            return;
                        flags.splice(index, 1);
                    } else
                        flags.push(flag.id);

                });

                this.properties.appendChild(flagLabel);
                this.properties.appendChild(flagBox);
            }
        }
    }

    renderGui() {
        this.parent.innerHTML = "";
        this.rowsElement.value = this.content.rows;
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
                            customData: null,
                            flags: []
                        };
                    }
                    this.content.items[i].push(json);
                }

                const background = document.createElement("img");
                const reference = this.getReferenceItem(this.content.items[i][j][this.showState], headItem => {
                    background.src = "data:image/png;base64," + headItem.icon;
                });
                background.src = reference.src;
                background.classList.add("slot");
                background.classList.add("unselectable");

                background.addEventListener("dragenter", cancelDefault);
                background.addEventListener("dragover", cancelDefault);
                background.addEventListener("drop", e => {
                    cancelDefault(e);
                    const parts = e.dataTransfer.getData("text/plain").split(";;;");
                    const id = parts[0];
                    const isHead = parts[1] === "true";
                    const customData = parts[2];

                    const item = document.getElementById(id);
                    background.src = item.src;
                    this.content.items[i][j][this.showState].id = isHead ? "player_head" : item.id;
                    this.content.items[i][j][this.showState].customData = customData;
                    document.getElementById("item-state-" + this.showState).src = item.src;
                });

                makeDraggableItem(background, false,
                    () => this.content.items[i][j][this.showState].id,
                    () => this.content.items[i][j][this.showState].customData
                );

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

    getReferenceItem(state, loadCb) {
        if (state.id !== "player_head")
            return document.getElementById(state.id);
        const texture = state.customData;
        if (texture == null)
            return document.getElementById(state.id);
        headIndex.getHeadId(texture)
            .then(x => loadCb(x))
            .catch(() => Toast.show("Couldn't fetch head " + state.id, "error"));
        return headNotLoadedImage;
    }

    updateSelected() {
        document.getElementById("properties-window").style.overflowY = "scroll";
        this.properties.style.visibility = "visible";
        for (let state of this.content.states) {
            const stateJson = this.selectedSlot[state];
            const stateItem = document.getElementById("item-state-" + state);
            if (state === this.showState) stateItem.removeAttribute("disabled");
            else stateItem.setAttribute("disabled", "disabled");
            const stateTranslationId = document.getElementById(`item-translation-id-${state}`);
            stateItem.src = document.getElementById(stateJson.id).src;
            stateTranslationId.value = stateJson.translationId;

            for (let flag of this.content.flags) {
                document.getElementById(`flag-${state}-${flag.id}`).checked =
                    stateJson.flags.includes(flag.id);
            }
        }
    }

    exportGui() {
        return JSON.stringify(this.content);
    }
}