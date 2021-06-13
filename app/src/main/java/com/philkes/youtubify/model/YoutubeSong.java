package com.philkes.youtubify.model;

import com.naman14.timber.models.Song;

public class YoutubeSong extends Song {

    public final String thumbnail;
    public final String videoId;

    public YoutubeSong() {
        super();
        this.thumbnail=null;
        this.videoId=null;

    }

    public YoutubeSong(long _id, long _albumId, long _artistId, String _title, String _artistName, String _albumName, int _duration, int _trackNumber, String thumbnail, String videoId) {
        super(_id, _albumId, _artistId, _title, _artistName, _albumName, _duration, _trackNumber);
        this.thumbnail=thumbnail;
        this.videoId=videoId;
    }

    public String getLink(){
        return "https://www.youtube.com/watch?v="+videoId;
    }

}
