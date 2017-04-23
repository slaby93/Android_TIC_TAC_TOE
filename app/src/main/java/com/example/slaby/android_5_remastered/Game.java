package com.example.slaby.android_5_remastered;

/**
 * Created by Slaby on 22.04.2017.
 */

public class Game {
    static MainActivity mainActivity;
    Type tura;
    GameState state;
    Type AIType;
    Boolean didAiMoved;

    Game() {
        tura = Type.CIRCLE;
        state = GameState.NEW_GAME;
        didAiMoved = false;
        mainActivity.setTura(tura);
    }

    public void touchedCell(int cellId) {
        if (state == GameState.CROSS_WIN || state == GameState.DRAW || state == GameState.CIRCLE_WIN) {
            return;
        }

        Point tmp = mainActivity.getPointByCellId(cellId);
        if (tmp.type != Type.BLANK) {
            mainActivity.signalTouchingAlreadyOccupiedCell();
            return;
        }
        tmp.setPointState(tura);
        proceedWithGame();
    }

    public void proceedWithGame() {
        checkGameState();
        System.out.println("GAME STATE: " + state);
        if (state == GameState.CIRCLE_WIN) {
            mainActivity.setWinner(Type.CIRCLE);
        } else if (state == GameState.CROSS_WIN) {
            mainActivity.setWinner(Type.CROSS);
        } else if (state == GameState.DRAW) {
            mainActivity.setDraw();
        } else if (state == GameState.PROGRESS) {
            mainActivity.vibrate();
            if (mainActivity.isAutoPlayEnabled && !didAiMoved) {
                tryToAutoplay();
            } else {
                didAiMoved = false;
            }
            changeTura();
        }
        mainActivity.shouldEnableSwitchSide(state == GameState.NEW_GAME);

    }

    public void tryToAutoplay() {
        AIType = this.tura == Type.CIRCLE ? Type.CROSS : Type.CIRCLE;
        didAiMoved = true;
        for (int i = 0; i < 9; i++) {
            Point tmp = mainActivity.arrayOfPoints.get(i);
            if (tmp.type == Type.BLANK) {
                tmp.setPointState(AIType);
                break;
            }
        }
        proceedWithGame();
    }

    private void changeTura() {
        this.tura = tura == Type.CIRCLE ? Type.CROSS : Type.CIRCLE;
        mainActivity.setTura(this.tura);

    }

    public void checkGameState() {
        short numberOfNotBlankCells = 0;
        boolean areCrossWin = false;
        boolean areCircleWin = false;

        for (int i = 0; i < mainActivity.arrayOfPoints.size(); i++) {
            Point tmp = mainActivity.arrayOfPoints.get(i);
            if (tmp.type != Type.BLANK) {
                numberOfNotBlankCells++;
            }
            if (checkIfPointWon(tmp)) {
                if (tmp.type == Type.CIRCLE) {
                    areCircleWin = true;
                } else {
                    areCrossWin = true;
                }
                mainActivity.vibrate();
            }
        }

        if (numberOfNotBlankCells == 0) {
            this.state = GameState.NEW_GAME;
        } else if (areCircleWin) {
            this.state = GameState.CIRCLE_WIN;
        } else if (areCrossWin) {
            this.state = GameState.CROSS_WIN;
        } else {
            this.state = GameState.PROGRESS;
        }


        if (numberOfNotBlankCells == 9) {
            this.state = GameState.DRAW;
            mainActivity.vibrate();
        }
    }

    public boolean isGameFinished() {
        return state == GameState.CROSS_WIN || state == GameState.CIRCLE_WIN || state == GameState.DRAW;
    }

    private boolean checkIfPointWon(Point startingPoint) {
        int x = startingPoint.x;
        int y = startingPoint.y;
        if (startingPoint.type == Type.BLANK) {
            return false;
        }
        Point right = mainActivity.getPointByXY(x + 1, y);
        Point rightMore = mainActivity.getPointByXY(x + 2, y);

        Point left = mainActivity.getPointByXY(x - 1, y);
        Point leftMore = mainActivity.getPointByXY(x - 2, y);

        Point top = mainActivity.getPointByXY(x, y + 1);
        Point topMore = mainActivity.getPointByXY(x, y + 2);


        Point bottom = mainActivity.getPointByXY(x, y - 1);
        Point bottomMore = mainActivity.getPointByXY(x, y - 2);


        Point skosLewoTop = mainActivity.getPointByXY(x - 1, y + 1);
        Point skosLewoTopMore = mainActivity.getPointByXY(x - 2, y + 2);


        Point skosPrawoTop = mainActivity.getPointByXY(x + 1, y + 1);
        Point skosPrawoTopMore = mainActivity.getPointByXY(x + 2, y + 2);


        Point skosLewoBottom = mainActivity.getPointByXY(x - 1, y - 1);
        Point skosLewoBottomMore = mainActivity.getPointByXY(x - 2, y - 2);


        Point skosPrawoBottom = mainActivity.getPointByXY(x + 1, y - 1);
        Point skosPrawoBottomMore = mainActivity.getPointByXY(x + 2, y - 2);


        boolean isWin = false;

        if (isSameType(startingPoint, right) && isSameType(startingPoint, rightMore)) {
            return true;
        }
        return false;

    }

    private boolean isSameType(Point a, Point b) {
        if (a == null || b == null) {
            return false;
        }
        return a.type == b.type;
    }
}
