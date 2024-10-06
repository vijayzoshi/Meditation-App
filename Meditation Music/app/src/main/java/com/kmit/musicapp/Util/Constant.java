package com.kmit.musicapp.Util;

import android.content.Context;
import android.content.SharedPreferences;

import com.kmit.musicapp.Model.Pacage;
import com.kmit.musicapp.Model.User;

public class Constant {

    //url
        public static final String BASE_URL = "https://5besttech.com/music/";
    public static final String image = BASE_URL;
    public static final String Signup = BASE_URL + "API/signup";
    public static final String Signin = BASE_URL + "API/signin";

    public static final String Home_Component_List = BASE_URL + "API/home_components";
    public static final String Home = BASE_URL + "API/home";
    public static final String Get_All_Musics = BASE_URL + "API/getAllMusics";
    public static final String Get_All_Artists = BASE_URL + "API/getAllArtists";
    public static final String Get_All_Albums = BASE_URL + "API/getAllAlbums";
    public static final String Get_All_Movies = BASE_URL + "API/getAllMovies";
    public static final String Get_All_Category = BASE_URL + "API/home";

    public static final String Single_Album_Screen = BASE_URL + "API/viewAlbum";
    public static final String Single_Movie_Screen = BASE_URL + "API/viewMovie";
    public static final String View_Artist_Music = BASE_URL + "API/viewArtist";
    public static final String Search = BASE_URL + "API/search";

    public static final String Like = BASE_URL + "API/like";
    public static final String Unlike = BASE_URL + "API/unlike";

    public static final String Get_User_Playlists = BASE_URL + "API/getPlaylists";
    public static final String AddInPlaylistMusic = BASE_URL + "API/addInPlaylist";
    public static final String CreateNewPlaylist = BASE_URL + "API/createPlayList";
    public static final String DeletePlayList = BASE_URL + "API/deletePlayList";
    public static final String Get_user_playlist_music = BASE_URL + "API/getPlaylistMusic";
    public static final String Check_Download_allowed = BASE_URL + "API/isAllowDownloads";
    public static final String Get_Packages = BASE_URL + "API/getPackages";
    public static final String Payment = BASE_URL + "API/getStripePaymentScreen";
    public static final String EdiProfile = BASE_URL + "API/ediProfile";
    public static final String token = BASE_URL + "API/registerUserToken";
    public static final String Settings_Flag = BASE_URL + "API/settingsFlag";
    public static final String Getcategory_music = BASE_URL + "API/getCategoryMusic";
    public static final String SingalMusic = BASE_URL + "API/playMusic";


    // login
    public static final String Single_Music_Screen = "Single Music Screen";
    public static final String METHOD_LOGIN = "user_login";
    public static final String METHOD_REGISTRATION = "user_register";
    public static final String METHOD_FORGOT_PASSWORD = "forgot_pass";
    public static final String METHOD_PROFILE = "user_profile";
    public static final String METHOD_EDIT_PROFILE = "edit_profile";

    //home
    public static final String METHOD_HOME_COMPONENT_LISt = "Home Component List";

    public static final String METHOD_HOME_Banner_Slider = "Banner Slider";
    public static final String METHOD_HOME_CETEGORY = "Category";
    public static final String METHOD_HOME_Top_Sounds = "Top Sounds";
    public static final String METHOD_HOME_Popular_Sounds = "Popular Sounds";
    public static final String METHOD_HOME_Recently_Played = "Recently Played";
    public static final String METHOD_HOME_Sleep_Stories = "Sleep Stories";

    public static final String METHOD_HOME_Recommended_Music = "Recommended Music";
    public static final String METHOD_HOME_CETEGORY_ditail = "getDiscoverlist";
    //all list fragment
    public static final String METHOD_All_music = "All Music";
    public static final String METHOD_Search = "Search";
    //singal screen
    public static final String Music_Screen = "Music_Screen";

    //like unlike addplaylist
    public static final String METHOD_Like = "Like";
    public static final String METHOD_Unlike = "Unlike";
    public static final String MY_add_music_playlis = "addmusic";
    public static final String CreatePlayList = "create new playlist";
    public static final String DeleteList = "Delete Playlist";
    public static final String Own_play_list_detail = "Get user playlist's music";
    public static final String Download = "Check Download allowed";
    public static final String S_Payment = "Webview";
    public static final String Edite_profile = "ediProfile";
    public static final String Metherd_Settings_Flag = "Settings_Flag";

    private static final String MY_PREFS_NAME = "user_info";

//varieabul

    public static String is_myProfile = "0";
    public static String is_myProfile_fav = "1";

    //album
    public static String album_id = "";
    public static String album_name = "";
    public static String album_image = "";
    public static String music_count = "";

    //album
    public static String category_id = "";
    public static String category_name = "";
    public static String category_image = "";

    //own_playlist
    public static String own_playlist_id = "";
    public static String own_playlist_name = "";
    public static String own_playlist_image = "";
    public static String own_playlist_music_count = "";

    //artist
    public static String artist_id = "";
    public static String artist_name = "";
    public static String artist_image = "";

    //movie
    public static String movie_id = "";
    public static String movie_name = "";
    public static String movie_image = "";
    public static String movie_music_count = "";

    //payment
    public static Pacage package_id;


    //sharepre
    public static final String mypreference = "mypref";
    public static final String pref_email = "pref_email";
    public static final String pref_password = "pref_password";
    public static final String pref_check = "pref_check";

    //login suxess
    public static String Login = "0";
    public static String user_id = "0";
    public static String user_name = "";
    public static String user_email = "";
    public static String user_profile_pic = "";

    public static String like_Music = "Music";
    public static String like_Album = "Album";
    public static String like_Artist = "Artist";
    public static String like_Movie = "Movie";

    public static final String search_Music = "music";
    public static final String search_Album = "album";
    public static final String search_Artist = "artist";
    public static final String search_Movie = "movie";


    public static void edite_profile(Context context, User user) {
        SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE).edit();
        editor.putString("user_id", user.getUser_id());
        editor.putString("user_name", user.getUser_name());
        editor.putString("user_email", user.getUser_email());
        editor.putString("user_profile_pic", user.getUser_profile_pic());
        editor.apply();
    }


    public static void get_profile(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
        user_id = prefs.getString("user_id", "0");
        user_name = prefs.getString("user_name", "0");
        user_email = prefs.getString("user_email", "0");
        user_profile_pic = prefs.getString("user_profile_pic", "0");
    }


    public static String get_profile_id(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
        return user_id = prefs.getString("user_id", "0");
    }

    public static boolean Notification = false;
    public static boolean Package = false;
    public static boolean Ads = false;
    public static boolean IS_DEBUG = false;

    public static String appads = "ca-app-pub-3940256099942544~3347511713";
    public static String bannerads = "ca-app-pub-3940256099942544/6300978111";
    public static String interstitialads = "ca-app-pub-3940256099942544/1033173712";

}
