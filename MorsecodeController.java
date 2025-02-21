// this classs will handle the logic for our GUI

import java.util.HashMap;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class MorsecodeController {
    // we will use the hashMap to translate the user input to morse code output
    private HashMap<Character, String> morsecodeMap;
    private HashMap<String, Character> reversemorseMap;

    public MorsecodeController() {
        morsecodeMap = new HashMap<>();
        reversemorseMap = new HashMap<>();
        String[] morse = { ".-", "-...", "-.-.", "-..", ".", "..-.", "--.", "....", "..", ".---",
                "-.-", ".-..", "--", "-.", "---", ".--.", "--.-", ".-.", "...", "-",
                "..-", "...-", ".--", "-..-", "-.--", "--.." };

        char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        char small;
        for (int i = 0; i < alphabet.length; i++) {
            small = (char) (alphabet[i] + 32);
            morsecodeMap.put(alphabet[i], morse[i]);
            morsecodeMap.put(small, morse[i]);
            reversemorseMap.put(morse[i], alphabet[i]);
        }

        // Adding numbers (0-9)
        String[] numbers = { "-----", ".----", "..---", "...--", "....-", ".....", "-....", "--...", "---..", "----." };
        for (int i = 0; i < numbers.length; i++) {
            morsecodeMap.put((char) ('0' + i), numbers[i]);
            reversemorseMap.put(numbers[i], (char) ('0' + i));
        }

        // Special characters in Morse code
        morsecodeMap.put('.', ".-.-.-"); // Period
        morsecodeMap.put(',', "--..--"); // Comma
        morsecodeMap.put('?', "..--.."); // Question mark
        morsecodeMap.put('!', "-.-.--"); // Exclamation mark
        morsecodeMap.put(':', "---..."); // Colon
        morsecodeMap.put(';', "-.-.-."); // Semicolon
        morsecodeMap.put('(', "-.--."); // Left parenthesis
        morsecodeMap.put(')', "-.--.-"); // Right parenthesis
        morsecodeMap.put('&', ".-..."); // Ampersand
        morsecodeMap.put('@', ".--.-."); // At symbol
        morsecodeMap.put('=', "-...-"); // Equals sign
        morsecodeMap.put('+', ".-.-."); // Plus sign
        morsecodeMap.put('-', "-....-"); // Hyphen
        morsecodeMap.put('_', "..--.-"); // Underscore
        morsecodeMap.put('"', ".-..-."); // Double quote
        morsecodeMap.put('$', "...-..-"); // Dollar sign
        morsecodeMap.put('\'', ".----."); // Apostrophe
        morsecodeMap.put('/', "-..-."); // Slash
        morsecodeMap.put(' ', "     "); // Space
        morsecodeMap.put('\n', "/"); // new line feed

    }

    public String TranslateToMorseCode(String text)
    {
        StringBuilder translatedText = new StringBuilder();
        for (Character letter : text.toCharArray()) 
        {
            // translate the letter and append the string to the returning value(to be dispalyed to the GUI)
            translatedText.append(morsecodeMap.get(letter)+" ");    
        }
        return translatedText.toString().trim();
    }
    // public String TranslateToText(String text)
    // {
    //     StringBuilder translatedText = new StringBuilder();
    //     String words[] = text.split(" / ");
    //     for (String word : words) 
    //     {
    //         for (String letter : word.split(" ")) {
    //             if (reversemorseMap.containsKey(letter)) {
    //                 translatedText.append(reversemorseMap.get(letter));
    //             }
    //         }
    //         translatedText.append(" "); 
    //     }
    //     return translatedText.toString().trim();
    // }

    public void PlaySound(String morsemessage) throws LineUnavailableException, InterruptedException{
        // audio fomat specifies the audio properties i.e. (the type of the audio we want)
        AudioFormat audioFormat = new AudioFormat(44100,16,1,true,false);

        // creates the dataline to play the incoming sound
        DataLine.Info datalineInfo = new DataLine.Info(SourceDataLine.class, audioFormat);
        SourceDataLine sourceDataLine = (SourceDataLine) AudioSystem.getLine(datalineInfo);
        sourceDataLine.open(audioFormat);
        sourceDataLine.start();

        // duration of the sound to be played
        int dotDuration = 200;
        int dashDuration = (int) 1.5* dotDuration;
        int slashDuration = (int) 2* dashDuration;

        for(char c: morsemessage.toCharArray() ){
            if(c=='.'){
                playBeep(sourceDataLine,dotDuration);
                Thread.sleep(dotDuration);
            }
            else if (c=='-') {
                playBeep(sourceDataLine,dotDuration);
                Thread.sleep(dashDuration);
            }
            else if(c=='/'){
                Thread.sleep(slashDuration);
            }
            // waits a bit before playing the next sequence 
            Thread.sleep(dotDuration);
        }
        //close audio output line(ie cleans the resources)
        sourceDataLine.drain();
        sourceDataLine.stop();
        sourceDataLine.close();

    }

    //sends audio data to be played to the data line
    private void playBeep(SourceDataLine line , int duration)
    {
        // create audio data
        byte data[] = new byte[duration * 44100/1000];

        for (int i = 0; i < data.length; i++) {
            // claculates the angle of the sine wave for the current sample  based o the sample rate and frequency
            double angle = i/(44100.0/440) * 2 * Math.PI;

            // calculates the aplitude of the sine wave and scale it to fit within the range of a signed byte (-128 to 127)
            // also in context of audio processing , asigned byte is often used to represent audio data because it can
            // represent both positve and negative amplitudes of the sound waves
            data[i]=(byte) (Math.sin(angle)*127);
        }

        // write the audio data in the data line to be palyed 
        line.write(data, 0, data.length);
    }
}