package com.kevinmenhinick.generativemusic.ui;

import com.kevinmenhinick.generativemusic.BeatAction;
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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.EmptyBorder;

public class Application extends javax.swing.JFrame {

    private static Generator generator;
    
    private static JButton btnGenerate;
    private static JButton btnStop;
    
    private static JPanel pnlTempo;
    private static JLabel lblTempo;
    private static JPanel pnlKey;
    private static JLabel lblKey;
    
    private static JPanel pnlMain;
    private static JPanel pnlBottom;
    private static JPanel pnlTop;
    
    private static Color colButton = new Color(240, 240, 250);

    public Application() {
        super("Java Music Generator - development version");
        
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

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        
        super.setSize(480, 320);
        super.setResizable(false);
        super.setLocation(getCenterPoint(this));

        pnlMain = new JPanel(new BorderLayout());
        pnlMain.setBorder(new EmptyBorder(8, 8, 8, 8));
        pnlBottom = new JPanel(new GridLayout(1, 2, 8, 8));
        pnlTop = new JPanel(new BorderLayout(8, 8));
        
        pnlTempo = new JPanel();
        lblTempo = new JLabel("0bpm");
        pnlKey = new JPanel();
        lblKey = new JLabel("Key: -");
        lblKey.setFont(lblKey.getFont().deriveFont(32f));
        
        btnGenerate = new JButton("Start generating");
        btnStop = new JButton("Stop");
        btnGenerate.setBackground(colButton);
        btnStop.setBackground(colButton);
        btnStop.setEnabled(false);
    }
    
    private void addComponents() {
        pnlTempo.add(lblTempo);
        pnlTop.add(pnlTempo, BorderLayout.NORTH);
        
        pnlKey.add(lblKey);
        pnlTop.add(pnlKey, BorderLayout.CENTER);
        
        pnlBottom.add(btnStop);
        pnlBottom.add(btnGenerate);

        pnlMain.add(pnlTop, BorderLayout.CENTER);
        
        pnlMain.add(pnlBottom, BorderLayout.SOUTH);
        
        getContentPane().add(pnlMain);
    }
    
    private void setListeners() {
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                generator.stop();
                setTitle("Closing...");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) { }
                System.exit(0);;
            }
        });
        
        btnGenerate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!generator.isPlaying()) {
                    try {
                        generator = new Generator();
                    } catch(GeneratorException ex) {
                        System.exit(0);
                    }
                    
                    generator.setBeatAction(new BeatAction() {
                        @Override
                        public void onBeat() {
                            try {
                                btnGenerate.setBackground(new Color(125, 125, 250));
                                Thread.sleep(100);
                                btnGenerate.setBackground(colButton);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }

                        @Override
                        public void onChordChange(String chord) {
                            lblKey.setText("Key: " + chord);
                        }
                    });
                    
                    generator.start();
                    lblTempo.setText(generator.getTempo() + "bpm");
                    btnStop.setEnabled(true);
                    btnGenerate.setEnabled(false);
                }
            }
        });
        btnStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generator.stop();
                btnStop.setEnabled(false);
                btnGenerate.setEnabled(true);
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

