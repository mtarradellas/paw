<html>
    <head>
        <title></title>
    </head>
    <body class="bg-light">
        <h1>${msg.getMessage("mail.from")}:</h1>
        <hr>
        <h3>${msg.getMessage("mail.hello")}</h3>
        <p>${msg.getMessage("mail.reset_password.description")}: <a href="${URLToken}"> ${URLToken}</a></p>
        <p>${msg.getMessage("mail.goodbye")}</p>
        <hr>
        <small>${msg.getMessage("mail.reset_password.wrongMail")}</small>
    </body>
</html>
