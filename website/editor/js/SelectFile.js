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
    parse(event.target.result)
        .then(x => {
            return new Promise(() => {
                document.getElementById("file-section").style.visibility = "hidden";
                document.getElementById("editor").classList.remove("blur");
                document.getElementById("editor").classList.remove("unselectable");
                gui.generateGui(x);
            });
        })
        .then(() => Toast.show("Uploaded gui", "success"))
        .catch(err => {
            console.error(err);
            Toast.show("Uploaded file contained invalid gui", "error")
        });
}

function parse(json) {
    return new Promise(resolve => resolve(JSON.parse(json)));
}

function cancelDefault(e) {
    e.preventDefault();
    e.stopPropagation();
    return false;
}