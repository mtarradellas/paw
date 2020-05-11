$('#search-criteria').on('change', function(event){

    const selectedCriteria = $(this).val();

    if(selectedCriteria === "any"){
        $('#search-order').attr('disabled', true);
    }else{
        $('#search-order').attr('disabled', false);
    }

});

