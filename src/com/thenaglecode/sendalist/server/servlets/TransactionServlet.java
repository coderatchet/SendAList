package com.thenaglecode.sendalist.server.servlets;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
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
 */
public class TransactionServlet extends HttpServlet{
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res){
        //
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res){
        try {
            OriginatorOfPersistentChange context = OriginatorOfPersistentChange.getContext(req);
            JsonReader reader = new JsonReader(req.getReader());
            JsonParser parser = new JsonParser();
            JsonElement json = parser.parse(reader);
            json.getAsJsonObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
