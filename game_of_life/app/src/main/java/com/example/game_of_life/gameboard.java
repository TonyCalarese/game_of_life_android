package com.example.game_of_life;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


//Source for animation: https://developer.android.com/guide/topics/graphics/drawable-animation.html
public class gameboard extends Fragment {
    public static final String LIFE_ID = "GAMEOFLIFE";
    private RecyclerView mGameBoard;
    private Button mReset, mStartButton;
    private TileAdapter mAdapter;
    private int mX_size= 20, mY_size = 20; // Making the 20 by 20 board
    private int mSize = mX_size * mY_size;
    private int[] mGrid = { //Must be a more efficient way to do this
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, //row1
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, //row2
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, //row3
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, //row4
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, //row5
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, //row6
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, //row7
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, //row8
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, //row9
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, //row10
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, //row11
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, //row12
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, //row13
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, //row14
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, //row15
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, //row16
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, //row17
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, //row18
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, //row19
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 //row20
            };
    private OnFragmentInteractionListener mListener;

    //private Handler mTimer;
    public gameboard() {
        // Required empty public constructor
    }

    //mUpdateHandler = new Handler(new Handler.Callback()
    //@Overridepublic )

    public static gameboard newInstance() {
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
        View view = inflater.inflate(R.layout.game_board_fragment, container, false);
        mGameBoard = (RecyclerView) view.findViewById(R.id.gameboard_recycler_view);
        mGameBoard.setLayoutManager(new GridLayoutManager(getActivity(), mX_size));
        mAdapter = new TileAdapter();
        mGameBoard.setAdapter(mAdapter);
        mReset = (Button) view.findViewById(R.id.reset_button);
        mReset.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View view) {
                                          resetBoard();
                                          mAdapter = new TileAdapter();
                                          mGameBoard.setAdapter(mAdapter);
                                      }
                                  }
        );
        mStartButton = (Button) view.findViewById(R.id.next_gen_button);
        mStartButton.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View view) {

                                          nextGeneration();

                                          mAdapter = new TileAdapter();
                                          mGameBoard.setAdapter(mAdapter);
                                      }
                                  }
        );
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
        void onFragmentInteraction(Uri uri);
    }

    private class TileHolder extends RecyclerView.ViewHolder{
        private Button mSquare;
        private int mPosition;
        public TileHolder(LayoutInflater inflater, ViewGroup container) {
            super(inflater.inflate(R.layout.tile, container, false));
            mSquare = (Button)itemView.findViewById(R.id.tile_button);
            mSquare.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View view) {
                                               if(mGrid[mPosition] == 0) {
                                                   mGrid[mPosition] = 1;
                                               }else {
                                                   mGrid[mPosition] = 0;
                                               }
                                                   mAdapter.notifyItemChanged(mPosition); // reload ViewHolder
                                               }
                                       }
            );
        }

        public void bindPosition(int p) {
            mPosition = p;
        }
    }
    private class TileAdapter extends RecyclerView.Adapter<TileHolder> {
        @Override
        public void onBindViewHolder(TileHolder holder, int position) {
            // tell holder which place on grid it is representing
            holder.bindPosition(position);
            // actually change image displayed
            if (mGrid[position] == 1) {
                holder.mSquare.setBackgroundResource(R.drawable.star_icon);
            } else {
                holder.mSquare.setBackgroundResource(R.drawable.tile_background);
            }
        }
        @Override
        public TileHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new TileHolder(inflater, parent);
        }

        @Override
        public int getItemCount() {
            return mX_size * mY_size;
        }
    }


    public void resetBoard() {
        //go through the board and reset it to 0
        for(int i= 0; i <mSize; i ++){
            mGrid[i] = 0;
        }
    }

    public void nextGeneration() {
        int[][] mGridCopy = new int[mX_size][mY_size];
        int lifeCounter = 0, n = 0, X_index, Y_index;

        //loading the array
        for (int i = 0; i < mX_size; i++) {
            for (int j = 0; j < mY_size; j++) {
                mGridCopy[i][j] = mGrid[n];
                n++; //Turning the 1d array into a 2d array
            }
        }

        //Getting the neighbor life count and setting the array
        n = 0; //Reset n
        for (int i = 0; i < mX_size; i++) {
            for (int j = 0; j < mY_size; j++) {
                /*
                int mLeft = mGridCopy[i][j-1],
                        mRight = mGridCopy[i][j+1],
                        mUpLeft = mGridCopy[i-1][j-1],
                        mUp = mGridCopy[i-1][j],
                        mUpRight = mGridCopy[i-1][j+1],
                        mDownLeft = mGridCopy[i+1][j-1],
                        mDown = mGridCopy[i+1][j],
                        mDownRight = mGridCopy[i+1][j+1];
                        */
               // X_index =  ((i + mX_size) % mX_size);
               // Y_index =  ((j + mY_size) % mY_size); //Making sure that we do not go out of bounds

                //Upperleft, center, right
                   lifeCounter = mGridCopy[(((i-1)+ mX_size) % mX_size)][(((j-1)+ mY_size) % mY_size)] +
                           mGridCopy[(((i-1)+ mX_size) % mX_size)][j] +
                           mGridCopy[(((i-1)+ mX_size) % mX_size)][(((j+1)+ mY_size) % mY_size)] +
                //Left, current, right
                           mGridCopy[i][(((j-1)+ mY_size) % mY_size)] +
                           mGridCopy[i][j] +
                           mGridCopy[i][(((j+1)+ mY_size) % mY_size)] +
                //Downleft, center, right
                           mGridCopy[(((i+1)+ mX_size) % mX_size)][(((j-1)+ mY_size) % mY_size)] +
                           mGridCopy[(((i+1)+ mX_size) % mX_size)][j] +
                           mGridCopy[(((i+1)+ mX_size) % mX_size)][(((j+1)+ mY_size) % mY_size)];



                //if the amount of alive neighbors is greater than or equal to 3
                if(lifeCounter >= 3){
                    mGrid[n] = 1;
                }
                else{
                    mGrid[n] = 0;
                }

                n++; //Update n to go to the next cell

            }
        }



    }
}
