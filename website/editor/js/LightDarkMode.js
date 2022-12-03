let isLightMode = true;

function setMode(lightMode) {
    document.getElementById("light-mode").disabled = lightMode;
    document.getElementById("dark-mode").disabled = !lightMode;
    isLightMode = lightMode;
}

const darkModeToggle = document.getElementById("darkmode-toggle");
darkModeToggle.addEventListener("change", e => {
    localStorage.setItem("dark-mode", e.target.checked);
    setMode(e.target.checked);
});

const lightMode = localStorage.getItem("dark-mode") === "true";

darkModeToggle.checked = lightMode;
setMode(lightMode);
