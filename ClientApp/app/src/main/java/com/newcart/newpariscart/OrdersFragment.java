package com.newcart.newpariscart;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.LinearLayout;

import com.newcart.newpariscart.Common.Common;
import com.newcart.newpariscart.Interface.ItemClickListener;
import com.newcart.newpariscart.Model.Category;
import com.newcart.newpariscart.ViewHolder.CateViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OrdersFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OrdersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrdersFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public OrdersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrdersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrdersFragment newInstance(String param1, String param2) {
        OrdersFragment fragment = new OrdersFragment();
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
    DatabaseReference menu;

    RecyclerView recycler_menu;
    RecyclerView.LayoutManager layout_menu;

    FirebaseRecyclerAdapter<Category, CateViewHolder> adapter_menu;

    LinearLayout hf_search;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_orders, container, false);

        database = FirebaseDatabase.getInstance();
        menu = database.getReference("Categories");
        menu.keepSynced(true);

        hf_search = rootView.findViewById(R.id.sf_search);
        hf_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),BuscaActivity.class);
                startActivity(intent);
            }
        });

        recycler_menu = rootView.findViewById(R.id.recycler_cate);
        recycler_menu.setLayoutManager(new GridLayoutManager(getContext(),2));
        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(recycler_menu.getContext(),
                R.anim.layout_fall_down);
        recycler_menu.setLayoutAnimation(controller);

        FirebaseRecyclerOptions<Category> optionsMenu =
                new FirebaseRecyclerOptions.Builder<Category>()
                        .setQuery(menu, Category.class)
                        .build();
        adapter_menu = new FirebaseRecyclerAdapter<Category, CateViewHolder>(optionsMenu)
        {
            @NonNull
            @Override
            public CateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.cate_item, parent, false);

                return new CateViewHolder(view);
            }
            @Override
            protected void onBindViewHolder(@NonNull CateViewHolder viewHolder, int position, @NonNull final Category model) {

                viewHolder.txtMenuName.setText(model.getName());
                Picasso.with(getContext()).load(model.getPhoto()).into(viewHolder.imageView);
                final Category local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                        Fragment someFragment = new RestaurantsFragment();
                        Common.myCategory = adapter_menu.getRef(position).getKey();
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

        return rootView;
    }


    @Override
    public void onStart() {
        super.onStart();
        adapter_menu.startListening();

    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter_menu != null){
            adapter_menu.startListening();

        }
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter_menu.stopListening();

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
