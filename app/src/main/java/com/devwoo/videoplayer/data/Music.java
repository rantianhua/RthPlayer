package com.devwoo.videoplayer.data;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.sql.DataTruncation;

/**
 * Created by rth on 15/10/28.
 * 存储音乐信息
 */
public class Music {

    public int id;
    public String displayName;  //显示的名字
    public String mussicData;
    public String album;
    public String artist;
    public int duration;
    public long size;

    //选择的列
    public static String[] projection = {
            MediaStore.Audio.Media._ID, //音乐文件的id
            MediaStore.Audio.Media.DISPLAY_NAME,    //音乐文件的名称
            MediaStore.Audio.Media.DATA,    //音乐文件的数据
            MediaStore.Audio.Media.ALBUM,   //音乐对应的相册
            MediaStore.Audio.Media.ARTIST,  //音乐家信息
            MediaStore.Audio.Media.DURATION,    //音乐时长
            MediaStore.Audio.Media.SIZE //大小
    };
    //查询的过滤条件
    private String where = "mime_type in ('audio/mpeg','audio/x-ms-wma') and bucket_display_name <> 'audio' and is_music > 0 ";

    /**
     * 从cursor中获取音乐信息
     * @param cursor
     */
    public void loadFromCursor(Cursor cursor) {
        if (cursor != null && !cursor.isClosed()) {
            id = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
            displayName = cursor.getString(1);
            mussicData = cursor.getString(2);
            album = cursor.getString(3);
            artist = cursor.getString(4);
            duration = cursor.getInt(5);
            size = cursor.getLong(6);
        }
    }

    /**
     * 获取该音乐对应的uri
     * @return
     */
    public Uri getContentUri() {
        Uri baseUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        return baseUri.buildUpon().appendPath(String.valueOf(id)).build();
    }

}
