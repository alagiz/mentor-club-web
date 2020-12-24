const aws = require("aws-sdk");

exports.handler = async (event) => {
    const ses = new aws.SES({region: "us-east-2"});

    const params = {
        Destination: {
            ToAddresses: [event.destinationEmail],
        },
        Message: {
            Body: {
                Text: {
                    Data: `${event.textMessageBody}`
                },
                Html: {
                    Data: `<div style="margin:0 20px"><div class="adM">
      </div><div style="margin:10px 0"><strong>Hi ${event.username}!</strong></div>
      <div style="margin:10px 0">${event.messageBody}</div>
      <div style="margin:10px 0">
        <a href="${event.confirmationUrl}" style="text-decoration:none;background-color:#205081;padding:10px 15px;color:white;display:inline-block" target="_blank" data-saferedirecturl="${event.confirmationUrl}">${event.buttonText}</a>
      </div>
      <div style="margin:10px 0">
        
      </div>
      <div style="margin:10px 0">
        If you have any problems performing this action, feel free to email us at <a href="mailto:alagizov@gmail.com" target="_blank">alagizov@gmail.com</a>.
      </div>
      <div style="margin:10px 0;color:grey;font-size:0.9em">
        Thanks,<br>
        The Mentors
      </div>
    </div>`
                }
            },

            Subject: {Data: event.emailTopic},
        },
        Source: "no-reply@artem-alagizov.com",
    };

    return ses.sendEmail(params).promise();
};