package com.devwoo.videoplayer.app;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.devwoo.videoplayer.R;
import com.devwoo.videoplayer.fragment.FragmentMusic;
import com.devwoo.videoplayer.fragment.FragmentVideo;

import java.util.ArrayList;
import java.util.List;

public class VideoActivity extends FragmentActivity {

	private static final String TAG = "VideoActivity";
	private FragmentVideo fragmentVideo;	//显示视频信息
	private FragmentMusic fragmentMusic;	//显示音乐信息
	private ViewPager viewPager;	//管理多个Fragments
	private PagerTabStrip tabStrip;	//表天栏

	private List<Fragment> fragments = new ArrayList<>();	//存储多个Fragment

	//管理fragments的适配器
	FragmentPagerAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		viewPager = (ViewPager) findViewById(R.id.viewpager);
		fragmentVideo = new FragmentVideo();
		fragments.add(fragmentVideo);
		fragmentMusic = new FragmentMusic();
		fragments.add(fragmentMusic);
		PagerTabStrip tabStrip = new PagerTabStrip(this);
		tabStrip=(PagerTabStrip)findViewById(R.id.pagertitle);
		tabStrip.setTabIndicatorColor(Color.BLUE);
		tabStrip.setTextSpacing(100);
		adapter= new FragmentPagerAdapter(getSupportFragmentManager()) {
			@Override
			public android.support.v4.app.Fragment getItem(int position) {
				return fragments.get(position);
			}

			@Override
			public int getCount() {
				return fragments.size();
			}

			@Override
			public CharSequence getPageTitle(int position) {
				switch (position){
					case 0:
						return "视频";
					case 1:
						return "音乐";
				}
				return "视频";
			}
		};
		viewPager.setAdapter(adapter);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

}
