package com.thenaglecode.sendalist.client.list;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.thenaglecode.sendalist.client.ClientFactory;
import com.thenaglecode.sendalist.shared.dto.GWTTask;
import com.thenaglecode.sendalist.shared.dto.GWTTaskList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Jared Nagle
 * Date: 4/06/12
 * Time: 10:27 PM
 */
public class ListActivity extends AbstractActivity{

    private ListPlace listPlace;
    private ClientFactory clientFactory;
    private ListView listView;
    private List<GWTTaskList> tasks;
    private boolean isInitialMessageInTextBox;

    public ListActivity(ListPlace listPlace, ClientFactory clientFactory) {
        this.listPlace = listPlace;
        this.clientFactory = clientFactory;
        this.listView = clientFactory.getListView();
        tasks = listPlace.getTasks();
        isInitialMessageInTextBox = true;
        init();
    }
    //todo constructor

    private void init(){
        listView.getTextBox().addFocusHandler(new FocusHandler() {
            @Override //change the welcome text from grey to black to signify typing
            public void onFocus(FocusEvent event) {
                if(isInitialMessageInTextBox){
                    listView.getTextBox().setText("");
                    listView.getTextBox().addStyleName("gwt-textBox-addTaskBox_typing");
                }
            }
        });
    }

    private static String[] dummyVerbs = {
        "Eat", "Dance with", "Laugh about", "Complain at", "Shop for", "Look like"
    };

    private static String[] dummyAdjs = {
        "silly", "good looking", "amazing", "hairy", "sightly", "munchable", "eloquent", "mischevious"
    };

    private static String[] dummyNouns = {
        "monkey", "flag pole", "president", "source code", "pina Colata", "maple syrup", ""
    };

    public GWTTaskList generateRandomTasks(int numOfTasks){
        List<GWTTask> tasks = new ArrayList<GWTTask>();
        int maxItems = 21;
        int maxVerbs = dummyVerbs.length;
        int maxAdjs = dummyAdjs.length;
        int maxNouns = dummyNouns.length;

        //generate tasks
        for(int i=0; i < numOfTasks; i++){
            StringBuilder s = new StringBuilder();
            int items = Random.nextInt(maxItems) + 1;
            s.append(dummyVerbs[Random.nextInt(maxVerbs)]).append(' ');
            s.append(items).append(' ');
            s.append(dummyAdjs[Random.nextInt(maxAdjs)]).append(' ');
            s.append(dummyNouns[Random.nextInt(maxNouns)]);
            if (items == 1) s.append('s');
            tasks.add(new GWTTask(Random.nextBoolean(), s.toString()));
        }

        GWTTaskList taskList = new GWTTaskList();
        taskList.setTasks(tasks);
        taskList.setOwner(clientFactory.getLoggedInUser().getEmail());
        return taskList;
    }

    public void refreshList(){
        //generate a random list of tasks up to 10 tasks
        listView.setTaskListData(generateRandomTasks(Random.nextInt(11)));
    }

    @Override
    public void start(AcceptsOneWidget panel, EventBus eventBus) {
        //todo implement
    }
}
