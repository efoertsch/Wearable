package com.fisincorporated.wearable.patient;


import android.os.Parcel;
import android.os.Parcelable;

public class PatientInfo implements Parcelable {
    private String key;
    private String value;

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public PatientInfo(String key, String value){
        this.key = key;
        this.value = value;

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.key);
        dest.writeString(this.value);
    }

    protected PatientInfo(Parcel in) {
        this.key = in.readString();
        this.value = in.readString();
    }

    public static final Parcelable.Creator<PatientInfo> CREATOR = new Parcelable.Creator<PatientInfo>() {
        @Override
        public PatientInfo createFromParcel(Parcel source) {
            return new PatientInfo(source);
        }

        @Override
        public PatientInfo[] newArray(int size) {
            return new PatientInfo[size];
        }
    };
}
