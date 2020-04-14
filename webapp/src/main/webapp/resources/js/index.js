

/**
 *  Event handling
 */

$('#filter-species').on('change', function(event){

    const selectedSpecie = $(this).val();


    const filterBreedSelector =  $('#filter-breed');
    if(selectedSpecie === "any"){
        filterBreedSelector.attr('disabled', true);
    }else{
        filterBreedSelector.attr('disabled', false);
    }

    const breedOptions = filterBreedSelector.find('option');

    breedOptions.not(".species-"+selectedSpecie).hide();
    breedOptions.filter(".species-"+selectedSpecie+",.species-any").show();

    filterBreedSelector.val("any");
});


$('#search-criteria').on('change', function(event){

    const selectedCriteria = $(this).val();

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

$('#search-tools-submit').on('click', function(evt){
    const queryParams = [];

    pushProperty(queryParams, 'species', $('#filter-species').val());
    pushProperty(queryParams, 'breed', $('#filter-breed').val());
    pushProperty(queryParams, 'gender', $('#filter-gender').val());

    const searchCriteriaSelector =  $('#search-criteria');
    pushProperty(queryParams, 'searchCriteria', searchCriteriaSelector.val());

    if(searchCriteriaSelector.val() !== 'any')
        pushProperty(queryParams, 'searchOrder', $('#search-order').val());

    const query = paramsToString(queryParams);

    location.href = window.location.href.split('?')[0] + query;
});

/**
 *  Load initial values from query string
 */

function getQueryParams(){
    const defaultValues = {
        species: 'any',
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
        species: keyValues.species || defaultValues.species,
        breed: keyValues.breed || defaultValues.breed,
        gender: keyValues.gender || defaultValues.gender,
        searchCriteria: keyValues.searchCriteria || defaultValues.searchCriteria,
        searchOrder: keyValues.searchOrder || defaultValues.searchOrder
    };
}

$(document).ready(function(event){
    const {species, breed, gender, searchCriteria, searchOrder} = getQueryParams();

    $('#filter-species').val(species);
    $('#filter-gender').val(gender);
    $('#search-criteria').val(searchCriteria);

    const breedSelect = $('#filter-breed');
    breedSelect.val(breed);
    const searchOrderSelect = $('#search-order');
    searchOrderSelect.val(searchOrder);

    if (species !== 'any')
        breedSelect.attr('disabled', false);


    if(searchCriteria !== 'any')
        searchOrderSelect.attr('disabled', false);
});