package com.newcart.newpariscart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.newcart.newpariscart.Common.Common;
import com.newcart.newpariscart.Interface.ItemClickListener;
import com.newcart.newpariscart.Model.Request;
import com.newcart.newpariscart.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.NumberFormat;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;


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

    FirebaseDatabase database;
    DatabaseReference rest;

    RecyclerView recycler_rest;
    RecyclerView.LayoutManager layout_rest;

    FirebaseRecyclerAdapter<Request, OrderViewHolder> adapter_rest;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        database = FirebaseDatabase.getInstance();
        rest = database.getReference("Requests");
        rest.keepSynced(true);

        recycler_rest = rootView.findViewById(R.id.recycler_orders);
        layout_rest = new LinearLayoutManager(getContext());
        recycler_rest.setLayoutManager(layout_rest);

        Query searchByName = rest.orderByChild("phone").equalTo(Common.currentUser.getPhoneNumber());

        FirebaseRecyclerOptions<Request> optionsRest =
                new FirebaseRecyclerOptions.Builder<Request>()
                        .setQuery(searchByName, Request.class) //TODO: CHANGE IT
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
            protected void onBindViewHolder(@NonNull OrderViewHolder viewHolder, int position, @NonNull final Request model) {

                viewHolder.txtOrderId.setText(model.getAddress());
                viewHolder.txtOrderStatus.setText(Common.convertCodeToStatus(model.getStatus()));

                Locale locale = new Locale("pt", "BR");
                NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
                //txtTotalPrice.setText(fmt.format(total));

                viewHolder.txtOrderAddress.setText(model.getTotal());

                if (model.getStatus().equals("0")){
                    viewHolder.color.setBackgroundResource(R.drawable.statusa);
                    viewHolder.txtOrderStatus.setTextColor(getResources().getColor(R.color.statusA));
                }
                if (model.getStatus().equals("1")){
                    viewHolder.color.setBackgroundResource(R.drawable.statusb);
                    viewHolder.txtOrderStatus.setTextColor(getResources().getColor(R.color.statusB));
                }
                if (model.getStatus().equals("2")){
                    viewHolder.color.setBackgroundResource(R.drawable.statusc);
                    viewHolder.txtOrderStatus.setTextColor(getResources().getColor(R.color.statusC));
                }
                if (model.getStatus().equals("3")){
                    viewHolder.color.setBackgroundResource(R.drawable.statusd);
                    viewHolder.txtOrderStatus.setTextColor(getResources().getColor(R.color.statusD));
                }

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                        Common.orderSelected= adapter_rest.getRef(position).getKey();
                        Common.currentOrdet= model;

                        Intent intent = new Intent(getContext(),MyOrderActivity.class);
                        intent.putExtra("OrderId",adapter_rest.getRef(position).getKey());
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
