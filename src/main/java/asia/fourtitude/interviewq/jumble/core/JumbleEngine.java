package asia.fourtitude.interviewq.jumble.core;

import java.io.*;
import java.util.*;
import java.util.stream.*;

public class JumbleEngine {

    /**
     * From the input `word`, produces/generates a copy which has the same
     * letters, but in different ordering.
     *
     * Example: from "elephant" to "lehnaetp".
     *
     * Evaluation/Grading:
     * a) pass unit test: JumbleEngineTest#scramble()
     * b) scrambled letters/output must not be the same as input
     *
     * @param word  The input word to scramble the letters.
     * @return  The scrambled output/letters.
     */
    public String scramble(String word) {
    	if (word == null || word.length() <= 1) {
            return word;
        }

        Random random = new Random();
        char[] chars = word.toCharArray();
        String scrambled;

        do {
            // Fisher-Yates Shuffle
            for (int i = chars.length - 1; i > 0; i--) {
                int j = random.nextInt(i+1);

                char temp = chars[i];
                chars[i] = chars[j];
                chars[j] = temp;
            }

            scrambled = new String(chars);

        } while (scrambled.equals(word));

        return scrambled;
    }

    /**
     * Retrieves the palindrome words from the internal
     * word list/dictionary ("src/main/resources/words.txt").
     *
     * Word of single letter is not considered as valid palindrome word.
     *
     * Examples: "eye", "deed", "level".
     *
     * Evaluation/Grading:
     * a) able to access/use resource from classpath
     * b) using inbuilt Collections
     * c) using "try-with-resources" functionality/statement
     * d) pass unit test: JumbleEngineTest#palindrome()
     *
     * @return  The list of palindrome words found in system/engine.
     * @see https://www.google.com/search?q=palindrome+meaning
     */
    public Collection<String> retrievePalindromeWords() {
        List<String> palindromeWordsList = new ArrayList<>();
        
        try (
        		InputStream inputStream = getClass().getClassLoader().getResourceAsStream("words.txt");
        		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))
        ) {
            String line;
            
            while ((line = reader.readLine()) != null) {

            	line = line.trim();

                if (line.length() <= 1) {
                    continue;
                }

                // Use inbuilt StringBuilder to reverse
                String reversed = new StringBuilder(line).reverse().toString();

                if (line.equalsIgnoreCase(reversed)) {
                    palindromeWordsList.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return palindromeWordsList;
    }

    /**
     * Picks one word randomly from internal word list.
     *
     * Evaluation/Grading:
     * a) pass unit test: JumbleEngineTest#randomWord()
     * b) provide a good enough implementation, if not able to provide a fast lookup
     * c) bonus points, if able to implement a fast lookup/scheme
     *
     * @param length  The word picked, must of length.
     *                When length is null, then return random word of any length.
     * @return  One of the word (randomly) from word list.
     *          Or null if none matching.
     */
    public String pickOneRandomWord(Integer length) {
    	Map<Integer, List<String>> wordsByLengthMap = new HashMap<>();
        List<String> allWordsList = new ArrayList<>();
        List<String> targetList = new ArrayList<>();
        Random random = new Random();
        
    	try (
    			InputStream inputStream = getClass().getClassLoader().getResourceAsStream("words.txt");
    			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))
        ) {
    		String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isEmpty()) {
                	String lowerWord = line.trim().toLowerCase();
                	allWordsList.add(lowerWord);
                    // Group words by length, if the length doesn't exist, create a new ArrayList, then add the word
                    wordsByLengthMap.computeIfAbsent(lowerWord.length(), k -> new ArrayList<>()).add(lowerWord);
                }
            }
    	} catch (IOException e) {
            e.printStackTrace();
        }
    	
        if (length == null) {
        	// No given length, pick from the entire master pool
        	targetList = allWordsList;
        } else {
            // Grab the pre-filtered bucket of words matching the length
            targetList = wordsByLengthMap.get(length);
        }
        
    	if (targetList == null || targetList.isEmpty()) {
            return null;
        }
    	
    	// Pick a random index from the chosen list in O(1) time
        int randomIndex = random.nextInt(targetList.size());
        return targetList.get(randomIndex);
    }

    /**
     * Checks if the `word` exists in internal word list.
     * Matching is case insensitive.
     *
     * Evaluation/Grading:
     * a) pass related unit tests in "JumbleEngineTest"
     * b) provide a good enough implementation, if not able to provide a fast lookup
     * c) bonus points, if able to implement a fast lookup/scheme
     *
     * @param word  The input word to check.
     * @return  true if `word` exists in internal word list.
     */
    public boolean exists(String word) {
    	Set<String> wordSet = new HashSet<>();
    	
    	if (word == null) {
            return false;
        }
    	
    	try (
    			InputStream inputStream = getClass().getClassLoader().getResourceAsStream("words.txt");
    			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))
    	) {
    		String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isEmpty()) {
                	String lowerWord = line.trim().toLowerCase();
                    wordSet.add(lowerWord);
                }
            }
    	} catch (IOException e) {
            e.printStackTrace();
        }
    	
    	// Convert input to lower case to match our stored scheme, then look up in O(1) time
    	return wordSet.contains(word.trim().toLowerCase());
    }

    /**
     * Finds all the words from internal word list which begins with the
     * input `prefix`.
     * Matching is case insensitive.
     *
     * Invalid `prefix` (null, empty string, blank string, non letter) will
     * return empty list.
     *
     * Evaluation/Grading:
     * a) pass related unit tests in "JumbleEngineTest"
     * b) provide a good enough implementation, if not able to provide a fast lookup
     * c) bonus points, if able to implement a fast lookup/scheme
     *
     * @param prefix  The prefix to match.
     * @return  The list of words matching the prefix.
     */
    public Collection<String> wordsMatchingPrefix(String prefix) {
        List<String> allWordsList = new ArrayList<>();
        List<String> matchWordsList = new ArrayList<>();
    	
		try (
    			InputStream inputStream = getClass().getClassLoader().getResourceAsStream("words.txt");
    			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))
    	) {
    		String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isEmpty()) {
                	String lowerWord = line.trim().toLowerCase();
                	allWordsList.add(lowerWord);
                }
            }
    	} catch (IOException e) {
            e.printStackTrace();
        }
    	
		// Invalid prefix
        if (prefix == null ||
            prefix.trim().isEmpty() ||
            !prefix.matches("[a-zA-Z]+")) {
            return matchWordsList;
        }

        String lowerPrefix = prefix.toLowerCase();

        for (String word : allWordsList) {
            if (word.toLowerCase().startsWith(lowerPrefix)) {
            	matchWordsList.add(word);
            }
        }

        return matchWordsList;
    }

    /**
     * Finds all the words from internal word list that is matching
     * the searching criteria.
     *
     * `startChar` and `endChar` must be 'a' to 'z' only. And case insensitive.
     * `length`, if have value, must be positive integer (>= 1).
     *
     * Words are filtered using `startChar` and `endChar` first.
     * Then apply `length` on the result, to produce the final output.
     *
     * Must have at least one valid value out of 3 inputs
     * (`startChar`, `endChar`, `length`) to proceed with searching.
     * Otherwise, return empty list.
     *
     * Evaluation/Grading:
     * a) pass related unit tests in "JumbleEngineTest"
     * b) provide a good enough implementation, if not able to provide a fast lookup
     * c) bonus points, if able to implement a fast lookup/scheme
     *
     * @param startChar  The first character of the word to search for.
     * @param endChar    The last character of the word to match with.
     * @param length     The length of the word to match.
     * @return  The list of words matching the searching criteria.
     */
    public Collection<String> searchWords(Character startChar, Character endChar, Integer length) {
    	// Input Validation
        if (startChar == null && endChar == null && length == null) {
            return Collections.emptyList();
        }
        if (length != null && length < 1) {
            return Collections.emptyList();
        }
        if (startChar != null && !Character.isLetter(startChar)) {
            return Collections.emptyList();
        }
        if (endChar != null && !Character.isLetter(endChar)) {
            return Collections.emptyList();
        }
    	
    	Map<Character, Map<Character, List<String>>> matrixIndex = new HashMap<>();
    	Map<Integer, List<String>> wordsByLengthMap = new HashMap<>();
    	
    	try (
    			InputStream inputStream = getClass().getClassLoader().getResourceAsStream("words.txt");
    			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))
    	) {
    		String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isEmpty()) {
                	String lowerWord = line.trim().toLowerCase();
                	char start = lowerWord.charAt(0);
                    char end = lowerWord.charAt(lowerWord.length()-1);
                    
                    if (Character.isLetter(start) && Character.isLetter(end)) {
                    	// Group to 2D matrix: startChar -> endChar -> List of matching words
                        matrixIndex
                            .computeIfAbsent(start, k -> new HashMap<>())
                            .computeIfAbsent(end, k -> new ArrayList<>())
                            .add(lowerWord);
                    }
                    
                    // Group by length of word
                    wordsByLengthMap.computeIfAbsent(lowerWord.length(), k -> new ArrayList<>()).add(lowerWord);
                }
            }
    	} catch (IOException e) {
            e.printStackTrace();
        }
    	
    	Character lowerStart = (startChar != null) ? Character.toLowerCase(startChar) : null;
        Character lowerEnd = (endChar != null) ? Character.toLowerCase(endChar) : null;
        List<String> resultList = new ArrayList<>();
        
        if (lowerStart != null && lowerEnd != null) {
            // Both characters given. Narrow down to exact grid intersection.
            Map<Character, List<String>> endMap = matrixIndex.get(lowerStart);
            if (endMap != null) {
            	resultList = endMap.get(lowerEnd);
            }
        } else if (lowerStart != null) {
            // Only start character given: Combine all lists that with 'start'
            Map<Character, List<String>> endMap = matrixIndex.get(lowerStart);
            if (endMap != null) {
                for (List<String> words : endMap.values()) {
                	resultList.addAll(words);
                }
            }
        } else if (lowerEnd != null) {
            // Only end character given: Combine all lists that with 'end' across all starting letters
            for (Map<Character, List<String>> endMap : matrixIndex.values()) {
                List<String> words = endMap.get(lowerEnd);
                if (words != null) {
                	resultList.addAll(words);
                }
            }
        } else {
            // Only length given
        	resultList = wordsByLengthMap.get(length);
            return resultList;
        }
    	
        // None of the start or end match
        if (resultList == null || resultList.isEmpty()) {
        	return Collections.emptyList();
        }
        
        // Length filter to the character-narrowed results
        if (length != null) {
        	final int targetLength = length; // Required for stream lambda closure
            return resultList.stream().filter(word -> word.length()==targetLength).collect(Collectors.toList());
        }
        
        // If length is null, return the accumulated character pool directly
        return resultList;
    }

    /**
     * Generates all possible combinations of smaller/sub words using the
     * letters from input word.
     *
     * The `minLength` set the minimum length of sub word that is considered
     * as acceptable word.
     *
     * If length of input `word` is less than `minLength`, then return empty list.
     *
     * The sub words must exist in internal word list.
     *
     * Example: From "yellow" and `minLength` = 3, the output sub words:
     *     low, lowly, lye, ole, owe, owl, well, welly, woe, yell, yeow, yew, yowl
     *
     * Evaluation/Grading:
     * a) pass related unit tests in "JumbleEngineTest"
     * b) provide a good enough implementation, if not able to provide a fast lookup
     * c) bonus points, if able to implement a fast lookup/scheme
     *
     * @param word       The input word to use as base/seed.
     * @param minLength  The minimum length (inclusive) of sub words.
     *                   When zero, return empty list.
     *                   Default is 3.
     * @return  The list of sub words constructed from input `word`.
     */
    public Collection<String> generateSubWords(String word, Integer minLength) {
    	class CountsHelper {
    		int[] getCharCounts(String word) {
		    	// Generate an integer frequency array for a string, 
		    	// which can use to count the char frequency of given word
    			int[] counts = new int[26];
		        String lower = word.trim().toLowerCase();
		        for (int i = 0; i < lower.length(); i++) {
		            char ch = lower.charAt(i);
		            if (ch >= 'a' && ch <= 'z') {
		            	counts[ch - 'a']++;
		            }
		        }
		        return counts;
    		}
    	}
    	
    	// Resolve defaults and perform validation
    	int actualMinLength = (minLength == null) ? 3 : minLength;
    	if (word == null || word.isEmpty() || actualMinLength <= 0 || word.length() < actualMinLength) {
            return Collections.emptyList();
        }
    	
    	CountsHelper countsHelper = new CountsHelper();
    	String oriWord = word.trim().toLowerCase();
    	int oriLength = oriWord.length();
    	int[] oriCharCounts = countsHelper.getCharCounts(oriWord);
    	List<String> matchSubList = new ArrayList<>();
        
        try (
    			InputStream inputStream = getClass().getClassLoader().getResourceAsStream("words.txt");
    			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))
    	) {
    		String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isEmpty()) {
                	String lowerWord = line.trim().toLowerCase();
                	if (lowerWord.length() < actualMinLength || 
                			lowerWord.length() > oriLength || 
                			lowerWord.equalsIgnoreCase(oriWord)) 
                	{
                        continue;
                    }
                	
                	int[] targetCharCounts = countsHelper.getCharCounts(lowerWord);
                	boolean canFormSubWord = true;
                	// Highly optimized primitive array comparison loop
                	for (int i = 0; i < 26; i++) {
                        if (oriCharCounts[i] < targetCharCounts[i]) {
                        	canFormSubWord = false; // Base seed lacks enough copies of this letter
                            break;
                        }
                    }
                	if (canFormSubWord) {
                		matchSubList.add(lowerWord);
                	}
                }
            }
    	} catch (IOException e) {
            e.printStackTrace();
        }
        
        return matchSubList;
    }

    /**
     * Creates a game state with word to guess, scrambled letters, and
     * possible combinations of words.
     *
     * Word is of length 6 characters.
     * The minimum length of sub words is of length 3 characters.
     *
     * @param length     The length of selected word.
     *                   Expects >= 3.
     * @param minLength  The minimum length (inclusive) of sub words.
     *                   Expects positive integer.
     *                   Default is 3.
     * @return  The game state.
     */
    public GameState createGameState(Integer length, Integer minLength) {
        Objects.requireNonNull(length, "length must not be null");
        if (minLength == null) {
            minLength = 3;
        } else if (minLength <= 0) {
            throw new IllegalArgumentException("Invalid minLength=[" + minLength + "], expect positive integer");
        }
        if (length < 3) {
            throw new IllegalArgumentException("Invalid length=[" + length + "], expect greater than or equals 3");
        }
        if (minLength > length) {
            throw new IllegalArgumentException("Expect minLength=[" + minLength + "] greater than length=[" + length + "]");
        }
        String original = this.pickOneRandomWord(length);
        if (original == null) {
            throw new IllegalArgumentException("Cannot find valid word to create game state");
        }
        String scramble = this.scramble(original);
        Map<String, Boolean> subWords = new TreeMap<>();
        for (String subWord : this.generateSubWords(original, minLength)) {
            subWords.put(subWord, Boolean.FALSE);
        }
        return new GameState(original, scramble, subWords);
    }

}
