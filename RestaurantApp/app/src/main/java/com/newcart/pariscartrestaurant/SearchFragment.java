package com.newcart.pariscartrestaurant;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.newcart.pariscartrestaurant.Common.Common;
import com.newcart.pariscartrestaurant.Interface.ItemClickListener;
import com.newcart.pariscartrestaurant.Model.FCMResponse;
import com.newcart.pariscartrestaurant.Model.FCMSendData;
import com.newcart.pariscartrestaurant.Model.Motorista;
import com.newcart.pariscartrestaurant.Model.Request;
import com.newcart.pariscartrestaurant.Model.TokenModel;
import com.newcart.pariscartrestaurant.Remote.IFCMService;
import com.newcart.pariscartrestaurant.Remote.RetrofitFCMClient;
import com.newcart.pariscartrestaurant.ViewHolder.MotoristaViewHolder;
import com.newcart.pariscartrestaurant.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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

    FirebaseDatabase database, dmoto;
    DatabaseReference rest, table_user;

    CardView novo,preparo,caminho,finalizado;

    RecyclerView recycler_rest;
    RecyclerView.LayoutManager layout_rest;

    LinearLayout dialo;

    FirebaseRecyclerAdapter<Request, OrderViewHolder> adapter_rest;
    String tipoBt, motoclick;

    RecyclerView recycler_moto;
    RecyclerView.LayoutManager layout_moto;

    FirebaseRecyclerAdapter<Motorista, MotoristaViewHolder> adapter_moto;

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    IFCMService ifcmService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        ifcmService = RetrofitFCMClient.getInstance().create(IFCMService.class);

        database = FirebaseDatabase.getInstance();
        rest = database.getReference("Requests");
        table_user = database.getReference("Motoristas");
        rest.keepSynced(true);

        novo = rootView.findViewById(R.id.car_novos);
        preparo = rootView.findViewById(R.id.car_prep);
        caminho = rootView.findViewById(R.id.car_caminho);
        finalizado = rootView.findViewById(R.id.car_finalizados);

        dialo = rootView.findViewById(R.id.motorista_dialog);
        dialo.setVisibility(View.GONE);

        tipoBt = Common.filtroOrder;

        if (Common.filtroOrder.equals("0")){
            novo.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));
            preparo.setCardBackgroundColor(getResources().getColor(R.color.gray_active_icon));
            caminho.setCardBackgroundColor(getResources().getColor(R.color.gray_active_icon));
            finalizado.setCardBackgroundColor(getResources().getColor(R.color.gray_active_icon));
        }
        else if (Common.filtroOrder.equals("1")){
            novo.setCardBackgroundColor(getResources().getColor(R.color.gray_active_icon));
            preparo.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));
            caminho.setCardBackgroundColor(getResources().getColor(R.color.gray_active_icon));
            finalizado.setCardBackgroundColor(getResources().getColor(R.color.gray_active_icon));
        }
        else if (Common.filtroOrder.equals("2")){
            novo.setCardBackgroundColor(getResources().getColor(R.color.gray_active_icon));
            preparo.setCardBackgroundColor(getResources().getColor(R.color.gray_active_icon));
            caminho.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));
            finalizado.setCardBackgroundColor(getResources().getColor(R.color.gray_active_icon));
        }
        else if (Common.filtroOrder.equals("3")){
            novo.setCardBackgroundColor(getResources().getColor(R.color.gray_active_icon));
            preparo.setCardBackgroundColor(getResources().getColor(R.color.gray_active_icon));
            caminho.setCardBackgroundColor(getResources().getColor(R.color.gray_active_icon));
            finalizado.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }

        recycler_rest = rootView.findViewById(R.id.recycler_orders);
        layout_rest = new LinearLayoutManager(getContext());
        recycler_rest.setLayoutManager(layout_rest);

        recycler_moto = rootView.findViewById(R.id.recycler_motodialog);
        layout_moto = new LinearLayoutManager(getContext());
        recycler_moto.setLayoutManager(layout_moto);

        FirebaseRecyclerOptions<Motorista> optionsMoto =
                new FirebaseRecyclerOptions.Builder<Motorista>()
                        .setQuery(table_user, Motorista.class)
                        .build();
        adapter_moto= new FirebaseRecyclerAdapter<Motorista, MotoristaViewHolder>(optionsMoto)
        {
            @NonNull
            @Override
            public MotoristaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.motorista_item, parent, false);

                return new MotoristaViewHolder(view);
            }
            @Override
            protected void onBindViewHolder(@NonNull final MotoristaViewHolder motoviewHolder, int position, @NonNull final Motorista modelmoto) {

                motoviewHolder.txtMenuName.setText(modelmoto.getNome());
                motoviewHolder.txtPlaca.setText(modelmoto.getPlaca());
                if (modelmoto.getStatus().equals("0")){
                    motoviewHolder.corridas.setText("Offline");
                }else if (modelmoto.getStatus().equals("1")){
                    motoviewHolder.corridas.setText("Disponível");
                }else{
                    motoviewHolder.corridas.setText("Ocupado");
                }
                //final Adicional Adicional = model;
                motoviewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                        if (modelmoto.getStatus().equals("1")){

                            modelmoto.setStatus("2");
                            Map<String, Object> passwordUpdate = new HashMap<>();
                            passwordUpdate.put("requestId",adapter_rest.getRef(position).getKey());
                            passwordUpdate.put("status","2");

                            DatabaseReference user = FirebaseDatabase.getInstance().getReference("Motoristas");
                            user.child(adapter_moto.getRef(position).getKey())
                                    .updateChildren(passwordUpdate)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });

                            dialo.setVisibility(View.GONE);

                            notifyDriver(modelmoto.getNumero(),motoclick,adapter_moto.getRef(position).getKey());
                        }else {
                            Toast.makeText(getContext(), "Motorista indisponível no momento", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        };
        recycler_moto.setAdapter(adapter_moto);

        adapter_moto.startListening();

        if (adapter_moto != null){
            adapter_moto.startListening();
        }

        adapter_moto.stopListening();

        novo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                novo.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));
                preparo.setCardBackgroundColor(getResources().getColor(R.color.gray_active_icon));
                caminho.setCardBackgroundColor(getResources().getColor(R.color.gray_active_icon));
                finalizado.setCardBackgroundColor(getResources().getColor(R.color.gray_active_icon));
                tipoBt = "0";Common.filtroOrder = "0";

                FragmentTransaction ft = getFragmentManager().beginTransaction();

                ft.detach(SearchFragment.this).attach(SearchFragment.this).commit();
            }
        });

        preparo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                novo.setCardBackgroundColor(getResources().getColor(R.color.gray_active_icon));
                preparo.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));
                caminho.setCardBackgroundColor(getResources().getColor(R.color.gray_active_icon));
                finalizado.setCardBackgroundColor(getResources().getColor(R.color.gray_active_icon));
                tipoBt = "1";Common.filtroOrder = "1";

                FragmentTransaction ft = getFragmentManager().beginTransaction();

                ft.detach(SearchFragment.this).attach(SearchFragment.this).commit();
            }
        });

        caminho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                novo.setCardBackgroundColor(getResources().getColor(R.color.gray_active_icon));
                preparo.setCardBackgroundColor(getResources().getColor(R.color.gray_active_icon));
                caminho.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));
                finalizado.setCardBackgroundColor(getResources().getColor(R.color.gray_active_icon));
                tipoBt = "2";Common.filtroOrder = "2";

                FragmentTransaction ft = getFragmentManager().beginTransaction();

                ft.detach(SearchFragment.this).attach(SearchFragment.this).commit();
            }
        });

        finalizado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                novo.setCardBackgroundColor(getResources().getColor(R.color.gray_active_icon));
                preparo.setCardBackgroundColor(getResources().getColor(R.color.gray_active_icon));
                caminho.setCardBackgroundColor(getResources().getColor(R.color.gray_active_icon));
                finalizado.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));
                tipoBt = "3";Common.filtroOrder = "3";

                FragmentTransaction ft = getFragmentManager().beginTransaction();

                ft.detach(SearchFragment.this).attach(SearchFragment.this).commit();
            }
        });

        if (tipoBt.equals("0")){
            Query searchFirst = rest.orderByChild("restaurant").equalTo(Common.currentUser.getRestId());

            FirebaseRecyclerOptions<Request> optionsRest =
                    new FirebaseRecyclerOptions.Builder<Request>()
                            .setQuery(searchFirst, Request.class) //TODO: CHANGE IT
                            .build();
            adapter_rest = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(optionsRest)
            {
                @NonNull
                @Override
                public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.order_item, parent, false);

                    return new OrderViewHolder(view);
                }
                @Override
                protected void onBindViewHolder(@NonNull final OrderViewHolder viewHolder, final int position, @NonNull final Request model) {

                    if (model.getStatus().equals("0") || model.getStatus().equals("1")){
                        viewHolder.vis.setVisibility(View.VISIBLE);
                        viewHolder.txtOrderId.setText(model.getAddress());

                        viewHolder.order_ac.setText("ACEITAR");
                        viewHolder.order_re.setText("RECUSAR");

                        Locale locale = new Locale("pt", "BR");
                        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
                        //txtTotalPrice.setText(fmt.format(total));

                        viewHolder.order_ac.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                viewHolder.order_ac.setVisibility(View.INVISIBLE);
                                viewHolder.order_re.setText("CANCELAR");
                                viewHolder.order_iniciatexto.setText("INICIAR PREPARO");
                                viewHolder.order_iniciapreparo.setVisibility(View.VISIBLE);
                                model.setStatus("1");

                                Map<String, Object> passwordUpdate = new HashMap<>();
                                passwordUpdate.put("status",model.getStatus());

                                DatabaseReference user = FirebaseDatabase.getInstance().getReference("Requests");
                                user.child(adapter_rest.getRef(position).getKey())
                                        .updateChildren(passwordUpdate)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                sendNotif(model.getPhone(),model.getStatus());
                            }
                        });

                        viewHolder.order_re.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                model.setStatus("5");
                            }
                        });

                        viewHolder.order_de.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Common.orderSelected= adapter_rest.getRef(position).getKey();
                                Common.currentOrdet= model;

                                Intent intent = new Intent(getContext(),OrderDetailsActivity.class);
                                intent.putExtra("OrderId",adapter_rest.getRef(position).getKey());
                                startActivity(intent);
                            }
                        });

                        viewHolder.order_iniciapreparo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                viewHolder.order_ac.setVisibility(View.VISIBLE);
                                viewHolder.order_ac.setText("ENVIAR");
                                viewHolder.order_re.setText("CANCELAR");
                                viewHolder.order_iniciapreparo.setVisibility(View.VISIBLE);
                                viewHolder.order_iniciatexto.setText("VER NO MAPA");
                                model.setStatus("2");
                                tipoBt = "1";

                                Map<String, Object> passwordUpdate = new HashMap<>();
                                passwordUpdate.put("status",model.getStatus());

                                DatabaseReference user = FirebaseDatabase.getInstance().getReference("Requests");
                                user.child(adapter_rest.getRef(position).getKey())
                                        .updateChildren(passwordUpdate)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                sendNotif(model.getPhone(),model.getStatus());
                            }
                        });

                        viewHolder.txtOrderAddress.setText("Total: "+model.getTotal());
                        viewHolder.ide.setText("Pedido #"+getRef(position).getKey());
                        viewHolder.namee.setText("Nome: "+model.getName());
                        viewHolder.payway.setText("Pagamento: ");

                        if (model.getStatus().equals("1")){
                            viewHolder.color.setBackgroundResource(R.color.statusB);
                        }
                        if (model.getStatus().equals("2")){
                            viewHolder.color.setBackgroundResource(R.color.statusC);
                        }
                        if (model.getStatus().equals("3")){
                            viewHolder.color.setBackgroundResource(R.color.statusD);
                        }

                        viewHolder.setItemClickListener(new ItemClickListener() {
                            @Override
                            public void onClick(View view, int position, boolean isLongClick) {

                                Common.orderSelected= adapter_rest.getRef(position).getKey();
                                Common.currentOrdet= model;

                                Intent intent = new Intent(getContext(),OrderDetailsActivity.class);
                                intent.putExtra("OrderId",adapter_rest.getRef(position).getKey());
                                startActivity(intent);

                            }
                        });
                    }
                    else {
                        viewHolder.vis.setVisibility(View.GONE);
                    }

                }
            };
        }

        else if (tipoBt.equals("1")){
            Query searchFirst = rest.orderByChild("restaurant").equalTo(Common.currentUser.getRestId());

            FirebaseRecyclerOptions<Request> optionsRest =
                    new FirebaseRecyclerOptions.Builder<Request>()
                            .setQuery(searchFirst, Request.class) //TODO: CHANGE IT
                            .build();
            adapter_rest = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(optionsRest)
            {
                @NonNull
                @Override
                public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.order_item, parent, false);

                    return new OrderViewHolder(view);
                }
                @Override
                protected void onBindViewHolder(@NonNull final OrderViewHolder viewHolder, final int position, @NonNull final Request model) {

                    if (model.getStatus().equals("2")){
                        viewHolder.vis.setVisibility(View.VISIBLE);
                        viewHolder.txtOrderId.setText(model.getAddress());

                        viewHolder.order_ac.setText("ENVIAR");
                        viewHolder.order_re.setText("CANCELAR");
                        viewHolder.order_iniciapreparo.setVisibility(View.VISIBLE);
                        viewHolder.order_iniciatexto.setText("VER NO MAPA");

                        Locale locale = new Locale("pt", "BR");
                        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
                        //txtTotalPrice.setText(fmt.format(total));

                        viewHolder.order_ac.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialo.setVisibility(View.VISIBLE);
                                motoclick = model.getAddress();
                            }
                        });

                        viewHolder.order_re.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                model.setStatus("5");
                            }
                        });

                        viewHolder.order_de.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Common.orderSelected= adapter_rest.getRef(position).getKey();
                                Common.currentOrdet= model;

                                Intent intent = new Intent(getContext(),OrderDetailsActivity.class);
                                intent.putExtra("OrderId",adapter_rest.getRef(position).getKey());
                                startActivity(intent);
                            }
                        });

                        viewHolder.txtOrderAddress.setText("Total: "+model.getTotal());
                        viewHolder.ide.setText("Pedido #"+getRef(position).getKey());
                        viewHolder.namee.setText("Nome: "+model.getName());
                        viewHolder.payway.setText("Pagamento: ");

                        if (model.getStatus().equals("1")){
                            viewHolder.color.setBackgroundResource(R.color.statusB);
                        }
                        if (model.getStatus().equals("2")){
                            viewHolder.color.setBackgroundResource(R.color.statusC);
                        }
                        if (model.getStatus().equals("3")){
                            viewHolder.color.setBackgroundResource(R.color.statusD);
                        }

                        viewHolder.setItemClickListener(new ItemClickListener() {
                            @Override
                            public void onClick(View view, int position, boolean isLongClick) {

                                Common.orderSelected= adapter_rest.getRef(position).getKey();
                                Common.currentOrdet= model;

                                Intent intent = new Intent(getContext(),OrderDetailsActivity.class);
                                intent.putExtra("OrderId",adapter_rest.getRef(position).getKey());
                                startActivity(intent);

                            }
                        });
                    }
                    else {
                        viewHolder.vis.setVisibility(View.GONE);
                    }

                }
            };
        }

        else if (tipoBt.equals("2")){
            Query searchFirst = rest.orderByChild("restaurant").equalTo(Common.currentUser.getRestId());

            FirebaseRecyclerOptions<Request> optionsRest =
                    new FirebaseRecyclerOptions.Builder<Request>()
                            .setQuery(searchFirst, Request.class) //TODO: CHANGE IT
                            .build();
            adapter_rest = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(optionsRest)
            {
                @NonNull
                @Override
                public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.order_item, parent, false);

                    return new OrderViewHolder(view);
                }
                @Override
                protected void onBindViewHolder(@NonNull final OrderViewHolder viewHolder, final int position, @NonNull final Request model) {

                    if (model.getStatus().equals("3")){
                        viewHolder.vis.setVisibility(View.VISIBLE);
                        viewHolder.txtOrderId.setText(model.getAddress());

                        viewHolder.order_ac.setText("FINALIZAR");
                        viewHolder.order_re.setText("CANCELAR");

                        Locale locale = new Locale("pt", "BR");
                        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
                        //txtTotalPrice.setText(fmt.format(total));

                        viewHolder.order_ac.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                viewHolder.order_ac.setVisibility(View.GONE);
                                viewHolder.order_re.setVisibility(View.GONE);
                                viewHolder.order_iniciapreparo.setVisibility(View.GONE);
                                model.setStatus("4");
                                tipoBt = "4";

                                Map<String, Object> passwordUpdate = new HashMap<>();
                                passwordUpdate.put("status",model.getStatus());

                                DatabaseReference user = FirebaseDatabase.getInstance().getReference("Requests");
                                user.child(adapter_rest.getRef(position).getKey())
                                        .updateChildren(passwordUpdate)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                sendNotif(model.getPhone(),model.getStatus());
                            }
                        });

                        viewHolder.order_re.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                model.setStatus("5");
                            }
                        });

                        viewHolder.order_de.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Common.orderSelected= adapter_rest.getRef(position).getKey();
                                Common.currentOrdet= model;

                                Intent intent = new Intent(getContext(),OrderDetailsActivity.class);
                                intent.putExtra("OrderId",adapter_rest.getRef(position).getKey());
                                startActivity(intent);
                            }
                        });

                        viewHolder.txtOrderAddress.setText("Total: "+model.getTotal());
                        viewHolder.ide.setText("Pedido #"+getRef(position).getKey());
                        viewHolder.namee.setText("Nome: "+model.getName());
                        viewHolder.payway.setText("Pagamento: ");

                        if (model.getStatus().equals("1")){
                            viewHolder.color.setBackgroundResource(R.color.statusB);
                        }
                        if (model.getStatus().equals("2")){
                            viewHolder.color.setBackgroundResource(R.color.statusC);
                        }
                        if (model.getStatus().equals("3")){
                            viewHolder.color.setBackgroundResource(R.color.statusD);
                        }

                        viewHolder.setItemClickListener(new ItemClickListener() {
                            @Override
                            public void onClick(View view, int position, boolean isLongClick) {

                                Common.orderSelected= adapter_rest.getRef(position).getKey();
                                Common.currentOrdet= model;

                                Intent intent = new Intent(getContext(),OrderDetailsActivity.class);
                                intent.putExtra("OrderId",adapter_rest.getRef(position).getKey());
                                startActivity(intent);

                            }
                        });
                    }
                    else {
                        viewHolder.vis.setVisibility(View.GONE);
                    }

                }
            };
        }

        else if (tipoBt.equals("3")){
            Query searchFirst = rest.orderByChild("restaurant").equalTo(Common.currentUser.getRestId());

            FirebaseRecyclerOptions<Request> optionsRest =
                    new FirebaseRecyclerOptions.Builder<Request>()
                            .setQuery(searchFirst, Request.class) //TODO: CHANGE IT
                            .build();
            adapter_rest = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(optionsRest)
            {
                @NonNull
                @Override
                public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.order_item, parent, false);

                    return new OrderViewHolder(view);
                }
                @Override
                protected void onBindViewHolder(@NonNull final OrderViewHolder viewHolder, final int position, @NonNull final Request model) {

                    if (model.getStatus().equals("4")){
                        viewHolder.vis.setVisibility(View.VISIBLE);
                        viewHolder.txtOrderId.setText(model.getAddress());

                        Locale locale = new Locale("pt", "BR");
                        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
                        //txtTotalPrice.setText(fmt.format(total));

                        viewHolder.order_de.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Common.orderSelected= adapter_rest.getRef(position).getKey();
                                Common.currentOrdet= model;

                                Intent intent = new Intent(getContext(),OrderDetailsActivity.class);
                                intent.putExtra("OrderId",adapter_rest.getRef(position).getKey());
                                startActivity(intent);
                            }
                        });

                        viewHolder.txtOrderAddress.setText("Total: "+model.getTotal());
                        viewHolder.ide.setText("Pedido #"+getRef(position).getKey());
                        viewHolder.namee.setText("Nome: "+model.getName());
                        viewHolder.payway.setText("Pagamento: ");

                        if (model.getStatus().equals("1")){
                            viewHolder.color.setBackgroundResource(R.color.statusB);
                        }
                        if (model.getStatus().equals("2")){
                            viewHolder.color.setBackgroundResource(R.color.statusC);
                        }
                        if (model.getStatus().equals("3")){
                            viewHolder.color.setBackgroundResource(R.color.statusD);
                        }

                        viewHolder.setItemClickListener(new ItemClickListener() {
                            @Override
                            public void onClick(View view, int position, boolean isLongClick) {

                                Common.orderSelected= adapter_rest.getRef(position).getKey();
                                Common.currentOrdet= model;

                                Intent intent = new Intent(getContext(),OrderDetailsActivity.class);
                                intent.putExtra("OrderId",adapter_rest.getRef(position).getKey());
                                startActivity(intent);

                            }
                        });
                    }
                    else {
                        viewHolder.vis.setVisibility(View.GONE);
                    }

                }
            };
        }

        recycler_rest.setAdapter(adapter_rest);
        adapter_rest.startListening();

        if (adapter_rest != null){
            adapter_rest.startListening();
        }

        adapter_rest.stopListening();

        return rootView;
    }

    public void notifyDriver(String phone, final String endereco, String reqId){
        FirebaseDatabase.getInstance()
                .getReference(Common.TOKEN_REF)
                .child(phone)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            //TODO: NOTIFICATIONS
                            TokenModel tokenModel = dataSnapshot.getValue(TokenModel.class);
                            Map<String,String> notiData = new HashMap<>();
                            notiData.put(Common.NOTI_TITLE,"ParisCart - Nova Entrega");
                            notiData.put(Common.NOTI_CONTENT,"Para: "+endereco);

                            FCMSendData sendData = new FCMSendData(tokenModel.getToken(),notiData);

                            compositeDisposable.add(ifcmService.sendNotification(sendData)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Consumer<FCMResponse>() {
                                                   @Override
                                                   public void accept(FCMResponse fcmResponse) throws Exception {
                                                       if (fcmResponse.getSuccess() == 1){
                                                           Toast.makeText(getContext(), "Avisaremos quando o motorista confirmar ou recusar a corrida", Toast.LENGTH_SHORT).show();
                                                       }else{
                                                           Toast.makeText(getContext(), "Falha ao Notificar Motorista", Toast.LENGTH_SHORT).show();
                                                       }

                                                   }
                                               }, new Consumer<Throwable>() {
                                                   @Override
                                                   public void accept(Throwable throwable) throws Exception {
                                                       Toast.makeText(getContext(), ""+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                                   }
                                               }
                                    ));

                        }else {
                            Toast.makeText(getContext(), "Error: Token not found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public void sendNotif(String phone, final String status){
        FirebaseDatabase.getInstance()
                .getReference(Common.TOKEN_REF)
                .child(phone)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            //TODO: NOTIFICATIONS
                            TokenModel tokenModel = dataSnapshot.getValue(TokenModel.class);
                            Map<String,String> notiData = new HashMap<>();
                            notiData.put(Common.NOTI_TITLE,"ParisCart - Pedido Atualizado");
                            notiData.put(Common.NOTI_CONTENT,"Pedido: "+Common.convertCodeToStatus(String.valueOf(status)));

                            FCMSendData sendData = new FCMSendData(tokenModel.getToken(),notiData);

                            compositeDisposable.add(ifcmService.sendNotification(sendData)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Consumer<FCMResponse>() {
                                                   @Override
                                                   public void accept(FCMResponse fcmResponse) throws Exception {
                                                       if (fcmResponse.getSuccess() == 1){
                                                           Toast.makeText(getContext(), "Status Atualizado", Toast.LENGTH_SHORT).show();
                                                       }else{
                                                           Toast.makeText(getContext(), "Falha ao Notificar Cliente", Toast.LENGTH_SHORT).show();
                                                       }

                                                   }
                                               }, new Consumer<Throwable>() {
                                                   @Override
                                                   public void accept(Throwable throwable) throws Exception {
                                                       Toast.makeText(getContext(), ""+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                                   }
                                               }
                                    ));

                        }else {
                            Toast.makeText(getContext(), "Error: Token not found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public void listaMotos(){

    }

    @Override
    public void onStart() {
        super.onStart();
        adapter_rest.startListening();
        adapter_moto.startListening();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter_rest != null){
            adapter_rest.startListening();
            adapter_moto.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter_rest.stopListening();
        adapter_moto.stopListening();
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
