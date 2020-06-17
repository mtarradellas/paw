
$('.are-you-sure').on('click', function(ev){
    ev.preventDefault();

    const btn = $(this);
    const modal = $('#are-you-sure-modal');
    const primaryBtn = $('.btn-danger', modal);
    const secondaryBtn = $('.btn-secondary', modal);
    const closeBtn = $('.close', modal);

    modal.modal('show');

    function closeModal(){
        modal.modal('hide');
        primaryBtn.off('click');
        secondaryBtn.off('click');
        closeBtn.off('click');
    }

    primaryBtn.on('click', function(ev){
        $(btn).closest('form').submit();
        closeModal();
    });

    secondaryBtn.on('click', function(ev){
        closeModal();
    });

    closeBtn.on('click', function(ev){
        closeModal();
    });

});

$('.accept-request').on('click', function(ev){
    ev.preventDefault();

    const btn = $(this);
    const modal = $('#accept-request');
    const primaryBtn = $('.btn-success', modal);
    const secondaryBtn = $('.btn-secondary', modal);
    const closeBtn = $('.close', modal);

    modal.modal('show');

    function closeModal(){
        modal.modal('hide');
        primaryBtn.off('click');
        secondaryBtn.off('click');
        closeBtn.off('click');
    }

    primaryBtn.on('click', function(ev){
        $(btn).closest('form').submit();
        closeModal();
    });

    secondaryBtn.on('click', function(ev){
        closeModal();
    });

    closeBtn.on('click', function(ev){
        closeModal();
    });

});