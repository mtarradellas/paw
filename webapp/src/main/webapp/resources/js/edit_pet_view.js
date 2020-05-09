

$(document).ready(function(ev){
    const selector = $(".delete-image-select");

    selector.each(function(ev){
        const val = $(this).attr("id");

        const assocCheckbox = $(`#delete-image-checkbox-${val}`);

        const isChecked = assocCheckbox.prop("checked");

        if(isChecked){
            $(this).addClass("image-checked");
        }else{
            $(this).removeClass("image-checked");
        }
    });

    selector.on('click', function(ev){
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