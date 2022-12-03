const Toast = {
    init() {
        this.hideTimeout = null;
        this.el = document.createElement("div");
        this.el.className = "toast";
        document.body.appendChild(this.el);
    },

    show(message, state) {
        clearTimeout(this.hideTimeout);

        this.el.textContent = message;
        this.el.className = "toast toast--visible";

        if (state)
            this.el.classList.add(`toast--${state}`);
        this.hideTimeout = setTimeout(() => {
            this.el.classList.remove("toast--visible");
        }, 3000);
    }
};

document.addEventListener("DOMContentLoaded", () => Toast.init());