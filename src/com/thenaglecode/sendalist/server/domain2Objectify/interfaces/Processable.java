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
    /**
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
