package com.example.nm_management_system;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.jar.Attributes;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>
{
    private List<String> mData;
    MyAdapter(List<String> data){
        mData = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //link to layout list_item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //set txtItem context display
        holder.Nametext.setText(mData.get(position));

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView Nametext;
        private TextView Accounttext;

        ViewHolder(View itemView) {
            super(itemView);
            Nametext = (TextView) itemView.findViewById(R.id.name_text);
            Accounttext = (TextView)itemView.findViewById(R.id.account_text);
        }
    }
}
