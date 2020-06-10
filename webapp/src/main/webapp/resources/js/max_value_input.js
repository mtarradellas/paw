

$(document).ready(function(){
    const selector = $('.input-max-value');

    selector.on('keydown keyup', function(e){
        const maxValue = $(this).data("max") || 999999999;

        if ($(this).val() > maxValue
            && e.keyCode !== 46 // keycode for delete
            && e.keyCode !== 8 // keycode for backspace
        ) {
            e.preventDefault();
            $(this).val(maxValue);
        }
    });
});