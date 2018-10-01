package com.dipak.dev.bloodapp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener{

    private OnFragmentInteractionListener mListener;
    private  CardAdapter cardAdapter;
    Button update_button, cancel_button;
    int UID;
    EditText username, dob, address, bloodgroup, mobile, donationDate;

    // private RecyclerView.Adapter adapter;

    //Creating a List of user
    private ArrayList<User> users;
    private RequestQueue requestQueue;

    public static final String UPLOAD_USERNAME = "Username";
    public static final String UPLOAD_ADDRESS = "Address";
    public static final String UPLOAD_DOB = "DOB";
    public static final String UPLOAD_MOBILE = "Mobile";
    public static final String UPLOAD_BLOOD_GROUP = "BloodGroup";
    public static final String UPLOAD_DONATION_DATE = "DonationDate";
    public static final String UPLOAD_ID = "ID";
    public static final String UPLOAD_URL = "http://10.0.2.2/BloodHub/update_profile.php";


    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view =  inflater.inflate(R.layout.fragment_profile, container, false);
        username = view.findViewById(R.id.p_username);
        dob = view.findViewById(R.id.p_dob);
        address = view.findViewById(R.id.p_address);
        bloodgroup = view.findViewById(R.id.p_bloodgroup);
        mobile = view.findViewById(R.id.p_mobile);
        donationDate = view.findViewById(R.id.p_donationDate);

        update_button = view.findViewById(R.id.update_btn);
        cancel_button = view.findViewById(R.id.p_cancel_button);
        update_button.setOnClickListener(this);
        cancel_button.setOnClickListener(this);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Config.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        //fetching the boolean value form shared preferences
        UID =sharedPreferences.getInt(Config.SHARED_PREFERENCE_UID, UID);
        //Toast.makeText(getActivity(),""+UID,Toast.LENGTH_SHORT).show();
        requestQueue = Volley.newRequestQueue(getActivity());
        //Initializing Views
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewProfile);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        cardAdapter = new CardAdapter(getContext());
        recyclerView.setAdapter(cardAdapter);
        getDataFromServer();
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.update_btn:
                 updateData();
                 break;

            case R.id.p_cancel_button:
                Toast.makeText(getContext(), "Operation Cancelled", Toast.LENGTH_LONG).show();
                break;

            default:
                Toast.makeText(getContext(), "Updating Failure", Toast.LENGTH_LONG).show();
                break;
        }
    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void updateData(){
        /////
        CardAdapter.ViewHolder cardAdapter = new CardAdapter.ViewHolder(getView().findViewById(R.id.p_username));
        EditText username =  cardAdapter.itemView.findViewById(R.id.p_username);

        CardAdapter.ViewHolder cardAdapter1 = new CardAdapter.ViewHolder(getView().findViewById(R.id.p_dob));
        EditText dob =  cardAdapter1.itemView.findViewById(R.id.p_dob);

        CardAdapter.ViewHolder cardAdapter2 = new CardAdapter.ViewHolder(getView().findViewById(R.id.p_address));
        EditText address =  cardAdapter2.itemView.findViewById(R.id.p_address);

        CardAdapter.ViewHolder cardAdapter3 = new CardAdapter.ViewHolder(getView().findViewById(R.id.p_bloodgroup));
        EditText BloodGroup =  cardAdapter3.itemView.findViewById(R.id.p_bloodgroup);

        CardAdapter.ViewHolder cardAdapter4 = new CardAdapter.ViewHolder(getView().findViewById(R.id.p_mobile));
        EditText mobile =  cardAdapter4.itemView.findViewById(R.id.p_mobile);

        CardAdapter.ViewHolder cardAdapter5 = new CardAdapter.ViewHolder(getView().findViewById(R.id.p_donationDate));
        EditText donationDate =  cardAdapter5.itemView.findViewById(R.id.p_donationDate);

        ////////////
        final String Username = username.getText().toString().trim();
        final String DOB = dob.getText().toString().trim();
        final String Address = address.getText().toString().trim();
        final String Bloodgroup = BloodGroup.getText().toString().trim();
        final String Mobile = mobile.getText().toString().trim();
        final String DonationDate = donationDate.getText().toString().trim();
        final String ID = String.valueOf(UID);

        //class UploadImage extends AsyncTask<Bitmap,Void,String>{
        @SuppressLint("StaticFieldLeak")
        class UploadImage extends AsyncTask<Void,Void,String> {

            private ProgressDialog loading;
            //RequestHandler rh = new RequestHandler();
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(), "Updating your Data", "Please wait...",true,true);
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                getDataFromServer();
                Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
            }
            @Override
            // protected String doInBackground(Bitmap... params) {
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();

                HashMap<String,String> data = new HashMap<>();
                data.put(UPLOAD_USERNAME, Username);
                data.put(UPLOAD_DOB, DOB);
                data.put(UPLOAD_ADDRESS, Address);
                data.put(UPLOAD_BLOOD_GROUP, Bloodgroup);
                data.put(UPLOAD_MOBILE, Mobile);
                data.put(UPLOAD_DONATION_DATE, DonationDate);
                data.put(UPLOAD_ID, ID);

                String result;
                result = rh.sendPostRequest(UPLOAD_URL,data);
                return result;
            }
        }
        UploadImage ui = new UploadImage();
        //ui.execute(bitmap);
        ui.execute();
        //getDataFromServer();
    }
    private void getDataFromServer() {
        //JsonArrayRequest of volley
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,Config.DATA_URL+String.valueOf(UID),null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        users = parseData(array);
                        cardAdapter.setUserList(users);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //     progressBar.setVisibility(View.GONE);
                        //If an error occurs that means end of the list has reached
                        Toast.makeText(getActivity(), "No More Items Available", Toast.LENGTH_SHORT).show();
                    }
                });
        //Returning the request
        requestQueue.add(jsonArrayRequest);
    }
    //This method will parse json data
    private ArrayList<User> parseData(JSONArray array) {
        ArrayList<User> userArrayList = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            //Creating the user object
            User user = new User();
            JSONObject json;
            try {
                //Getting json
                json = array.getJSONObject(i);
                //  Toast.makeText(getActivity(), json.getString(Config.TAG_IMAGE_URL),Toast.LENGTH_LONG).show();
                user.setUsername(json.getString(Config.TAG_USERNAME));
                user.setDob(json.getString(Config.TAG_DOB));
                user.setAddress(json.getString(Config.TAG_ADDRESS));
                user.setBlood_group(json.getString(Config.TAG_BLOOD_GROUP));
                user.setMobile(json.getString(Config.TAG_MOBILE));
                user.setDonation_Date(json.getString(Config.TAG_DONATION_DATE));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Adding the user object to the list
            userArrayList.add(user);
        }
        return userArrayList;
    }
}