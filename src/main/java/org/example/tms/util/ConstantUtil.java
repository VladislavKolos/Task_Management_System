package org.example.tms.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ConstantUtil {
    public static final int TIME_OUT = 1;
    public static final int ACCESS_TOKEN_EXPIRATION = 15 * 60 * 1000;
    public static final int REFRESH_TOKEN_EXPIRATION = 7 * 24 * 60 * 60 * 1000;
    public static final String BASE_PATH = "/api";
    public static final String TASKS_PATH = BASE_PATH + "/tasks/{id}";
    public static final String COMMENTS_PATH = BASE_PATH + "/comments/{id}";
    public static final String TASK_ASSIGNEES_PATH = BASE_PATH + "/tasks-assignees/{id}";
}
