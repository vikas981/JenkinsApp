package com.viksingh.jenkins.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class JobModel implements Parcelable {
    public static final Parcelable.Creator<JobModel> CREATOR = new Parcelable.Creator<JobModel>() {
        public JobModel createFromParcel(Parcel param1Parcel) {
            return new JobModel(param1Parcel);
        }

        public JobModel[] newArray(int param1Int) {
            return new JobModel[param1Int];
        }
    };

    String color;

    String name;

    String url;

    public JobModel() {}

    protected JobModel(Parcel paramParcel) {
        this.name = paramParcel.readString();
        this.url = paramParcel.readString();
        this.color = paramParcel.readString();
    }

    public JobModel(String paramString1, String paramString2, String paramString3) {
        this.name = paramString1;
        this.url = paramString2;
        this.color = paramString3;
    }

    public int describeContents() {
        return 0;
    }

    public String getColor() {
        return this.color;
    }

    public String getName() {
        return this.name;
    }

    public String getUrl() {
        return this.url;
    }

    public void setColor(String paramString) {
        this.color = paramString;
    }

    public void setName(String paramString) {
        this.name = paramString;
    }

    public void setUrl(String paramString) {
        this.url = paramString;
    }

    public void writeToParcel(Parcel paramParcel, int paramInt) {
        paramParcel.writeString(this.name);
        paramParcel.writeString(this.url);
        paramParcel.writeString(this.color);
    }
}
