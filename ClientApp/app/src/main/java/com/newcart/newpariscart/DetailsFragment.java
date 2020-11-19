package com.newcart.newpariscart;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.newcart.newpariscart.Common.Common;
import com.newcart.newpariscart.Interface.ItemClickListener;
import com.newcart.newpariscart.Model.Food;
import com.newcart.newpariscart.Model.Restaurant;
import com.newcart.newpariscart.ViewHolder.FoodViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public DetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailsFragment newInstance(String param1, String param2) {
        DetailsFragment fragment = new DetailsFragment();
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

    FirebaseRecyclerAdapter<Food, FoodViewHolder> adapter_rest;
    TextView name, cat, ship;
    ImageView img;

    Restaurant currentFood;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_details, container, false);

        name = rootView.findViewById(R.id.name_toolbar_worker);
        cat = rootView.findViewById(R.id.worker_detail_city);
        ship = rootView.findViewById(R.id.worker_detail_age);
        img = rootView.findViewById(R.id.img_worker);

        database = FirebaseDatabase.getInstance();
        rest = database.getReference("Pratos");
        rest.keepSynced(true);
        DatabaseReference fpp = database.getReference("Restaurants").child(Common.restSelected.getRestId());

        fpp.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentFood = dataSnapshot.getValue(Restaurant.class);
                Picasso.with(getContext()).load(currentFood.getImage())
                        .into(img);

                name.setText(currentFood.getName());
                cat.setText(currentFood.getCat());

                //For Shipping Cost
                if (currentFood.getEntrega().equals("0,00")){
                    ship.setText("Entrega Grátis");
                    ship.setTextColor(getResources().getColor(R.color.green));
                }else {
                    ship.setText("Entrega R$" + currentFood.getEntrega());
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        recycler_rest = rootView.findViewById(R.id.recycler_foods);
        layout_rest = new LinearLayoutManager(getContext());
        recycler_rest.setLayoutManager(layout_rest);

        Query searchByName = rest.orderByChild("restId").equalTo(Common.restSelected.getRestId());

        FirebaseRecyclerOptions<Food> optionsRest =
                new FirebaseRecyclerOptions.Builder<Food>()
                        .setQuery(searchByName, Food.class)
                        .build();
        adapter_rest = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(optionsRest)
        {
            @NonNull
            @Override
            public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.food_item, parent, false);

                return new FoodViewHolder(view);
            }
            @Override
            protected void onBindViewHolder(@NonNull FoodViewHolder viewHolder, int position, @NonNull final Food model) {

                viewHolder.txtMenuName.setText(model.getName());
                viewHolder.txtIngred.setText(model.getIngredientes());
                viewHolder.txtPrice.setText("R$"+model.getPrice());
                Picasso.with(getContext()).load(model.getImage()).into(viewHolder.imageView);
                final Food local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                        Common.foodName = adapter_rest.getRef(position).getKey();
                        Common.foodSelected = model;
                        Intent intent = new Intent(getContext(),FoodDetailActivity.class);
                        startActivity(intent);

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
