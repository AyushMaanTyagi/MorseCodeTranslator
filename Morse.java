import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.sound.sampled.LineUnavailableException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

public class Morse extends JFrame implements KeyListener {
    // textarea - user input text to be converted
    // morsecodearea- converted text that appears
    JTextArea textarea;
    JTextArea morsecodearea;
    MorsecodeController morsecodeController;

    public Morse() {

        // sets the title on the bar of GUI
        super("Morse Code Translator");
        // sets the size of the frame to be 540X760
        setSize(new Dimension(540, 560));
        // prevents from being able to GUI
        setResizable(false);
        /*
         * setting the layout to be null allows us to manually psition and set the size
         * of the coponents in our GUI
         */
        setLayout(null);

        // exits the progarm ion closing
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // changes the color of the background
        getContentPane().setBackground(Color.decode("#ffffff"));

        // places the GUI in the center of the screen when run
        setLocationRelativeTo(null);

        morsecodeController = new MorsecodeController();

        addGUIComponent();
    }

    private void addGUIComponent() { // title Lable
        JLabel titleLabel = new JLabel("Morse Code Generator");

        // changes the font size and font label and the font weight
        titleLabel.setFont(new Font("Dialog", Font.BOLD, 32));

        // changes the font color of the text
        titleLabel.setForeground(Color.BLACK);

        // centers text relative to its container's width
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // sets the x,y position and the width and height dimensions to make
        // that the title aligns to the center of our GUI, we made it to the same width
        titleLabel.setBounds(0, 0, 540, 100);

        //
        JLabel textinputlabel = new JLabel("Enter Your Text");
        textinputlabel.setFont(new Font("Dialog", Font.BOLD, 16));
        textinputlabel.setForeground(Color.BLACK);
        textinputlabel.setBounds(20, 100, 200, 30);

        textarea = new JTextArea();
        textarea.setFont(new Font("Dialog", Font.PLAIN, 18));

        // simulates the border of 10px in the txt area
        textarea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        textarea.setForeground(Color.GRAY);

        // makes it so that words wrap to te next line after reaching the end of the
        // text area
        textarea.setLineWrap(true);

        // makes it so that when the words to get wrap, the words doesn't split off
        textarea.setWrapStyleWord(true);

        // makes the current (this) text area so that we are listening to key presses within this textarea.
        textarea.addKeyListener(this);

        // adds scrolling ability to input textarea
        JScrollPane textInputScroll = new JScrollPane(textarea);
        textInputScroll.setBounds(20, 132, 484, 136);

        // morse code Output
        JLabel morsecodeoutputLabel = new JLabel("Morse Code");
        morsecodeoutputLabel.setFont(new Font("Dialog", Font.BOLD, 16));
        morsecodeoutputLabel.setForeground(Color.BLACK);
        morsecodeoutputLabel.setBounds(20, 272, 200, 30);

        morsecodearea = new JTextArea();
        morsecodearea.setFont(new Font("Dialog", Font.PLAIN, 18));
        morsecodearea.setEditable(false);
        morsecodearea.setLineWrap(true);
        morsecodearea.setWrapStyleWord(true);
        morsecodearea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        // morsecodearea.addKeyListener(this);

        // adds scrolling ability to output morsecode textarea
        JScrollPane textouputScroll = new JScrollPane(morsecodearea);
        textouputScroll.setBounds(20, 304, 484, 136);

        // play a sound button
        JButton playSoundButton = new JButton("Play Sound");
        playSoundButton.setBounds(20,450,100,30);
        playSoundButton.addActionListener(_ -> {
            playSoundButton.setEnabled(false);
        
            Thread playSoundButtonThread = new Thread(() -> {
                try {
                    String msg = morsecodearea.getText().trim();
                    morsecodeController.PlaySound(msg);
                } catch (LineUnavailableException | InterruptedException ex) {
                    ex.printStackTrace();
                } finally {
                    playSoundButton.setEnabled(true);
                }
            });
        
            playSoundButtonThread.start();
        });
        


        // add GUI
        add(titleLabel);
        add(textinputlabel);
        add(textInputScroll);
        add(morsecodeoutputLabel);
        add(textouputScroll);
        add(playSoundButton);
    }


    @Override
    //this method is activated whenever the user releases the key after pressing it
    public void keyReleased(KeyEvent e) {
       if(e.getKeyCode()!=KeyEvent.VK_SHIFT)
       {
        String text2morse = textarea.getText();
        // update the morse code GUI with the translated text
        morsecodearea.setText(morsecodeController.TranslateToMorseCode(text2morse));

        // String morse2text = morsecodearea.getText();

        // // update the textarea with the morse code translated int text
        // textarea.setText(morsecodeController.TranslateToMorseCode(morse2text));
       }
    }

    @Override
    public void keyTyped(KeyEvent e) {
       
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

}