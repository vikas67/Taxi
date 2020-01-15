package tech.iwish.taxi.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;


import com.firebase.client.Firebase;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tech.iwish.taxi.R;

import tech.iwish.taxi.config.Constants;
import tech.iwish.taxi.config.SharedpreferencesUser;
import tech.iwish.taxi.connection.ConnectionServer;
import tech.iwish.taxi.connection.JsonHelper;
import tech.iwish.taxi.extended.TextViewFont;
import tech.iwish.taxi.fragment.RideConfiremDriverDetailsFragment;
import tech.iwish.taxi.other.DistanceList;
import tech.iwish.taxi.other.VehicleList;

import static tech.iwish.taxi.config.SharedpreferencesUser.USER_CONTACT;

public class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.Viewholder> {

    List<VehicleList> vehicleList;
    Context context;
    private int currentSelectedPosition = RecyclerView.NO_POSITION ;
    private Map  data ;
    Map<String , LatLng>  allLatLng ;
    Map<String , Double>  latitude_logitude ;
    List<DistanceList> distanceLists = new ArrayList<>();


    public VehicleAdapter(FragmentActivity activity, List<VehicleList> vehicleList , Map<String, LatLng> allLatLng  , Map<String, Double> latitude_logitude) {

        this.vehicleList = vehicleList;
        this.context = activity;
        this.allLatLng = allLatLng ;
        this.latitude_logitude = latitude_logitude ;


    }



    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_vehicle_design, parent, false);
        Viewholder viewholder = new Viewholder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull final Viewholder holder, final int position) {

        holder.total_rate.setText(vehicleList.get(position).getTotrate());
        holder.total_titme.setText(vehicleList.get(position).getTottime());

        holder.clickConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            SharedpreferencesUser sharedpreferencesUser = new SharedpreferencesUser(context);
            data = sharedpreferencesUser.getShare();
            Object userContact =data.get(USER_CONTACT);

                currentSelectedPosition = position;
                notifyDataSetChanged();

            }
        });

        if (currentSelectedPosition == position){

//            holder.clickConfirm.setBackgroundColor(context.getResources().getColor(R.color.yellowColor));
            holder.clickConfirm.setBackground(context.getResources().getDrawable(R.drawable.click_design));
            holder.button_Conirm.setVisibility(View.VISIBLE);
            holder.booking_confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                Firebase url = new Firebase("https://driver-ef8e9.firebaseio.com/confirm/");
                Firebase firebase = url.child("LatlngPick");
                Firebase firebase1 = url.child("VehicleType");
                Firebase firebase2 = url.child("UserType");
                Firebase firebase3 = url.child("LatlngDrop");
                firebase.setValue(allLatLng.get("PickupCurrentLocation"));
                firebase1.setValue(vehicleList.get(position).getCatagory_name());
                firebase2.setValue(data.get(USER_CONTACT));
                firebase3.setValue(allLatLng.get("DroplocationLatLng"));

                    RideConfiremDriverDetailsFragment rideConfiremDriverDetailsFragment = new RideConfiremDriverDetailsFragment();
                    FragmentManager fm = ((FragmentActivity)context).getSupportFragmentManager();
                    fm.beginTransaction().replace(R.id.confirRide, rideConfiremDriverDetailsFragment).commit();


                }
            });
        }else {
            holder.button_Conirm.setVisibility(View.GONE);
            holder.clickConfirm.setBackground(context.getResources().getDrawable(R.drawable.fragment_design_vehicle));
        }

    }

    @Override
    public int getItemCount() {

        return vehicleList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        LinearLayout clickConfirm;
        LinearLayout button_Conirm;
        Button booking_confirm;
        TextView total_rate , total_titme;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            clickConfirm = (LinearLayout) itemView.findViewById(R.id.clickConfirm);
            button_Conirm = (LinearLayout) itemView.findViewById(R.id.button_Conirm);
            booking_confirm = (Button) itemView.findViewById(R.id.booking_confirm);
            total_rate = (TextView) itemView.findViewById(R.id.total_rate);
            total_titme = (TextView) itemView.findViewById(R.id.total_titme);
        }
    }
}