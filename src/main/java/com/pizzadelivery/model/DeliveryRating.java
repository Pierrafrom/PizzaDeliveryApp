package com.pizzadelivery.model;

public interface DeliveryRating {
    static final int MIN_TO_SEC = 60;

    public static final int NOTE_10_MAX_TIME = 20 * MIN_TO_SEC;   // 20 minutes
    public static final int NOTE_9_MAX_TIME = 25 * MIN_TO_SEC;    // 25 minutes
    public static final int NOTE_8_MAX_TIME = 30 * MIN_TO_SEC;    // 30 minutes
    public static final int NOTE_7_MAX_TIME = 35 * MIN_TO_SEC;    // 35 minutes
    public static final int NOTE_6_MAX_TIME = 40 * MIN_TO_SEC;   // 40 minutes
    public static final int NOTE_5_MAX_TIME = 45 * MIN_TO_SEC;   // 45 minutes
    public static final int NOTE_4_MAX_TIME = 50 * MIN_TO_SEC;   // 50 minutes
    public static final int NOTE_3_MAX_TIME = 55 * MIN_TO_SEC;   // 55 minutes
    public static final int NOTE_2_MAX_TIME = 65 * MIN_TO_SEC;   // 65 minutes
    public static final int NOTE_1_MAX_TIME = 75 * MIN_TO_SEC;   // 75 minutes
}
