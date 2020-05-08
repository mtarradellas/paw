
function hideOrShow(event){
    const selectedSpecie = $(this).val();

    const filterBreedSelector =  $('#breedId');

    const breedOptions = filterBreedSelector.find('option');

    breedOptions.not(".species-"+selectedSpecie).hide();
    breedOptions.filter(".species-"+selectedSpecie+",.species-any").show();

    filterBreedSelector.val("-1");
}


/**
 *  Event handling
 */

$('#speciesId').on('change', hideOrShow);
