package com.android.locus.Model;

import android.util.Log;

import com.google.gson.JsonArray;
import com.android.locus.constants.Project_Constant;

/**
 * Created by vishal.kumar1 on 04/10/16.
 */

public class PlaceInfo {
    private int noOfResturants;
    private float avgRatingofResturants;
    private int openResturant;
    private int noOfHospital;
    private int noOfPolice;
    private int noOfShoppingMalls;


    public void setHospital(JsonArray values){
        noOfHospital=values.size();
    }

    public void setPolice(JsonArray values){
        noOfPolice=values.size();
    }
    public void setShoppingMall(JsonArray values){
        noOfShoppingMalls =values.size();
    }

    public void setResturant(JsonArray values){
        noOfResturants=values.size();

        try{
            float avg=0;
            for(int i=0;i<values.size();i++){
                if(values.get(i).getAsJsonObject().get("rating")!=null)
                    avg=avg + values.get(i).getAsJsonObject().get("rating").getAsFloat();
                else
                    avg=avg+2.5f;
            }
            avgRatingofResturants=avg/values.size();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        try{
            int cnt=0;
            for(int i=0;i<values.size();i++){
                if(values.get(i).getAsJsonObject().get("opening_hours")!=null)
                    if(values.get(i).getAsJsonObject().get("opening_hours").getAsJsonObject().get("open_now").getAsBoolean())
                        cnt=cnt+1;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    public void Print(){
        Log.d("val ", "hosp " + noOfResturants + " " + noOfHospital + " " + noOfShoppingMalls + " " +noOfPolice);
    }

    public float safePercentage(){
        return  (noOfResturants/ Project_Constant.MaxResturant + noOfPolice/Project_Constant.MaxPolice
                + noOfHospital/ Project_Constant.MaxHospital + noOfShoppingMalls/ Project_Constant.MaxShoppingMall)*25;
    }

    public int getNoOfResturants() {
        return noOfResturants;
    }

    public void setNoOfResturants(int noOfResturants) {
        this.noOfResturants = noOfResturants;
    }

    public float getAvgRatingofResturants() {
        return avgRatingofResturants;
    }

    public void setAvgRatingofResturants(float avgRatingofResturants) {
        this.avgRatingofResturants = avgRatingofResturants;
    }

    public int getOpenResturant() {
        return openResturant;
    }

    public void setOpenResturant(int openResturant) {
        this.openResturant = openResturant;
    }

    public int getNoOfHospital() {
        return noOfHospital;
    }

    public void setNoOfHospital(int noOfHospital) {
        this.noOfHospital = noOfHospital;
    }

    public int getNoOfPolice() {
        return noOfPolice;
    }

    public void setNoOfPolice(int noOfPolice) {
        this.noOfPolice = noOfPolice;
    }

    public int getNoOfShoppingMalls() {
        return noOfShoppingMalls;
    }

    public void setNoOfShoppingMalls(int noOfShoppingMalls) {
        this.noOfShoppingMalls = noOfShoppingMalls;
    }
}
