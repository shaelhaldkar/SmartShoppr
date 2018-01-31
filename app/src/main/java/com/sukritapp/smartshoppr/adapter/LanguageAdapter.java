package com.sukritapp.smartshoppr.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sukritapp.smartshoppr.R;
import com.sukritapp.smartshoppr.listner.RecycleViewItemClickListner;
import com.sukritapp.smartshoppr.util.AppLog;

import java.util.ArrayList;

/**
 * Created by abc on 2/19/2017.
 * This adapter will show language list
 */

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.MyAdapter> {
    private final String TAG = AppLog.getClassName();

    ArrayList<String> mListLanguage = new ArrayList<>();
    private Context context;
    RecycleViewItemClickListner recycleViewItemClickListner;


    public LanguageAdapter(ArrayList<String> countrylist, RecycleViewItemClickListner recycleViewItemClickListner, Context applicationContext) {
        this.mListLanguage = countrylist;
        this.context = applicationContext;
        this.recycleViewItemClickListner=recycleViewItemClickListner;
    }

    @Override
    public MyAdapter onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.lay_language_item, parent, false);

        return new LanguageAdapter.MyAdapter(itemView,recycleViewItemClickListner);
    }

    @Override
    public void onBindViewHolder(MyAdapter holder, int position) {
        holder.txtviewCountry.setText(mListLanguage.get(position));
//        ImageLoader.getInstance().displayImage(IMAGE_URLS[position], holder.imageViewCountryIcon, options);

    }

    @Override
    public int getItemCount() {
        return mListLanguage.size();
    }



    public class MyAdapter extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtviewCountry;

        public MyAdapter(View itemView,RecycleViewItemClickListner recycleViewItemClickListner) {
            super(itemView);
            itemView.setOnClickListener(this);
            txtviewCountry = (TextView) itemView.findViewById(R.id.tv_language_item);
        }

        @Override
        public void onClick(View view) {

            recycleViewItemClickListner.onItemClick(txtviewCountry.getText().toString(),null, null);
        }
    }


}
