package com.company;

import javafx.util.Pair;

import java.io.Serializable;
import java.util.Vector;

public class Document implements Serializable {
    private String path;
    private String url;

    private String name;
    private Vector<Pair<String, Object>> tags;

    private int id;
    private static int totalId = 0;

    public Document() {
        id = totalId;
        totalId++;

        name = "default";
        url = "undefined";
        path = "undefined";
    }

    public Document(String path) {
        this.path = path;
        id = totalId;
        totalId++;

        name = "default";
        url = "undefined";
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Vector<Pair<String, Object>> getTags() {
        return tags;
    }

    public String getTagsToString() {
        return "tags=" + tags;
    }

    public void setTags(Vector<Pair<String, Object>> tags) {
        this.tags = tags;
    }

    public boolean addTag(String name, Object value){
        Pair<String, Object> tag = new Pair<String, Object>(name, value);
        return tags.add(tag);
    }

    public int getId() {
        return id;
    }

    public static int getTotalId() {
        return totalId;
    }

    @Override
    public String toString() {
        return "Document{" +
                "path='" + path + '\'' +
                ", url='" + url + '\'' +
                ", name='" + name + '\'' +
                ", tags=" + tags +
                ", id=" + id +
                '}';
    }
}
