
function hideOrShow(event){
    const selectedSpecie = $(this).val();

    const filterBreedSelector =  $('#breedName');

    const breedOptions = filterBreedSelector.find('option');

    breedOptions.not(".species-"+selectedSpecie).hide();
    breedOptions.filter(".species-"+selectedSpecie+",.species-any").show();

    filterBreedSelector.val("empty");
}


/**
 *  Event handling
 */

$('#speciesName').on('change', hideOrShow);