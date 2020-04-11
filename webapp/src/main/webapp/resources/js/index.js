
/**
 *  Event handling
 */

$('#filter-specie').bind('change', event=>{

    const selectedSpecie = $('#filter-specie').val();

    if(selectedSpecie === "any"){
        $('#filter-breed').attr('disabled', true);
    }else{
        $('#filter-breed').attr('disabled', false);
    }

    const breedOptions = $('#filter-breed > option');

    breedOptions.not(".specie-"+selectedSpecie).hide();
    breedOptions.filter(".specie-"+selectedSpecie+",.specie-any").show();

    $('#filter-breed').val("any");
});


$('#search-criteria').bind('change', event=>{

    const selectedCriteria = $('#search-criteria').val();

    if(selectedCriteria === "any"){
        $('#search-order').attr('disabled', true);
    }else{
        $('#search-order').attr('disabled', false);
    }

});

function pushProperty(list, key, value){
    if(value !== 'any')
        list.push({
            key,
            value
        });
}

function paramsToString(list){
    let string = '?';
    list.forEach(item=>{
        string+=item.key + '=' + item.value + '&';
    });
    return string;
}

$('#search-tools-submit').bind('click', event=>{
    const queryParams = [];

    pushProperty(queryParams, 'specie', $('#filter-specie').val());
    pushProperty(queryParams, 'breed', $('#filter-breed').val());
    pushProperty(queryParams, 'gender', $('#filter-gender').val());
    pushProperty(queryParams, 'searchCriteria', $('#search-criteria').val());

    if($('#search-criteria').val() !== 'any')
        pushProperty(queryParams, 'searchOrder', $('#search-order').val());

    const query = paramsToString(queryParams);

    location.href = window.location.href.split('?')[0] + query;
});

/**
 *  Load initial values from query string
 */

function getQueryParams(){
    const defaultValues = {
        specie: 'any',
        breed: 'any',
        gender: 'any',
        searchCriteria: 'any',
        searchOrder: 'asc'
    };

    //check if there is a query
    if(!window.location.href.includes('?'))
        return defaultValues;

    const query = window.location.href.split('?')[1];
    const parameters = query.split('&');

    const keyValues = {};
    parameters.forEach(parameter => {
        const [key, value] = parameter.split('=');
        keyValues[key] = value;
    });

    return {
        specie: keyValues.specie || defaultValues.specie,
        breed: keyValues.breed || defaultValues.breed,
        gender: keyValues.gender || defaultValues.gender,
        searchCriteria: keyValues.searchCriteria || defaultValues.searchCriteria,
        searchOrder: keyValues.searchOrder || defaultValues.searchOrder
    };
}

$(document).ready(()=>{
    const {specie, breed, gender, searchCriteria, searchOrder} = getQueryParams();
    console.log(getQueryParams());
    $('#filter-specie').val(specie);
    $('#filter-gender').val(gender);
    $('#search-criteria').val(searchCriteria);

    const breedSelect = $('#filter-breed');
    breedSelect.val(breed);
    const searchOrderSelect = $('#search-order');
    searchOrderSelect.val(searchOrder);

    if (specie !== 'any')
        breedSelect.attr('disabled', false);


    if(searchCriteria !== 'any')
        searchOrderSelect.attr('disabled', false);
});