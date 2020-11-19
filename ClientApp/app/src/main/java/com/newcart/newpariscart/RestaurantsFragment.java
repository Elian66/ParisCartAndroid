package com.newcart.newpariscart;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.newcart.newpariscart.Common.Common;
import com.newcart.newpariscart.Interface.ItemClickListener;
import com.newcart.newpariscart.Model.Restaurant;
import com.newcart.newpariscart.ViewHolder.RestaurantViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RestaurantsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RestaurantsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RestaurantsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public RestaurantsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RestaurantsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RestaurantsFragment newInstance(String param1, String param2) {
        RestaurantsFragment fragment = new RestaurantsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    FirebaseDatabase database;
    DatabaseReference rest;

    RecyclerView recycler_rest;
    RecyclerView.LayoutManager layout_rest;

    FirebaseRecyclerAdapter<Restaurant, RestaurantViewHolder> adapter_rest;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_restaurants, container, false);

        database = FirebaseDatabase.getInstance();
        rest = database.getReference("Restaurants");
        rest.keepSynced(true);

        recycler_rest = rootView.findViewById(R.id.recycler_cateRests);
        layout_rest = new LinearLayoutManager(getContext());
        recycler_rest.setLayoutManager(layout_rest);

        Query searchByName = rest.orderByChild("cat").equalTo(Common.myCategory);

        TextView txt = rootView.findViewById(R.id.name_resta);
        txt.setText(Common.catName);

        FirebaseRecyclerOptions<Restaurant> optionsRest =
                new FirebaseRecyclerOptions.Builder<Restaurant>()
                        .setQuery(searchByName, Restaurant.class)
                        .build();
        adapter_rest = new FirebaseRecyclerAdapter<Restaurant, RestaurantViewHolder>(optionsRest)
        {
            @NonNull
            @Override
            public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.restaurant_item, parent, false);

                return new RestaurantViewHolder(view);
            }
            @Override
            protected void onBindViewHolder(@NonNull RestaurantViewHolder viewHolder, int position, @NonNull final Restaurant model) {

                //For Shipping Cost
                if (model.getEntrega().equals("0,00")){
                    viewHolder.txtEntrega.setText("Entrega Grátis");
                    viewHolder.txtEntrega.setTextColor(getResources().getColor(R.color.green));
                }else {
                    viewHolder.txtEntrega.setText("Entrega R$" + model.getEntrega());
                }

                viewHolder.txtMenuName.setText(model.getName());
                viewHolder.txtCategory.setText(model.getCat());
                viewHolder.txtAddress.setText(model.getAddress());
                Picasso.with(getContext()).load(model.getImage()).into(viewHolder.imageView);
                final Restaurant local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                        Common.restSelected = model;
                        Fragment someFragment = new DetailsFragment();
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.container, someFragment ); // give your fragment container id in first parameter
                        transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                        transaction.commit();

                    }
                });
            }
        };
        recycler_rest.setAdapter(adapter_rest);

        adapter_rest.startListening();

        if (adapter_rest != null){
            adapter_rest.startListening();
        }

        adapter_rest.stopListening();

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter_rest.startListening();

    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter_rest != null){
            adapter_rest.startListening();

        }
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter_rest.stopListening();

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /*
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
    */

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
}
