package com.example.game_of_life;

public class life_generator {
    public int mX_size, mY_size;

    public life_generator(int X_size, int Y_size){
        mX_size = X_size;
        mY_size = Y_size;
    }

    public int[] nextGeneration(int[] mGrid) {
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
                //upperleft, uppercenter, upperright
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
                }
                n++; //Update n to go to the next cell

            } //end of j for loop
        } //end of i for loop
        return mGrid;
    }
}
