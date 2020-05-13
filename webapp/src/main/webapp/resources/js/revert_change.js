function revertChangeSetAsModified(selector){
    const div = selector.closest(".input-modifiable-div");

    const data = "" + div.data("current");
    const val = selector.val();

    if(val !== data){
        div.addClass("input-modified");
    }else{
        div.removeClass("input-modified");
    }
}

function initializeRevertChange(index, obj){
    revertChangeSetAsModified($(obj));
}

function clickRevertButton(buttonSelector){
    const div = buttonSelector.closest(".input-modifiable-div");
    const input = buttonSelector.siblings(".input-modifiable");

    const revertAlsoButton = buttonSelector.data("revert-also");
    if(revertAlsoButton){
        $("#"+revertAlsoButton).trigger("click");
    }

    input.val("" + div.data("current")).trigger("click");

    div.removeClass("input-modified");

    input.removeClass("is-invalid");

    div.siblings(".text-error").remove();
}

function thisWrapper(func){
    return function(ev){
        func($(this));
    }
}

$(document).ready(function(ev){

    const modifiableDiv = $(".input-modifiable-div");

    modifiableDiv.find(".revert-input-anchor").on("click", thisWrapper(clickRevertButton));

    modifiableDiv.find('input[type=text]').on('input', thisWrapper(revertChangeSetAsModified));
    modifiableDiv.find('input[type=number]').on('input', thisWrapper(revertChangeSetAsModified));
    modifiableDiv.find('input[type=date]').on('change', thisWrapper(revertChangeSetAsModified));
    modifiableDiv.find('select').on('change', thisWrapper(revertChangeSetAsModified));

    modifiableDiv.find(".input-modifiable").each(initializeRevertChange);

});