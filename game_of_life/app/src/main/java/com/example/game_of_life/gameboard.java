package com.example.game_of_life;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;



public class gameboard extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String LIFE_ID = "GAMEOFLIFE";
    private RecyclerView mGameBoard;
    private PlanetAdapter mAdapter;

    private OnFragmentInteractionListener mListener;

    public gameboard() {
        // Required empty public constructor
    }

    public static gameboard newInstance(String param1, String param2) {
        gameboard fragment = new gameboard();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_gameboard, container, false);

        mGameBoard = (RecyclerView) view.findViewById(R.id.gameboard_recycler_view);
        mGameBoard.setLayoutManager(new LinearLayoutManager(getActivity()));


        return view;
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putBoolean(LIFE_ID, mBoardState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private class TileHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private Button mButton;
        private int mPosition;

        public TileHolder(LayoutInflater inflater, ViewGroup container) {
            super(inflater.inflate(R.layout.tile, container, false));
            mButton = (Button)itemView.findViewById(R.id.tile_button);
            mButton.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View view) {
                                               if (mTouchEnabled && (mGrid[mPosition] == 0)) {
                                                   mGrid[mPosition] = mTurn; // set move
                                                   mTurn = 3 - mTurn; // flip player
                                                   mAdapter.notifyItemChanged(mPosition); // reload ViewHolder

                                               }
                                           }
                                       }
            );
        }

        public void bindPosition(int p) {
            mPosition = p;
        }
        public void onClick(View view){
            Intent intent = PlanetPagerActivity.newIntent(getActivity(), mPlanet.getId());
            startActivity(intent);
        }

    }



    private class SquareAdapter extends RecyclerView.Adapter<SquareHolder> {
        @Override
        public void onBindViewHolder(SquareHolder holder, int position) {
            // tell holder which place on grid it is representing
            holder.bindPosition(position);
            // actually change image displayed
            if (mGrid[position] == X) {
                holder.mButton.setBackgroundResource(R.drawable.x_sign);
            } else if (mGrid[position] == O) {
                holder.mButton.setBackgroundResource(R.drawable.o_sign);
            } else {
                holder.mButton.setBackgroundResource(R.drawable.empty);
            }
        }

        @Override
        public SquareHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new SquareHolder(inflater, parent);
        }

        @Override
        public int getItemCount() {
            return 9;
        }
    }

}
