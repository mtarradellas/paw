function createAnswer(answer){
    return '' +
        '<p class="a">' +
            '<svg class="bi bi-reply" width="1em" height="1em" viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg">' +
                '<path fill-rule="evenodd" d="M9.502 5.013a.144.144 0 0 0-.202.134V6.3a.5.5 0 0 1-.5.5c-.667 0-2.013.005-3.3.822-.984.624-1.99 1.76-2.595 3.876C3.925 10.515 5.09 9.982 6.11 9.7a8.741 8.741 0 0 1 1.921-.306 7.403 7.403 0 0 1 .798.008h.013l.005.001h.001L8.8 9.9l.05-.498a.5.5 0 0 1 .45.498v1.153c0 .108.11.176.202.134l3.984-2.933a.494.494 0 0 1 .042-.028.147.147 0 0 0 0-.252.494.494 0 0 1-.042-.028L9.502 5.013zM8.3 10.386a7.745 7.745 0 0 0-1.923.277c-1.326.368-2.896 1.201-3.94 3.08a.5.5 0 0 1-.933-.305c.464-3.71 1.886-5.662 3.46-6.66 1.245-.79 2.527-.942 3.336-.971v-.66a1.144 1.144 0 0 1 1.767-.96l3.994 2.94a1.147 1.147 0 0 1 0 1.946l-3.994 2.94a1.144 1.144 0 0 1-1.767-.96v-.667z"/>' +
            '</svg>' +
            `<a>${answer.ownerUsername}</a>: ${answer.content}` +
        '</p>';
}

function createQuestion(question){
    return '' +
        '<p class="q">' +
            '<svg class="bi bi-question" width="1em" height="1em" viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg">' +
                '<path d="M5.25 6.033h1.32c0-.781.458-1.384 1.36-1.384.685 0 1.313.343 1.313 1.168 0 .635-.374.927-.965 1.371-.673.489-1.206 1.06-1.168 1.987l.007.463h1.307v-.355c0-.718.273-.927 1.01-1.486.609-.463 1.244-.977 1.244-2.056 0-1.511-1.276-2.241-2.673-2.241-1.326 0-2.786.647-2.754 2.533zm1.562 5.516c0 .533.425.927 1.01.927.609 0 1.028-.394 1.028-.927 0-.552-.42-.94-1.029-.94-.584 0-1.009.388-1.009.94z"/>' +
            '</svg>' +
            `<a>${question.ownerUsername}</a>: ${question.content}` +
        '</p>';
}

function answerForm(answerId){
        return '' +
            '<form class="form" method="post" action="' + SERVER_URL + '/pet/' + PET_ID + '/answer">' +
                '<div class="form-group mr-sm-3 mb-2">' +
                    '<input name="answerId" value="' + answerId + '" type="hidden" />' +
                    '<label for="questionInput" class="sr-only">' + WRITE_AN_ANSWER_TXT + '</label>' +
                    '<textarea name="content" class="form-control input-max-value" id="questionInput" placeholder="' + WRITE_AN_ANSWER_TXT + '"></textarea>' +
                '</div>' +
                '<button type="submit" class="btn btn-primary mb-2">' + SEND_ANSWER_TXT + '</button>' +
            '</form>';
}

function noAnswerYet(){
    return '' +
        '<p class="a">' +
            NO_ANSWER_YET_TXT +
        '</p>';
}

function createQuestionAndAnswer(question, answer, questionId){

    return '' +
        '<li class="question list-group-item">' +
            createQuestion(question) +
            (answer ? createAnswer(answer) : '') +
            (!answer && IS_OWNER ? answerForm(questionId) : '') +
            (!answer && !IS_OWNER ? noAnswerYet() : '') +
        '</li>';
}

function noQuestionsYet(){
    return '' +
        '<p class="a">' +
            NO_QUESTIONS_YET_TXT +
        '</p>';
}

function ajaxSuccess(data){
    const mapped = data.commentList.map(qa=>{
        const {question, answer} = qa;
        return createQuestionAndAnswer(question, answer, question.id);
    });

    mapped.forEach(x=>{
        $('.questions').append(x);
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
function queryForQuestions(){
    $.ajax({
        type: "GET",
        url: SERVER_URL + "/pet/" + PET_ID + "/comments" + "?page=" + currentPage,
        success: ajaxSuccess,
        dataType: "json"
    });
}

$(document).ready(function(){
    queryForQuestions();

    $('.load-more').on('click', function(ev){
        queryForQuestions();
    });
});