
$('.pet-photo-link').on('click', function(evt){
    const modal = $('#image-modal');

    modal.find('img').attr('src', $(this).find('.pet-photo').attr('src'));

    modal.modal('show');
});
