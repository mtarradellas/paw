function setModified(ev){
    const div = $(this).closest(".input-modifiable-div");

    const data = "" + div.data("current");
    const val = $(this).val();

    console.log(val, data);

    if(val !== data){
        div.addClass("input-modified");
    }else{
        div.removeClass("input-modified");
    }
}

$(document).ready(function(ev){
    const modifiableDiv = $(".input-modifiable-div");

    modifiableDiv.find(".revert-input-anchor").on("click", function(ev){
        const div = $(this).closest(".input-modifiable-div");
        const input = $(this).siblings(".input-modifiable");

        input.val("" + div.data("current"));

        div.removeClass("input-modified");
    });

    modifiableDiv.find('input[type=text]').on('input', setModified);

    modifiableDiv.find('input[type=number]').on('input', setModified);
    modifiableDiv.find('input[type=date]').on('change', setModified);
    modifiableDiv.find('select').on('change', setModified);

    const inputs = modifiableDiv.find(".input-modifiable");

    inputs.each(setModified);

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