<html>
    <head>
        <title></title>
    </head>
    <body class="bg-light">
        <h1>${msg.getMessage("mail.from")}:</h1>
        <hr>
        <h3>${msg.getMessage("mail.hello")}</h3>
        <p>${msg.getMessage("mail.request_recover.description")}</p>
        <ul>
            <li>${msg.getMessage("mail.request_recover.user")}: <a href="${ownerURL}">${ownerUsername}</a></li>
            <li>${msg.getMessage("mail.request_recover.pet")}: <a href="${petURL}">${petName}</a></li>
        </ul>
        <a href="${requestURL}">${msg.getMessage("mail.request_recover.clickHere")}</a>
        <p>${msg.getMessage("mail.goodbye")}</p>
        <hr>
        <small>${msg.getMessage("mail.wrongMail")}</small>
    </body>
</html>