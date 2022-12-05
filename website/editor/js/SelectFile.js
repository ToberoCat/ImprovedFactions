const selector = document.getElementById("file-chooser");
const fileUpload = document.getElementById("file-upload");

selector.addEventListener("click", () => fileUpload.click());
fileUpload.addEventListener("change", e => uploadFile(e.target.files[0]));

selector.addEventListener("dragenter", cancelDefault);
selector.addEventListener("dragover", e => {
    cancelDefault(e);
    e.dataTransfer.dropEffect = 'copy';
});
selector.addEventListener("drop", e => {
    cancelDefault(e);
    uploadFile(e.dataTransfer.files[0]);
});

function uploadFile(file) {
    if (!file.name) return;
    const reader = new FileReader()
    reader.onload = handleFileLoad;

    Toast.show("Uploading " + file.name, "upload");
    reader.readAsText(file);
}

function handleFileLoad(event) {
    const currentUrl = window.location.href.split(/(?<=editor)\//gm)[0];
    toBase64(event.target.result)
        .then(gui => location.href = `${currentUrl}/index.html?gui=${gui}`)
        .catch(err => {
            console.error(err);
            Toast.show("Uploaded file contained invalid gui", "error")
        });
}

function toBase64(json) {
    return new Promise(resolve =>
        resolve(btoa(JSON.stringify(JSON.parse(json)))));
}

function cancelDefault(e) {
    e.preventDefault();
    e.stopPropagation();
    return false;
}