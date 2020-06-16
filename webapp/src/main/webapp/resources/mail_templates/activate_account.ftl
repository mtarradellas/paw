<html>
    <head>
        <title></title>
    </head>
    <body class="bg-light">
        <h1>${msg.getMessage("mail.from")}:</h1>
        <hr>
        <h3>${msg.getMessage("mail.hello")}</h3>
        <p>${msg.getMessage("mail.activate_account.description")}</p>
        <p>${msg.getMessage("mail.activate_account.name")}: ${username}</p>
        <p><a href="${URLToken}">${msg.getMessage("mail.activate_account.link")}</a></p>
        <p>${msg.getMessage("mail.goodbye")}</p>
        <hr>
        <small>${msg.getMessage("mail.activate_account.wrongMail")}</small>
    </body>
</html>
