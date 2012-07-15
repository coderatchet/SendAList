package com.thenaglecode.sendalist.shared.dto;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: JELLYBEANS
 * Date: 6/6/12
 * Time: 8:10 AM
 *
 * Contains a list and it's task members
 */
public class GWTTaskList implements IsSerializable {
    /** generally an email address of the owner */
    private String ownerId;
    private List<GWTTask> tasks;
    //todo define variables

    public GWTTaskList() {
    }

    public static GWTTaskList fromJson(String jsonString){
        //todo implement
        return new GWTTaskList();
    }

    public void setTasks(List<GWTTask> tasks) {
        this.tasks = tasks;
    }

    public void setOwner(String ownerId) {
        this.ownerId = ownerId;
    }
}
