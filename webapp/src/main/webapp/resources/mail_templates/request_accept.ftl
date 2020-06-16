<html>
    <head>
        <title></title>
    </head>
    <body class="bg-light">
        <h1>${msg.getMessage("mail.from")}:</h1>
        <hr>
        <h3>${msg.getMessage("mail.hello")}</h3>
        <p>${msg.getMessage("mail.request_accept.description")}</p>
        <ul>
            <li>${msg.getMessage("mail.request_accept.user")}: <a href="${ownerURL}">${ownerUsername}</a></li>
            <li>${msg.getMessage("mail.request_accept.pet")}: <a href="${petURL}">${petName}</a></li>
            <li>${msg.getMessage("mail.request_accept.userMail")}: ${contactEmail}</li>
        </ul>
        ${msg.getMessage("mail.request_accept.info")}
        <p>${msg.getMessage("mail.goodbye")}</p>
        <hr>
        <small>${msg.getMessage("mail.wrongMail")}</small>
    </body>
</html>
