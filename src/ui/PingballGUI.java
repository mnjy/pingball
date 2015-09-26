package ui;

import implementation.Board;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import client.Pingball;

import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.List;

import boardparser.BoardParser;
import ui.GameGUI;


/**
 * PingballGUI is a user interface for a client to play different versions of the Pingball
 * Game. A user can choose a file for what game to play, play/pause the game, connect to a 
 * server at a specified port and host, change the ball display to have floating heads, and
 * use keyboard controls for specific objects.
 * 
 */
public class PingballGUI extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L; //default, required by Java
    private final JButton playPauseButton;
    private final JButton restartButton;
    private JButton disconnectButton;
    private final JButton loadFileButton;
    private final JButton exitButton;
    private boolean isPaused=true;   
    private final ImageIcon playPause = new ImageIcon(getClass().getResource("/resources/pauseplay.png"));
    private final JPanel panel;
    private final JDialog serverConnection= new JDialog();
    private final JPanel serverConnectionPanel=new JPanel();
    private JTextField port=new JTextField("10987");
    private JTextField host= new JTextField("localhost");
    private final JButton okButton;
    private final JFileChooser boardfileChooser;
    private final JButton headsButton;
    private final JButton discoButton;
    private final JTextArea keyCommands;
    
    private static final int DEFAULT_GAMEBOARD_SIZE = 410; //in pixels

    private final Pingball pingball;
    private int gameboardSize = DEFAULT_GAMEBOARD_SIZE; //in pixels
    private final GameGUI gameboard;
    private final Timer timer;
    private File boardfile;
    
    /**
     * Initializes a pingball gui with a pingball client
     * @param pingball the client for the game
     */
    public PingballGUI(Pingball pingball){
        this.pingball = pingball;
        
        headsButton=new JButton("Heads?");
        headsButton.setName("headsButton");
        headsButton.addActionListener(new ActionListener() {
            /**
             * Changes ball representation to and from miller's and goldman's heads
             */
            @Override
            public synchronized void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        if (gameboard.getHeads()){
                            gameboard.setHeads(false);
                        }else{
                            gameboard.setHeads(true);   
                        }
                        gameboard.requestFocusInWindow();
                    }
                });
            }

        });
        
        discoButton=new JButton("Disco!");
        discoButton.setName("discoButton");
        discoButton.addActionListener(new ActionListener() {
            /**
             * Changes the background of the game to flash different colors
             * or resets back to white background
             */
            @Override
            public synchronized void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        if (gameboard.getDisco()){
                            gameboard.setDisco(false);
                        }else{
                            gameboard.setDisco(true);   
                        }
                        gameboard.requestFocusInWindow();
                    }
                });
            }

        });
        
        exitButton= new JButton("Exit");
        exitButton.setName("exitButton");
        exitButton.addActionListener(new ActionListener() {
            /**
             * Exits the server, client, and window
             */
            @Override
            public synchronized void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        setVisible(false);
                        System.exit(0);
                    }
                });
            }

        });
        
        // connect popup
        serverConnectionPanel.add(new JLabel("Host Name:"));
        serverConnectionPanel.add(host);
        serverConnectionPanel.add(new JLabel("Port Number:"));
        serverConnectionPanel.add(port);
        okButton= new JButton("OK");
        okButton.setName("okButton");
        serverConnectionPanel.add(okButton);
        serverConnection.add(serverConnectionPanel);
        serverConnection.pack();
        
        // connect/disconnect button
        disconnectButton=new JButton("Connect");
        if (pingball.isConnected()){disconnectButton.setText("Disconnect");}
        disconnectButton.setName("disconnectButton");
        disconnectButton.addActionListener(new ActionListener() {
            /**
             * When connect is hit, a server connection window opens to select port and host
             * When disconnect is hit, the game is removed from the server 
             */
            @Override
            public synchronized void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                          if (!pingball.isConnected()){
                              serverConnection.setVisible(true);                              
                          }
                          else{
                              disconnectFromServer();
                          }
                          gameboard.requestFocusInWindow();
                    }
                });
            }

        });
        
        okButton.addActionListener(new ActionListener() {
            /**
             * Connects the game to the specified host and port
             */
            @Override
            public synchronized void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        serverConnection.setVisible(false);
                        try {
                            pingball.connectToClientAndServer(host.getText(), Integer.parseInt(port.getText()));
                            disconnectButton.setText("Disconnect");
                        } catch (NumberFormatException | InterruptedException | IOException e) {
                            e.printStackTrace();
                        }
                        gameboard.requestFocusInWindow();
                    }
                });
            }

        });
        
        
        // load file button (opens up a FileChooser)
        boardfileChooser = new JFileChooser("C:/Users/mnjy/Documents/Archive/Fall2014/6.005/pingball-phase3/src/resources");
        
        loadFileButton = new JButton("Load File");
        loadFileButton.setName("loadFileButton");
        loadFileButton.addActionListener(new ActionListener() {
            /**
             * Gets new board file from the user and resets the board game according to that file
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                gameboard.requestFocusInWindow();
                int result = boardfileChooser.showDialog(null, "Load board file");
                
                if (result == JFileChooser.CANCEL_OPTION){return;}
                
                boardfile = boardfileChooser.getSelectedFile();
                disconnectFromServer();
                setBoardFromFile(boardfile);
                
                //personal preferences -- we will pause the game and turn special effects off
                pause();
                gameboard.setDisco(false);
                
            }
        });
        
        
        // restart button
        restartButton=new JButton("Restart");
        restartButton.setName("restartButton");
        restartButton.addActionListener(new ActionListener() {
            /**
             * Restarts the game and pauses
             */
            @Override
            public synchronized void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        restartGame();
                        pause();
                        gameboard.requestFocusInWindow();
                    }
                });
            }

        });
        
        // play/pause button
        playPauseButton= new JButton(playPause);
        playPauseButton.setName("playPauseButton");
        playPauseButton.addActionListener(new ActionListener() {
            /**
             * if game is not paused: gadgets and balls continue moving with previous velocity
             * if game is not paused: all gadgets and balls stop moving
             */
            @Override
            public synchronized void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                          if (isPaused){
                              play();
                          }
                          else{
                              pause();
                          }
                          gameboard.requestFocusInWindow();
                    }
                });
            }

        });
        
        // keyboard commands
        keyCommands = new JTextArea();
        keyCommands.setName("keyCommands");
        keyCommands.setEditable(false);

        
        // game board
        gameboard = new GameGUI(gameboardSize);
        
        gameboard.setPreferredSize(new Dimension(gameboardSize, gameboardSize));
        setBoard(pingball.board());
        int delay = gameboard.getDelay();
        timer = new Timer(delay, this);
        
        // make layout
        panel = new JPanel();
        makeLayout();
        this.pack();
        
        // set to exit on close
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // set colors
        panel.setBackground(Color.ORANGE);
        playPauseButton.setBackground(Color.WHITE);
        restartButton.setBackground(Color.GREEN);
        disconnectButton.setBackground(Color.YELLOW);
        loadFileButton.setBackground(Color.CYAN);
        exitButton.setBackground(Color.RED);
        keyCommands.setBackground(Color.ORANGE);
        
        gameboard.requestFocusInWindow();
    }
    
    /**
     * Makes the layout. Called in constructor.
     */
    private void makeLayout(){
        // instantiate layout
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);
        this.setContentPane(panel);

        // create gaps
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        // add components
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addComponent(playPauseButton)
                        .addComponent(restartButton)
                        .addComponent(disconnectButton)
                        .addComponent(loadFileButton)
                        .addComponent(exitButton))
                .addComponent(gameboard, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createSequentialGroup()
                        .addComponent(headsButton)
                        .addComponent(discoButton))
                .addComponent(keyCommands)
                );
                                
        layout.setVerticalGroup(
                layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(playPauseButton)
                        .addComponent(restartButton)
                        .addComponent(disconnectButton)
                        .addComponent(loadFileButton)
                        .addComponent(exitButton))
                        .addComponent(gameboard, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(headsButton)
                                .addComponent(discoButton))
                        .addComponent(keyCommands)
                );

    }
    
    
    /**
     * Pauses the game
     */
    private void pause(){
        isPaused = true;
        timer.stop();
    }
    
    /**
     * Plays the game. Resumes from the last paused place.
     */
    private void play(){
        isPaused = false;
        timer.start();
    }
    
    /**
     * Disconnects the client from the server and updates display accordingly 
     */
    private void disconnectFromServer(){
        pingball.disconnectFromClientAndServer();
        disconnectButton.setText("Connect");
    }
    
    /**
     * Sets the board to be played
     * @param board the board to be played
     */
    public void setBoard(Board board){
        pingball.changeBoard(board);
        gameboard.setBoard(board);
        gameboard.repaint();
       setKeyboardCommands(gameboard.getKeyListener().getCommands());
    }
    
    /**
     * Sets the board to be played
     * @param boardfile the file specifying board to be played
     */
    public void setBoardFromFile(File boardfile){
        BoardParser boardParser = new BoardParser();
        try {
            Board board = boardParser.getBoardFromFile(boardfile);
            setBoard(board);
        } catch (IOException e) {
            //do nothing
        }
    }
    
    /**
     * Sets the keyboard commands to be displayed in the gui
     * @param commands the commands to be displayed
     */
    public void setKeyboardCommands(String commands){
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                String text = "Keyboard controls are: "+commands;
                keyCommands.setText(text);
            }
        });
        
    }
    
    /**
     * Restarts the game from the initial state of the current board
     */
    public void restartGame(){
        setBoardFromFile(boardfile); //sets it to the most recently loaded boardfile
    }
    
    
    /**
     * Starts a game of pingball
     * @param pingball the client
     */
    public static void playPingball(Pingball pingball) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                PingballGUI main = new PingballGUI(pingball);
                main.setVisible(true);
            }
        });
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (pingball.isConnected()){ //read messages and all that jazz
            pingball.readMessages();
            gameboard.actionPerformed(e);
            List<String> messages = gameboard.getMessages();
            if (!messages.isEmpty()){
                pingball.sendMessages(messages);
            }
        }
        else { //just update board
            gameboard.actionPerformed(e);
        }
        
    }
}
