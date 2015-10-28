package com.devwoo.videoplayer.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.devwoo.videoplayer.R;
import com.devwoo.videoplayer.app.MovieActivity;
import com.devwoo.videoplayer.data.Video;
import com.devwoo.videoplayer.data.VideoDataAdapter;

/**
 * Created by rth on 15/10/27.
 */
public class FragmentVideo extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>,
        AdapterView.OnItemClickListener{

    private VideoDataAdapter mAdapter;  //内容适配器
    private ListView mListView; //展示视频内容
    private View mEmptyView;    //没有视频时展示的空视图

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new VideoDataAdapter(getActivity());
        mAdapter.registerDataSetObserver(new VideoDataObserver());
        getLoaderManager().initLoader(0, null,this);    //加载视频
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_medio,null);
        mListView = (ListView) view.findViewById(R.id.video_list);
        mEmptyView = view.findViewById(R.id.empty_view);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(),
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                Video.PROJECTION,
                null,
                null,
                null);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(view != null){
            Intent intent = new Intent(Intent.ACTION_VIEW).setDataAndType(
                    Uri.parse(((VideoDataAdapter.ViewHolder) view.getTag()).video.filePath), "video/*");
            intent.setClass(getActivity(), MovieActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
    }

    private class VideoDataObserver extends DataSetObserver {

        @Override
        public void onChanged() {
            super.onChanged();
            final int count = mAdapter.getCount();
            if(count <= 0){
                mEmptyView.setVisibility(View.VISIBLE);
            }else{
                mEmptyView.setVisibility(View.INVISIBLE);
            }
        }

    }
}
