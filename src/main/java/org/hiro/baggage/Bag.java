package org.hiro.baggage;

import org.hiro.Global;
import org.hiro.IOUtil;
import org.hiro.things.Food;
import org.hiro.things.Potion;
import org.hiro.things.Scroll;
import org.hiro.things.Thing;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Bag {
    private int limitSize;
    private int size;
    private List<Thing> contents;

    public Bag() {
        this.contents = new LinkedList<>();
        this.limitSize = 23;
        this.size = 0;
    }

    public boolean addContents(Thing t) {
        if (this.limitSize <= this.getFillingSize()) {
            if (!Global.terse) {
                IOUtil.addmsg("there's ");
            }
            IOUtil.addmsg("no room");
            if (!Global.terse) {
                IOUtil.addmsg(" in your pack");
            }
            IOUtil.endmsg();
            return false;
        }
        List<Thing> selections = this.contents.stream()
                .filter(it -> t.getClass() == it.getClass())
                .collect(Collectors.toList());
        this.size++;
        if (selections.size() < 1) {
            this.contents.add(t);
        } else if (t.isGroup() && t.getGroup() == selections.get(0).getGroup()) {
            selections.get(0).addCount(t.getCount());
            this.size--;
        } else if (t instanceof Potion || t instanceof Scroll || t instanceof Food) {
            selections.get(0).addCount(t.getCount());
        } else {
            this.contents.add(t);
        }
        return true;
    }

    public List<Thing> getContents() {
        return contents;
    }

    public int getFillingSize() {
        return this.size;
    }

    public char getPosition(Thing t) {
        return (char) ('a' + this.contents.indexOf(t));
    }

    public boolean isContent(Thing t){
        return this.contents.contains(t);
    }

}
