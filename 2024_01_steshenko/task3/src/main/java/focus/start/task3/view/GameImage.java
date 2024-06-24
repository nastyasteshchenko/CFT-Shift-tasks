package focus.start.task3.view;

import javax.swing.*;

public enum GameImage {
    CLOSED("image/cell.png"),
    MARKED("image/flag.png"),
    EMPTY("image/empty.png"),
    NUM_1("image/1.png"),
    NUM_2("image/2.png"),
    NUM_3("image/3.png"),
    NUM_4("image/4.png"),
    NUM_5("image/5.png"),
    NUM_6("image/6.png"),
    NUM_7("image/7.png"),
    NUM_8("image/8.png"),
    BOMB("image/mine.png"),
    TIMER("image/timer.png"),
    BOMB_ICON("image/mineImage.png");

    private final String fileName;
    private ImageIcon imageIcon;

    GameImage(String fileName) {
        this.fileName = fileName;
    }

    public synchronized ImageIcon getImageIcon() {
        if (imageIcon == null) {
            imageIcon = new ImageIcon(ClassLoader.getSystemResource(fileName));
        }

        return imageIcon;
    }

    public static GameImage getImageByMinesAmount(int minesAmount) {
        return switch (minesAmount) {
            case 1 -> NUM_1;
            case 2 -> NUM_2;
            case 3 -> NUM_3;
            case 4 -> NUM_4;
            case 5 -> NUM_5;
            case 6 -> NUM_6;
            case 7 -> NUM_7;
            case 8 -> NUM_8;
            default -> EMPTY;
        };
    }
}
