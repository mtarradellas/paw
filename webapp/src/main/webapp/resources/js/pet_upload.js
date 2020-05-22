$('#adopt').on('click', function(){
    $('#price').attr('disabled', $(this).is(':checked'));
    if($(this).is(':checked')){
        $('#price').val('0')
    }else{
        $('#price').val('')
    }
});