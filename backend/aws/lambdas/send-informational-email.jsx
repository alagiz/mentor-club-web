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
        
      </div>
      <div style="margin:10px 0">
        If you have any questions, feel free to email us at <a href="mailto:alagizov@gmail.com" target="_blank">alagizov@gmail.com</a>.
      </div>
      <div style="margin:10px 0;color:grey;font-size:0.9em">
        Thanks,<br>
        The Company
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
    