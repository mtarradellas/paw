

function parentModified(parent){

    const child = $('#'+parent.data("child"));

    child.find("option").each(function (index, obj) {
        const option = $(obj);

        const assoc = option.data("dependency");

        if(parent.val() === "-1"){
            child.attr("disabled", true);
            return;
        }else{
            child.attr("disabled", false);
        }

        if(!assoc || parent.val() === "" + assoc){
            option.show();
        }else{
            option.hide();
        }
    });

    const selectedOptionDep = child.find("option[value='" + child.val() + "']").data("dependency");
    if(selectedOptionDep && selectedOptionDep + "" !== parent.val()){
        child.val(-1).trigger("change");
    }

}

function initializeSelectorDependency(index, obj){
    parentModified($(obj));
}

function onClickSelectorDependency(){
    parentModified($(this));
}

$(document).ready(function(ev){
    const selectorParent = $(".selector-parent");

    selectorParent.each(initializeSelectorDependency);

    selectorParent.on('change', onClickSelectorDependency);
});