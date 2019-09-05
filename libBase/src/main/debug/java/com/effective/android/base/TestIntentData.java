package com.effective.android.base;

import android.os.Parcel;
import android.os.Parcelable;
public class TestIntentData implements Parcelable {

    String data;

    public TestIntentData(String data) {
        this.data = data;
    }

    public TestIntentData(Parcel in) {
        data = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(data);
    }

    public static final Creator<TestIntentData> CREATOR = new Creator<TestIntentData>() {
        @Override
        public TestIntentData createFromParcel(Parcel in) {
            return new TestIntentData(in);
        }

        @Override
        public TestIntentData[] newArray(int size) {
            return new TestIntentData[size];
        }
    };
}
