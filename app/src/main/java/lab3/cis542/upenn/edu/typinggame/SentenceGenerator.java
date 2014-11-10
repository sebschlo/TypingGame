package lab3.cis542.upenn.edu.typinggame;

import java.util.Random;

/**
 * Created by Sebastian on 11/9/14.
 */
public class SentenceGenerator {

    // CONSTRUCTORS
    SentenceGenerator() {
        r = new Random();
    }

    SentenceGenerator(long seed) {
        r = new Random(seed);
    }

    // CONSTANTS
    final int NO_WORDS_IN_LISTS = 5;
    final String SPACE = " ";
    final String PERIOD = ".";
    final String article[] = { "the", "a", "one", "some", "any" };
    final String noun[] = { "boy", "girl", "dog", "bug", "cat" };
    final String noun2[] = { "town", "car", "sponge", "rock", "building" };
    final String verb[] = { "drove", "jumped", "ran", "walked", "skipped" };
    final String preposition[] = { "to", "from", "over", "under", "on" };
    final String adjective[] = { "nice", "bold", "crazy", "wild", "dreary" };
    final String adverb[] = {"quickly", "slowly", "begrudgingly", "nicely", "willfully"};

    // Variables
    Random r;

    public String generate(int numWords) {
        // Initialize sentence with an article
        String sent = article[rand()];

        // Capitalize first letter
        sent = sent.substring(0,1).toUpperCase() + sent.substring(1);

        // Determine sentence structure based on how many words it should contain.
        switch (numWords) {
            case 4:
                sent += SPACE + noun[rand()] + " is " + adjective[rand()] + PERIOD;
                break;

            case 7:
                sent += SPACE + noun[rand()] +
                        SPACE + verb[rand()] +
                        SPACE + preposition[rand()] +
                        SPACE + article[rand()] +
                        SPACE + adjective[rand()] +
                        SPACE + noun[rand()] + PERIOD;
                break;

            case 10:
                sent += SPACE + noun[rand()] +
                        " and " + article[rand()] +
                        SPACE + noun[rand()] +
                        SPACE + verb[rand()] +
                        SPACE + preposition[rand()] +
                        SPACE + article[rand()] +
                        SPACE + noun[rand()] +
                        SPACE + adverb[rand()] + PERIOD;
                break;
        }

        return sent;
    }

    int rand() {
        int ri = r.nextInt() % NO_WORDS_IN_LISTS;
        if (ri < 0) {
            ri += NO_WORDS_IN_LISTS;
        }
        return ri;
    }

}
