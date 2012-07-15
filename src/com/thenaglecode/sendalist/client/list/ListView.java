package com.thenaglecode.sendalist.client.list;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.TextBox;
import com.thenaglecode.sendalist.client.Presenter;
import com.thenaglecode.sendalist.shared.dto.GWTTask;
import com.thenaglecode.sendalist.shared.dto.GWTTaskList;

/**
 * Created by IntelliJ IDEA.
 * User: Jared Nagle
 * Date: 4/06/12
 * Time: 4:41 PM
 */
public interface ListView extends IsWidget{

    void setTaskListData(GWTTaskList gwtTaskList);
    void addTask(GWTTask gwtTask);
    void removeTask(GWTTask gwtTask);
    TextBox getTextBox();
    HasClickHandlers getAddButton();
    String getBlankTextMessage();

    public interface ListPresenter extends Presenter{
        //todo /*void setData()*/
    }
}
