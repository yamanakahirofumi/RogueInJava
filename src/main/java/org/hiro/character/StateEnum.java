package org.hiro.character;

public enum StateEnum {
    NoChange(0),
    CANHUH(0000001),        /* creature can confuse */
    CANSEE(0000002),        /* creature can see invisible creatures */
    ISBLIND(0000004),        /* creature is blind */
    ISCANC(0000010),        /* creature has special qualities cancelled */
    ISLEVIT(0000010),        /* hero is levitating */
    ISFOUND(0000020),        /* creature has been seen (used for objects) */
    ISGREED(0000040),        /* creature runs to protect gold */
    ISHASTE(0000100),        /* creature has been hastened */
    ISTARGET(000200),        /* creature is the target of an 'f' command */
    ISHELD(0000400),        /* creature has been held */ //怪物を封じ込める
    ISHUH(0001000),    /* creature is confused */
    ISINVIS(0002000),    /* creature is invisible */
    ISMEAN(0004000),    /* creature can wake when player enters room */
    ISHALU(0004000),    /* hero is on acid trip */
    ISREGEN(0010000),    /* creature can regenerate */
    ISRUN(0020000),    /* creature is running at the player */
    SEEMONST(040000),    /* hero can detect unseen monsters */
    ISFLY(0040000),    /* creature can fly */
    ISSLOW(0100000);    /* creature has been slowed */

    private int value;

    StateEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
