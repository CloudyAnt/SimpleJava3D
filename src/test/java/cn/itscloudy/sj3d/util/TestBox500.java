package cn.itscloudy.sj3d.util;

import javax.swing.*;

public class TestBox500 {

    public final JFrame frame;
    JLayeredPane layers;
    JLabel noticeLabel;

    public TestBox500() {
        this.frame = new JFrame();
        frame.setBounds(0, 0, 500, 500);
        layers = new JLayeredPane();
        frame.setLayeredPane(layers);

        noticeLabel = new JLabel();
        noticeLabel.setOpaque(false);
        noticeLabel.setVerticalAlignment(JLabel.TOP);
        noticeLabel.setBounds(0, 0, 500, 500);

        layers.add(noticeLabel);
        layers.setLayer(noticeLabel, 1);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void setNotice(String notice) {
        noticeLabel.setText(notice);
    }

    public void setContent(JPanel panel) {
        panel.setBounds(0, 0, 500, 500);
        layers.add(panel);
        layers.setLayer(panel, 0);
    }
}
