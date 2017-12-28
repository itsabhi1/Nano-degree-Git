package com.example.jerry.newsapplication;

import java.util.Date;
public class Newss
{
    private String mTitle;
    private String mDescription;
    private String mCategory;
    private Date mPublishDate;
    private String mAuthoor;
    private String mLink;
    public Newss(String title,String description,String categoory,String authoor,String lonk,Date publishedDate){
        mTitle=title;
        mDescription=description;
        mCategory=categoory;
        mAuthoor=authoor;
        mLink=lonk;
        mPublishDate= publishedDate;

    }
    public String getTitlee(){
        return mTitle;
    }
public String getDescriptionn(){return mDescription;
}
public String getCategoory(){
    return mCategory;
}
public String getmAuthoor(){
    return mAuthoor;
}
public String getMLinkk(){
    return mLink;

}
public Date getPublishedDatee(){
    return mPublishDate;
}
}
