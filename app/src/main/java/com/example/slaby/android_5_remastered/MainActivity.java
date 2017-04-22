package com.example.slaby.android_5_remastered;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Toolbar myToolbar;
    Boolean isAutoPlayEnabled = false;
    MenuItem enableComputerAIMenuItem;
    MenuItem switchSideMenuItem;
    Menu menu;
    Game game;
    List<Point> arrayOfPoints;
    Drawable circle;
    Drawable cross;
    RelativeLayout wrapper;
    ImageView nowTura;
    TextView winnerText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupTopbar();
        Game.mainActivity = this;
        Point.mainActivity = this;
        winnerText = (TextView) findViewById(R.id.winnerText);
        cross = ContextCompat.getDrawable(this, R.drawable.cross);
        circle = ContextCompat.getDrawable(this, R.drawable.circle);
        nowTura = (ImageView) findViewById(R.id.nowTura);
        wrapper = (RelativeLayout) findViewById(R.id.wrapper);
        wrapper.setOnTouchListener(onTouch());
        initializeArrayOfPoints();
        startNewGame();
    }

    public void initializeArrayOfPoints() {
        arrayOfPoints = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                ImageView tmp = null;
                int cellId = getCellIdByXY(j, i);
                switch (cellId) {
                    case 0:
                        tmp = (ImageView) findViewById(R.id.point0);
                        break;
                    case 1:
                        tmp = (ImageView) findViewById(R.id.point1);
                        break;
                    case 2:
                        tmp = (ImageView) findViewById(R.id.point2);
                        break;
                    case 3:
                        tmp = (ImageView) findViewById(R.id.point3);
                        break;
                    case 4:
                        tmp = (ImageView) findViewById(R.id.point4);
                        break;
                    case 5:
                        tmp = (ImageView) findViewById(R.id.point5);
                        break;
                    case 6:
                        tmp = (ImageView) findViewById(R.id.point6);
                        break;
                    case 7:
                        tmp = (ImageView) findViewById(R.id.point7);
                        break;
                    case 8:
                        tmp = (ImageView) findViewById(R.id.point8);
                        break;
                }
                arrayOfPoints.add(new Point(cellId, j, i, Type.BLANK, tmp));
            }
        }
    }

    public void clearAllPoints() {
        for (int i = 0; i < arrayOfPoints.size(); i++) {
            arrayOfPoints.get(i).image.setImageDrawable(null);
        }
    }

    public void setupTopbar() {
        myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    public View.OnTouchListener onTouch() {
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                handleTouchEvent(event.getX(), event.getY());
                return false;
            }
        };
    }

    public void setTura(Type tura) {
        if (tura == Type.CIRCLE) {
            nowTura.setImageDrawable(circle);
        } else {
            nowTura.setImageDrawable(cross);
        }
    }

    public void handleTouchEvent(float x, float y) {
        final float row1YMin = 850;
        final float row1YMax = 1170;

        final float row2YMin = 1170;
        final float row2YMax = 1480;

        final float row3YMin = 1500;
        final float row3YMax = 1900;

        // ROW1
        if (y >= row1YMin && y <= row1YMax) {
            hanldeCellClick(0, x);
        } else if (y > row2YMin && y <= row2YMax) {
            hanldeCellClick(1, x);
        } else if (y >= row3YMin && y <= row3YMax) {
            hanldeCellClick(2, x);
        } else {
            System.out.println("OUT OF BOX ROW");
        }
    }

    public Point getPointByCellId(int cellId) {
        return arrayOfPoints.get(cellId);
    }

    public void setWinner(Type winner) {
        winnerText.setText("Winner: " + winner);
    }

    public void setDraw() {
        winnerText.setText("DRAW");
    }

    public Point getPointByXY(int x, int y) {
        for (int i = 0; i < arrayOfPoints.size(); i++) {
            Point tmp = arrayOfPoints.get(i);
            if (tmp.x == x && tmp.y == y) {
                return tmp;
            }
        }
        return null;
    }

    public void hanldeCellClick(int row, float x) {
        final float col1Min = 120;
        final float col1Max = 370;

        final float col2Min = 370;
        final float col2Max = 685;

        final float col3Min = 720;
        final float col3Max = 1050;
        // COL1
        if (x >= col1Min && x <= col1Max) {
            clickedCell(0, row);
        } else if (x >= col2Min && x <= col2Max) {
            clickedCell(1, row);
        } else if (x >= col3Min && x <= col3Max) {
            clickedCell(2, row);
        } else {
            System.out.println("OUT OF BOX COL");
        }
    }

    public void signalTouchingAlreadyOccupiedCell() {
        System.out.println("ALREADY OCCUPIED CELL");
    }

    public int getCellIdByXY(int x, int y) {
        if (x == 0) {
            if (y == 0) {
                return 0;
            } else if (y == 1) {
                return 3;
            }
            return 6;
        } else if (x == 1) {
            if (y == 0) {
                return 1;
            } else if (y == 1) {
                return 4;
            }
            return 7;
        } else if (x == 2) {
            if (y == 0) {
                return 2;
            } else if (y == 1) {
                return 5;
            }
            return 8;
        }
        return -1;
    }

    public void clickedCell(int x, int y) {
        game.touchedCell(getCellIdByXY(x, y));
    }

    public void setCellDrawable(int cellId, Type type) {
        if (type == Type.BLANK) {
            arrayOfPoints.get(cellId).image.setImageDrawable(null);
        } else if (type == Type.CIRCLE) {
            arrayOfPoints.get(cellId).image.setImageDrawable(circle);
        } else {
            arrayOfPoints.get(cellId).image.setImageDrawable(cross);
        }

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        enableComputerAIMenuItem = menu.findItem(R.id.auto_game);
        switchSideMenuItem = menu.findItem(R.id.switch_sides);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.new_game) {
            startNewGame();
        } else if (item.getItemId() == R.id.switch_sides) {
            switchSides();
        } else {
            enableComputerAIMenuItem();
        }

        return super.onOptionsItemSelected(item);
    }

    public void clearWinner() {
        winnerText.setText("");
    }

    public void startNewGame() {
        System.out.println("NEW GAME");
        game = new Game();
        clearWinner();
        initializeArrayOfPoints();
        clearAllPoints();
    }

    public void switchSides() {
        System.out.println("SWITCH SIDES");
    }

    public void enableComputerAIMenuItem() {
        this.isAutoPlayEnabled = !this.isAutoPlayEnabled;
        if (this.isAutoPlayEnabled) {
            enableComputerAIMenuItem.setTitle("Disable Computer AI");
        } else {
            enableComputerAIMenuItem.setTitle("Enable Computer AI");
        }
    }

}