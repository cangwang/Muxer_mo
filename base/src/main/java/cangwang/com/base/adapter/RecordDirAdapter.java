package cangwang.com.base.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cangwang.com.base.R;
import cangwang.com.base.utils.ImageLoader;
import cangwang.com.base.utils.ReadUtil;


/**
 * Created by air on 16/10/12.
 */

public class RecordDirAdapter extends RecyclerView.Adapter<RecordDirAdapter.DirViewHolder> {
    private List<Map<String,Object>> datas = new ArrayList<>();
    private Context mContext;
    private String choosePath;

    private ImageLoader loader = ImageLoader.getInstane();

    public RecordDirAdapter(Context context){
        mContext = context;
    }

    public RecordDirAdapter(Context context, String path){
        mContext = context;
        if(path == null){
            datas.addAll(ReadUtil.getDir("/"));
        }else {
            datas.addAll(ReadUtil.getDir(path));
            choosePath = path;
        }
    }

    public void setDatas(List<Map<String,Object>> list){
        datas.addAll(list);
        notifyDataSetChanged();
    }

    public void addData(Map<String,Object> item){
        datas.add(item);
        notifyDataSetChanged();
    }

    @Override
    public DirViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DirViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.record_dir_item_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(DirViewHolder holder, int position) {
        Map<String,Object> dat= datas.get(position);
        if(loader.isBimapExist((String)dat.get("name"))){
            holder.img.setImageBitmap(loader.getImage((String)dat.get("name")));
        }else {
            Bitmap bitmap =BitmapFactory.decodeResource(mContext.getResources(),(int)dat.get("image"));
            loader.addImage((String)dat.get("name"), bitmap);
            holder.img.setImageBitmap(bitmap);
        }
//        holder.img.setImageDrawable(mContext.getResources().getDrawable((int)dat.get("image"),null));

        holder.tv.setText((String)dat.get("name"));
        holder.path = (String)dat.get("path");
        holder.isDire = (boolean)dat.get("isDire");
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public String getChoosePath(){
        return choosePath;
    }

    public class DirViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView img;
        TextView tv;
        String path;
        boolean isDire = false;

        public DirViewHolder(View view)
        {
            super(view);
            img = (ImageView) view.findViewById(R.id.dir_img);
            tv = (TextView) view.findViewById(R.id.dir_txt);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(isDire) {
                datas.clear();
                datas.addAll(ReadUtil.getDir(path));
                choosePath = path;
                notifyDataSetChanged();
            }
        }
    }
}
