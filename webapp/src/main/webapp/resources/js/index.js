

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
