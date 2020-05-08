function readURL(input, func) {
    for(let i=0; i<input.files.length; i++){
        const reader = new FileReader();

        reader.onload = function(e) {
            func(e);
        }

        reader.readAsDataURL(input.files[i]); // convert to base64 string
    }
}

$(document).ready(function(event){
    const imageUpload = $('.image-upload');

    imageUpload.find('.photos-input').on('change', function(ev){
        const closestImageUpload = $(this).closest('.image-upload');

        const imagePreviewContainer = closestImageUpload.find('.image-preview-container');
        imagePreviewContainer.empty();

        function handler(e){
            imagePreviewContainer.append(
                `<img src="${e.target.result}" alt="" class="pet-photo-preview">`
            );
        }

        readURL(this, handler);
    })

});
