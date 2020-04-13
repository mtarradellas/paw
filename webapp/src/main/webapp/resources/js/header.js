$('#search-button').on('click',function(evt){
    let string = '?';
    const value = $('#search-value').val();

    string+="search=" + value + '&';
    const query = string;

    location.href = window.location.href.split('?')[0] + query;
});

$(document).ready(function(event){
    if(!window.location.href.includes('?'))
        return;

    const query = window.location.href.split('=')[1];
    const search = query.split('&')[0];

    $('#search-value').val(search);
});