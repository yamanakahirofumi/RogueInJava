package org.hiro.output;

import org.hiro.map.AbstractCoordinate;

public class Display {

    static public int LINES;
    static public int COLS;

    static public void move(int x, int y) {

    }

    static public void clrtoeol() {
    }

    // 現在のカーソルの文字 (色情報を含む)
    static public int inch() {
        return 0;
    }

    static public void clear() {

    }

    static public void delwin(Object obj) {
    }

    static public void addch(char c) {
    }

    static public void addstr(String str) {
    }

    static public void endwin() {
    }

    static public boolean isendwin() {
        return true;
    }

    static public void newwin(int line, int col, int y, int x) {
    }

    static public void standout() {

    }

    static public void refresh() {
    }

    static public void standend() {
    }

    static public char mvinch(int y, int x) {
        return 'a';
    }

    static public void mvaddch(int y, int x, char c) {
    }

    static public void mvaddch(AbstractCoordinate coordinate, char c){

    }

    static public void mvaddstr(int y, int x, String str) {
    }

    static public int mvcur(int oldrow, int oldcol, int newrow, int newcol){
        return 0;
    }

    static public void mvprintw(int y, int x, String str, String... arg) {
    }

    static public void printw(String fmt, Object... obj) {
    }

    static public char unctrl(int s) {
        return (char) s;
    }

}
