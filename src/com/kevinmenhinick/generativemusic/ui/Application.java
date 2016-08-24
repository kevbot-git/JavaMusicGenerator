package com.kevinmenhinick.generativemusic.ui;

import com.kevinmenhinick.generativemusic.Generator;
import com.kevinmenhinick.generativemusic.exception.GeneratorException;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;

public class Application extends javax.swing.JFrame {

    private static Generator generator;
    
    private static JButton btnGenerate;
    private static JButton btnStop;
    
    private static JPanel pnlBottom;
    
    public Application() {
        try {
            generator = new Generator();
        } catch(GeneratorException e) {
            System.exit(0);
        }
        
        initComponents();
        addComponents();
        setListeners();
    }

    @SuppressWarnings("unchecked")                         
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        
        super.setSize(320, 240);
        super.setResizable(false);
        super.setLocation(getCenterPoint(this));

        pnlBottom = new JPanel(new GridLayout(2, 1));
        pnlBottom.setSize(320, 80);
        
        btnGenerate = new JButton("Generate one 4/4 bar");
        btnStop = new JButton("Stop playing");
    }
    
    private void addComponents() {
        pnlBottom.add(btnGenerate);
        pnlBottom.add(btnStop);
        
        getContentPane().add(pnlBottom);
        pack();
    }
    
    private void setListeners() {
        btnGenerate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generator.start();
            }
        });
        btnStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generator.stop();
            }
        });
    }
    
    public static void main(String args[]) {
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Application().setVisible(true);
            }
        });
        
    }
    
    public static Point getCenterPoint(Frame frame) {
        Toolkit tk = Toolkit.getDefaultToolkit();
        int x = (int) tk.getScreenSize().getWidth() / 2;
        int y = (int) tk.getScreenSize().getHeight() / 2;

        int diffX = (int) frame.getWidth() / 2;
        int diffY = (int) frame.getHeight() / 2;

        return new Point(x - diffX, y - diffY);
    }
}

