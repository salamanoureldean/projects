import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.Color.*;
import java.net.URL;
import java.applet.AudioClip;
import java.applet.Applet;

/**
 * A class modelling a tic-tac-toe (noughts and crosses, Xs and Os) game.
 * 
 * @author Salama Noureldean 101154365
 * @version 1.0, April 8, 2023
 */

public class TicTacToe extends MouseAdapter implements ActionListener, MouseListener
{
   public static final String PLAYER_X = "X"; // player using "X"
   
   public static final String PLAYER_O = "O"; // player using "O"
   
   public static final String EMPTY = " "; // empty cell
   
   // records the last player (either player x or o) to display on the status bar in case one of them won
   private String lastPlayer;
   
   // display current status of the game
   private JLabel statusDisplay;
   
   // display current scores of the game
   private JLabel scoreDisplay;
     
   // display current status of the game
   private JLabel gameStatus;
       
   // panel for the buttons
   private JPanel buttonPanel;
   
   // buttons for the board
   private JButton[][] board;
   
   // new item in the menubar
   private JMenuItem newItem;
   
   // quit item in the menubar
   private JMenuItem quitItem;
   
   // reset item in the menubar
   private JMenuItem resetStatsItem;
   
   // current player (PLAYER_X or PLAYER_O)
   private String player;   

   // number of squares still free
   private int numFreeSquares; 
   
   private AudioClip click;
   
   // x win count
   private int xWinCount;
   
   // o win count
   private int oWinCount;
   
   // tie count
   private int tieCount;
   
   /** 
    * Constructs a new Tic-Tac-Toe board GUI
    */
   public TicTacToe()
   {
      JFrame frame = new JFrame("TIC-TAC-TOE"); // create new frame titles tictactoe
      Container contentPane = frame.getContentPane(); // get the content pane so we can put stuff in
      
      frame.setPreferredSize (new Dimension (600, 600));
      frame.pack();
      frame.setResizable(true); 
      frame.setVisible(true);
      frame.pack();
      
      // create the grid button arrangement:
      board = new JButton[3][3];//create 3x3 grid of JButtons
      buttonPanel = new JPanel();
      
      // arrange the buttons in a 3x3 grid layout in the JPanel
      buttonPanel.setLayout(new GridLayout(3, 3));
      for(int i = 0; i < 3; i++){
          for(int j = 0; j < 3; j++){
              board[i][j] = new JButton();
              board[i][j].setFont(new Font("Comic Sans MS", Font.BOLD, 30));
              board[i][j].setBackground(Color.yellow);
              board[i][j].addActionListener(this);

              buttonPanel.add(board[i][j]);
              
          }
      }
      // add the game board to the middle of the frame, between the menu and the status field
      contentPane.add(buttonPanel, BorderLayout.CENTER);
      frame.pack();
      
      // create textfield at the bottom of the frame
      statusDisplay = new JLabel();

      statusDisplay.setFont(new Font("Comic Sans MS", Font.BOLD, 18)); // bold 18pt font
      statusDisplay.setHorizontalAlignment(JLabel.LEFT); // left justified
      
      scoreDisplay = new JLabel();
      scoreDisplay.setFont(new Font("Comic Sans MS", Font.BOLD, 18)); // bold 18pt font
      scoreDisplay.setHorizontalAlignment(JLabel.LEFT); // left justified
      
      scoreDisplay.setText("Statistics: X wins: " + xWinCount + "    O wins: " + oWinCount + "     Ties: " + tieCount); 
      contentPane.add(scoreDisplay, BorderLayout.NORTH); //add on the north side
      
      // update to reflect the initial state of the model
      statusDisplay.setText(EMPTY); 
      contentPane.add(statusDisplay, BorderLayout.SOUTH); //add on the south side
      
      //creating the menu bar
      JMenuBar menuBar = new JMenuBar();
      frame.setJMenuBar(menuBar);
      
      // creating the Game option to put in the menubar
      JMenu gameMenu = new JMenu("Game");
      menuBar.add(gameMenu);
      gameMenu.addMouseListener(this);
      
      // creating the menu item to put in New in menu
      newItem = new JMenuItem("New");
      gameMenu.add(newItem);
      
      // creating the menu item to put in Quit in menu
      quitItem = new JMenuItem("Quit");
      gameMenu.add(quitItem);
      
      // creating the menu item to put in Reset in menu
      resetStatsItem = new JMenuItem("Reset statistics");
      gameMenu.add(resetStatsItem);
      
      
      // this portion allows us to use shortcuts for the menu items (Ctrl-N, Ctrl-Q, and Ctrl-R)
      final int SHORTCUT_MASK = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();
      newItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, SHORTCUT_MASK));
      quitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, SHORTCUT_MASK));
      resetStatsItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, SHORTCUT_MASK));
      
      // listen for menu selections
      newItem.addActionListener(this);
      quitItem.addActionListener(this);
      resetStatsItem.addActionListener(this);
      
      
      frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
      
      frame.pack();  
      
      // call clearboard() to start a new game with an empty board
      clearBoard();
   }
   
   /** This action listener is called when the user clicks on 
    * any of the GUI's buttons. 
    * @param e
    */
   public void actionPerformed(ActionEvent e)
   {
       // get the action
       Object o = e.getSource();
       
       // see if its a JButton
       if(o instanceof JButton){
           // cast to a JButton
           JButton button = (JButton) o;
           
           // start checking each button on the board
           for(int i = 0; i < 3; i++){
               for(int j = 0; j < 3; j++){
                   if(button == board[i][j]){ //if button is equal to the action button:
                           if(player.equals(PLAYER_X) && board[i][j].getText() == EMPTY){ 
                               board[i][j].setText(PLAYER_X); // set the button to X
                               player = PLAYER_O; // give the turn to O 
                               lastPlayer = PLAYER_X; // set last player to the player who played last (player x here)
                               statusDisplay.setText("Game in Progress: " + PLAYER_O + "'s turn"); // display this message as the status 
                               board[i][j].setEnabled(false); // disable the button after pressing 
                               // decrement number of free squares
                               numFreeSquares--;
                               
                               // play sound on click
                               URL urlClick = TicTacToe.class.getResource("coin-collect-retro-8-bit-sound-effect-145251.wav"); 
                               click = Applet.newAudioClip(urlClick);
                               click.play(); // plays sound once
                            }
                            
                            // if the player whos turn it is is player o:
                           else if(player.equals(PLAYER_O) && board[i][j].getText()== EMPTY){ 
                               board[i][j].setText(PLAYER_O); // set the button to O
                               player = PLAYER_X; // give the turn to x
                               lastPlayer = PLAYER_O; // set last player to the player who played last (player x here)
                               statusDisplay.setText("Game in Progress: " + PLAYER_X + "'s turn"); // display this message as the status
                               board[i][j].setEnabled(false); // disable the button after pressing
                               // decrement number of free squares
                               numFreeSquares--;
                               
                               // play sound on click
                               URL urlClick = TicTacToe.class.getResource("osound.wav"); 
                               click = Applet.newAudioClip(urlClick);
                               click.play(); // plays sound once
                            }
                            
                           // check if we have a winner, and if so, display the last player to have played the turn as the winner
                           if(haveWinner() == true){
                               for(int k = 0; k < 3; k++){
                                   for(int l = 0; l < 3; l++){
                                       // disable all buttons
                                       board[k][l].setEnabled(false);
                                   }
                               }
                               
                               statusDisplay.setText("Game over, " + lastPlayer + " wins!");
                               
                               // if x wins increase x win count
                               if(lastPlayer == PLAYER_X){
                                   xWinCount++;// increase the amount of x wins in the statistics
                                   scoreDisplay.setText("Statistics: X wins: " + xWinCount + "    O wins: " + oWinCount + "     Ties: " + tieCount); // update the statistics 
                                   URL urlClick = TicTacToe.class.getResource("mixkit-arcade-bonus-alert-767.wav"); // win sound
                                   click = Applet.newAudioClip(urlClick);
                                   click.play(); // plays sound once
                               }
                               else if(lastPlayer == PLAYER_O){
                                   oWinCount++;
                                   scoreDisplay.setText("Statistics: X wins: " + xWinCount + "    O wins: " + (oWinCount) + "     Ties: " + tieCount); 
                                   URL urlClick = TicTacToe.class.getResource("mixkit-arcade-bonus-alert-767.wav"); // win sound
                                   click = Applet.newAudioClip(urlClick);
                                   click.play(); // plays sound once
                               }
                           }
                           
                           // if there are no more free squares and there is no winner, then it is a tie
                           if(numFreeSquares == 0 && haveWinner() == false){
                               // disable all buttons
                               for(int k = 0; k < 3; k++){
                                   for(int l = 0; l < 3; l++){
                                       board[k][l].setEnabled(false);
                                       board[k][l].setBackground(Color.red);
                                    }
                               }
                               statusDisplay.setText("Game over, it's a tie");
                               
                               URL urlClick = TicTacToe.class.getResource("game-over-arcade-6435.wav"); // tie sound
                               click = Applet.newAudioClip(urlClick);
                               click.play(); // plays sound once
                               
                               // increase tie count
                               tieCount++;
                               scoreDisplay.setText("Statistics: X wins: " + xWinCount + "    O wins: " + oWinCount + "     Ties: " + tieCount); // update the statistics
                           }
                   }  
               }
           }
       }
        
       // if one of the menu items is clicked: 
       if(o instanceof JMenuItem){    
           JMenuItem item = (JMenuItem) o;
           
           // if item clicked is new
           if(item == newItem){
               clearBoard();
           }
           // if item clicked is quit
           else if(item == quitItem){
               System.exit(0);
           }
           
           // if item clicked is reset statistics
           else if(item == resetStatsItem){
               xWinCount = 0;
               oWinCount = 0; 
               tieCount = 0;
               scoreDisplay.setText("Statistics: X wins: " + xWinCount + "    O wins: " + oWinCount + "     Ties: " + tieCount);
            
           }
       }
   }

   /**
    * Sets everything up for a new game.  Marks all squares in the Tic Tac Toe board as empty,
    * and indicates no winner yet, 9 free squares and the current player is player X.
    */
   private void clearBoard()
   {
      for(int i = 0; i < 3; i++){
         for(int j = 0; j < 3; j++){
            board[i][j].setText(EMPTY);
            // enable all buttons
            board[i][j].setEnabled(true);
            // set their backgrounds to yellow
            board[i][j].setBackground(Color.yellow);
         }
      }
      
      // update to reflect the initial state of the model, x being first
      statusDisplay.setText("Game in Progress: " + PLAYER_X + "'s turn");
      numFreeSquares = 9;
      player = PLAYER_X; // player X always has the first turn.
   }
   
   /**
    * Detects when the mouse enters the item game.
    * @param e
    */
   public void mouseEntered(MouseEvent e){
       JMenu item = (JMenu) e.getSource();
       item.setSelected(true); // highlight the menu item
   }
   
   /**
    * Detects when the mouse leaves the item game.
    * @param e
    */
   public void mouseExited(MouseEvent e){
       JMenu item = (JMenu) e.getSource();
       item.setSelected(false); //stop highlighting the menu item
   }

   /**
    * Returns true if filling the square gives a winning combination of squares, and false if there is no winning combination.
    * 
    * @return true if we have a winner, false if there is no winner.
    */
   private boolean haveWinner() 
   {
        // we don't go any further if we have less than 4 squares free
 
        if (numFreeSquares > 4){
            return false;
        }

        // check the rows for if they match        
        if (!board[0][0].getText().equals(EMPTY) && board[0][0].getText().equals(board[0][1].getText()) && board[0][0].getText().equals(board[0][2].getText())){
            // set the winning combination to green
            board[0][0].setBackground(Color.green);
            board[0][1].setBackground(Color.green);
            board[0][2].setBackground(Color.green);
            return true;
        } 
        else if (!board[1][0].getText().equals(EMPTY) && board[1][0].getText().equals(board[1][1].getText()) && board[1][0].getText().equals(board[1][2].getText())){
            // set the winning combination to green
            board[1][0].setBackground(Color.green);
            board[1][1].setBackground(Color.green);
            board[1][2].setBackground(Color.green);
            return true;
        } 
        else if (!board[2][0].getText().equals(EMPTY) && board[2][0].getText().equals(board[2][1].getText()) && board[2][0].getText().equals(board[2][2].getText())){
            // set the winning combination to green
            board[2][0].setBackground(Color.green);
            board[2][1].setBackground(Color.green);
            board[2][2].setBackground(Color.green);
            return true;
        }
        
        // check the columns for if they match
        else if (!board[0][0].getText().equals(EMPTY) && board[0][0].getText().equals(board[1][0].getText()) && board[0][0].getText().equals(board[2][0].getText())){
            // set the winning combination to green
            board[0][0].setBackground(Color.green);
            board[1][0].setBackground(Color.green);
            board[2][0].setBackground(Color.green);
            return true;
        } 
        else if (!board[0][1].getText().equals(EMPTY) && board[0][1].getText().equals(board[1][1].getText()) && board[0][1].getText().equals(board[2][1].getText())){
            // set the winning combination to green
            board[0][1].setBackground(Color.green);
            board[1][1].setBackground(Color.green);
            board[2][1].setBackground(Color.green);
            return true;
        } 
        else if (!board[0][2].getText().equals(EMPTY) && board[0][2].getText().equals(board[1][2].getText()) && board[0][2].getText().equals(board[2][2].getText())){
            // set the winning combination to green
            board[0][2].setBackground(Color.green);
            board[1][2].setBackground(Color.green);
            board[2][2].setBackground(Color.green);
            return true;
        }
        
        //check the diagonals for if they match
        else if (!board[0][0].getText().equals(EMPTY) && board[0][0].getText().equals(board[1][1].getText()) && board[0][0].getText().equals(board[2][2].getText())){
            // set the winning combination to green
            board[0][0].setBackground(Color.green);
            board[1][1].setBackground(Color.green);
            board[2][2].setBackground(Color.green);
            return true;
        } 
        else if (!board[0][2].getText().equals(EMPTY) && board[0][2].getText().equals(board[1][1].getText()) && board[0][2].getText().equals(board[2][0].getText())){
            // set the winning combination to green
            board[0][2].setBackground(Color.green);
            board[1][1].setBackground(Color.green);
            board[2][0].setBackground(Color.green);
            return true;
        }

        // no winner
        return false;
   }
}


