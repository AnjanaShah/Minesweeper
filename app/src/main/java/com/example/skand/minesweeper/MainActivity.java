package com.example.skand.minesweeper;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    int n=9;
    int m=9;
    boolean PLAYER_WINS ;
    public final static int WIN = 1;
    public final static int INCOMPLETE = 2;
    int scoreCount,mineCount;
    LinearLayout mainLayout;
    LinearLayout row[];
    MyButton button[][];
    TextView score;
    TextView mines;
    boolean gameOver;


    @Override
    public SharedPreferences getPreferences(int mode) {
        return super.getPreferences(mode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainLayout=(LinearLayout)findViewById(R.id.layout2);
        score=(TextView) findViewById(R.id.textView);
        mines=(TextView) findViewById(R.id.textView2);
        setUpBoard();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.mine_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();

        if(id==R.id.newGame)
            setUpBoard();
        else if (id == R.id.Easy) {

            n=5;
            m=3;
            setUpBoard();
            }


         else if (id == R.id.Intermediate) {
                 n=7;
                 m=5;
                setUpBoard();
            }

        else if (id == R.id.Hard) {
                n=9;
                m=9;
                setUpBoard();

        }
        return true;
    }

    private void setUpBoard()
    {
        row= new LinearLayout[n];
        button=new MyButton[n][m];
        mainLayout.removeAllViews();

        for(int i=0;i<n;i++)
        {
            row[i]= new LinearLayout(this);
            LinearLayout.LayoutParams params= new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0,1);
            params.setMargins(0,0,0,0);
            row[i].setLayoutParams(params);
            row[i].setOrientation(LinearLayout.HORIZONTAL);
            mainLayout.addView(row[i]);
        }

        for(int i=0;i<n;i++) {
            for (int j = 0; j < m; j++) {
                button[i][j] = new MyButton(this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0,ViewGroup.LayoutParams.MATCH_PARENT, 1);
                params.setMargins(1, 1, 1, 1);
                button[i][j].setLayoutParams(params);
               // button[i][j].setPadding(0,0,0,0);
                button[i][j].setText("");
                button[i][j].setOnClickListener(this);
                button[i][j].setOnLongClickListener(this);
                button[i][j].setI(i);
                button[i][j].setJ(j);
                row[i].addView(button[i][j]);

            }
        }

        setUpGame();
    }
    private void setUpGame()
    {
        gameOver=false;
        PLAYER_WINS=false;
        scoreCount=0;
        mineCount=n;
//putting bombs
        Random rand = new Random();
        for( int i=0;i<n;i++)
        {
            int k = rand.nextInt(n-1) + 0;
            int l = rand.nextInt(m-1) + 0;
            if(button[k][l].isBomb())
                i--;
            else
                button[k][l].setBomb(true);
        }
        //setting count for each button
        for(int i=0;i<n;i++)
        {
            for(int j=0;j<m;j++)
            {
                int count=0;
                if(!button[i][j].isBomb()==true) {
                    if ( i > 0 && j > 0&&button[i - 1][j - 1].isBomb() == true )
                        count++;
                    if (j > 0&&button[i][j - 1].isBomb() == true )
                        count++;
                    if (i < n-1 && j > 0&&button[i + 1][j - 1].isBomb() == true )
                        count++;
                    if (i < n-1&&button[i + 1][j].isBomb() == true )
                        count++;
                    if (i > 0&&button[i - 1][j].isBomb() == true )
                        count++;
                    if (j < m-1 && i > 0&&button[i - 1][j + 1].isBomb() == true )
                        count++;
                    if (j < m-1&&button[i][j + 1].isBomb() == true )
                        count++;
                    if (j < m-1 && i < n-1 &&button[i + 1][j + 1].isBomb() == true )
                        count++;
                }
                else
                {
                    count=-1;
                }
                button[i][j].setBombCount(count);
            }
        }

        score.setText("SCORE:"+scoreCount);
        mines.setText("MINES:"+mineCount);

    }

    @Override
    public void onClick(View v) {
        MyButton b= (MyButton)v;
        if(b.isFlag()==true)
            return;

        if(b.isBomb()==true)
        {
            Toast.makeText(this,"GAMEOVER!",Toast.LENGTH_LONG).show();
            //disclosing the board
            gameOver=true;
            for(int i=0;i<n;i++)
            {
                for(int j=0;j<m;j++)
                {
                    if(button[i][j].isBomb()==true)
                    {
                        button[i][j].setText("*");
                    }
                    else
                    {
                        button[i][j].setText(""+button[i][j].getBombCount());
                    }

                }
            }

            return;
        }
        else
        {
            reveal(b);
        }
         //checking the current state of the board and updating the score
        int count=0;
        int bombCount=0;
        for(int i=0;i<n;i++)
        {
            for(int j=0;j<m;j++)
            {
                if(button[i][j].isRevealed()==true)
                {
                    count++;
                    if(button[i][j].isBomb()==true)
                    {
                      bombCount++;
                    }
                }

            }
        }
        scoreCount=count;
        mineCount=mineCount-bombCount;
        score.setText("SCORE:"+scoreCount);
        mines.setText("MINES:"+mineCount);

        int status=gameStatus();
        if (status == WIN ) {
            //disclosing the board
            for(int i=0;i<n;i++)
            {
                for(int j=0;j<m;j++)
                {
                    if(button[i][j].isBomb()==true)
                    {
                        button[i][j].setText("*");
                    }
                    else
                    {
                        button[i][j].setText(""+button[i][j].getBombCount());
                    }

                }
            }
            gameOver = true;
            Toast.makeText(this, "YOU WINS!", Toast.LENGTH_SHORT).show();
            return;
        }

    }

    private int gameStatus() {
       // boolean flag=true;

        if((n*m)-scoreCount<=mineCount)
        {
            return WIN;
        }
        else
        {
            return INCOMPLETE;
        }
    }

    public  void reveal(MyButton b)
    {
         if(!b.isRevealed())
        {
            b.setRevealed(true);
            int count;
            count=b.getBombCount();
            if(count!=0)
            {
                b.setText(""+count);
            }
            else
            {
                b.setText(""+count);
                int i=b.getI();
                int j=b.getJ();
                if ( i > 0 && j > 0 )
                     reveal(button[i - 1][j - 1]);
                if (j > 0 )
                    reveal(button[i][j - 1]);
                if (i < n-1 && j > 0)
                    reveal(button[i + 1][j - 1]);
                if (i < n-1 )
                    reveal(button[i + 1][j]);
                if (i > 0)
                    reveal(button[i - 1][j] );
                if (j < m-1 && i > 0)
                    reveal(button[i - 1][j + 1]);
                if (j < m-1)
                    reveal(button[i][j + 1]);
                if (j < m-1 && i < n-1 )
                    reveal(button[i + 1][j + 1]);


            }


        }
    }


    @Override
    public boolean onLongClick(View v) {
        MyButton b= (MyButton) v;
        if(b.isFlag())
        {
            b.setFlag(false);
        }
        else
        {
            b.setFlag(true);
        }
        return false;
    }
}
