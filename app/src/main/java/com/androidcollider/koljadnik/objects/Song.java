package com.androidcollider.koljadnik.objects;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by pseverin on 24.12.14.
 */
public class Song implements Parcelable{

    public static long current_min_rating;
    public static long current_max_rating;

    private int id;
    private String name;
    private long rating;
    private int idType;
    private String text;
    private String remarks;
    private String source;
    private ArrayList<String> comments;

    /*private Bitmap chord;
    private Bitmap note;*/

    public Song(int id, String name, long rating, int idType,
                String text, String remarks, String source, ArrayList<String> comments) {
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.idType = idType;
        this.text = text;
        this.remarks = remarks;
        this.source = source;
        this.comments = comments;
    }

    /*public Song(int id, long rating, String name) {
        this.id = id;
        this.name = name;
        this.rating = rating;
    }*/

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getRating() {
        return rating;
    }

    public int getIdType() {
        return idType;
    }

    public String getText() {
        return text;
    }

    public String getRemarks() {
        return remarks;
    }

    public String getSource() {
        return source;
    }

    public ArrayList<String> getComments() {
        return comments;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRating(long rating) {
        this.rating = rating;
    }

    public void setIdType(int id_type) {
        this.idType = id_type;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setComments(ArrayList<String> comments) {
        this.comments = comments;
    }


    public Song(Parcel in){
        this.id = in.readInt();
        this.name = in.readString();
        this.rating = in.readLong();
        this.idType = in.readInt();
        this.text = in.readString();
        this.remarks = in.readString();
        this.source = in.readString();
        if (this.comments!=null){
            in.readStringList(this.comments);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeLong(this.rating);
        dest.writeInt(this.idType);
        dest.writeString(this.text);
        dest.writeString(this.remarks);
        dest.writeString(this.source);
        dest.writeStringList(this.comments);
        /*private int id;
        private String name;
        private long rating;
        private int id_type;
        private String text;
        private ArrayList<String> remarks;
        private String source;
        private ArrayList<String> comments;*/
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

    @Override
    public String toString() {
        String comm="";
        if (comments!=null){
            comm=comments.toString();
        }
        return id+"  "+name+"  "+rating+"  "+idType+"  "+text+"  "+remarks+"  "+source+"  "+comm;
    }
}
