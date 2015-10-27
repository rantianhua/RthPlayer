package com.devwoo.videoplayer.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.devwoo.videoplayer.R;

/**
 * Created by rth on 15/10/27.
 * 该类控制、显示音频和视频播放的进度
 */
public class FooterProgressView {

    private View footerView;    //底部视图
    private SeekBar seekBar;    //显示和控制进度
    private TextView tvCurrentTime,tvTotalTime;    //显示当前已播放时间和总的时间

    /**
     * 构造函数，实例化底部进度视图
     * @param context 加载布局文件
     */
    public FooterProgressView(Context context) {
        initView(context);
    }

    /**
     * 初始化视图
     * @param context
     */
    private void initView(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        footerView = (View) inflater.inflate(R.layout.footer_progress,null);
        seekBar = (SeekBar) footerView.findViewById(R.id.seekbar_progress);
        tvCurrentTime = (TextView) footerView.findViewById(R.id.tv_current_time);
        tvTotalTime = (TextView) footerView.findViewById(R.id.tv_total_time);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //fromUser表示该变化是用户触发的

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }



}
