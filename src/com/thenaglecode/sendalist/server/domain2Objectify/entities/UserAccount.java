package com.thenaglecode.sendalist.server.domain2Objectify.entities;

import com.google.apps.easyconnect.easyrp.client.basic.data.Account;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Unindexed;
import com.googlecode.objectify.condition.IfFalse;
import com.thenaglecode.sendalist.server.Globals;
import com.thenaglecode.sendalist.server.domain2Objectify.SendAListDAO;
import com.thenaglecode.sendalist.server.domain2Objectify.interfaces.Processable;
import com.thenaglecode.sendalist.server.domain2Objectify.interfaces.ToJson;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Jared Nagle
 * Date: 3/06/12
 * Time: 11:46 AM
 */

public class UserAccount implements Account, ToJson, Processable {
    private static UserAccount testUser = new UserAccount();
    public static final int SALT_LENGTH = 10;
    public static final String FIELD_CLASS = "c"; //not displayed
    public static final String FIELD_ID = "i"; //read-only
    public static final String FIELD_FIRST = "fname"; //writable
    public static final String FIELD_LAST = "lname"; //writable
    public static final String FIELD_PASS = "pass"; //not displayed //writable
    public static final String FIELD_DISPLAY = "display"; //writable
    public static final String FIELD_PIC_URL = "picurl"; //writable
    public static final String FIELD_FED = "isfed"; //read-only

    static {
        testUser.setEmail("test@sendalist.com");
        testUser.setFirstName("tester");
        testUser.setLastName("man");
        testUser.setDisplayName("test");
        testUser.setPhotoUrl("/test");
    }

    @Id
    String email;

    @Unindexed(IfFalse.class)
    boolean isAdmin;
    String firstName;
    String lastName;
    String displayname;
    @Unindexed
    String photoUrl;
    @Unindexed
    String encryptedPassword;
    @Unindexed
    String ps;
    private static final String DEL = "del";

    /**
     * sets the last name of the user
     *
     * @param lastName the new last name of the user
     * @return this UserAccount, daisy chain style
     */
    public UserAccount setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    /**
     * sets the first name of the user
     *
     * @param firstName the new first name of the user
     * @return this UserAccount, daisy chain style
     */
    public UserAccount setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    /**
     * sets the url that contains the photo of this user
     *
     * @param photoUrl the url that contains the photo of this user
     * @return this UserAccount, daisy chain style
     */
    public UserAccount setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
        return this;
    }

    /**
     * sets the display name to be used by this user
     *
     * @param displayName the new display name for this user
     * @return this UserAccount, daisy chain style
     */
    public UserAccount setDisplayName(String displayName) {
        this.displayname = displayName;
        return this;
    }

    /**
     * sets the email to be used for this user
     *
     * @param email the email for this user
     * @return this UserAccount, daisy chain style
     */
    public UserAccount setEmail(String email) {
        this.email = email;
        return this;
    }

    /**
     * sets whether this user is an admin or not
     *
     * @param isAdmin whether this user is an admin or not
     * @return this UserAccount, daisy chain style
     */
    public UserAccount setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
        return this;
    }

    /**
     * sets the password used for this account, also hashes it with a randomly generated salt that is stored along side
     * the password. to delete the password (for changing from legacy to federated account) set the password to
     * "{@value #DEL}"
     *
     * @param plainText the plain text password to set
     * @return this UserAccount, daisy chain style
     */
    public UserAccount setPassword(@NotNull String plainText) {
        if (plainText.equals(DEL)) {
            this.encryptedPassword = null;
        } else {
            BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
            if (ps == null) ps = Globals.generateRandomToken(SALT_LENGTH);
            encryptedPassword = passwordEncryptor.encryptPassword(getPS() + plainText);
        }
        return this;
    }

    /**
     * get the keys of the task list linked to this user
     *
     * @return the List of TaskList keys linked to this user
     */
    public List<Key<TaskList>> getTaskLists() {
        if (taskLists == null) {
            taskLists = new ArrayList<Key<TaskList>>();
        }
        return taskLists;
    }

    public List<TaskList> getTaskListsFromDatabase() {
        return new SendAListDAO().ofy().query(TaskList.class).filter("owner", this).list();
    }

    List<Key<TaskList>> taskLists = null;


    /**
     * returns whether this account is a federated one or not. the function checks whether the password field is null.
     *
     * @return true if is federated, false if not
     */
    @Override
    public boolean isFederated() {
        return this.encryptedPassword == null;
    }

    /**
     * gets the email (id) of this UserAccount
     *
     * @return the email of this account
     */
    @Override
    public String getEmail() {
        return email;
    }

    /**
     * returns whether this account is an admin or not
     *
     * @return true if the user is an admin, false if not
     */
    public boolean isAdmin() {
        return isAdmin;
    }

    /**
     * gets the hashed password for this user
     *
     * @return the hashed password for this user
     */
    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    /**
     * gets the users display name
     *
     * @return the display name if set otherwise the first and last name if set, otherwise the term "User"
     */
    @Override
    public String getDisplayName() {
        if (displayname != null) {
            return displayname;
        } else {
            if (firstName != null) {
                if (lastName != null) {
                    return firstName + " " + lastName;
                } else return firstName;
            }
            return "User";
        }
    }

    /**
     * returns the url for the user's photo
     *
     * @return the url for the user's photo
     */
    @Override
    public String getPhotoUrl() {
        return photoUrl;
    }

    /**
     * returns the user's first name
     *
     * @return the user's first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * returns the user's last name
     *
     * @return the user's last name.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * serialises this user into Json format. includes email, first and last name, display name, photo Url and whether
     * this account is federated or not.
     *
     * @return a formatted json object representing this user
     */
    @Override
    public String toJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty(FIELD_ID, getEmail());
        if (getFirstName() != null) obj.addProperty(FIELD_FIRST, getFirstName());
        if (getLastName() != null) obj.addProperty(FIELD_LAST, getLastName());
        if (getDisplayName() != null) obj.addProperty(FIELD_DISPLAY, getDisplayName());
        if (getPhotoUrl() != null) obj.addProperty(FIELD_PIC_URL, getPhotoUrl());
        obj.addProperty(FIELD_FED, isFederated());
        return obj.toString();
    }

    /**
     * @return the salt used to generate the encrypted password
     */
    @NotNull
    public String getPS() {
        if (ps == null) ps = Globals.generateRandomToken(SALT_LENGTH);
        if (ps == null) return "aww fishsticks";
        return ps;
    }

    /**
     * generates the key that is used to access this object from the Objectify database
     *
     * @return the key for this user
     */
    public Key<UserAccount> generateKey() {
        return new Key<UserAccount>(UserAccount.class, email);
    }

    /**
     * adds a TaskList key to the object
     *
     * @param taskListKey the key of the TaskList that will be added
     * @return this UserAccount, daisy chain style
     */
    private UserAccount addTaskList(@NotNull Key<TaskList> taskListKey) {
        List<Key<TaskList>> taskLists = getTaskLists();
        boolean found = false;
        for (Key<TaskList> key : getTaskLists()) {
            if (key.equals(taskListKey)) {
                found = true;
                break;
            }
        }
        if (!found) taskLists.add(taskListKey);
        return this;
    }

    public UserAccount saveAndAddTaskLists(@NotNull TaskList... taskLists) {
        SendAListDAO dao = new SendAListDAO();
        for (TaskList taskList : taskLists) {
            taskList.setOwner(this.generateKey());
            Key<TaskList> key = dao.saveTaskList(taskList);
            addTaskList(key);
        }
        dao.saveUser(this);
        return this;
    }

    /**
     * saves the task list to the database, then adds the returned key to the list of tasklists for the user
     *
     * @param taskList the task list to be saved
     * @return this UserAccount, daisy chain style
     */
    public UserAccount saveAndAddTaskList(@NotNull TaskList taskList) {
        SendAListDAO dao = new SendAListDAO();
        taskList.setOwner(this.generateKey());
        Key<TaskList> key = dao.saveTaskList(taskList);
        addTaskList(key);
        dao.saveUser(this);
        return this;
    }

    public UserAccount deleteTaskList(@NotNull Key<TaskList> taskListKey) {
        SendAListDAO dao = new SendAListDAO();
        TaskList taskList = dao.findTaskList(taskListKey.getId());
        if (taskList == null) return this;
        dao.deleteTaskList(taskListKey.getId());
        //noinspection unchecked
        deleteTaskListKeys(taskListKey);
        dao.saveUser(this);
        return this;
    }

    private void deleteTaskListKeys(@NotNull Key<TaskList>... taskListKeys) {
        if (taskListKeys.length < 1) return;
        List<Key<TaskList>> keysToDelete = new ArrayList<Key<TaskList>>();
        List<Key<TaskList>> existingKeys = getTaskLists();
        for (Key<TaskList> key : taskListKeys) {
            if (existingKeys.contains(key) && !keysToDelete.contains(key)) {
                keysToDelete.add(key);
            }
        }
        existingKeys.removeAll(keysToDelete);
    }

    /**
     * adds a list of tasks to the userAccount by key
     *
     * @param taskLists the list of tasks or seperately specified Key<TaskList>
     * @return this UserAccount, daisy chain style
     * @see #addTaskList(com.googlecode.objectify.Key)
     */
    public UserAccount addTaskLists(@NotNull Key<TaskList>... taskLists) {
        for (Key<TaskList> key : taskLists) {
            addTaskList(key);
        }
        return this;
    }

    /**
     * calls the toString method of each task list and appends it to a single string titled by the user's display name
     *
     * @return the task lists as a string
     */
    public String getTaskListsAsString() {
        SendAListDAO dao = new SendAListDAO();
        StringBuilder sb = new StringBuilder();
        sb.append(getDisplayName()).append("'s lists: ");
        List<TaskList> list = getTaskListsFromDatabase();
        for (TaskList taskList : list) {
            sb.append(taskList.toString());
        }
        return sb.toString();
    }

    public String getTaskListsAsHtmlString() {
        SendAListDAO dao = new SendAListDAO();
        StringBuilder sb = new StringBuilder();
        sb.append("<h2>");
        sb.append(getDisplayName()).append("'s lists: </h2>");
        List<TaskList> list = getTaskListsFromDatabase();
        if (list.size() > 0) {
            sb.append("<ol>");
            for (TaskList taskList : list) {
                sb.append("<li>");
                sb.append(taskList.toHtmlList());
                sb.append("</li>");
            }
            sb.append("</ol>");
        } else {
            sb.append("<h3> No Lists! </h3>");
        }
        return sb.toString();
    }

    /**
     * {@inheritDoc}
     * <br/><br/>
     * Possible fields:
     * <ul>
     * <li>displayName - the name of the user for others to see</li>
     * <li>fname - the first name of the user</li>
     * <li>lname - the last name of the user</li>
     * <li>picurl - the url for the photo of this user</li>
     * </ul>
     */
    @Override
    public String processTransaction(JsonObject tx) {
        boolean changed = false;

        for (Map.Entry<String, JsonElement> entry : tx.entrySet()) {
            String key = entry.getKey();
            JsonElement value = entry.getValue();
            boolean couldNotParse = false;
            String valueAsString = null;

            if (FIELD_CLASS.equals(key) || FIELD_ID.equals(key)) {
                //do nothing
            } else if (FIELD_DISPLAY.equals(key)) {
                valueAsString = value.getAsString();
                if (this.getDisplayName() == null || !this.getDisplayName().equals(valueAsString)) {
                    changed = true;
                    this.setDisplayName(valueAsString);
                }
            } else if (FIELD_FIRST.equals(key)) {
                valueAsString = value.getAsString();
                if (this.getFirstName() == null || !this.getFirstName().equals(valueAsString)) {
                    changed = true;
                    this.setFirstName(valueAsString);
                }
            } else if (FIELD_LAST.equals(key)) {
                valueAsString = value.getAsString();
                if (this.getLastName() == null || !this.getLastName().equals(valueAsString)) {
                    changed = true;
                    this.setLastName(valueAsString);
                }
            } else if (FIELD_PIC_URL.equals(key)) {
                valueAsString = value.getAsString();
                if (this.getPhotoUrl() == null || !this.getPhotoUrl().equals(valueAsString)) {
                    changed = true;
                    this.setPhotoUrl(valueAsString);
                }
            } else if (FIELD_PASS.equals(key)) {
                valueAsString = value.getAsString();
                String[] terms = valueAsString.split(",");
                if (terms.length != 2) {
                    return "could not understand value: " + valueAsString
                            + " correct format: \"<oldpassword>,<newpassword>\"";
                } else if (isFederated() && !"new".equals(terms[0])) {
                    return "changing password is not allowed for a federated account";
                }
                String oldPass = ("null".equalsIgnoreCase(terms[0]) || "new".equalsIgnoreCase(terms[0])) ? null : terms[0];
                String newPass = terms[1];
                boolean isOldPassValid = checkPassword(oldPass);
                if (!isOldPassValid) {
                    return "previous password was invalid! could not change password";
                } else {
                    this.setPassword(newPass);
                }
            } else {
                return "did not understand field: " + key;
            }
        }

        return (changed) ? null : Processable.Nop;
    }

    private String processDeleteTransaction() {
        String err = null;

        //delete all the related task lists?
        SendAListDAO dao = new SendAListDAO();
        for (Key<TaskList> key : this.getTaskLists()) {
            TaskList list = dao.findTaskList(key.getId());
            if (list != null) dao.deleteTaskList(list.getId());
        }


        return err;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isSafeToPersist() {
        boolean safe = this.getEmail() != null;
        if (!isFederated()) {
            safe = safe && this.getEncryptedPassword() != null;
        }
        return safe;
    }

    /**
     * checks the password using the Password Encryptor.
     * @param plainTextPass the password to check against the encrypted password
     * @return true if the same, false if not.
     */
    public boolean checkPassword(@Nullable String plainTextPass) {
        BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
        if (encryptedPassword == null) return plainTextPass == null;
        else return plainTextPass != null && passwordEncryptor.checkPassword(getPS() + plainTextPass, encryptedPassword);
    }

}
