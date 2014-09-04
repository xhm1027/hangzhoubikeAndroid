package com.xhm.hangzhoubike;

import android.content.Context;
import android.os.*;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.xhm.hangzhoubike.http.Client;
import com.xhm.hangzhoubike.object.BikeStation;
import com.xhm.hangzhoubike.object.Page;
import com.xhm.hangzhoubike.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.view.View;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class TravesalActivity extends Activity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * If set, will toggle the system UI visibility upon interaction. Otherwise,
     * will show the system UI visibility upon interaction.
     */
    private static final boolean TOGGLE_ON_CLICK = true;

    /**
     * The flags to pass to {@link SystemUiHider#getInstance}.
     */
    private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

    /**
     * The instance of the {@link SystemUiHider} for this activity.
     */
    private SystemUiHider mSystemUiHider;


    private List<BikeStation> stationList = new ArrayList<BikeStation>();

    private ListView stationListView;
    
    private ItemAdapter itemAdapter;
    
    private Client client = new Client();

    private final int UPDATE_UI = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_travesal);

        final View controlsView = findViewById(R.id.fullscreen_content_controls);
        final View contentView = findViewById(R.id.fullscreen_content);

        // Set up an instance of SystemUiHider to control the system UI for
        // this activity.
        mSystemUiHider = SystemUiHider.getInstance(this, contentView, HIDER_FLAGS);
        mSystemUiHider.setup();
        mSystemUiHider
                .setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
                    // Cached values.
                    int mControlsHeight;
                    int mShortAnimTime;

                    @Override
                    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
                    public void onVisibilityChange(boolean visible) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                            // If the ViewPropertyAnimator API is available
                            // (Honeycomb MR2 and later), use it to animate the
                            // in-layout UI controls at the bottom of the
                            // screen.
                            if (mControlsHeight == 0) {
                                mControlsHeight = controlsView.getHeight();
                            }
                            if (mShortAnimTime == 0) {
                                mShortAnimTime = getResources().getInteger(
                                        android.R.integer.config_shortAnimTime);
                            }
                            controlsView.animate()
                                    .translationY(visible ? 0 : mControlsHeight)
                                    .setDuration(mShortAnimTime);
                        } else {
                            // If the ViewPropertyAnimator APIs aren't
                            // available, simply show or hide the in-layout UI
                            // controls.
                            controlsView.setVisibility(visible ? View.VISIBLE : View.GONE);
                        }

                        if (visible && AUTO_HIDE) {
                            // Schedule a hide().
                            delayedHide(AUTO_HIDE_DELAY_MILLIS);
                        }
                    }
                });

        // Set up the user interaction to manually show or hide the system UI.
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TOGGLE_ON_CLICK) {
                    mSystemUiHider.toggle();
                } else {
                    mSystemUiHider.show();
                }
            }
        });

        itemAdapter = new ItemAdapter(this);//创建一个适配器

        stationListView = (ListView) findViewById(R.id.station_list_view);//实例化ListView
        stationListView.setAdapter(itemAdapter);//为ListView控件绑定适配器
        AnsyQueryStationTask anys = new AnsyQueryStationTask(client);
        anys.execute(0);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }



    Handler mHideHandler = new Handler();
    Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            mSystemUiHider.hide();
        }
    };

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    private Handler mPageHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_UI: {
                    Log.i("TravesalActivity", "UPDATE_UI");
                    itemAdapter.notifyDataSetChanged();
                    break;
                }
                default:
                    break;
            }
        }
    };



    /** 自定义适配器 */
    public class ItemAdapter extends BaseAdapter {
        private LayoutInflater mInflater;// 动态布局映射

        public ItemAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        // 决定ListView有几行可见
        @Override
        public int getCount() {
            return stationList.size();// ListView的条目数
        }

        @Override
        public Object getItem(int arg0) {
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = mInflater.inflate(R.layout.station_list, null);//根据布局文件实例化view
            TextView name = (TextView) convertView.findViewById(R.id.name);//找某个控件
            name.setText(stationList.get(position).getName());//给该控件设置数据(数据从集合类中来)
            TextView cenBeRent = (TextView) convertView.findViewById(R.id.canBeRent);//找某个控件
            cenBeRent.setText("可借：" + stationList.get(position).getCanBeRent());//给该控件设置数据(数据从集合类中来)
            TextView canBeReturn = (TextView) convertView.findViewById(R.id.canBeReturn);//找某个控件
            canBeReturn.setText("可还：" + stationList.get(position).getCanBeRent());//给该控件设置数据(数据从集合类中来)
            TextView address = (TextView) convertView.findViewById(R.id.address);
            address.setText(stationList.get(position).getAddress());
            return convertView;
        }
    }


    public class AnsyQueryStationTask extends AsyncTask<Integer, Client, Page<BikeStation>> {
        Client client = null;
        public AnsyQueryStationTask(Client client){
            this.client = client;
        }
        @Override
        protected Page<BikeStation> doInBackground(Integer... params) {
            Page<BikeStation> result =null;
            try {
                result = client.queryByPage(params[0]);
                if(result!=null){
                    stationList = result.getDatas();
                    mPageHandler.sendEmptyMessageDelayed(UPDATE_UI, 0);
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }
    }
}
