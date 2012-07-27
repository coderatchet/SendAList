package com.thenaglecode.sendalist.server.domain2Objectify;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.NotFoundException;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.util.DAOBase;
import com.thenaglecode.sendalist.server.domain2Objectify.entities.TaskList;
import com.thenaglecode.sendalist.server.domain2Objectify.entities.UserAccount;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Jared Nagle
 * Date: 7/07/12
 * Time: 5:44 PM
 */
public class SendAListDAO extends DAOBase {

    static {
        ObjectifyService.register(UserAccount.class);
        ObjectifyService.register(TaskList.class);
    }

    /**
     * finds the UserAccount with the given email and returns it, otherwise returns null if not found
     *
     * @param email the email of the UserAccount to search for
     * @return the UserAccount if found or null if not
     */
    @Nullable
    public UserAccount findUser(@Nullable String email) {
        if (email == null) return null;
        return findOrCreateUser(email, false);
    }

    /**
     * finds the TaskList with the given id and returns it, otherwise returns null if not found
     *
     * @param id the id of the TaskList to search for
     * @return the TaskList if found or null if not
     */
    @Nullable
    public TaskList findTaskList(long id) {
        if (id < 1) return null;
        return findOrCreateTaskList(id, false);
    }

    /**
     * finds the user with the given email and returns it or creates a new one if allowed. otherwise returns null if not
     * found or not allowed to create.
     *
     * @param email           the email of the account being searched for
     * @param allowedToCreate whether a user can be created if not found
     * @return the user if found, the new user if allowed to create or null if niether are true
     */
    @Nullable
    public UserAccount findOrCreateUser(String email, boolean allowedToCreate) {
        UserAccount userAccount;
        try {
            userAccount = ofy().get(UserAccount.class, email);
        } catch (NotFoundException e) {
            return allowedToCreate ? new UserAccount(email) : null;
        }
        return userAccount;
    }

    /**
     * finds the tasklist with the given id and returns it or creates a new one if allowed. otherwise returns null if not
     * found or not allowed to create.
     *
     * @param id              the id of the TaskList being searched for
     * @param allowedToCreate whether a TaskList can be created if not found
     * @return the TaskList if found, the new TaskList if allowed to create or null if niether are true
     */
    @Nullable
    public TaskList findOrCreateTaskList(long id, boolean allowedToCreate) {
        TaskList taskList;
        try {
            taskList = ofy().find(TaskList.class, id);
        } catch (NotFoundException e) {
            return allowedToCreate ? new TaskList(id) : null;
        }
        return taskList;
    }

    /**
     * persists the object state to the database, if a userAccount with the same id is present,
     * the object will be overwritten.
     *
     * @param user the user to be persisted
     * @return the key for the UserAccount that was persisted
     */
    public Key<UserAccount> saveUser(UserAccount user) {
        return ofy().put(user);
    }

    /**
     * <b>WARNING: Not to be called in production server, may take a long time!</b><br/>
     * returns a list of all users on the system.
     *
     * @return a list of users stored in the system.
     */
    public List<UserAccount> getAllUsers() {
        return ofy().query(UserAccount.class).list(); //lists all the users currently in the database
    }

    /**
     * persists the object state to the database, if a tasklist with the same id is present, the object will be overwritten
     *
     * @param taskList the TaskList to be persisted
     * @return the key for the TaskList that was persisted
     */
    public Key<TaskList> saveTaskList(TaskList taskList) {
        return ofy().put(taskList);
    }

    /**
     * returns true if the plaintext password is the same as the encrypted password when encrypted using the user's
     * salt. returns false if the password is incorrect or the user was not found.
     *
     * @param email    the email of the user to check the password against
     * @param password the password to check against the database
     * @return true if the password is correct or false if the password is incorrect or the user doesn't exist
     */
    public boolean checkPassword(@Nullable String email, String password) {
        UserAccount user = findUser(email);
        if (user == null) {
            return false;
        } else if (user.getEncryptedPassword() != null) {
            BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
            //we add the salt and then check for
            return passwordEncryptor.checkPassword(user.getPS() + password, user.getEncryptedPassword());
        } else return false;
    }

    public void deleteTaskList(long id) {
        ofy().delete(TaskList.class, id);
    }
}
