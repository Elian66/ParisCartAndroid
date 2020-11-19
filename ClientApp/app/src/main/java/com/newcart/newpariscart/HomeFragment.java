package com.newcart.newpariscart;

import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.Query;
import com.newcart.newpariscart.Common.Common;
import com.newcart.newpariscart.Interface.ItemClickListener;
import com.newcart.newpariscart.Model.Category;
import com.newcart.newpariscart.Model.Promo;
import com.newcart.newpariscart.Model.Restaurant;
import com.newcart.newpariscart.ViewHolder.MenuViewHolder;
import com.newcart.newpariscart.ViewHolder.PromoViewHolder;
import com.newcart.newpariscart.ViewHolder.RestaurantViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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

    LinearLayout hf_search;

    TextView address_hf;

    FirebaseDatabase database;
    DatabaseReference promo, menu, rest;

    LinearLayout home_endereco;

    RecyclerView recycler_menu, recycler_rest,recycler_close;
    RecyclerView.LayoutManager layout_promo, layout_menu, layout_rest,layout_close;

    FirebaseRecyclerAdapter<Promo, PromoViewHolder> adapter_promo;
    FirebaseRecyclerAdapter<Category, MenuViewHolder> adapter_menu;
    FirebaseRecyclerAdapter<Restaurant, RestaurantViewHolder> adapter_rest;
    FirebaseRecyclerAdapter<Restaurant, RestaurantViewHolder> adapter_close;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        home_endereco = rootView.findViewById(R.id.home_endereco);

        home_endereco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(),EditProfileActivity.class);
                startActivity(i);
            }
        });

        //Firebase
        database = FirebaseDatabase.getInstance();
        promo = database.getReference("Promos");

        hf_search = rootView.findViewById(R.id.hf_search);
        hf_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),BuscaActivity.class);
                startActivity(intent);
            }
        });

        address_hf = rootView.findViewById(R.id.address_hf);
        address_hf.setText(Common.currentUser.getAddress());

        //recycler_promo = rootView.findViewById(R.id.recycler_horizontal_promos);
        //layout_promo = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        //recycler_promo.setLayoutManager(layout_promo);

        /*
        FirebaseRecyclerOptions<Promo> optionsPromo =
                new FirebaseRecyclerOptions.Builder<Promo>()
                        .setQuery(promo, Promo.class)
                        .build();
        adapter_promo= new FirebaseRecyclerAdapter<Promo,PromoViewHolder>(optionsPromo)
        {
            @NonNull
            @Override
            public PromoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.promo_item, parent, false);

                return new PromoViewHolder(view);
            }
            @Override
            protected void onBindViewHolder(@NonNull PromoViewHolder viewHolder, int position, @NonNull final Promo model) {

                viewHolder.txtMenuName.setText(model.getText());
                Picasso.with(getContext()).load(model.getImage()).into(viewHolder.imageView);
                final Promo local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                        //TODO: FOR COUPONS

                    }
                });
            }
        };
        recycler_promo.setAdapter(adapter_promo);

        adapter_promo.startListening();

        if (adapter_promo != null){
            adapter_promo.startListening();
        }

        adapter_promo.stopListening();*/

        //CAT

        menu = database.getReference("Categories");
        menu.keepSynced(true);

        recycler_menu = rootView.findViewById(R.id.recycler_horizontal_menu);
        layout_menu = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recycler_menu.setLayoutManager(layout_menu);

        FirebaseRecyclerOptions<Category> optionsMenu =
                new FirebaseRecyclerOptions.Builder<Category>()
                        .setQuery(menu, Category.class)
                        .build();
        adapter_menu = new FirebaseRecyclerAdapter<Category, MenuViewHolder>(optionsMenu)
        {
            @NonNull
            @Override
            public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.menu_item, parent, false);

                return new MenuViewHolder(view);
            }
            @Override
            protected void onBindViewHolder(@NonNull MenuViewHolder viewHolder, int position, @NonNull final Category model) {

                viewHolder.txtMenuName.setText(model.getName());
                Picasso.with(getContext()).load(model.getImage()).into(viewHolder.imageView);
                final Category local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                        //TODO: FOR CAT
                        Fragment someFragment = new RestaurantsFragment();
                        Common.myCategory = adapter_menu.getRef(position).getKey();
                        //AAAAAAAAAAAAAAAAAAA
                        Common.catName = adapter_menu.getRef(position).getKey();
                        //BBBBBBBBBBBBBBBBBBB
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.container, someFragment ); // give your fragment container id in first parameter
                        transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                        transaction.commit();

                    }
                });
            }
        };
        recycler_menu.setAdapter(adapter_menu);

        adapter_menu.startListening();

        if (adapter_menu != null){
            adapter_menu.startListening();
        }

        adapter_menu.stopListening();

        //REST

        rest = database.getReference("Restaurants");
        rest.keepSynced(true);

        recycler_rest = rootView.findViewById(R.id.recycler_restaurants);
        layout_rest = new LinearLayoutManager(getContext());
        recycler_rest.setLayoutManager(layout_rest);

        recycler_close = rootView.findViewById(R.id.recycler_restaurants_closed);
        layout_close = new LinearLayoutManager(getContext());
        recycler_close.setLayoutManager(layout_close);

        String disponibilidade = "ABERTO";
        String indisponivel = "FECHADO";

        Query available = rest.orderByChild("address").equalTo(disponibilidade);
        Query unavailable = rest.orderByChild("address").equalTo(indisponivel);

        FirebaseRecyclerOptions<Restaurant> optionsRest =
                new FirebaseRecyclerOptions.Builder<Restaurant>()
                        .setQuery(available, Restaurant.class)
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
            protected void onBindViewHolder(final @NonNull RestaurantViewHolder viewHolder, int position, @NonNull final Restaurant model) {

                //For Shipping Cost
                if (model.getEntrega().equals("0,00")){
                    viewHolder.txtEntrega.setText("Entrega Grátis");
                    viewHolder.txtEntrega.setTextColor(getResources().getColor(R.color.green));
                }else {
                    viewHolder.txtEntrega.setText("R$" + model.getEntrega());
                }

                viewHolder.txtMenuName.setText(model.getName());
                viewHolder.txtCategory.setText(model.getCat());
                viewHolder.txtAddress.setText(model.getAddress());
                if (model.getAddress().equals("ABERTO")){
                    Picasso.with(getContext()).load(model.getImage()).into(viewHolder.imageView);
                }else {
                    viewHolder.imageView.setImageDrawable(getResources().getDrawable(R.drawable.fechado));
                }
                final Restaurant local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                        if (model.getAddress().equals("ABERTO")){

                            Common.restSelected = model;
                            Fragment someFragment = new DetailsFragment();
                            FragmentTransaction transaction = getFragmentManager().beginTransaction();
                            transaction.replace(R.id.container, someFragment ); // give your fragment container id in first parameter
                            transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                            transaction.commit();

                        }else {

                            Toast.makeText(getContext(), "Restaurante Fechado", Toast.LENGTH_SHORT).show();

                        }

                    }
                });
            }
        };

        FirebaseRecyclerOptions<Restaurant> optionsRest2 =
                new FirebaseRecyclerOptions.Builder<Restaurant>()
                        .setQuery(unavailable, Restaurant.class)
                        .build();
        adapter_close = new FirebaseRecyclerAdapter<Restaurant, RestaurantViewHolder>(optionsRest2)
        {
            @NonNull
            @Override
            public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.restaurant_closed, parent, false);

                return new RestaurantViewHolder(view);
            }
            @Override
            protected void onBindViewHolder(final @NonNull RestaurantViewHolder viewHolder, int position, @NonNull final Restaurant model) {

                viewHolder.txtMenuName.setText(model.getName());
                viewHolder.txtCategory.setText("");
                viewHolder.txtAddress.setText(model.getAddress());
                Picasso.with(getContext()).load(model.getImage()).into(viewHolder.imageView);
                /*if (model.getAddress().equals("ABERTO")){
                    Picasso.with(getContext()).load(model.getImage()).into(viewHolder.imageView);
                }else {
                    viewHolder.imageView.setImageDrawable(getResources().getDrawable(R.drawable.fechado));
                }*/
                final Restaurant local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                        if (model.getAddress().equals("ABERTO")){

                            Common.restSelected = model;
                            Fragment someFragment = new DetailsFragment();
                            FragmentTransaction transaction = getFragmentManager().beginTransaction();
                            transaction.replace(R.id.container, someFragment ); // give your fragment container id in first parameter
                            transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                            transaction.commit();

                        }else {

                            Toast.makeText(getContext(), "Restaurante Fechado", Toast.LENGTH_SHORT).show();

                        }

                    }
                });
            }
        };
        recycler_rest.setAdapter(adapter_rest);
        recycler_close.setAdapter(adapter_close);

        adapter_rest.startListening();
        adapter_close.startListening();

        if (adapter_rest != null){
            adapter_rest.startListening();
            adapter_close.startListening();
        }

        adapter_rest.stopListening();
        adapter_close.stopListening();

        return rootView;


    }

    @Override
    public void onStart() {
        super.onStart();
//        adapter_promo.startListening();
        adapter_menu.startListening();
        adapter_rest.startListening();
        adapter_close.startListening();

    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter_menu != null){
            //adapter_promo.startListening();
            adapter_menu.startListening();
            adapter_rest.startListening();
            adapter_close.startListening();

        }
    }

    @Override
    public void onStop() {
        super.onStop();
        //adapter_promo.stopListening();
        adapter_menu.stopListening();
        adapter_rest.stopListening();
        adapter_close.stopListening();

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
