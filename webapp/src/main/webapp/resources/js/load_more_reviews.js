function createScore(score){
    const MAX_SCORE = 5;
    let result = '';

    for(let i=0; i<score; i++){
        result += '<i class="fas fa-star star-rating"></i>';
    }

    for(let i=score; i<MAX_SCORE; i++){
        result += '<i class="far fa-star star-rating"></i>';
    }

    return result;
}

function createLinkToUser(userId, username){
    return '' +
        '<a href="' + SERVER_URL + '/user/' + userId + '">' +
            username +
        '</a>';
}

function createDescriptionAndCol(description){
    return '' +
        '<div class="col">' +
            description +
        '</div>';
}

function createColumn(content){
    return '' +
        '<div class="col-lg-2">' +
            content +
        '</div>';
}

function createRow(userId, username, score, description){
    return '' +
        '<div class="row ml-0 mr-0 bg-white">' +
            createColumn(createLinkToUser(userId, username)) +
            createColumn(createScore(score)) +
            createDescriptionAndCol(description) +
        '</div>';
}

function ajaxSuccess(data){
    const mapped = data.reviewList.map(review=>{
        const {ownerId, ownerUsername, score, content} = review.review;
        return createRow(ownerId, ownerUsername, score, content);
    });

    mapped.forEach(x=>{
        $('.js-reviews-container').append(x);
    })

    if(data.maxPage <= currentPage){
        $('.load-more').remove();

        if(currentPage === 1 && data.maxPage === 0){
            $('.questions').append(noQuestionsYet());
        }
    }

    currentPage++;
}

let currentPage = 1;
function queryForReviews(){
    $.ajax({
        type: "GET",
        url: SERVER_URL + "/user/" + USER_ID + "/reviews" + "?page=" + currentPage,
        success: ajaxSuccess,
        dataType: "json"
    });
}

$(document).ready(function(){
    queryForReviews();

    $('.load-more').on('click', function(ev){
        queryForReviews();
    });
});