package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GroupTest {
    @Test
    void mergeTest() {
        Group a = new Group(1);
        Group b = new Group(10);
        Group c = new Group(5);
        Group d = new Group(7);
        Group e = new Group(7);
        Group f = new Group(7);

        Group.merge(a, b);
        Group.merge(b, c);
        Group.merge(a, d);
        Group.merge(e, e);
        Group.merge(f, f);
        Group.merge(f, e);

        System.out.println(e);
        assertEquals(1, a.getHead());
        assertEquals(1, b.getHead());
        assertEquals(1, c.getHead());
        assertEquals(1, d.getHead());
    }
}
