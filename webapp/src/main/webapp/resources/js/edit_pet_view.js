

$(document).ready(function(ev){
    $(".delete-image-select").on('click', function(ev){
        const val = $(this).attr("id");

        const assocCheckbox = $(`#delete-image-checkbox-${val}`);

        const isChecked = assocCheckbox.prop("checked");

        assocCheckbox.prop("checked", !isChecked);

        if(isChecked){
            $(this).removeClass("image-checked");
        }else{
            $(this).addClass("image-checked");
        }
        $(this).prop("checked", !isChecked);

    });
});