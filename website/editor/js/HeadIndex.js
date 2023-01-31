const categoryPages = {
    "alphabet": 6,
    "animals": 4,
    "blocks": 2,
    "decoration": 12,
    "food-drinks": 2,
    "humanoid": 7,
    "humans": 12,
    "miscellaneous": 3,
    "monsters": 4,
    "plants": 2
}

const { cancelable } = window.CancelablePromise;
const headNotLoadedImage = "data:image/png;base64,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";

class HeadIndex {
    constructor() {
        this.container = document.getElementById("head-container");
        this.selectedCategory = "alphabet";
        this.promise = null;
        document.getElementById("head-category-selector")
            .addEventListener("change", e => {
               this.selectedCategory = e.target.value;
               this.createView();
            });
    }

    async loadEverything() {
        this.paths = await (await fetch(`res/heads/paths.json`)).json();
        this.tags = await (await fetch(`res/heads/tags.json`)).json();
    }

    async getHeadId(texture) {
        const {id, path} = this.paths[texture];
        const page = await (await fetch(`res/${path}`)).json();
        return page[id];
    }

    createView() {
        if (this.promise)
            this.promise.cancel();

        this.promise = cancelable(new Promise(async res => {
            this.container.innerHTML = "";
            for (let i = 0; i < categoryPages[this.selectedCategory]; i++)
                await this.createPage(i);

            res();
        }));
    }

    async createPage(page) {
        const elements = await (await fetch(`res/heads/${this.selectedCategory}/page_${page}.json`)).json();
        for (let headId in elements) {
            const head = elements[headId];
            const el = htmlToElement(
                `<input type="image" src="data:image/png;base64,${head.icon}" 
                           name="${head.name}" alt="${head.name}" title="${head.name}" id="${headId}">`
            );
            makeDraggableItem(el, true, () => headId, () => head.texId);
            this.container.appendChild(el);
        }
    }
}

const headIndex = new HeadIndex();
headIndex.createView();