package com.kevinmenhinick.generativemusic.ui;

import com.kevinmenhinick.generativemusic.Generator;
import com.kevinmenhinick.generativemusic.exception.GeneratorException;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.EmptyBorder;

public class Application extends javax.swing.JFrame {

    private static Generator generator;
    
    private static JButton btnGenerate;
    private static JButton btnStop;
    
    private static JPanel pnlMain;
    private static JPanel pnlBottom;
    private static JPanel pnlTop;
    
    public Application() {
        super("Java Music Generator");
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
        
        super.setSize(480, 320);
        super.setResizable(false);
        super.setLocation(getCenterPoint(this));

        pnlMain = new JPanel(new BorderLayout());
        pnlMain.setBorder(new EmptyBorder(8, 8, 8, 8));
        pnlBottom = new JPanel(new GridLayout(1, 2, 8, 8));
        pnlTop = new JPanel(new BorderLayout(8, 8));
        
        btnGenerate = new JButton("Start generating");
        btnStop = new JButton("Stop");
    }
    
    private void addComponents() {
        pnlTop.add(new JSlider(JSlider.VERTICAL), BorderLayout.EAST);
        
        pnlBottom.add(btnStop);
        pnlBottom.add(btnGenerate);

        pnlMain.add(pnlTop, BorderLayout.CENTER);
        
        pnlMain.add(pnlBottom, BorderLayout.SOUTH);
        
        //pnlMain.add();
        
        getContentPane().add(pnlMain);
    }
    
    private void setListeners() {
        btnGenerate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!generator.isPlaying()) {
                    try {
                        generator = new Generator();
                    } catch(GeneratorException ex) {
                        System.exit(0);
                    }
                    generator.start();
                    btnStop.setBackground(Color.RED);
                }
            }
        });
        btnStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generator.stop();
                btnStop.setBackground(new Color(238, 238, 238));
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

