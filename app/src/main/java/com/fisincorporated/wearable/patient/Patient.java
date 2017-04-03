package com.fisincorporated.wearable.patient;


import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;

import com.fisincorporated.wearable.BR;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class Patient extends BaseObservable implements Parcelable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("bp")
    @Expose
    private String bp;
    @SerializedName("pulse")
    @Expose
    private Integer pulse;

    public Patient(Map<String, String> patientInfo) {
        for (Map.Entry<String, String> entry : patientInfo.entrySet()) {
            switch ( entry.getKey()) {
                case "id":
                    try {
                        id = Integer.decode(entry.getValue());
                    } catch (NumberFormatException nfe) {
                        id = -1;
                    }
                    break;
                case "patient":
                    name = entry.getValue();
                    break;
                case "bp":
                    setBp(entry.getValue());
                    break;
                case "pulse":
                    try {
                        pulse = Integer.decode(entry.getValue());
                    } catch (NumberFormatException nfe) {
                        pulse = 0;
                    }
                    break;
            }
        }
    }

    public Patient(Integer id, String name, String bp, Integer pulse) {
        this.id = id;
        this.name = name;
        this.bp = bp;
        this.pulse = pulse;
    }

    public int getId() {
        return id;
    }

    @Bindable
    public String getName() {
        return name;
    }

    @Bindable
    public String getBp() {
        return bp;
    }

    public void setBp(String bp) {
        this.bp = bp;
        notifyPropertyChanged(BR.bp);
    }

    @Bindable
    public int getPulse() {
        return pulse;
    }

    public void setPulse(int pulse) {
        this.pulse = pulse;
        notifyPropertyChanged(BR.pulse);
    }

    public String toString(){
        return "Id:" + id + " Name:" + name + " bp:" + bp + " pulse:" + pulse;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.bp);
        dest.writeInt(this.pulse);
    }

    @SuppressWarnings({"unchecked"})
    protected Patient(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.bp = in.readString();
        this.pulse = in.readInt();
    }

    public static final Parcelable.Creator<Patient> CREATOR = new Parcelable.Creator<Patient>() {
        @Override
        public Patient createFromParcel(Parcel source) {
            return new Patient(source);
        }

        @Override
        public Patient[] newArray(int size) {
            return new Patient[size];
        }
    };
}
