
function maxValueNumber(e){
    const maxValue = $(this).data("max") || 999999999;

    if ($(this).val() > maxValue
        && e.keyCode !== 46 // keycode for delete
        && e.keyCode !== 8 // keycode for backspace
    ) {
        e.preventDefault();
        $(this).val(maxValue);
    }

}

function maxValueText(e){
    const maxValue = $(this).data("max") || 255;

    const text = $(this).val();

    console.log(text);
    if (text.length > maxValue
        && e.keyCode !== 46 // keycode for delete
        && e.keyCode !== 8 // keycode for backspace
    ) {
        e.preventDefault();
        $(this).val(text.slice(0, maxValue));
    }
}


$(document).ready(function(){
    const selectorNumber = $('.input-max-value[type=number]');

    selectorNumber.on('keydown keyup', maxValueNumber);

    const selectorText = $('.input-max-value[type=text],textarea.input-max-value');

    selectorText.on('keydown keyup', maxValueText);

    const delegatorSelector = $('.input-max-value-delegator');

    delegatorSelector.on('keydown keyup', '.input-max-value[type=number]', maxValueNumber);
    delegatorSelector.on('keydown keyup', '.input-max-value[type=text],textarea.input-max-value', maxValueText);
});