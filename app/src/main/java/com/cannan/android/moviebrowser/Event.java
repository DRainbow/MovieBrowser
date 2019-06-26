package com.cannan.android.moviebrowser;

/**
 * @ClassName: Event
 * @Description:
 * @author: Cannan
 * @date: 2019-06-26 16:01
 */
public class Event<T> {

    private T mContent;

    private boolean hasBeenHandled = false;

    public Event(T content) {
        if (content == null) {
            throw new IllegalArgumentException("null values in Event are not allowed.");
        }
        mContent = content;
    }

    public T getContentIfNotHandled() {
        if (hasBeenHandled) {
            return null;
        } else {
            hasBeenHandled = true;
            return mContent;
        }
    }

    public boolean hasBeenHandled() {
        return hasBeenHandled;
    }
}