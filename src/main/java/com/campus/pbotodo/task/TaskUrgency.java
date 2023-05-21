package com.campus.pbotodo.task;

public enum TaskUrgency {

    IMPORTANT("important", "important"),

    NOT_IMPORTANT("not_important", "default");

    /**
     * The notification's urgency.
     */
    public final String value;

    /**
     * The notification's sound.
     */
    public final String sound;

    private TaskUrgency(String urgency, String sound) {
        this.value = urgency;
        this.sound = sound;
    }

    /**
     *
     * @return the value of the state
     */
    public static TaskUrgency fromValue(String urgency) {
        for (TaskUrgency taskUrgency : TaskUrgency.values()) {
            if (taskUrgency.value.equals(urgency)) {
                return taskUrgency;
            }
        }

        return TaskUrgency.NOT_IMPORTANT;
    }

}
