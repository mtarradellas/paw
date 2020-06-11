function createAnswer(answer){
    return '' +
        '<p class="a">' +
            '<svg class="bi bi-reply" width="1em" height="1em" viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg">' +
                '<path fill-rule="evenodd" d="M9.502 5.013a.144.144 0 0 0-.202.134V6.3a.5.5 0 0 1-.5.5c-.667 0-2.013.005-3.3.822-.984.624-1.99 1.76-2.595 3.876C3.925 10.515 5.09 9.982 6.11 9.7a8.741 8.741 0 0 1 1.921-.306 7.403 7.403 0 0 1 .798.008h.013l.005.001h.001L8.8 9.9l.05-.498a.5.5 0 0 1 .45.498v1.153c0 .108.11.176.202.134l3.984-2.933a.494.494 0 0 1 .042-.028.147.147 0 0 0 0-.252.494.494 0 0 1-.042-.028L9.502 5.013zM8.3 10.386a7.745 7.745 0 0 0-1.923.277c-1.326.368-2.896 1.201-3.94 3.08a.5.5 0 0 1-.933-.305c.464-3.71 1.886-5.662 3.46-6.66 1.245-.79 2.527-.942 3.336-.971v-.66a1.144 1.144 0 0 1 1.767-.96l3.994 2.94a1.147 1.147 0 0 1 0 1.946l-3.994 2.94a1.144 1.144 0 0 1-1.767-.96v-.667z"/>' +
            '</svg>' +
            `<a>${answer.username}</a>: ${answer.content}` +
        '</p>';
}

function createQuestion(question){
    return '' +
        '<p class="q">' +
            '<svg class="bi bi-question" width="1em" height="1em" viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg">' +
                '<path d="M5.25 6.033h1.32c0-.781.458-1.384 1.36-1.384.685 0 1.313.343 1.313 1.168 0 .635-.374.927-.965 1.371-.673.489-1.206 1.06-1.168 1.987l.007.463h1.307v-.355c0-.718.273-.927 1.01-1.486.609-.463 1.244-.977 1.244-2.056 0-1.511-1.276-2.241-2.673-2.241-1.326 0-2.786.647-2.754 2.533zm1.562 5.516c0 .533.425.927 1.01.927.609 0 1.028-.394 1.028-.927 0-.552-.42-.94-1.029-.94-.584 0-1.009.388-1.009.94z"/>' +
            '</svg>' +
            `<a>${question.username}</a>: ${question.content}` +
        '</p>';

}

function createRespondButton(questionId){
    return '' +
        '<button data-questionId="' + questionId + '">' +
            '' +
        '</button>';
}

function createQuestionAndAnswer(question, answer, isOwner, questionId){
    const shouldRespond = !answer && isOwner;

    return '' +
        '<li class="question list-group-item">' +
            createQuestion(question) +
            (answer ? createAnswer(answer) : '') +
            (shouldRespond ? createRespondButton(questionId) : '') +
        '</li>';
}

function ajaxCallMock(page){
    const pageSize = 3;
    const allQuestions = [
        {
            question: {
                username: 'fastiz',
                content: 'Hola, tienen de otro color?'
            },
            answer: {
                username: 'Duenio',
                content: 'No capo'
            }
        },
        {
            question: {
                username: 'pepe',
                content: 'tienen stock'
            }
        },
        {
            question: {
                username: 'martin',
                content: 'por donde estas?'
            },
            answer: {
                username: 'Duenio',
                content: 'Pte savedra'
            }
        },
        {
            question: {
                username: 'fastiz',
                content: 'Hola, tienen de otro color?'
            },
            answer: {
                username: 'Duenio',
                content: 'No capo'
            }
        },
        {
            question: {
                username: 'pepe',
                content: 'tienen stock'
            }
        },
        {
            question: {
                username: 'martin',
                content: 'por donde estas?'
            },
            answer: {
                username: 'Duenio',
                content: 'Pte savedra'
            }
        },
        {
            question: {
                username: 'fastiz',
                content: 'Hola, tienen de otro color?'
            },
            answer: {
                username: 'Duenio',
                content: 'No capo'
            }
        }
    ];
    return new Promise((resolve, reject)=>{
        setTimeout(()=>{
            resolve({
                response: allQuestions.slice(page*pageSize, (page + 1)*pageSize),
                moreToQuery: (page + 1)*pageSize < allQuestions.length
            });
        }, 200);
    })
}

let currentPage = 0;
async function queryForQuestions(){
    const queryResult = await ajaxCallMock(currentPage++);

    const mapped = queryResult.response.map(result=>{
        const {question, answer} = result;
        return createQuestionAndAnswer(question, answer, false);
    });

    mapped.forEach(x=>{
        $('.questions').append(x);
    })

    if(queryResult.moreToQuery === false){
        $('.load-more').remove();
    }

}

$(document).ready(function(){
    queryForQuestions();

    $('.load-more').on('click', function(ev){
        queryForQuestions();
    });
});