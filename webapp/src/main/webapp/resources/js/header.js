$('#search-button').on('click',function(evt){
    const value = $('#search-value').val();

    if(value === ""){
        location.href = window.location.href.split('?')[0];
        return;
    }
    let string = '?';

    string+="find=" + value + '&';
    const query = string;

    location.href = window.location.href.split('?')[0] + query;
});

$(document).ready(function(event){
    if(!window.location.href.includes('?'))
        return;

    const field = window.location.href.split('?')[1];
    const search = field.split('=')[0];

    if(search === "find"){
        const query = field.split('=')[1];
        const searchVal = query.split('&')[0];
        $('#search-value').val(searchVal);
    }
});