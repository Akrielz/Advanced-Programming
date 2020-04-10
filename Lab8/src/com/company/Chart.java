package com.company;

import java.util.Objects;

public class Chart {
    private int id;
    private String name;

    public Chart(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Chart(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chart chart = (Chart) o;
        return id == chart.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Chart{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
