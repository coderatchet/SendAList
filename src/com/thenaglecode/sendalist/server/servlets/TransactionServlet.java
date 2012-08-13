package com.thenaglecode.sendalist.server.servlets;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.thenaglecode.sendalist.server.domain2Objectify.interfaces.RequestProcessor;
import com.thenaglecode.sendalist.shared.OriginatorOfPersistentChange;
import com.thenaglecode.sendalist.shared.dto.ErrorSet;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static com.thenaglecode.sendalist.shared.dto.ErrorSet.UNKNOWN_FORMAT_ERROR;

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
 * in the future, the servlet will be able to process batch requests by iterating through multiple json objects.
 * </p>
 * <p>
 * the custom header <b>"{@value #PRETTY_JSON_HEADER}"</b> can be set to "pretty" to specify whether the response should be
 * outputted with clearly readable json as opposed to compact json. the default is compact.
 * </p>
 * <p>
 * the transactions can be in the format { "txs":[{//transaction 1},{//transaction 1},{//transaction 1}]} } or in
 * the format { "txs":{//a single transaction} }
 * </p>
 */
public class TransactionServlet extends HttpServlet {

    public static final String PRETTY_JSON_HEADER = "Json-Response-Format";
    public static final String FIELD_RESPONSES = "responses";

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) {
        //
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) {
        try {
            boolean isPrettyJson = false;
            OriginatorOfPersistentChange context = OriginatorOfPersistentChange.getContext(req);
            String isPrettyJsonValue = req.getHeader(PRETTY_JSON_HEADER);
            if (isPrettyJsonValue != null && "pretty".equalsIgnoreCase(isPrettyJsonValue)) isPrettyJson = true;

            //get the json data
            JsonReader reader = new JsonReader(req.getReader());
            JsonElement root = new JsonParser().parse(reader);
            PrintWriter writer = res.getWriter();

            JSONArray responses = new JSONArray();
            if (!root.isJsonObject()) {
                //incorrect format
                responses.put(UNKNOWN_FORMAT_ERROR);
            }

            if (responses.length() == 0) {
                JsonElement txsElement = root.getAsJsonObject().get("txs");
                if (txsElement == null || (!txsElement.isJsonArray() && !txsElement.isJsonObject())) {
                    //incorrect format
                    responses.put(UNKNOWN_FORMAT_ERROR);
                } else if (txsElement.isJsonArray()) {
                    //process each one
                    JsonArray array = txsElement.getAsJsonArray();
                    for (JsonElement element : array) {
                        if (!element.isJsonObject()) responses.put(UNKNOWN_FORMAT_ERROR);
                        else responses.put(processTransaction(element.getAsJsonObject(), context));
                    }
                } else if (txsElement.isJsonObject()) {
                    //process singular transaction
                    responses.put(processTransaction(txsElement.getAsJsonObject(), context));
                }
            }

            //return results
            res.setContentType("application/json");
            writer.append(getReturnData(responses, isPrettyJson));
            res.flushBuffer();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @NotNull
    JSONObject processTransaction(JsonObject tx, OriginatorOfPersistentChange context) {
        return new RequestProcessor().processTransaction(tx, context);
    }

    /**
     * converts the list of errors into a string suitable for return to the client
     *
     * @param responses the list of errors to return corresponding to each transaction
     * @param pretty    whether the response is compact or pretty (legible)
     * @return the errors in a json format { "errors":[{//error 1},{//error 2},...]}
     */
    public String getReturnData(@NotNull JSONArray responses, boolean pretty) {
        JSONObject root = new JSONObject();
        try {
            if (responses.length() == 0) {
                responses.put(new ErrorSet(400, "That's odd, no responses.", null));
            } else {
                root.put("responses", responses);
                return (pretty) ? root.toString(2) : root.toString();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new ErrorSet(500, "Server Error",
                "Something terrible has happened in the TransactionServlet").toString();
    }
}
