class Gui {
    constructor() {
        this.parent = document.getElementById("gui-slots");
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

    renderGui() {
        this.parent.innerHTML = "";
        const rows = Math.min(6, Math.max(1, parseInt(rowElement.value)));
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