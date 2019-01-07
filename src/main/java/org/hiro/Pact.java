package org.hiro;

import org.hiro.character.StateEnum;

import java.lang.reflect.Method;

public class Pact {
    StateEnum pa_flags;
    Method pa_daemon;
    int pa_time;
    String pa_high;
    String pa_straight;

    public Pact(StateEnum pa_flags, Method pa_daemon, int pa_time, String pa_high, String pa_straight) {
        this.pa_flags = pa_flags;
        this.pa_daemon = pa_daemon;
        this.pa_time = pa_time;
        this.pa_high = pa_high;
        this.pa_straight = pa_straight;
    }
}
