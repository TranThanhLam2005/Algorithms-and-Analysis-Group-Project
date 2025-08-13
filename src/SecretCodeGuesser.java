import java.util.Arrays;
import java.util.HashMap;

public class SecretCodeGuesser {
    private static final char[] LETTERS = {'B', 'A', 'C', 'X', 'I', 'U'};

    public void start() {
        SecretCode sc = new SecretCode();
        // Step 1: Find the length of the secret code
        int length = 1;
        while (true) {
            String guessStr = "B".repeat(length);
            int res = sc.guess(guessStr);
            if (res != -2) break; // -2 means guess length is wrong
            length++;
        }
        // Step 2: Determine frequency of each letter
        HashMap<Character, Integer> remainingLetters = new HashMap<>();
        for (char c : LETTERS) remainingLetters.put(c, 0);

        for (char letter : LETTERS) {
            char[] guessArr = new char[length];
            Arrays.fill(guessArr, letter);
            int matches = sc.guess(new String(guessArr));
            remainingLetters.put(letter, matches);
        }

        // Step 3: Start with the most frequent letter
        char mostFreqLetter = 'B';
        int maxCount = -1;
        for (char c : LETTERS) {
            int count = remainingLetters.get(c);
            if (count > maxCount) {
                maxCount = count;
                mostFreqLetter = c;
            }
        }

        char[] code = new char[length];
        Arrays.fill(code, mostFreqLetter);
        int currentMatches = maxCount;

        // Reduce quota for the most frequent letter
        remainingLetters.put(mostFreqLetter, 0);

        // Step 4: Assign remaining letters to correct positions
        for (int pos = 0; pos < length; pos++) {
            for (char ch : LETTERS) {
                int quota = remainingLetters.get(ch);
                if (quota <= 0) continue;

                char[] tempGuess = code.clone();
                tempGuess[pos] = ch;
                int feedback = sc.guess(new String(tempGuess));

                if (feedback > currentMatches) {
                    code = tempGuess;
                    currentMatches = feedback;
                    remainingLetters.put(ch, quota - 1);
                    break; // move to next position
                } else if (feedback < currentMatches) {
                    break; // current letter is correct here
                }
            }
        }

        System.out.println("Secret code: " + new String(code));
    }
}
