package lab3.cis542.upenn.edu.typinggame;

import static java.lang.Math.abs;

/**
 * Created by Sebastian on 11/11/14.
 */
public class ScoreCalculator {


    private final String letters = "qwertyuiopasdfghjkl;zxcvbnm,. ";


    public int calculateScore(String sentence, long time) {

        int score = 0;
        char temp = Character.toLowerCase(sentence.charAt(0));

        for (int i=1; i<sentence.length(); i++) {
            int indexPast = letters.indexOf(temp) % 10;
            int indexCurr = letters.indexOf(sentence.charAt(i)) % 10;

            // the score calculates how far each letter is horizontally, meaning that if the word
            // you are typing is Ed, it will have a low score because the D is right below the E so
            // they are in the same horizontal position, resulting in a difference of 0. If the word
            // were Ap, it would have a high score, because the a is far away from the p. This
            // distance is multiplied by 10, meaning that the highest possible score is 90 a transition
            // between 2 letters. These are added, which means that a sentence with 10 letters could
            // potentially have a score of 900.
            score += abs(indexCurr-indexPast)*10;

            // update temp to current
            temp = sentence.charAt(i);
        }

        // 10,000 is divided by the time, meaning that if you take more than 10 seconds to
        // type the sentence your score due to time is essentially nothing. If you typed the sentence
        // in 5 seconds your score would be 2. This score is then multiplied by 300. meaning that if
        // you wrote the sentence in 10 seconds, you would get 300 points, if you wrote it in 3 sec,
        // you would get approximately 1000 points.
        score += 3000000/time;

        return score;
    }









}
