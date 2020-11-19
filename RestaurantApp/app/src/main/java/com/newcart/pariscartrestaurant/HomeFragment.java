package com.newcart.pariscartrestaurant;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.newcart.pariscartrestaurant.Common.Common;
import com.newcart.pariscartrestaurant.Interface.ItemClickListener;
import com.newcart.pariscartrestaurant.Model.Food;
import com.newcart.pariscartrestaurant.ViewHolder.FoodViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;


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

    TextView add, meuStatus,toqueStatus;
    LinearLayout trocaStatus;

    FirebaseDatabase database;
    DatabaseReference rest;
    CardView cardStatus;

    TextView rsprice, btcprice, pedidos, lastpedido;

    RecyclerView recycler_rest;
    RecyclerView.LayoutManager layout_rest;

    FirebaseRecyclerAdapter<Food, FoodViewHolder> adapter_rest;
    TextView name, cat, ship;
    ImageView img;

    LinearLayout linearLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        //add =rootView.findViewById(R.id.home_address);
        //add.setText(Common.currentUser.getAddress());

        database = FirebaseDatabase.getInstance();
        rest = database.getReference("Pratos");
        rest.keepSynced(true);
        DatabaseReference fpp = database.getReference("Restaurants").child(Common.currentUser.getRestId());

        trocaStatus = rootView.findViewById(R.id.trocaStatus);
        meuStatus = rootView.findViewById(R.id.meuStatus);
        cardStatus = rootView.findViewById(R.id.cardStatus);
        toqueStatus = rootView.findViewById(R.id.toqueStatus);

        rsprice = rootView.findViewById(R.id.dash_rsprice);
        btcprice = rootView.findViewById(R.id.dash_btcprice);
        pedidos = rootView.findViewById(R.id.dash_pedidos);
        lastpedido = rootView.findViewById(R.id.dash_lastpedido);

        rsprice.setText("R$"+Common.currentUser.getSaldoReais());
        btcprice.setText(Common.currentUser.getSaldoBitcoin()+" BTC");
        pedidos.setText(Common.currentUser.getTotalPedidos());
        lastpedido.setText(Common.currentUser.getUltimoPedido());

        trocaStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Common.currentUser.getAddress().equals("FECHADO")){
                    Common.currentUser.setAddress("ABERTO");
                    Map<String, Object> passwordUpdate = new HashMap<>();
                    passwordUpdate.put("address","ABERTO");

                    cardStatus.setCardBackgroundColor(getResources().getColor(R.color.vermelho));
                    meuStatus.setText("ABERTO");
                    toqueStatus.setText("Toque para Fechar");

                    DatabaseReference user = FirebaseDatabase.getInstance().getReference("Restaurants");
                    user.child(Common.currentUser.getRestId())
                            .updateChildren(passwordUpdate)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(getContext(), "Restaurante Aberto", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }else {
                    Common.currentUser.setAddress("FECHADO");
                    Map<String, Object> passwordUpdate = new HashMap<>();
                    passwordUpdate.put("address","FECHADO");

                    cardStatus.setCardBackgroundColor(getResources().getColor(R.color.verde));
                    meuStatus.setText("FECHADO");
                    toqueStatus.setText("Toque para Abrir");

                    DatabaseReference user = FirebaseDatabase.getInstance().getReference("Restaurants");
                    user.child(Common.currentUser.getRestId())
                            .updateChildren(passwordUpdate)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(getContext(), "Restaurante Fechado", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

        if (Common.currentUser.getAddress().equals("ABERTO")){
            cardStatus.setCardBackgroundColor(getResources().getColor(R.color.verde));
            toqueStatus.setText("Toque para Fechar");
        }else if (Common.currentUser.getAddress().equals("FECHADO")){
            cardStatus.setCardBackgroundColor(getResources().getColor(R.color.vermelho));
            toqueStatus.setText("Toque para Abrir");
        }else {
            cardStatus.setCardBackgroundColor(getResources().getColor(R.color.gray_active_icon));
        }
        meuStatus.setText(Common.currentUser.getAddress());

        recycler_rest = rootView.findViewById(R.id.recycler_foods);
        layout_rest = new LinearLayoutManager(getContext());
        recycler_rest.setLayoutManager(layout_rest);

        Query searchByName = rest.orderByChild("restId").equalTo(Common.currentUser.getRestId());

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
                viewHolder.txtIngred.setText(model.getCategoryId());
                viewHolder.txtPrice.setText("R$"+model.getPrice());
                Picasso.with(getContext()).load(model.getImage()).into(viewHolder.imageView);
                final Food local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                        Common.foodName = adapter_rest.getRef(position).getKey();
                        Common.foodSelected = model;
                        Intent intent = new Intent(getContext(),ListAdicionaisActivity.class);
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

        linearLayout = rootView.findViewById(R.id.linlay);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),AddNewFood.class);
                startActivity(intent);
            }
        });

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
