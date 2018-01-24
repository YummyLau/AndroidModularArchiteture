package example.basiclib;

import android.arch.lifecycle.LiveData;

/**
 * A LiveData class that has {@code null} value.
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2017/12/13
 */

public class AbsentLiveData extends LiveData {
    private AbsentLiveData() {
        postValue(null);
    }

    public static <T> LiveData<T> create() {
        //noinspection unchecked
        return new AbsentLiveData();
    }
}
