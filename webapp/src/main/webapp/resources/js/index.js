
/**
 *  Event handling
 */

$('#filter-specie').bind('change', event=>{

    const selectedSpecie = $('#filter-specie').val();

    console.log(selectedSpecie);

    if(selectedSpecie === "specie-any"){
        $('#filter-breed').attr('disabled', true);
    }else{
        $('#filter-breed').attr('disabled', false);
    }

    const breedOptions = $('#filter-breed > option');

    breedOptions.filter("."+selectedSpecie).show();
    breedOptions.not("."+selectedSpecie).hide();
    breedOptions.find("specie_any").show();
});


$('#search-criteria').bind('change', event=>{

    const selectedCriteria = $('#search-criteria').val();

    if(selectedCriteria === "any"){
        $('#search-order').attr('disabled', true);
    }else{
        $('#search-order').attr('disabled', false);
    }

});


$('#search-tools-submit').bind('click', event=>{
    //GET of index.jsp with search query
});