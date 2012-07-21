package com.thenaglecode.sendalist.server.servlets;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.thenaglecode.sendalist.server.domain2Objectify.interfaces.RequestProcessor;
import com.thenaglecode.sendalist.shared.OriginatorOfPersistentChange;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Jared Nagle
 * Date: 15/07/12
 * Time: 8:12 PM
 * <p/>
 * The transaction servlet will handle json objects that need to be processed.
 * <p/>
 * <p>
 * This will not handle the validity of a request, such as if a user is allowed to perform the operation, but will
 * possibly handle any DOS attacks such as spammed objects from a particular ip address.
 * </p>
 * <p>
 *     in the future, the servlet will be able to process batch requests by iterating through multiple json objects.
 * </p>
 */
public class TransactionServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) {
        //
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) {
        try {
            String returnMessage;
            String returnReason = null;
            int returnCode;
            OriginatorOfPersistentChange context = OriginatorOfPersistentChange.getContext(req);
            JsonReader reader = new JsonReader(req.getReader());
            JsonElement json = new JsonParser().parse(reader);
            RequestProcessor processor = new RequestProcessor();
            String err = processor.processTransaction(json.getAsJsonObject(), context);
            if (null == err) {
                returnCode = 200;
                returnMessage = "success";
            } else {
                if (RequestProcessor.returnedError(err)) {
                    returnCode = 400;
                    returnMessage = "Bad Request";
                    returnReason = err;
                } else {
                    returnCode = 200;
                    returnMessage = "no change";
                }
            }

            res.setContentType("application/json");
            JsonObject obj = new JsonObject();
            obj.addProperty("message", returnMessage);
            obj.addProperty("code", returnCode);
            if (returnReason != null) obj.addProperty("reason", returnReason);

            json.getAsJsonObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
