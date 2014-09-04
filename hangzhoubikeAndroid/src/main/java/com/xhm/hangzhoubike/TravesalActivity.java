package com.xhm.hangzhoubike;

import android.content.Context;
import android.os.*;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.xhm.hangzhoubike.http.Client;
import com.xhm.hangzhoubike.object.BikeStation;
import com.xhm.hangzhoubike.object.Page;
import com.xhm.hangzhoubike.util.SystemUiHider;

import android.app.Activity;
import android.view.View;
import com.xhm.hangzhoubike.view.XListView;

import java.util.ArrayList;
import java.util.List;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class TravesalActivity extends Activity implements XListView.IXListViewListener {
    private List<BikeStation> stationList = new ArrayList<BikeStation>();

    private XListView stationListView;
    
    private ItemAdapter itemAdapter;
    
    private Client client = new Client();

    private final int UPDATE_UI = 1;
    
    private int page = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_travesal);

        itemAdapter = new ItemAdapter(this);//创建一个适配器

        stationListView = (XListView) findViewById(R.id.station_list_view);//实例化ListView
        stationListView.setAdapter(itemAdapter);//为ListView控件绑定适配器
        stationListView.setPullLoadEnable(true);
        stationListView.setXListViewListener(this);
        AnsyQueryStationTask anys = new AnsyQueryStationTask(client);
        anys.execute(page);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }
    
    private Handler mPageHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_UI: {
                    Log.i("TravesalActivity", "UPDATE_UI");
                    onLoad();
//                    stationListView.setSelection(stationListView.getCount() - 1);
                    itemAdapter.notifyDataSetChanged();
                    break;
                }
                default:
                    break;
            }
        }
    };
    private void onLoad() {
        stationListView.stopRefresh();
        stationListView.stopLoadMore();
        stationListView.setRefreshTime("刚刚");
    }

    @Override
    public void onRefresh() {
        mPageHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                page=0;
                stationList.clear();
                AnsyQueryStationTask anys = new AnsyQueryStationTask(client);
                anys.execute(page);
            }
        }, 2000);
    }

    @Override
    public void onLoadMore() {
        mPageHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                page++;
                AnsyQueryStationTask anys = new AnsyQueryStationTask(client);
                anys.execute(page);
            }
        }, 2000);
    }


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
                    stationList.addAll(result.getDatas());
                    mPageHandler.sendEmptyMessageDelayed(UPDATE_UI, 0);
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }
    }
}
