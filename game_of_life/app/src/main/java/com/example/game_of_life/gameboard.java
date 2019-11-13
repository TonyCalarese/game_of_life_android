package com.example.game_of_life;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.os.Handler;


//Source for animation: https://developer.android.com/guide/topics/graphics/drawable-animation.html
public class gameboard extends Fragment {
    public static final String LIFE_ID = "GAMEOFLIFE";
    public static final String GRID_ID = "GAMEOFLIFEGRID";
    private RecyclerView mGameBoard;
    private Button mResetButton, mStartButton, mCloneButton, mSaveButton, mOpenButton, mColorButton;
    private TileAdapter mAdapter;
    private int mX_size= 20, mY_size = 20; // Making the 20 by 20 board
    private int mSize = mX_size * mY_size;
    private int mColorIndex = 0;
    private int[] mColors = {R.color.colorPrimary, R.color.colorAccent, R.color.red,
    R.color.gold, R.color.skyBlue};
    private boolean mStarted = false;
    private int[] mGrid = {
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
    private Handler mNextGenHnadler = new Handler();
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

        int[] Grid = getActivity().getIntent().getIntArrayExtra(GRID_ID);
        if(Grid != null){
            mGrid = Grid;
        }
        View view = inflater.inflate(R.layout.game_board_fragment, container, false);
        mGameBoard = (RecyclerView) view.findViewById(R.id.gameboard_recycler_view);
        mGameBoard.setLayoutManager(new GridLayoutManager(getActivity(), mX_size));
        mAdapter = new TileAdapter();
        mGameBoard.setAdapter(mAdapter);
        mResetButton = (Button) view.findViewById(R.id.reset_button);
        mResetButton.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View view) {
                                          ceaseFunctions();
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
                                          if(mStarted == false){
                                              mNextGenHnadler.post(nextGen);
                                              mStartButton.setText(R.string.stop);
                                              mStarted=true;
                                          } else {
                                              ceaseFunctions();
                                          }
                                      }
                                  }
        );
        mCloneButton = (Button) view.findViewById(R.id.clone_button);
        mCloneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ceaseFunctions();
                cloneBoard();
            }});
        mSaveButton= (Button) view.findViewById(R.id.save_button);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }});
        mOpenButton = (Button) view.findViewById(R.id.open_button);
        mOpenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }});
        mColorButton = (Button) view.findViewById(R.id.color_button);
        mColorButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                changeColors();
                                                mAdapter = new TileAdapter();
                                                mGameBoard.setAdapter(mAdapter);
                                            }
                                        }
        );
        return view;
    }

    //ceased the current Handler if it is present
    public void ceaseFunctions()
    {
        mNextGenHnadler.removeCallbacks(nextGen);
        mStartButton.setText(R.string.start);
        mStarted = false;
    }
    public void changeColors(){
        //Code Source: https://stackoverflow.com/questions/46714018/change-android-button-drawable-icon-color-programmatically
        //Please give credit where it is due
        Drawable tile = getResources().getDrawable(R.drawable.tile_background);
        tile = DrawableCompat.wrap(tile);
        mColorIndex = (mColorIndex + 1) % mColors.length;
        DrawableCompat.setTint(tile, getResources().getColor(mColors[mColorIndex]));
        mColorButton.setCompoundDrawables(null, tile, null, null);

        Drawable star = getResources().getDrawable(R.drawable.star_icon);
        star = DrawableCompat.wrap(star);
        mColorIndex = (mColorIndex + 1) % mColors.length;
        DrawableCompat.setTint(star, getResources().getColor(mColors[mColorIndex]));
        mColorButton.setCompoundDrawables(null, star, null, null);
    }

    //Used in the CloneButton to Clone the copy of the board
    public void cloneBoard(){
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.putExtra(GRID_ID, mGrid);
        startActivity(intent);
    }

    // Define the code block to be executed

    private Runnable nextGen = new Runnable() {
        @Override
        public void run() {
            //Code Reference: https://stackoverflow.com/questions/37995564/what-is-the-way-to-make-an-infinite-loop-in-a-thread-android
            //Please give credit where it is due
            if (checkBoard() == true) {
                ceaseFunctions();
            } else {
                nextGeneration();
                mAdapter = new TileAdapter();
                mGameBoard.setAdapter(mAdapter);
                mNextGenHnadler.postDelayed(nextGen, 250);
            }
        }
    };

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
                //upperleft, center, right
                   lifeCounter = mGridCopy[(((i-1)+ mX_size) % mX_size)][(((j-1)+ mY_size) % mY_size)] +
                           mGridCopy[(((i-1)+ mX_size) % mX_size)][j] +
                           mGridCopy[(((i-1)+ mX_size) % mX_size)][(((j+1)+ mY_size) % mY_size)] +
                //Left, right
                           mGridCopy[i][(((j-1)+ mY_size) % mY_size)] +
                           //mGridCopy[i][j] +
                           mGridCopy[i][(((j+1)+ mY_size) % mY_size)] +
                //Downleft, center, right
                           mGridCopy[(((i+1)+ mX_size) % mX_size)][(((j-1)+ mY_size) % mY_size)] +
                           mGridCopy[(((i+1)+ mX_size) % mX_size)][j] +
                           mGridCopy[(((i+1)+ mX_size) % mX_size)][(((j+1)+ mY_size) % mY_size)];


               /* Each generation, a living cell with two or three living neighbors stays alive.
                  A cell with any other number of neighbors (less or more) dies.*/

                if(mGridCopy[i][j] == 1){
                    if(lifeCounter == 3 || lifeCounter == 2) {
                        mGrid[n] = 1;
                    }
                    else
                    {
                        mGrid[n] = 0;
                    }
                }else{
                    if(lifeCounter == 3) {
                            mGrid[n] = 1;
                    }
                    else{
                        mGrid[n] = 0;
                    }

                }
                n++; //Update n to go to the next cell

            } //end of j for loop
        } //end of i for loop
    }

    //Used to stop the handler if the board is already empty, that way we can interrupt the loop
    public Boolean checkBoard() {

        for (int i = 0; i < mSize; i++) {
            if (mGrid[i] == 1) {
                return false; //if you have at least one tile that is alive then return false
            }
        }
        //if everything is 0 then it will return true
        return true;
    }

}
