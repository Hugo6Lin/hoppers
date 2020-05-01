import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class Board extends JFrame implements ActionListener {
    private static Square[][] squares;

    private Square currentSquare;
    int level = 1;
    private boolean win = false;
    private boolean over = false;

    JButton levelSubmitBtn;
    JTextField levelField;
    JPanel frogJPanel = new JPanel();

    public Board() {
        setSize(750, 820);
        setTitle("SCC110: Software Development Term 2");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        init();

        setVisible(true);
    }

    private void init() {
        add(levelSelectJPanel(), BorderLayout.NORTH);
        initGameJPanel();
        add(frogJPanel,BorderLayout.CENTER);
    }

    public void reload() {
        initGameJPanel();
        this.setVisible(true);
    }

    private JPanel levelSelectJPanel() {
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new FlowLayout());
        levelField = new JTextField(5);
        levelField.setSize(400, 400);
        levelField.setText("1");
        northPanel.add(levelField);

        levelSubmitBtn = new JButton("submit");
        levelSubmitBtn.addActionListener(this);
        northPanel.add(levelSubmitBtn);
        return northPanel;
    }

    private JPanel initGameJPanel() {
        level = parseLevel();
        win = false;
        over = false;
        frogJPanel.removeAll();

        frogJPanel.setLayout(new GridLayout(5, 5));
        squares = Square.initSquare(level, 5, 5);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                Square square = squares[i][j];
                square.getBtn().addActionListener(this);
                frogJPanel.add(square.getBtn());
            }
        }
        return frogJPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton btn = (JButton) e.getSource();
        if (btn.equals(levelSubmitBtn)) {
            reload();
            return;
        }
        if (over) {
            return;
        }

        Square square = Square.parseBtn(btn);
        if (square.getImageType() == Square.TYPE_GREEN_FROG_1 || square.getImageType() == Square.TYPE_RED_FROG_1) {
            if (currentSquare != null) {
                currentSquare.cleanSelectFrogSquare();
            }
            square.selectFrogSquare();
            currentSquare = square;
        } else if (square.getImageType() == Square.TYPE_LILY_PAD && currentSquare != null) {
            if (currentSquare.canMoveTo(square)) {
                currentSquare.moveTo(square);
                cleanJumpOverFrog(currentSquare, square);
                currentSquare = null;
                checkGameOver();
            }
        }
    }

    private int parseLevel() {
        String value = levelField.getText();
        level = 1;
        try {
            level = Integer.parseInt(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (level < 1) {
            level = 1;
        } else if (level > 40) {
            level = 40;
        }
        return level;
    }

    private void checkGameOver() {
        boolean hasRedFrogLeft = false;
        int leftFrogCount = 0;
        for (int i = 0; i < squares.length; i++) {
            for (int j = 0; j < squares[0].length; j++) {
                Square square = squares[i][j];
                if (square.getImageType() == Square.TYPE_RED_FROG_1 || square.getImageType() == Square.TYPE_RED_FROG_2) {
                    hasRedFrogLeft = true;
                    leftFrogCount++;
                } else if (square.getImageType() == Square.TYPE_GREEN_FROG_1 || square.getImageType() == Square.TYPE_GREEN_FROG_2) {
                    leftFrogCount++;
                }
            }
        }
        if (!hasRedFrogLeft) {
            over = true;
            win = false;
            JOptionPane.showMessageDialog(null, "Failed: Red Frog Removed!","Game Over", JOptionPane.INFORMATION_MESSAGE);
        } else if (leftFrogCount == 1) {
            over = true;
            win = true;
            JOptionPane.showMessageDialog(null, "Win","Game Over", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void cleanJumpOverFrog(Square sourceSquare, Square targetSquare) {
        if (sourceSquare.getRowNum() == targetSquare.getRowNum()) {
            int colMax = Math.max(sourceSquare.getColNum(), targetSquare.getColNum());
            int colMin = Math.min(sourceSquare.getColNum(), targetSquare.getColNum());
            for (int i = colMin + 1; i < colMax; i++) {
                Square square = Square.getSquare(sourceSquare.getRowNum(), i);
                cleanJumpOverFrog(square);
            }
        } else if (sourceSquare.getColNum() == targetSquare.getColNum()) {
            int rowMax = Math.max(sourceSquare.getRowNum(), targetSquare.getRowNum());
            int rowMin = Math.min(sourceSquare.getRowNum(), targetSquare.getRowNum());
            for (int i = rowMin + 1; i < rowMax; i++) {
                Square square = Square.getSquare(i, sourceSquare.getColNum());
                cleanJumpOverFrog(square);
            }
        } else {
            if ((sourceSquare.getColNum() - targetSquare.getColNum()) * (sourceSquare.getRowNum() - targetSquare.getRowNum()) > 0) {
                Square startSquare = sourceSquare.getColNum() > targetSquare.getColNum() ? targetSquare : sourceSquare;

                Square endSquare = sourceSquare.getColNum() > targetSquare.getColNum() ? sourceSquare : targetSquare;
                for (int i = 1; i < endSquare.getRowNum() - startSquare.getRowNum(); i++) {
                    int row = startSquare.getRowNum() + i;
                    int col = startSquare.getColNum() + i;
                    Square square = Square.getSquare(row, col);
                    cleanJumpOverFrog(square);
                }
            } else {
                Square upperSquare = sourceSquare.getRowNum() > targetSquare.getRowNum() ? targetSquare : sourceSquare;

                Square lowerSquare = sourceSquare.getRowNum() > targetSquare.getRowNum() ? sourceSquare : targetSquare;
                for (int i = 1; i < lowerSquare.getRowNum() - upperSquare.getRowNum(); i++) {
                    int row = upperSquare.getRowNum() + i;
                    int col = upperSquare.getColNum() - i;
                    Square square = Square.getSquare(row, col);
                    cleanJumpOverFrog(square);
                }
            }
        }

    }

    private void cleanJumpOverFrog(Square square) {
        if (square.getImageType() == Square.TYPE_GREEN_FROG_1 || square.getImageType() == Square.TYPE_RED_FROG_1) {
            square.changeImage(Square.TYPE_LILY_PAD);
        }
    }


}
    