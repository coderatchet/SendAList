package com.thenaglecode.sendalist.server.domain2Objectify.interfaces;

import com.google.gson.JsonObject;

/**
 * this interface denotes a class which has persistence code. persistence code will detect whether a change has been
 * made and reports null if it has. otherwise it will report an error when there are problems or report Nop when the
 * object wasn't changed by the transaction.
 * <br/><br/>
 * <p/>
 * Created by IntelliJ IDEA.
 * User: Jared Nagle
 * Date: 14/07/12
 * Time: 5:57 PM
 */
public interface Processable {
    public final static String Nop = "Nop";

    /**
     * a processable object receives a json object that represents the object that is to be persisted in the database.
     * the function then determines whether any changes need to be made to the object (type specified by "c" and id
     * specified by "i"). the function modifies the raw variables accordingly. if no changes are made, "{@value #Nop}" is
     * returned. if there was an error with the transaction, e.g. the json object was malformed or had incorrect fields
     * or values, the appropriate error message is returned specifying what needs to happen to rectify the situation
     * in future. delete transactions are processed by the caller of this function. i.e. detect whether there is a
     * "del" field and process the deletion <br/>
     * see <a href="https://github.com/naredjagle/SendAList/issues/1" >Issue 1</a>
     * @param tx the json object to be processed, unless the main processing unit, the class and id ("c" and "i") fields
     *           are processed by the calling function.
     * @return Error string if problem, Nop if no change or null if the transaction was valid
     */
    String processTransaction(JsonObject tx);

    /**
     * This function ensures that this object is a valid object with the correct variable values.
     * */
    boolean isSafeToPersist();
}
