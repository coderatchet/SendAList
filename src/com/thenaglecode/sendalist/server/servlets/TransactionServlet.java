package com.thenaglecode.sendalist.server.servlets;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.thenaglecode.sendalist.server.domain2Objectify.interfaces.RequestProcessor;
import com.thenaglecode.sendalist.shared.OriginatorOfPersistentChange;
import org.jetbrains.annotations.NotNull;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    public static final ErrorSet UNKNOWN_FORMAT_ERROR = new ErrorSet(400, "unknown request format!",
            "the request was not in the correct format, please consult the api documentation for the desired formats");

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

            if (!root.isJsonObject()) {
                //incorrect format
                writer.append(getReturnData(Arrays.asList(UNKNOWN_FORMAT_ERROR), isPrettyJson));
                res.flushBuffer();
                return;
            }

            JsonElement txsElement = root.getAsJsonObject().get("txs");
            List<ErrorSet> errorSets = new ArrayList<ErrorSet>();
            if (txsElement == null || (!txsElement.isJsonArray() && !txsElement.isJsonObject())) {
                //incorrect format
                writer.append(getReturnData(Arrays.asList(UNKNOWN_FORMAT_ERROR), isPrettyJson));
                res.flushBuffer();
                return;
            } else if (txsElement.isJsonArray()) {
                //process each one
                JsonArray array = txsElement.getAsJsonArray();
                for (JsonElement element : array) {
                    if (!element.isJsonObject()) errorSets.add(UNKNOWN_FORMAT_ERROR);
                    else errorSets.add(processTransaction(element.getAsJsonObject(), context));
                }
            } else if (txsElement.isJsonObject()) {
                //process singular transaction
                errorSets.add(processTransaction(txsElement.getAsJsonObject(), context));
            }

            //return results
            res.setContentType("application/json");
            writer.append(getReturnData(errorSets, isPrettyJson));
            res.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @NotNull
    ErrorSet processTransaction(JsonObject tx, OriginatorOfPersistentChange context) {
        ErrorSet errorSet = new ErrorSet();
        RequestProcessor processor = new RequestProcessor();
        String err = processor.processTransaction(tx, context);
        if (null == err) {
            errorSet.setCode(HttpServletResponse.SC_OK);
            errorSet.setMessage("Success");
        } else {
            if (RequestProcessor.returnedError(err)) {
                errorSet.setCode(HttpServletResponse.SC_BAD_REQUEST);
                errorSet.setMessage("Bad Request");
                errorSet.setReason(err);
            } else {
                errorSet.setCode(HttpServletResponse.SC_NOT_MODIFIED);
                errorSet.setReason("No Change");
            }
        }
        return errorSet;
    }

    /**
     * converts the list of errors into a string suitable for return to the client
     * @param errors the list of errors to return corresponding to each transaction
     * @param pretty whether the response is compact or pretty (legible)
     * @return the errors in a json format { "errors":[{//error 1},{//error 2},...]}
     */
    public String getReturnData(@NotNull List<ErrorSet> errors, boolean pretty) {
        Gson gson = (pretty) ? new GsonBuilder().setPrettyPrinting().create() : new GsonBuilder().create();
        JsonObject root = new JsonObject();
        JsonArray errorArray = new JsonArray();
        if (errors.size() == 0) {
            errorArray.add(new ErrorSet(400, "That's odd, no error sets.", null).toJson());
        } else {
            for (ErrorSet errorSet : errors) {
                errorArray.add(errorSet.toJson());
            }
        }
        root.add("errors", errorArray);
        return gson.toJson(root);
    }

    public static class ErrorSet {

        public int code;
        public String message;
        public String reason = null;

        public ErrorSet() {
        }

        public ErrorSet(int code, String message, String reason) {
            this.code = code;
            this.message = message;
            this.reason = reason;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public JsonObject toJson() {
            Gson gson = new GsonBuilder().create();
            return (JsonObject) gson.toJsonTree(this);
        }
    }
}
