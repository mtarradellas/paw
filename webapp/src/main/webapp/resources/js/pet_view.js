
const imagesList = []

function displayModalOfImage(link){

    const modal = $('#image-modal');

    modal.find('img').attr('src', $(link).find('.img-thumbnail').attr('src'));

    modal.modal('show');

    const index = imagesList.findIndex(x=>x === link);

    const prevImg = modal.find('.prev-img');
    prevImg.off('click');
    if(index === 0){
        prevImg.addClass('disabled');
    }else{
        prevImg.removeClass('disabled');
        prevImg.on('click', function(ev){
            displayModalOfImage(imagesList[index - 1]);
        })
    }

    const nextImg = modal.find('.next-img');
    nextImg.off('click');
    if(index === imagesList.length - 1){
        nextImg.addClass('disabled');
    }else{
        nextImg.removeClass('disabled');
        nextImg.on('click', function(ev){
            displayModalOfImage(imagesList[index + 1]);
        })
    }
}

const link = $('.pet-photo-link');

link.on('click', function(evt){
    displayModalOfImage(this);
});

link.each((i, link)=>{
    imagesList.push(link);
})
