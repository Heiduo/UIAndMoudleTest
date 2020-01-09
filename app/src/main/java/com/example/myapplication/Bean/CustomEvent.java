package com.example.myapplication.Bean;

public class CustomEvent {
    private String tag;
    private Object content;

    /**
     * The Object for EventBus
     * @param tag the unique identifier
     * @param content the content of Object
     */
    public CustomEvent(String tag, Object content) {
        this.tag = tag;
        this.content = content;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }
}