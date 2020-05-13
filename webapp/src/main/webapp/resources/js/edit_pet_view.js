

$(document).ready(function(ev){


    const deleteImageSelect = $(".delete-image-select");

    deleteImageSelect.each(function(ev){
        const val = $(this).attr("id");

        const assocCheckbox = $(`#delete-image-checkbox-${val}`);

        const isChecked = assocCheckbox.prop("checked");

        if(isChecked){
            $(this).addClass("image-checked");
        }else{
            $(this).removeClass("image-checked");
        }
    });


    deleteImageSelect.on('click', function(ev){
        const val = $(this).attr("id");

        const assocCheckbox = $(`#delete-image-checkbox-${val}`);

        const isChecked = assocCheckbox.prop("checked");

        assocCheckbox.prop("checked", !isChecked);

        if(isChecked){
            $(this).removeClass("image-checked");
        }else{
            $(this).addClass("image-checked");
        }

    });

});