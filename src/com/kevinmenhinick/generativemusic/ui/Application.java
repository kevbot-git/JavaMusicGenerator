package com.kevinmenhinick.generativemusic.ui;

import com.kevinmenhinick.generativemusic.BeatAction;
import com.kevinmenhinick.generativemusic.Generator;
import com.kevinmenhinick.generativemusic.exception.GeneratorException;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.EmptyBorder;

public class Application extends javax.swing.JFrame {

    private static Generator generator;
    
    private static JButton btnGenerate;
    private static JButton btnStop;
    
    private static JLabel lblHelp;
    private static JComboBox<MidiDevice.Info> selOutput; 
    private static JPanel pnlTempo;
    private static JLabel lblTempo;
    private static JPanel pnlKey;
    private static JLabel lblKey;
    
    private static JPanel pnlMain;
    private static JPanel pnlBottom;
    private static JPanel pnlTop;
    
    private static ArrayList<MidiDevice.Info> devices;
    
    private static Color colButton = new Color(240, 240, 250);

    public Application() {
        super("Java Music Generator by Kevin Menhinick");
        
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        
        devices = new ArrayList<MidiDevice.Info>(Arrays.asList(MidiSystem.getMidiDeviceInfo()));
        for (int i = 0; i < devices.size(); i++) {

            try {
                MidiDevice receiverDevice = MidiSystem.getMidiDevice(devices.get(i));
                receiverDevice.open();
                Receiver receiver = receiverDevice.getReceiver();
                if (receiver == null)
                    throw new GeneratorException();
            } catch (MidiUnavailableException | GeneratorException e) {
                devices.remove(devices.get(i));
            }
        }
        MidiDevice.Info[] arr = new MidiDevice.Info[devices.size()];
        devices.toArray(arr);
        selOutput = new JComboBox<MidiDevice.Info>(arr);
        
        try {
            generator = new Generator((MidiDevice.Info) selOutput.getSelectedItem());
        } catch(GeneratorException e) {
            System.exit(0);
        }
        
        initComponents();
        addComponents();
        setListeners();
    }

    @SuppressWarnings("unchecked")                         
    private void initComponents() {

        super.setSize(480, 320);
        super.setResizable(false);
        super.setLocation(getCenterPoint(this));

        pnlMain = new JPanel(new BorderLayout());
        pnlMain.setBorder(new EmptyBorder(8, 8, 8, 8));
        pnlBottom = new JPanel(new GridLayout(1, 3, 8, 8));
        pnlTop = new JPanel(new GridLayout(4, 1, 8, 8));
        pnlTop.setBorder(new EmptyBorder(8, 8, 8, 8));
        
        lblHelp = new JLabel("<html><font color=\"#0000CF\"><u>Need Help?</u></font></html>");
        lblHelp.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        for (MidiDevice.Info i: devices) {
            if (i.getDescription().equals("External MIDI Port")) {
                selOutput.setSelectedItem(i);
            }
        }
        
        pnlTempo = new JPanel();
        lblTempo = new JLabel("0bpm");
        lblTempo.setFont(lblTempo.getFont().deriveFont(24f));
        pnlKey = new JPanel();
        lblKey = new JLabel("Key: -");
        lblKey.setFont(lblKey.getFont().deriveFont(24f));
        
        btnGenerate = new JButton("Start generating");
        btnStop = new JButton("Stop");
        btnGenerate.setBackground(colButton);
        btnStop.setBackground(colButton);
        btnStop.setEnabled(false);
    }
    
    private void addComponents() {
        
        pnlTop.add(new JLabel(""));
        
        pnlTempo.add(lblTempo);
        pnlTop.add(pnlTempo);
        
        pnlKey.add(lblKey);
        pnlTop.add(pnlKey);

        pnlTop.add(selOutput);
        
        pnlBottom.add(lblHelp);
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
        
        lblHelp.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI("https://github.com/kevbot-git/JavaMusicGenerator#usage"));
                } catch (IOException | URISyntaxException ex) { }
            }
        });
        
        btnGenerate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!generator.isPlaying()) {
                    try {
                        generator = new Generator((MidiDevice.Info) selOutput.getSelectedItem());
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
                    lblKey.setText("Key: " + generator.getKey());
                    btnStop.setEnabled(true);
                    btnGenerate.setEnabled(false);
                    selOutput.setEnabled(false);
                }
            }
        });
        btnStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generator.stop();
                btnStop.setEnabled(false);
                btnGenerate.setEnabled(true);
                selOutput.setEnabled(true);
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

