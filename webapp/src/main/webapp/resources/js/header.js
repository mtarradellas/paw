$(document).ready(function(event){
    if(!window.location.href.includes('?')) {
        return;
    }

    const field = window.location.href.split('?')[1];
    const search = field.split('=')[0];

    if(search === "find"){
        const query = field.split('=')[1];
        $('#search-value').val(query);
    }
});