package com.sukritapp.smartshoppr.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.sukritapp.smartshoppr.R;
import com.sukritapp.smartshoppr.fragments.SeeAllDeatilFragments;
import com.sukritapp.smartshoppr.model.HomeTittle;
import com.sukritapp.smartshoppr.model.ResponseModel;

import java.util.ArrayList;

/**
 * Created by abc on 2/19/2017.
 */

public class HomeCategoryAdapter extends RecyclerView.Adapter<HomeCategoryAdapter.HomeAdapter> implements View.OnClickListener {

    private final Fragment activity;
    ArrayList<HomeTittle> homeContentList = new ArrayList<>();


    public HomeCategoryAdapter(ArrayList<HomeTittle> homeContentList, Fragment homeFragment) {
        this.homeContentList = homeContentList;
        this.activity = homeFragment;
    }

    @Override
    public HomeAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_item_categories, parent, false);

        return new HomeAdapter(itemView);
    }

    @Override
    public void onBindViewHolder(HomeAdapter holder, int position) {
        String tittle= homeContentList.get(position).getTittle();
        String output = tittle.substring(0, 1).toUpperCase() + tittle.substring(1);
        holder.tvCategories.setText(output);
        holder.btnExpand.setTag(homeContentList.get(position).getTittle());

        //setting adapter for each recycleview

        holder.recyVwSubCategories.setLayoutManager(new LinearLayoutManager(activity.getActivity(), LinearLayoutManager.HORIZONTAL, false));
      //  holder.recyVwSubCategories.addItemDecoration(new DividerItemDecoration(SmartShopprApp.getApplication(), DividerItemDecoration.HORIZONTAL_LIST));
        holder.recyVwSubCategories.setAdapter(new CategoryHorizontalAdapter(homeContentList.get(position).getMlist(),activity));

        holder.btnExpand.setTag("" + position);
        holder.btnExpand.setOnClickListener(this);


    }

    @Override
    public int getItemCount() {
        return homeContentList.size();
    }

    @Override
    public void onClick(View view) {

        int position = Integer.parseInt(view.getTag().toString());

        Bundle bundle = new Bundle();
        ArrayList<ResponseModel> listresponseModels = homeContentList.get(position).getMlist();
        String tittle = homeContentList.get(position).getTittle();
        bundle.putParcelableArrayList("list", listresponseModels);
        bundle.putString("tittle", tittle);
        Fragment nextFrag = new SeeAllDeatilFragments();
        nextFrag.setArguments(bundle);
        activity.getFragmentManager().beginTransaction().replace(R.id.content_frame, nextFrag, null).addToBackStack(null).commit();



    }

    public class HomeAdapter extends RecyclerView.ViewHolder {
        TextView tvCategories;
        TextView btnExpand;
        RecyclerView recyVwSubCategories;

        public HomeAdapter(View itemView) {
            super(itemView);
            btnExpand = (TextView) itemView.findViewById(R.id.btnMore);
            tvCategories = (TextView) itemView.findViewById(R.id.txtVwCategoriestittle);
            recyVwSubCategories = (RecyclerView) itemView.findViewById(R.id.recyVwHomeCategories);
        }
    }
}
