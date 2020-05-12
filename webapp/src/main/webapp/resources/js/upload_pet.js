
function hideOrShow(event){
    const selectedSpecie = $(this).val();

    const filterBreedSelector =  $('#breedId');

    const breedOptions = filterBreedSelector.find('option');

    breedOptions.not(".species-"+selectedSpecie).hide();
    breedOptions.filter(".species-"+selectedSpecie+",.species-any").show();

    filterBreedSelector.val("-1");
}

function conditionalDropdown(event){
    const province = $('#province');
    $('#department').find("option").each(function (ev) {
        const option = $(this);

        const assocProvinceId = option.data("province");

        if(province.val() === "" + assocProvinceId){
            option.show();
        }else{
            option.hide();
        }
    });
}

/**
 *  Event handling
 */

$('#speciesId').on('change', hideOrShow);

$('#province').on('change', conditionalDropdown);

$(document).ready(conditionalDropdown);