package br.com.gbguerra.popularfilms.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Giovani Guerra on 07/11/2016.
 */

public class Film implements Parcelable {

    private int id;
    private String title;
    private String overview;
    private String posterUrl;
    private String releaseDate;
    private double voteAverage;

    public Film(int id, String title, String overview, String posterUrl, String releaseDate, double voteAverage) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.posterUrl = posterUrl;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
    }

    private Film(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.overview = in.readString();
        this.posterUrl = in.readString();
        this.releaseDate = in.readString();
        this.voteAverage = in.readDouble();
    }

    public static final Creator<Film> CREATOR = new Creator<Film>() {
        @Override
        public Film createFromParcel(Parcel in) {
            return new Film(in);
        }

        @Override
        public Film[] newArray(int size) {
            return new Film[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(overview);
        parcel.writeString(posterUrl);
        parcel.writeString(releaseDate);
        parcel.writeDouble(voteAverage);
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }
}
