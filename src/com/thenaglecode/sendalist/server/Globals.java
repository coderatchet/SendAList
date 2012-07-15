package com.thenaglecode.sendalist.server;

import com.googlecode.objectify.Key;
import com.thenaglecode.sendalist.server.domain2Objectify.SendAListDAO;
import com.thenaglecode.sendalist.server.domain2Objectify.entities.Task;
import com.thenaglecode.sendalist.server.domain2Objectify.entities.TaskList;
import com.thenaglecode.sendalist.server.domain2Objectify.entities.UserAccount;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.validation.constraints.Size;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Created by IntelliJ IDEA.
 * User: JELLYBEANS
 * Date: 6/10/12
 * Time: 11:00 PM
 */
public class Globals {

    private static final SecureRandom gen = new SecureRandom();

    private Globals() {
    }

    @Nullable
    static private SecureRandom random = null;
    private static final int symbolsLength = 62;
    @NotNull
    private static final char[] symbols = new char[symbolsLength];

    /**
     * init the symbols used in generating a random token
     */
    private static void initSymbols() {
        char ch = '0';
        for (int idx = 0; idx < 10; idx++)
            symbols[idx] = ch++;
        ch = 'a';
        for (int idx = 10; idx < 36; idx++)
            symbols[idx] = ch++;
        ch = 'A';
        for (int idx = 36; idx < 62; idx++)
            symbols[idx] = ch++;
    }

    @Nullable
    /** generates a random token with a size of 1 - 50 characters. possible characters include: a-z, A-Z and 0-9
     *
     * @param length the length of the token to be generated
     * @return a randomly generated token
     * */
    public static String generateRandomToken(@Size(min = 1, max = 50) int length) {
        try {
            if (random == null) {
                random = SecureRandom.getInstance("SHA1PRNG");
                initSymbols();
            }
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            assert random != null;
            sb.append(symbols[random.nextInt(symbolsLength)]);
        }
        return sb.toString();
    }


    public static String init() {
        setupTestAccounts();

        return null;
    }

    /**
     * This fn creates test information so that the admin does not have to re enter it every time
     */
    private static void setupTestAccounts() {

    }

    /* Example TaskList Generation */

    /**
     * when a user first registers, there will be an example task list in his account for him to get used to the system.
     * This is that task list. dummmmmm.
     *
     * @return the example task list to be added to a user.
     */
    public static TaskList generateExampleTaskList() {
        return null; //todo implement
    }

    /* Fake User Generation */

    public static void createAndPersistFakeUsers(@Size(max = 20) int numberOfUsers) {
        SendAListDAO dao = new SendAListDAO();
        for (int i = 0; i < numberOfUsers; i++) {
            int maxTaskLists = gen.nextInt(5) + 1; //each user has 1 - 5 task lists
            UserAccount user = null;
            boolean userExists = true;

            while (userExists) {
                user = generateFakeUser();
                userExists = dao.findUser(user.getEmail()) != null;
            }

            Key<UserAccount> userKey = dao.saveUser(user);

            for (int j = 0; j < maxTaskLists; j++) {
                int maxTasks = gen.nextInt(10) + 1; // each list has 1 - 10 random entries
                TaskList taskList = generateFakeTaskList(j + 1, userKey);

                for (int k = 0; k < maxTasks; k++) {
                    Task task = generateFakeTask();
                    if (task.isSafeToPersist()) {
                        taskList.addTask(task);
                    } else {
                        System.out.println("The task created was not safe to persist so was not added!");
                    }
                }

                dao.saveTaskList(taskList);
            }
        }
    }

    private static String FAKE_PASS = "1234";
    private static String[] FIRST_NAMES = new String[]{"Jack", "Jill", "Fred", "Maxwell", "Martha", "April", "Jordan", "Pennelope"};
    private static String[] LAST_NAMES = new String[]{"Smith", "Baker", "Tools", "Winters", "Summers", "West", "Georges", "Bopp"};

    /**
     * generate the user
     *
     * @return the user that was generated
     */
    public static UserAccount generateFakeUser() {
        String first = FIRST_NAMES[gen.nextInt(FIRST_NAMES.length)];
        String last = LAST_NAMES[gen.nextInt(LAST_NAMES.length)];
        return new UserAccount()
                .setFirstName(first)
                .setLastName(last)
                .setEmail(first + last + "@notreal.com")
                .setPassword(FAKE_PASS)
                .setPhotoUrl("/resources/images/no.jpg");
    }

    /**
     * generates a fake task list
     *
     * @param number the index of the current list being created
     * @param owner  the user account that owns this task list.
     * @return the randomly generated task list
     */
    public static TaskList generateFakeTaskList(int number, Key<UserAccount> owner) {
        TaskList fakeTaskList = new TaskList();
        fakeTaskList.setSummary("task list number " + number);
        fakeTaskList.setOwner(owner);
        return fakeTaskList;
    }

    private static String[] VERBS = new String[]{"Objectify", "Manipulate", "Plead with", "Make stir fry out of", "Emancipate", "Excommunicate", "Topple over", "construct", "pillage", "test"};
    private static String[] NOUNS = new String[]{"lemon", "dictator", "father", "mexican", "fry pan", "bible", "pope", "black hole"};

    /**
     * generates a fake task
     *
     * @return the randomly Generated task
     */
    public static Task generateFakeTask() {
        Task fakeTask = new Task();
        int amount = gen.nextInt(10) + 1;
        fakeTask.setSummary(VERBS[gen.nextInt(VERBS.length)] + " " + amount + " "
                + NOUNS[gen.nextInt(NOUNS.length)] + ((amount > 1) ? "s" : ""));
        fakeTask.setDone(gen.nextBoolean());
        return fakeTask;
    }
}
