
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


/** FileWordSplitter reads a text file, extracts each word, and stores them.
 * @author Adam Abdulmajid
 */
class FileWordSplitter{

    //Field to store each extracted word from the file (i.e. array of all the words).
    private ArrayList<String> words;

    //Constructor
    public FileWordSplitter(String filename){
        //Initialize as empty arraylist
        words = new ArrayList<>();
       
       try {
   
        //Read the file line by line with Scanner class.
        Scanner scanner = new Scanner(new File(filename));

        //Fetch the new line from the file and split into words/strings to store in the string array if whitespace occures. 
        //Store each word in the "main" storage type (word arraylist).
        while (scanner.hasNextLine()){

            //Splitting into words
            String line = scanner.nextLine();
            String[] linewords = line.split("\\s+");

            for(String word : linewords){
                words.add(word);
            }


        }

        scanner.close();
        
       } catch (Exception e) {
        //If filepath not valid.
        System.out.println(filename + " not found");
       }

    }

    //Return all words
    public ArrayList<String> getWords(){
        return words;
    }


    // public static void main(String[] args){
    
    //     //Instances of FileWordSplitter & FileTextAnalyzer
    //     FileWordSplitter splitter = new FileWordSplitter("Wins.txt");
    //     FileTextAnalyzer analyzer = new FileTextAnalyzer("Wins.txt");
     
    //     ArrayList<String> hamletWords = splitter.getWords();
        
    //     System.out.println(hamletWords.get(3)); // Get the fourth word in the text
       
    //     int totalWords = analyzer.wordCount();
    //     System.out.println("Total word count: " + totalWords);

        
    //     // System.out.println(analyzer.getWordOccurences());
    //     System.out.println(analyzer.occurencesOf("the"));
    //     System.out.printf( "%.10f%n", analyzer.frequencyOf("the"));
    //     System.out.println(analyzer.uniqueWordCount());

    
    // }

}

// @author Adam Abdulmajid
/** FileTextAnalyzer relies on the FileWordSplitter to perform analyses on the text file.
 * It counts the word occurences, calculates word frequencies, and counts the number of unique words. 
 */
class FileTextAnalyzer{
    //the word is the key, and the number of times it occures is the value for the Hashmap.
    static HashMap<String, Integer> wordOccurences = new HashMap<>();
        //Instance of the splitter class
        private FileWordSplitter words;
    
    //Constructor: initializes the analyzer by populating the "words" instance with every word in hamlet
        public FileTextAnalyzer(String filename){
            this.words = new FileWordSplitter(filename);
       
            wordOccurences(); 
        }
    
        //Return the total number of words in the file
        public int wordCount(){
            
       ArrayList<String> wordsList = words.getWords();
           return wordsList.size();
         }
         //Loop through each word in the text. If the word (key in the HashMap) is new (i.e. hasn't occured in Hasmap yet),
         //then add to map with inital value of 1. Else (already exists) increment value by 1.
         private void wordOccurences(){
            ArrayList<String> wordsList = words.getWords();
        
            for(String word : wordsList){
                if (!wordOccurences.containsKey(word)){
                    wordOccurences.merge(word, 1, Integer::sum);
                    
                 }else{
                    int currentCount = wordOccurences.get(word);
                     wordOccurences.put(word, currentCount +1);
                 }
           }
        }
        
        //Getter for wordOccurences 
    public HashMap<String,Integer> getWordOccurences(){
        return wordOccurences;
    }
    
    //Returns numbers of time when a specific word occures in the text.
    public int occurencesOf(String word){
    
    //Case inensitivity by using toLowerCase method within the String class.
        String IgnoreCaseWord = word.toLowerCase();
        
        //If the word exists in the HashMap, return then reutrn its corresponding value (i.e. count), else return 0.
       if(wordOccurences.containsKey(IgnoreCaseWord)){
        return wordOccurences.get(IgnoreCaseWord);
   }else{
   return 0;
   }
}

//Return the frecuency of a specific word in relation to the total amount of words in the file.
public double frequencyOf(String word){
    
   String IgnoreCaseWord = word.toLowerCase();
   double wordCount = occurencesOf(IgnoreCaseWord);
   double totalWords = wordCount();

   double fraction = (wordCount)/totalWords;
   return fraction;
}

//Returns the number of unique words in the text (count of 1)
public int uniqueWordCount(){

int counter = 0;
for(int count : wordOccurences.values()){
    if(count == 1){
        counter ++;
    }
}
return counter;


//Without hasmap soultion
// int counter = 0;
// ArrayList<String> wordsList = words.getWords();
// for(String word: wordsList){
// if(occurencesOf(word) == 1){
//     counter ++;
// }
// }
// return counter;

}
}
