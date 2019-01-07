package org.hiro.things;

import java.util.Optional;

public interface Thing {
    void add_o_flags(int flag);

    void delete_o_flags(int flag);

    void set_o_flags(int flag);

    boolean contains_o_flags(int flag);

    int foodValue();

    Optional<Thing> eat();
}
