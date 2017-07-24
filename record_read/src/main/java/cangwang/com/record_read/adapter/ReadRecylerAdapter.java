package cangwang.com.record_read.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cangwang.com.base.bean.ReadItemBean;
import cangwang.com.base.utils.ReadUtil;
import cangwang.com.record_read.R;


/**
 * Created by air on 16/7/6.
 */
public class ReadRecylerAdapter extends RecyclerView.Adapter<ReadRecylerAdapter.ReadRecordHolder> {
    private Context mContext;
    private List<ReadItemBean> datas= new ArrayList<>();

    public ReadRecylerAdapter(Context context){
        mContext = context;
//        datas = ReadUtil.getRecordFiles("/sdcard/Movies/YYRecord");
    }

    public void setDatas(List<ReadItemBean> list){
        datas.addAll(list);
    }

    public void clear(){
        datas.clear();
    }



    @Override
    public void onBindViewHolder(ReadRecylerAdapter.ReadRecordHolder holder, int position) {
        ReadItemBean bean = datas.get(position);
        holder.recordName.setText(bean.getRecordName());
        holder.recordSize.setText(bean.getRecordSize());
        holder.readImg.setImageBitmap(ReadUtil.getVideoThumbnail(bean.getRecordName(),bean.getCurPath()));
//        holder.recordCeateTime.setText(bean.getRecordCreateTime());
        holder.videoPath = bean.getCurPath();
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    @Override
    public ReadRecylerAdapter.ReadRecordHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ReadRecordHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.read_dir_item,parent,false));
    }

    class ReadRecordHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView readImg;
        TextView recordName;
        TextView recordSize;
//        TextView recordCeateTime;
        String videoPath;

        public ReadRecordHolder(View view){
            super(view);
            readImg = (ImageView)view.findViewById(R.id.read_file_img);
            recordName = (TextView)view.findViewById(R.id.record_file_name);
            recordSize = (TextView)view.findViewById(R.id.record_file_size);
//            recordCeateTime = (TextView)view.findViewById(R.id.record_create_time);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            String strend="";
            if(videoPath.toLowerCase().endsWith(".mp4")){
                strend="mp4";
            }
            else if(videoPath.toLowerCase().endsWith(".3gp")){
                strend="3gp";
            }
            else if(videoPath.toLowerCase().endsWith(".mov")){
                strend="mov";
            }
            else if(videoPath.toLowerCase().endsWith(".wmv")){
                strend="wmv";
            }

            intent.setDataAndType(Uri.parse("file://"+videoPath), "video/"+strend);
            v.getContext().startActivity(intent);
        }

    }
}
