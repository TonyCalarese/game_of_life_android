package com.example.game_of_life;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.os.Handler;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


//Source for animation: https://developer.android.com/guide/topics/graphics/drawable-animation.html
public class gameboard extends Fragment {
    public static final String GRID_ID = "GAMEOFLIFEGRID", file = "saveGrid.txt";

    //Handler Elements
    //Boolean for the Handler for going to the next generation
    private boolean mStarted = false;
    private Handler mNextGenHandler = new Handler();

    //App Elements
    private RecyclerView mGameBoard;
    private Button mResetButton, mStartButton, mCloneButton, mSaveButton, mOpenButton, mColorButton,
            m5SecButton, m1SecButton, m10SecButton, mSuperFastSpeedButton;
    private TileAdapter mAdapter;
    private TextView mGenCounterLabel, mCurrentSpeedLabel;

    //Integers
    public int mX_size= 20, mY_size = 20, mSize = mX_size * mY_size, // Making the 20 by 20 board and size 400
            mColorIndex = 0, mGenNumber = 0, mSpeed = 10000;
    //Colors in an array
    private int[] mColors = {R.color.colorPrimary, R.color.colorAccent, R.color.red, R.color.gold, R.color.skyBlue};
    //Grid
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
    //Declaring the Life CalculatorClass
    life_generator life = new life_generator(mX_size, mY_size);

    //Empty Constructor
    public gameboard() { }

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

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Getting the Intent for the clone
        int[] Grid = getActivity().getIntent().getIntArrayExtra(GRID_ID);
        if(Grid != null){
            mGrid = Grid;
        }

        //Get View
        View view = inflater.inflate(R.layout.game_board_fragment, container, false);

        //Get the GameBord and adapter
        mGameBoard = (RecyclerView) view.findViewById(R.id.gameboard_recycler_view);
        mGameBoard.setLayoutManager(new GridLayoutManager(getActivity(), mX_size));
        resetAdapter(); //Setting up the adapter is the same as my resetAdapter Function

        //Labels
        mGenCounterLabel = (TextView) view.findViewById((R.id.GenCounter));
        mGenCounterLabel.setText("0"); //Default it to
        mCurrentSpeedLabel = (TextView) view.findViewById((R.id.current_speed));
        mCurrentSpeedLabel.setText(String.valueOf(mSpeed));
        //Buttons
        mResetButton = (Button) view.findViewById(R.id.reset_button);
        mResetButton.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View view) {
                                          ceaseFunctions();
                                          resetGenCounter();
                                          resetBoard();
                                          resetAdapter();
                                      }
                                  }
        );
        mStartButton = (Button) view.findViewById(R.id.next_gen_button);
        mStartButton.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View view) {
                                          if(mStarted == false){
                                              mNextGenHandler.post(mNextGen);
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
                try {
                    FileOutputStream fos = getActivity().openFileOutput(file, Context.MODE_PRIVATE);
                    ObjectOutputStream oos = new ObjectOutputStream(fos);
                    oos.writeObject(mGrid);
                    fos.close();
                }catch (Exception e) {
                    e.printStackTrace();
                }

            }});
        mOpenButton = (Button) view.findViewById(R.id.open_button);
        mOpenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt = "Empty";
                try{
                    FileInputStream fis = getActivity().openFileInput(file);
                    ObjectInputStream ois = new ObjectInputStream(fis);
                    ois.readObject();
                    fis.close();
                    txt = ois.toString();
                    Log.v(GRID_ID, txt);

                }catch (Exception e) {
                    e.printStackTrace();
                }
                mAdapter = new TileAdapter();
                mGameBoard.setAdapter(mAdapter);
            }});
        mColorButton = (Button) view.findViewById(R.id.color_button);
        mColorButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                changeColors();
                                                resetAdapter();
                                            }
                                        }
        );


        //Speed Adjusters
        m10SecButton = (Button) view.findViewById(R.id.TenSec_button);
        m10SecButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                mSpeed = 10000; //Setting the speed to 10,000 millisecs
                                                changeSpeedLabel();
                                            }
                                        }
        );
        m5SecButton = (Button) view.findViewById(R.id.FiveSec_button);
        m5SecButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                mSpeed = 5000; //Setting the speed to 5,000 millisecs
                                                changeSpeedLabel();
                                            }
                                        }
        );
        m1SecButton = (Button) view.findViewById(R.id.OneSec_button);
        m1SecButton.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View view) {
                                               mSpeed = 1000; //Setting the speed to 1,000 millisecs
                                               changeSpeedLabel();
                                           }
                                       }
        );
        mSuperFastSpeedButton = (Button) view.findViewById(R.id.superFast_button);
        mSuperFastSpeedButton.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View view) {
                                               mSpeed = 10; //Setting the speed to 50 millisecs
                                               changeSpeedLabel();
                                           }
                                       }
        );



        //Return
        return view;
    }
    //This will set the counter back to 0
    public void resetGenCounter()
    {
        mGenNumber = 0;
    }
    public void incCounter()
    {
        mGenNumber++;
        mGenCounterLabel.setText(String.valueOf(mGenNumber));
    }
    //ceased the current Handler if it is present
    public void ceaseFunctions()
    {
        if(checkBoard()== true){
            resetGenCounter(); //if the board is blank then reset the counter
        }
        mNextGenHandler.removeCallbacks(mNextGen);
        mStartButton.setText(R.string.start);
        mStarted = false;
    }
    public void changeSpeedLabel()
    {
        mCurrentSpeedLabel.setText(String.valueOf(mSpeed));
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

    private Runnable mNextGen = new Runnable() {
        @Override
        public void run() {
            //Code Reference: https://stackoverflow.com/questions/37995564/what-is-the-way-to-make-an-infinite-loop-in-a-thread-android
            //Please give credit where it is due
            if (checkBoard() == true) { //if the board is empty then stop everything
                ceaseFunctions();
                //But we want to keep the generation number because the user might want to know what generation
                //the cells died at
            } else {
                mGrid = life.nextGeneration(mGrid);
                resetAdapter();
                mNextGenHandler.postDelayed(mNextGen, mSpeed);
                incCounter();
            }
        }
    };

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putBoolean(LIFE_ID, mBoardState);
    }

    //Idividual spots on the recycler view are considered individualTiles, this
    //Will set up the adapter to interact with each of the tiles
    //Setting the alive tiles to a star and the dead ones to a square
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
                //Source Code:  https://stackoverflow.com/questions/14216798/how-to-create-imageview-pulse-effect-using-nine-old-androids-animation -
               Animation spinner = AnimationUtils.loadAnimation(getActivity(), R.anim.pulse_animation);
               holder.mSquare.startAnimation(spinner);
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

    //Reset the board back to all 0s
    public void resetBoard() {
        //go through the board and reset it to 0
        for(int i= 0; i <mSize; i ++){
            mGrid[i] = 0;
        }
    }

    //Func to reset the adapter
    public void resetAdapter(){
        mAdapter = new TileAdapter();
        mGameBoard.setAdapter(mAdapter);
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
