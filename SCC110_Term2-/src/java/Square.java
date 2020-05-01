import javax.swing.*;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class Square {
    private static final String path = URLDecoder.decode(Square.class.getClassLoader().getResource("pieces").getPath());

    // image
    public static final String WATER = path + "/Water.png";
    public static final String LILY_PAD = path + "/LilyPad.png";
    public static final String GREEN_FROG_1 = path + "/GreenFrog.png";
    public static final String GREEN_FROG_2 = path + "/GreenFrog2.png";
    public static final String RED_FROG_1 = path + "/RedFrog.png";
    public static final String RED_FROG_2 = path + "/RedFrog2.png";

    // image type
    public static final int TYPE_WATER = 0;
    public static final int TYPE_LILY_PAD = 1;
    public static final int TYPE_GREEN_FROG_1 = 2;
    public static final int TYPE_GREEN_FROG_2 = 3;
    public static final int TYPE_RED_FROG_1 = 4;
    public static final int TYPE_RED_FROG_2 = 5;

    private static final Map<Integer, String> typeToImageMap = new HashMap<>();
    static {
        typeToImageMap.put(TYPE_WATER, WATER);
        typeToImageMap.put(TYPE_LILY_PAD, LILY_PAD);
        typeToImageMap.put(TYPE_GREEN_FROG_1, GREEN_FROG_1);
        typeToImageMap.put(TYPE_GREEN_FROG_2, GREEN_FROG_2);
        typeToImageMap.put(TYPE_RED_FROG_1, RED_FROG_1);
        typeToImageMap.put(TYPE_RED_FROG_2, RED_FROG_2);
    }

    private JButton btn;
    private int imageType = TYPE_WATER;
    private int rowNum;
    private int colNum;

    private static Square[][] squares;

    public static Square[][] initSquare(int level, int rowCount, int colCount) {
        squares = new Square[rowCount][colCount];
        for (int i = 0; i < rowCount; i++) {
            int[][] levelImageType = levelLocationMap.get(level);
            levelImageType = levelImageType == null ? new int[5][5] : levelImageType;
            for (int j = 0; j < colCount; j++) {
                Square square = new Square();
                square.changeImage(levelImageType[i][j]);
                squares[i][j] = square;
                square.rowNum = i;
                square.colNum = j;
            }
        }
        return squares;
    }

    public static Square getSquare(int rowNum, int colNum) {
        return squares[rowNum][colNum];
    }

    public Square() {
       this.btn = new JButton();
        this.imageType = TYPE_WATER;
        btn.setIcon(new ImageIcon(typeToImageMap.get(imageType)));
    }

    public void selectFrogSquare() {
        if (this.imageType == TYPE_GREEN_FROG_1) {
            changeImage(TYPE_GREEN_FROG_2);
        } else if (this.imageType == TYPE_RED_FROG_1) {
            changeImage(TYPE_RED_FROG_2);
        }
    }

    public void cleanSelectFrogSquare() {
        if (this.imageType == TYPE_GREEN_FROG_2) {
            changeImage(TYPE_GREEN_FROG_1);
        } else if (this.imageType == TYPE_RED_FROG_2) {
            changeImage(TYPE_RED_FROG_1);
        }
    }

    public void changeImage(int imageType) {
        this.imageType = imageType;
        ImageIcon icon = new ImageIcon(typeToImageMap.get(imageType));
        btn.setIcon(icon);
    }

    public void moveTo(Square targetSquare) {
        int targetImageType = targetSquare.imageType;
        if (this.imageType == TYPE_GREEN_FROG_2) {
            targetSquare.changeImage(TYPE_GREEN_FROG_1);
        } else if (this.imageType == TYPE_RED_FROG_2) {
            targetSquare.changeImage(TYPE_RED_FROG_1);
        } else {
            targetSquare.changeImage(this.imageType);
        }
        changeImage(targetImageType);
    }

    public boolean canMoveTo(Square targetSquare) {
        if (this.equals(targetSquare)) {
            return false;
        }
        // current not select
        if (TYPE_GREEN_FROG_2 != imageType && TYPE_RED_FROG_2 != imageType) {
            return false;
        }
        // target not pad
        if (TYPE_LILY_PAD != targetSquare.imageType) {
            return false;
        }
        // horizontally, vertically or diagonally
        return rowNum == targetSquare.rowNum || colNum == targetSquare.colNum
                || Math.abs(targetSquare.rowNum - rowNum) == Math.abs(targetSquare.colNum - colNum);
    }

    public static Square parseBtn(JButton btn) {
        for (int i = 0; i < squares.length; i++) {
            for (int j = 0; j < squares[0].length; j++) {
                if (btn.equals(squares[i][j].btn)) {
                    return squares[i][j];
                }
            }
        }
        return null;
    }

    private static Map<Integer, int[][]> levelLocationMap = new HashMap<>();
    static {
        // 0-Water、1-Pad、2-GreenFrog、4-RedFrog
        int[][] level1 = new int[][]{{4,0,1,0,1},{0,2,0,1,0},{1,0,1,0,1},{0,1,0,1,0},{1,0,1,0,1}};
        int[][] level2 = new int[][]{{1,0,1,0,4},{0,1,0,1,0},{1,0,1,0,2},{0,1,0,1,0},{1,0,1,0,1}};
        int[][] level15 = new int[][]{{2,0,2,0,1},{0,1,0,1,0},{1,0,1,0,2},{0,4,0,2,0},{1,0,1,0,1}};
        levelLocationMap.put(1, level1);
        levelLocationMap.put(2, level2);
        levelLocationMap.put(15, level15);
    }

    public int getRowNum() {
        return rowNum;
    }

    public int getColNum() {
        return colNum;
    }

    public JButton getBtn() {
        return btn;
    }

    public int getImageType() {
        return imageType;
    }


}