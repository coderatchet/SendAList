package com.thenaglecode.sendalist.client.list;

import com.thenaglecode.sendalist.client.placeGroups.MainPlace;
import com.thenaglecode.sendalist.shared.dto.GWTTaskList;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Jared Nagle
 * Date: 4/06/12
 * Time: 10:27 PM
 */
public class ListPlace extends MainPlace {
    private List<GWTTaskList> tasks;

    public List<GWTTaskList> getTasks() {
        return tasks;
    }
}
