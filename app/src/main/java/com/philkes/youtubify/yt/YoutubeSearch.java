package com.philkes.youtubify.yt;
/**
 * Sample Java code for youtube.search.list
 * See instructions for running these code samples locally:
 * https://developers.google.com/explorer-help/guides/code_samples#java
 */

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeRequestInitializer;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.SearchResultSnippet;
import com.philkes.youtubify.model.YoutubeSong;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static com.philkes.youtubify.yt.Credentials.API_KEY;

public class YoutubeSearch {
    private static final Collection<String> SCOPES =
            Arrays.asList("https://www.googleapis.com/auth/youtube.readonly");

    private static final String APPLICATION_NAME = "API code samples";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static YouTube mService=null;

    /**
     * Build and return an authorized API client service.
     *
     * @return an authorized API client service
     * @throws GeneralSecurityException, IOException
     */
    public static YouTube getService() throws GeneralSecurityException, IOException {
        //final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        //Credential credential = authorize(httpTransport);
        if(mService!=null){
            return mService;
        }
        mService= new YouTube.Builder(new NetHttpTransport(), JSON_FACTORY, new HttpRequestInitializer() {
            @Override
            public void initialize(HttpRequest httpRequest) throws IOException {

            }
        }).setYouTubeRequestInitializer(new YouTubeRequestInitializer(API_KEY)).setApplicationName(APPLICATION_NAME).build();
        return mService;
    }

    /**
     * Call function to create API service object. Define and
     * execute API request. Print API response.
     *
     * @throws GeneralSecurityException, IOException, GoogleJsonResponseException
     */
    public static List<YoutubeSong> searchYoutubeSongs(final String queryString) {
        List<YoutubeSong> songs= new ArrayList<>();
        try {
            YouTube youtubeService =getService();
            // Define and execute the API request
            YouTube.Search.List request = youtubeService.search()
                    .list("snippet")
                    .setType("video")
                    .setVideoCategoryId("10")
                    .setMaxResults(5L)
                    .setQ(queryString);
            SearchListResponse response = request.execute();
            System.out.println(response);
            List<SearchResult> results= response.getItems();
            for(SearchResult result : results){
                SearchResultSnippet snippet=result.getSnippet();
                YoutubeSong song= new YoutubeSong(-1,-1,-1,
                        snippet.getTitle(),snippet.getChannelTitle(),
                        "",-1,-1,
                        snippet.getThumbnails().getDefault().getUrl(),
                        result.getId().getVideoId());
                songs.add(song);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return songs;
    }


}