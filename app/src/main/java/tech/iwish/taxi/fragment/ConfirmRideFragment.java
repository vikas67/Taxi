package tech.iwish.taxi.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tech.iwish.taxi.R;
import tech.iwish.taxi.adapter.VehicleAdapter;
import tech.iwish.taxi.config.Constants;
import tech.iwish.taxi.connection.ConnectionServer;
import tech.iwish.taxi.connection.JsonHelper;
import tech.iwish.taxi.other.VehicleList;

public class ConfirmRideFragment extends Fragment {

    RecyclerView vehicle_recycle;
    List<VehicleList> vehicleList = new ArrayList<>();
    Map<String , LatLng>  allLatLng ;
    Map<String , Double>  latitude_logitude ;

    public ConfirmRideFragment(Map<String, LatLng> allLatLng   , Map<String , Double> latitude_logitude) {

        this.allLatLng = allLatLng ;
        this.latitude_logitude = latitude_logitude ;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_confirm_ride , null);


        vehicle_recycle = (RecyclerView)view.findViewById(R.id.vehicle_recycle);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        vehicle_recycle.setLayoutManager(linearLayoutManager);

        Double PickLatitude = latitude_logitude.get("PickLatitude");
        Double PickLogitude = latitude_logitude.get("PickLogitude");
        Double destinationsLatitude = latitude_logitude.get("dropLatitude");
        Double destinationsLogitude = latitude_logitude.get("dropLongitude");

        ConnectionServer connectionServer = new ConnectionServer();
        connectionServer.requestedMethod("POST");
        connectionServer.set_url(Constants.Vehicle);
        connectionServer.buildParameter("PickLatitude" ,PickLatitude.toString());
        connectionServer.buildParameter("PickLogitude" ,PickLogitude.toString());
        connectionServer.buildParameter("destinationsLatitude" ,destinationsLatitude.toString());
        connectionServer.buildParameter("destinationsLogitude" ,destinationsLogitude.toString());
        connectionServer.execute(new ConnectionServer.AsyncResponse() {
            @Override
            public void processFinish(String output) {
                Log.e("output" , output);
                JsonHelper jsonHelper = new JsonHelper(output);
                if(jsonHelper.isValidJson()){
                    String response = jsonHelper.GetResult("response");
                    if(response.equals("TRUE")){
                        JSONArray jsonArray = jsonHelper.setChildjsonArray(jsonHelper.getCurrentJsonObj(), "result1");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            jsonHelper.setChildjsonObj(jsonArray, i);

                            vehicleList.add(new VehicleList(jsonHelper.GetResult("catagory_id"),jsonHelper.GetResult("catagory_name"),jsonHelper.GetResult("vehicle_type"),jsonHelper.GetResult("waitingRate_m"),jsonHelper.GetResult("totrate"),jsonHelper.GetResult("tottime")));

                        }
                        VehicleAdapter vehicleAdapter = new VehicleAdapter(getActivity() , vehicleList , allLatLng , latitude_logitude);
                        vehicle_recycle.setAdapter(vehicleAdapter);
                    }
                }
            }
        });

        return view;
    }
}