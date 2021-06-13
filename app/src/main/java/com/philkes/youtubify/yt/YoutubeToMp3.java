package com.philkes.youtubify.yt;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import com.github.kotvertolet.youtubejextractor.YoutubeJExtractor;
import com.github.kotvertolet.youtubejextractor.exception.ExtractionException;
import com.github.kotvertolet.youtubejextractor.exception.VideoIsUnavailable;
import com.github.kotvertolet.youtubejextractor.exception.YoutubeRequestException;
import com.github.kotvertolet.youtubejextractor.models.newModels.VideoPlayerConfig;
import com.google.api.services.youtube.YouTube;

import org.apache.http.util.ByteArrayBuffer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutionException;

import static com.naman14.timber.dataloaders.SongLoader.musicMediaPath;

public class YoutubeToMp3 {



    public static String extractAndDownloadAudio(String videoId) {
        VideoPlayerConfig data=YoutubeToMp3.getYtMP3(videoId);
        String audioUrl=data.getStreamingData().getAdaptiveAudioStreams().get(0).getUrl();
        System.out.println(audioUrl);
        try {
            return new YtDownloadAudioTask().execute(audioUrl,data.getVideoDetails().getTitle()+".m4a").get();
        }
        catch(ExecutionException e) {
            e.printStackTrace();
        }
        catch(InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    static class YtGetVideoConfigTask extends AsyncTask<String, Void, VideoPlayerConfig> {

        protected VideoPlayerConfig doInBackground(String... params) {
           return extractVideo(params[0]);
        }

        protected void onPostExecute(VideoPlayerConfig videoData) {

        }
    }
    static class YtDownloadAudioTask extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... params) {
           return downloadFile(params[0],params[1]);
        }

        protected void onPostExecute(String videoData) {

        }
    }

    static VideoPlayerConfig extractVideo(String videoId) {
        YoutubeJExtractor youtubeJExtractor = new YoutubeJExtractor();
        try {
            VideoPlayerConfig config= youtubeJExtractor.extract(videoId);
            return config;
        }
        catch (ExtractionException e) {
            // Something really bad happened, nothing we can do except just show some error notification to the user
            e.printStackTrace();
        }
        catch (YoutubeRequestException e) {
            // Possibly there are some connection problems, ask user to check the internet connection and then retry
            e.printStackTrace();
        }
        catch(VideoIsUnavailable videoIsUnavailable) {
            videoIsUnavailable.printStackTrace();
        }
        return null;
    }

    public static VideoPlayerConfig getYtMP3(String videoId){
        try {
            return new YtGetVideoConfigTask().execute(videoId).get();
        }
        catch(ExecutionException e) {
            e.printStackTrace();
        }
        catch(InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Downlaods File from URL to musicMediaPath with fileName*/
    public static String downloadFile(String strUrl, String fileName) {


        try {
            File dir = new File(musicMediaPath);
            if (dir.exists() == false) {
                dir.mkdirs();
            }

            URL url = new URL(strUrl);
            File file = new File(dir, fileName);

            URLConnection ucon = url.openConnection();
            InputStream is = ucon.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            org.apache.commons.io.FileUtils.copyInputStreamToFile(is, file);
            //TODO Permission denied
           /* ByteArrayBuffer baf = new ByteArrayBuffer(20000);
            int current = 0;
            while ((current = bis.read()) != -1) {
                baf.append((byte) current);
            }
            FileOutputStream fos = new FileOutputStream(file);

            fos.write(baf.toByteArray());
            fos.flush();
            fos.close();*/
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}