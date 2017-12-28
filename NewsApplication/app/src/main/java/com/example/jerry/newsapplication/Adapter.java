package com.example.jerry.newsapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Adapter extends ArrayAdapter<Newss>
{
    private final static String DATE_FORMAT="MM//dd//yyyy";
    public Adapter(Context context,ArrayList<Newss>news){super(context,0,news);}
    static class ViewHolder{
        TextView tvTitlee;
        TextView tvDescriptionn;
        TextView tvCategoory;
        TextView tvAuthoor;
        TextView tvPublishedDatee;
    }
    @Override
    public View getView(int position, View counterView,ViewGroup parent){
        Newss news=getItem(position);
        ViewHolder view;
        if(counterView== null){
            counterView=LayoutInflater.from(getContext()).inflate(R.layout.itm_list,parent,false);
            view=new ViewHolder();
            view.tvTitlee=(TextView)counterView.findViewById(R.id.title);
            view.tvDescriptionn=(TextView)counterView.findViewById(R.id.descriptionn);
            view.tvCategoory=(TextView)counterView.findViewById(R.id.categoory);
            view.tvAuthoor=(TextView)counterView.findViewById(R.id.authoor);
            view.tvPublishedDatee=(TextView)counterView.findViewById(R.id.published_datee);
            counterView.setTag(view);
        }else{
            view=(ViewHolder)counterView.getTag();

        }
        view.tvTitlee.setText(news.getTitlee());
        view.tvDescriptionn.setText(news.getDescriptionn());
        view.tvCategoory.setText(news.getCategoory());
        view.tvAuthoor.setText(news.getmAuthoor());
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
        if(news.getPublishedDatee()!=null){
            view.tvPublishedDatee.setText(formatter.format(news.getPublishedDatee()));

        }else{
            view.tvPublishedDatee.setText(R.string.nodate);
        }
        return counterView;

    }
}