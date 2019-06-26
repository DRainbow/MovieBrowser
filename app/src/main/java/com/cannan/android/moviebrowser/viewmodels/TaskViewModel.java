package com.cannan.android.moviebrowser.viewmodels;

import com.cannan.android.moviebrowser.Event;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * @ClassName: TaskViewModel
 * @Description:
 * @author: Cannan
 * @date: 2019-06-26 16:05
 */
public class TaskViewModel extends ViewModel {

    private MutableLiveData<Event<Integer>> mVideoSlideTask = new MutableLiveData<>();

    private MutableLiveData<Event<Integer>> mImageSlideTask = new MutableLiveData<>();

    public MutableLiveData<Event<Integer>> getVideoSlideTask() {
        return mVideoSlideTask;
    }

    public MutableLiveData<Event<Integer>> getImageSlideTask() {
        return mImageSlideTask;
    }

    public void videoScrollToPosition(int position) {
        System.out.println("---- VIDEO scroll to position " + position);
        mVideoSlideTask.setValue(new Event(position));
    }

    public void imageScrollToPosition(int position) {
        System.out.println("---- IMG scroll to position " + position);
        mImageSlideTask.setValue(new Event(position));
    }
}
