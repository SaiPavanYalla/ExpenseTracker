package com.xa.xpensauditor;

import static com.xa.xpensauditor.AddTransactionActivity.printLog;

import android.os.AsyncTask;

import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.resource.Email;
import com.mailjet.client.resource.Emailv31;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EmailSender extends AsyncTask<Void, Integer, Boolean> {

    private String email;
    private String amt;
    private String shop;
    private String cat;

    public EmailSender(String email, String amt, String shop, String cat) {
        this.email = email;
        this.amt = amt;
        this.shop = shop;
        this.cat = cat;
    }

    private Boolean sendEmailUpdate(String message) {
        MailjetClient client;
        MailjetRequest request;
        MailjetResponse response;

        String api_key;
        String secret_key;

        ClientOptions clientOptions = ClientOptions.builder().apiKey(api_key).apiSecretKey(secret_key).build();

        printLog("created the client and stuff, trying to send an email now!");

        client = new MailjetClient(clientOptions);

        printLog("created the client with the required options");

        String from_email_id = "spsusarl@ncsu.edu";
        String from_email_name = "Xpense Auditor Group 11";

        String subject = "XpenseAuditorG11 -> Shared Expense Alert!";
        String textpart = "Shared expense notification message";
        String content = "<h3>You've been added to an expense!</h3><br />You were added to an expense at " + shop + ", costing " + amt + ", for " + cat;
        String id = "xpense_auditor_g11_message";

        try {
            request = new MailjetRequest(Emailv31.resource)
                    .property(Emailv31.MESSAGES, new JSONArray()
                            .put(new JSONObject()
                                    .put(Emailv31.Message.FROM, new JSONObject()
                                            .put("Email", from_email_id)
                                            .put("Name", from_email_name))
                                    .put(Emailv31.Message.TO, new JSONArray()
                                            .put(new JSONObject()
                                                    .put("Email", email)
                                                    .put("Name", email)))
                                    .put(Emailv31.Message.SUBJECT, subject)
                                    .put(Emailv31.Message.TEXTPART, textpart)
                                    .put(Emailv31.Message.HTMLPART, content)
                                    .put(Emailv31.Message.CUSTOMID, id)));
        } catch ( JSONException e ) {
            printLog("json exception when creating the contents for the email , " + e.getMessage());
            return false;
        }
        printLog("created a request with all the contents involved, " + request.toString());
        try {
            response = client.post(request);
        } catch  (MailjetException e) {
            printLog("exception when sending the email, " + e.getMessage());
            return false;
        }
        printLog("email response status : " + response.getStatus());
        printLog("email response data : " + response.getData());
        return true;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        return sendEmailUpdate("");
    }
}
