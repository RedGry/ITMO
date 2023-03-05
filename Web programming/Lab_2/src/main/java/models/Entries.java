package models;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;

public class Entries implements Serializable {
    private final Deque<Entry> entries;
    private final SimpleDateFormat simpleDateFormat;

    public Entries() {
        entries = new ArrayDeque<>();
        simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
    }

    public Deque<Entry> getEntries() {
        return entries;
    }

    public void addEntry(Entry entry) {
        entries.addFirst(entry);
    }

    public SimpleDateFormat getSimpleDateFormat() {
        return simpleDateFormat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (! (o instanceof Entries)) return false;
        Entries entries1 = (Entries) o;
        return Objects.equals(getEntries(), entries1.getEntries()) && Objects.equals(getSimpleDateFormat(), entries1.getSimpleDateFormat());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEntries(), getSimpleDateFormat());
    }
}

