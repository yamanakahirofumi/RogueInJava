package org.hiro;

import org.hiro.things.Thing;
import org.hiro.things.ThingImp;

/**
 * Functions for dealing with linked lists of goodies
 */
public class ListMethod {

    static int total = 0;

    /*
     * new_item
     *	Get a new item with a specified size
     */
    static ThingImp new_item() {
        ThingImp item = new ThingImp();

        boolean MASTER = true;
        if (MASTER) {
            total++;
        }

        item._l_next = null;
        item._l_prev = null;
        return item;
    }

    /*
     * _attach:
     *	add an item to the head of a list
     *
     *  実はlistよりstackだけどaddしてみる
     */

    static void _attach(ThingImp list, ThingImp item) {
        if (list != null) {
            item._l_next = list;
            list._l_prev = item;
            item._l_prev = null;
        } else {
            item._l_next = null;
            item._l_prev = null;
        }

        // TODO:戻り値にするべきかな
        list = item;
    }

    /*
     * _free_list:
     *	Throw the whole blamed thing away
     *
     *
     */
    static void free_list(ThingImp ptr) {
        ThingImp item;

        while (ptr != null) {
            item = ptr;
            ptr = item._l_next;
            discard(item);
        }
    }

    /*
     * discard:
     *	Free up an item
     */
    static void discard(Thing item) {
        boolean MASTER = true;
        if (MASTER) {
            total--;
        }

        // free(item);
    }

    /*
     * detach:
     *	takes an item out of whatever linked list it might be in
     *
     *   list.remove(item)
     */
    static void _detach(Thing list, ThingImp item) {
        if (list == item) {
            list = item._l_next;
        }
        if (item._l_prev != null) {
            item._l_prev._l_next = item._l_next;
        }
        if (item._l_next != null) {
            item._l_next._l_prev = item._l_prev;
        }
        item._l_next = null;
        item._l_prev = null;
    }

}
