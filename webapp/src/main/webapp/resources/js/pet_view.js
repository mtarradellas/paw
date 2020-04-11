
$('.pet-photo-link').bind('click', function(evt){
    const modalImage = $('#image-modal img');

    console.log(jQuery(".pet-photo", this));
    modalImage.attr('src', jQuery(".pet-photo", this).attr('src'));

    $('#image-modal').modal('show');
});
