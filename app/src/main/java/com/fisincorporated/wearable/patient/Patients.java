package com.fisincorporated.wearable.patient;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Patients implements Parcelable {

    public Patients() {
    }

    /**
     * @param patients
     */
    public Patients(List<Patient> patients) {
        super();
        this.patients = patients;
    }

    public List<Patient> getPatients() {
        return patients;
    }

    public void setPatients(List<Patient> patients) {
        this.patients = patients;
    }

    public Patients withPatients(List<Patient> patients) {
        this.patients = patients;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (patients != null) {
            for (Patient patient : patients){
                sb.append(patient.toString() + '\n');
            }
        }
        return sb.toString();
    }

    @SerializedName("patients")
    @Expose
    private List<Patient> patients = null;
    public final static Parcelable.Creator<Patients> CREATOR = new Creator<Patients>() {

        @SuppressWarnings({
                "unchecked"
        })
        public Patients createFromParcel(Parcel in) {
            Patients instance = new Patients();
            in.readList(instance.patients, (com.fisincorporated.wearable.patient.Patient.class.getClassLoader()));
            return instance;
        }

        public Patients[] newArray(int size) {
            return (new Patients[size]);
        }

    };

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(patients);
    }

    public int describeContents() {
        return 0;
    }

}