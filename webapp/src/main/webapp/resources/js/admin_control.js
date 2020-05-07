function confirmDelete(evt){
    if(!confirm($("#confirmMessage").text())){
        evt.preventDefault();
    }
}