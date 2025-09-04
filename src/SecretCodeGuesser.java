public class SecretCodeGuesser {
    private static final char[] LETTERS = {'B', 'A', 'C', 'X', 'I', 'U'};
    int counter = 0;

    public void start() {
        SecretCode sc = new SecretCode();
        // Step 1: Find the length of the secret code
        int length = 1;
        while (true) {
            // Fill the guess string with 'B' using a loop
            char[] guessArr1 = new char[length];
            for (int i = 0; i < length; i++) {
                guessArr1[i] = 'B';
            }
            String guessStr = new String(guessArr1);
            int res = sc.guess(guessStr);
            counter++;
            if (res != -2) {
                if (res == length) {
                    System.out.println("Secret code: " + guessStr);
                    return; // stop, already solved
                }
                break; // correct length found, continue to Step 2
            }
            length++;
        }

        // Step 2: Determine frequency of each letter
        CustomHashMap remainingLetters = new CustomHashMap(6);
        int totalAssigned = 0;

        // Go through each letter and guess its frequency
        for (int i = 0; i < LETTERS.length; i++) {
            char letter = LETTERS[i];
            if (i < LETTERS.length - 1) { // first 5 letters → use guess()
                char[] guessArr = new char[length];
                // Fill the guess array with the current letter using a loop
                for (int j = 0; j < length; j++) {
                    guessArr[j] = letter;
                }
                int matches = sc.guess(new String(guessArr));
                counter++;
                // Put the letter and its matches in the map
                remainingLetters.put(letter, matches);
                totalAssigned += matches;
            } else { // last letter → deduce without guessing
                int remaining = length - totalAssigned;
                remainingLetters.put(letter, remaining);
                if (remaining == length) { // edge case: all 'U'
                    char[] allU = new char[length];
                    for (int j = 0; j < length; j++) {
                        allU[j] = 'U';
                    }
                    System.out.println("Secret code: " + new String(allU));
                    System.out.println("Number of guesses: " + counter);
                    return; // stop early, solved
                }
            }
        }

        // Step 3: Start with the most frequent letter
        char mostFreqLetter = 'B';
        int maxCount = -1;
        for (int i = 0; i < LETTERS.length; i++) {
            char c = LETTERS[i];
            int count = remainingLetters.get(c);
            if (count > maxCount) {
                maxCount = count;
                mostFreqLetter = c;
            }
        }

        char[] code = new char[length];
        // Fill the code array with the most frequent letter using a loop
        for (int i = 0; i < code.length; i++) {
            code[i] = mostFreqLetter;
        }
        int currentMatches = maxCount;

        // Reduce quota for the most frequent letter
        remainingLetters.put(mostFreqLetter, 0);

        // Step 4: Assign remaining letters to correct positions
        for (int pos = 0; pos < length; pos++) {
            for (int i = 0; i < LETTERS.length; i++) {
                char ch = LETTERS[i];
                int quota = remainingLetters.get(ch);
                if (quota <= 0) continue;

                char[] tempGuess = new char[length];
                // clone manually
                for (int j = 0; j < length; j++) {
                    tempGuess[j] = code[j];
                }
                tempGuess[pos] = ch;
                int feedback = sc.guess(new String(tempGuess));
                counter++;

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

// Development suppport by ChatGPT(model GPT-4 August 2025 version)