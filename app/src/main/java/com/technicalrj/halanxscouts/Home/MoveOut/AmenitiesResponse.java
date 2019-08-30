package com.technicalrj.halanxscouts.Home.MoveOut;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;

public class AmenitiesResponse {

    public static final String STATUS_OK = "ok";
    public static final String STATUS_MISSING = "missing";
    public static final String STATUS_DAMAGED = "damaged";
    public static final String STATUS_NOT_SELECTED = "Select";
    public static final String AMENITY_KEY = "amenity_key";

    private int id;

    @SerializedName("amenities_json")
    private AmenityJsonData amenityJsonData;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public AmenityJsonData getAmenityJsonData() {
        return amenityJsonData;
    }

    public void setAmenityJsonData(AmenityJsonData amenityJsonData) {
        this.amenityJsonData = amenityJsonData;
    }

    public static class AmenityJsonData implements Parcelable{

        @SerializedName("data")
        private AmenityData amenityData;

        public AmenityJsonData(AmenityData amenityData) {
            this.amenityData = amenityData;
        }

        protected AmenityJsonData(Parcel in) {
            amenityData = in.readParcelable(AmenityData.class.getClassLoader());
        }

        public static final Creator<AmenityJsonData> CREATOR = new Creator<AmenityJsonData>() {
            @Override
            public AmenityJsonData createFromParcel(Parcel in) {
                return new AmenityJsonData(in);
            }

            @Override
            public AmenityJsonData[] newArray(int size) {
                return new AmenityJsonData[size];
            }
        };

        public AmenityData getAmenityData() {
            return amenityData;
        }

        public void setAmenityData(AmenityData amenityData) {
            this.amenityData = amenityData;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeParcelable(amenityData, flags);
        }
    }

    public static class AmenityData implements Parcelable{

        @SerializedName("amenities_dict")
        private HashMap<String, Amenity> amenityHashMap;

        public AmenityData(HashMap<String, Amenity> amenityHashMap) {
            this.amenityHashMap = amenityHashMap;
        }

        protected AmenityData(Parcel in) {
            int hashMapSize = in.readInt();
            if(amenityHashMap == null){
                amenityHashMap = new HashMap<>(hashMapSize);
            }
            for(int i = 0; i<hashMapSize; i++){
                String key = in.readString();
                Amenity amenity = in.readParcelable(Amenity.class.getClassLoader());
                amenityHashMap.put(key, amenity);
            }
        }

        public static final Creator<AmenityData> CREATOR = new Creator<AmenityData>() {
            @Override
            public AmenityData createFromParcel(Parcel in) {
                return new AmenityData(in);
            }

            @Override
            public AmenityData[] newArray(int size) {
                return new AmenityData[size];
            }
        };

        public HashMap<String, Amenity> getAmenityHashMap() {
            return amenityHashMap;
        }

        public void setAmenityHashMap(HashMap<String, Amenity> amenityHashMap) {
            this.amenityHashMap = amenityHashMap;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(amenityHashMap.size());
            for(String key : amenityHashMap.keySet()){
                dest.writeString(key);
                dest.writeParcelable(amenityHashMap.get(key), flags);
            }
        }
    }

    public static class Amenity implements Parcelable {

        private int id;

        private String status;

        private String name;

        private int quantity;

        public Amenity(int id) {
            this.id = id;
        }

        public Amenity(int id, String status, String name, int quantity) {
            this.id = id;
            this.status = status;
            this.name = name;
            this.quantity = quantity;
        }

        protected Amenity(Parcel in) {
            id = in.readInt();
            status = in.readString();
            name = in.readString();
            quantity = in.readInt();
        }

        public static final Creator<Amenity> CREATOR = new Creator<Amenity>() {
            @Override
            public Amenity createFromParcel(Parcel in) {
                return new Amenity(in);
            }

            @Override
            public Amenity[] newArray(int size) {
                return new Amenity[size];
            }
        };

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(id);
            dest.writeString(status);
            dest.writeString(name);
            dest.writeInt(quantity);
        }
    }

    public ArrayList<Amenity> getListOfAmenity(){
        HashMap<String, Amenity> hashMap = getAmenityJsonData().getAmenityData().getAmenityHashMap();
        return new ArrayList<>(hashMap.values());
    }

    public static ArrayList<Amenity> getListOfAmenity(HashMap<String, Amenity> hashMap){
        return new ArrayList<>(hashMap.values());
    }
}
