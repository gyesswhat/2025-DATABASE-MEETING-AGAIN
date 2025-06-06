package app;

import javax.swing.SwingUtilities;
import admin.RoomManagement;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BaseFrame baseFrame = new BaseFrame();
            // BaseFrame 내부에 default panel이 있고, 그 위에 RoomManagement 화면을 얹음
            baseFrame.change(baseFrame.panel, new RoomManagement(baseFrame));
            baseFrame.setVisible(true);
        });
    }
}