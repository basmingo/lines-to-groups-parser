package org.example;

public class Group {
    private final int index;
    private Group parent;
    private Group child;

    public Group(int index) {
        this.index = index;
    }

    public void setParent(Group parent) {
        if (this.parent == null) {
            this.parent = parent;
        } else {
            this.parent.setParent(parent);
        }
    }

    public void setChild(Group child) {
        if (this.child == null) {
            this.child = child;
        } else {
            this.child.setChild(child);
        }
    }

    public int getHead() {
        Group head = this;
        int min = head.index;
        while (head.parent != null) {
            head = head.parent;
        }

        while (head != null) {
            min = Math.min(min, head.index);
            head = head.child;
        }
        return min;
    }

    public static void merge(Group a, Group b) {
        if (a.getHead() != b.getHead()) {
            a.setChild(b);
            b.setParent(a);
        }
    }
}
