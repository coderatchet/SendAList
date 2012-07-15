package com.thenaglecode.sendalist.client.list;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import com.thenaglecode.sendalist.shared.dto.GWTTask;
import com.thenaglecode.sendalist.shared.dto.GWTTaskList;

/**
 * Created by IntelliJ IDEA.
 * User: Jared Nagle
 * Date: 4/06/12
 * Time: 5:19 PM
 */
public class ListViewImpl extends Composite implements ListView {
    private String BLANK_TEXT_MESSAGE = "Enter a task";

    @Override
    public void setTaskListData(GWTTaskList gwtTaskList) {
        //todo implement
    }

    @Override
    public void addTask(GWTTask gwtTask) {
        //todo implement
    }

    @Override
    public void removeTask(GWTTask gwtTask) {
        //todo implement
    }

    @Override
    public TextBox getTextBox() {
        return textBox;
    }

    @Override
    public HasClickHandlers getAddButton() {
        return addButton;
    }

    @Override
    public String getBlankTextMessage() {
        return BLANK_TEXT_MESSAGE;
    }

    interface ListViewImplUiBinder extends UiBinder<HTMLPanel, ListViewImpl> {
    }

    private static ListViewImplUiBinder ourUiBinder = GWT.create(ListViewImplUiBinder.class);

    @UiField
    Button addButton;
    @UiField
    TextBox textBox;
    @UiField
    FlexTable taskList;

    public ListViewImpl() {
        ourUiBinder.createAndBindUi(this);
        taskList.setText(0, 0, "done");
        taskList.setText(0, 1, "summary");
    }
}