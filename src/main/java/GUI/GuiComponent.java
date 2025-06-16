package GUI;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class GuiComponent extends JComponent {
    private int x1;
    private int y1;
    private int x2;
    private int y2;

    private JLabel idLabel;

    public GuiComponent(int x1, int y1, int x2, int y2, Graphics2D g2d, Color color, String id) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;

        this.setBorder(new LineBorder(color, 1));
        this.setBounds(x1, y1, x2 - x1, y2 - y1);
        this.setBackground(Color.BLACK);
        this.setOpaque(false);

        this.idLabel = new JLabel(id);
        this.idLabel.setBounds(0, 0, 10,10);
        this.idLabel.setText(id);
        this.add(idLabel);

        this.paint(g2d);

    }

    public String toString(){
        return "X1: " + this.x1 + ", Y1: " + this.y1 + ", X2: " + this.x2 + ", Y2: " + this.y2;
    }

}
