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
class HeadIndex {
    constructor() {
        this.container = document.getElementById("head-container");
        this.selectedCategory = "alphabet";
        document.getElementById("head-category-selector")
            .addEventListener("change", e => {
               this.selectedCategory = e.target.value;
               this.createView();
            });
    }

    async createView() {
        this.container.innerHTML = "";
        for (let i = 0; i < categoryPages[this.selectedCategory]; i++)
            await this.createPage(i);
    }

    async createPage(page) {
        this.elements = await (await fetch(`res/heads/${this.selectedCategory}/page_${page}.json`)).json();
        for (let headId in this.elements) {
            const head = this.elements[headId];
            this.container.appendChild(htmlToElement(
                `<input type="image" src="data:image/png;base64,${head.icon}" 
                           name="${head.name}" alt="${head.name}" title="${head.name}" id="${headId}">`
            ))
        }
    }
}

const headIndex = new HeadIndex();
headIndex.createView();