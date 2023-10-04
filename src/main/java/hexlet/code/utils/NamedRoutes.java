package hexlet.code.utils;

public class NamedRoutes {
    public static final String LABELS_PATH = "/labels";
    public static final String USERS_PATH = "/users";
    public static final String STATUSES_PATH = "/statuses";
    public static final String TASKS_PATH = "/tasks";
    private static final String LABEL_PATH = "/labels/%s";
    private static final String USER_PATH = "/users/%s";
    private static final String STATUS_PATH = "/statuses/%s";
    private static final String TASK_PATH = "/tasks/%s";

    public static String labelsPath() {
        return LABELS_PATH;
    }

    public static String usersPath() {
        return USERS_PATH;
    }

    public static String statusesPath() {
        return STATUSES_PATH;
    }

    public static String tasksPath() {
        return TASKS_PATH;
    }

    public static String labelPath(long id) {
        return String.format(LABEL_PATH, id);
    }

    public static String userPath(long id) {
        return String.format(USER_PATH, id);
    }

    public static String statusPath(long id) {
        return String.format(STATUS_PATH, id);
    }

    public static String taskPath(long id) {
        return String.format(TASK_PATH, id);
    }
}
