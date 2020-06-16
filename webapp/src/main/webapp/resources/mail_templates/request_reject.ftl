<html>
    <head>
        <title></title>
    </head>
    <body class="bg-light">
        <h1>${msg.getMessage("mail.from")}:</h1>
        <hr>
        <h3>${msg.getMessage("mail.hello")}</h3>
        <p>${msg.getMessage("mail.request_reject.description")}</p>
        <ul>
            <li>${msg.getMessage("mail.request_reject.user")}: <a href="${ownerURL}">${ownerUsername}</a></li>
            <li>${msg.getMessage("mail.request_reject.pet")}: <a href="${petURL}">${petName}</a></li>
        </ul>
        <p>${msg.getMessage("mail.request_reject.info")}</p>
        <p>${msg.getMessage("mail.goodbye")}</p>
        <hr>
        <small>${msg.getMessage("mail.wrongMail")}</small>
    </body>
</html>
