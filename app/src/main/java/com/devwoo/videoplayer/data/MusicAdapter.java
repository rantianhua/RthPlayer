package com.devwoo.videoplayer.data;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.devwoo.videoplayer.R;
import com.devwoo.videoplayer.utils.VideoUtils;

/**
 * Created by mimashi1-8 on 15/10/28.
 */
public class MusicAdapter extends CursorAdapter {

    private Context context;

    public MusicAdapter(Context context) {
        super(context,null,false);
        this.context = context;
    }

    @Override
    public Object getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        getCursor().moveToPosition(position);
        View v;
        if (convertView == null) {
            v = newView(context, getCursor(), parent);
        } else {
            v = convertView;
        }
        bindView(v, context, getCursor());
        return v;
    }

    @Override
    public Cursor swapCursor(Cursor newCursor) {
        return super.swapCursor(newCursor);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        final View view = LayoutInflater.from(context).inflate(
                R.layout.video_list_item, parent, false);
        final ViewHolder holder = new ViewHolder();
        holder.thumbnail = (ImageView) view.findViewById(R.id.video_thumbnail);
        holder.name = (TextView) view.findViewById(R.id.video_name);
        holder.duration = (TextView) view.findViewById(R.id.video_duration);
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        final Music music = new Music();
        music.loadFromCursor(cursor);
        final ViewHolder holder = (ViewHolder) view.getTag();
        view.setBackgroundColor(context.getResources().getColor(
                VideoDataAdapter.mBgColor[(int) (Math.random() * 10 + 1)]));
        view.getBackground().setAlpha(150);
        holder.music = music;
        holder.thumbnail.setBackgroundResource(R.drawable.music);
        holder.name.setText(music.displayName);
        holder.duration.setText(VideoUtils.durationFormat(music.duration));
    }

    @Override
    protected void onContentChanged() {
        super.onContentChanged();
    }

    /**
     * 缓存视图
     */
    public class ViewHolder {
        ImageView thumbnail;
        TextView name;
        TextView duration;
        public Music music;
    }
}
