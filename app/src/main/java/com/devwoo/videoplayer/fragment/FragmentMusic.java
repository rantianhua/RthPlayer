package com.devwoo.videoplayer.fragment;

import android.database.Cursor;
import android.database.DataSetObserver;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.devwoo.videoplayer.R;
import com.devwoo.videoplayer.data.Music;
import com.devwoo.videoplayer.data.MusicAdapter;
import com.devwoo.videoplayer.ui.FooterProgressView;
import com.devwoo.videoplayer.utils.VideoUtils;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by rth on 15/10/27.
 */
public class FragmentMusic extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,AdapterView.OnItemClickListener,Handler.Callback,
        MediaPlayer.OnPreparedListener{

    private ListView listView;
    private View mEmptyView;
    private View footerView;    //底部视图
    private SeekBar seekBar;    //显示和控制进度
    private TextView tvCurrentTime,tvTotalTime;    //显示当前已播放时间和总的时间
    private RelativeLayout rlBottom;

    private MusicAdapter adapter;      //内容适配器
    //工作线程
    private Handler workhan;
    private HandlerThread handlerThread;
    private MediaPlayer player; //播放器
    private boolean isWorking = false;  //标识工作线程有没开启
    private Music music; //正在播放的音乐
    private  FooterProgressView footerProgressView; //底部视图
    private int currentPro;
    private static Handler mainHan;
    private boolean isChanging = false;
    private Timer mTimer = new Timer();
    private TimerTask timerTask;
    private boolean isPause = false;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new MusicAdapter(getActivity());
        adapter.registerDataSetObserver(new MusicDataObserver());
        getLoaderManager().initLoader(0, null, this);    //加载音频
        player = new MediaPlayer();
        player.setOnPreparedListener(this);
        mainHan = new Handler(Looper.myLooper(),this);
    }

    @Override
    public void onStart() {
        super.onStart();
        if(!isWorking) {
            isWorking = true;
            handlerThread = new HandlerThread("music");
            handlerThread.start();
            workhan = new Handler(handlerThread.getLooper(),this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(player.isPlaying()) {
            player.pause();
            isPause = true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(isPause) {
            isPause = false;
            player.start();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(player.isPlaying()) {
            player.stop();
        }
        player.release();
        handlerThread.quit();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_medio,null);
        listView = (ListView) view.findViewById(R.id.video_list);
        mEmptyView = view.findViewById(R.id.empty_view);
        rlBottom = (RelativeLayout) view.findViewById(R.id.rl_bottom);
        seekBar = (SeekBar) view.findViewById(R.id.seekbar_progress);
        tvCurrentTime = (TextView) view.findViewById(R.id.tv_current_time);
        tvTotalTime = (TextView) view.findViewById(R.id.tv_total_time);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //fromUser表示该变化是用户触发的

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isChanging = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isChanging = false;
                workhan.obtainMessage(1, seekBar.getProgress()).sendToTarget();
            }
        });
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        return view;
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case 0:
                Music currMusic = (Music)msg.obj;
                if(music != null && currMusic.id == music.id) {
                    if(player.isPlaying()) {
                        player.pause();
                    }else {
                        player.start();
                    }
                }else {
                    playMusic(currMusic.mussicData);
                    music = currMusic;
                }
                return true;
            case 1:
                int pro = (int)msg.obj;
                float scale = Float.valueOf(fo.format(pro / (float)100));
                player.seekTo((int)(scale * player.getDuration()));
                return true;
            case 2:
                int max = (int)msg.obj;
                seekBar.setMax(100);
                tvTotalTime.setText(VideoUtils.durationFormat(max));
                return true;
            case 3:
                if(!isChanging) {
                    tvCurrentTime.setText(VideoUtils.durationFormat(msg.arg2));
                    seekBar.setProgress(msg.arg1);
                }
                return true;
        }
        return false;
    }

    private void playMusic(String path) {
        try {
            player.reset();
            player.setDataSource(path);
            player.prepare();
        }catch (Exception e) {

        }
    }

    DecimalFormat fo = new DecimalFormat("0.00");
    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
        mainHan.obtainMessage(2,mp.getDuration()).sendToTarget();
        if(timerTask != null) {
            timerTask.cancel();
        }

        timerTask = new TimerTask() {
            @Override
            public void run() {
                int current = player.getCurrentPosition();
                float scale = Float.valueOf(fo.format((float)current / (float)player.getDuration()));
                int pr = (int)(scale * 100);
                mainHan.obtainMessage(3,pr,current).sendToTarget();
            }
        };
        mTimer.schedule(timerTask, 0, 10);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(),
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                Music.projection,
                null,
                null,
                null);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(rlBottom.getVisibility() != View.VISIBLE) {
            rlBottom.setVisibility(View.VISIBLE);
        }
        workhan.obtainMessage(0,-1,-1,((MusicAdapter.ViewHolder)view.getTag()).music).sendToTarget();
    }

    //内容观察者
    private class MusicDataObserver extends DataSetObserver {

        @Override
        public void onChanged() {
            super.onChanged();
            final int count = adapter.getCount();
            if(count <= 0){
                mEmptyView.setVisibility(View.VISIBLE);
            }else{
                mEmptyView.setVisibility(View.INVISIBLE);
            }
        }

    }
}
