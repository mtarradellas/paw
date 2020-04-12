$('#search-button').on('click',function(evt){
    let string = '?';

    string+="search=" + $('#search-value').val() + '&';
    const query = string;

    location.href = window.location.href.split('?')[0] + query;

});