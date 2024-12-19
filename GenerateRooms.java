import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


/**This class creates and manages the text-based componenet of the game.
 * It reads a data from a textfile (Rooms.txt), constructs room objects with descriptions and directional paths
 * and enables/manages the consoled based navigation between them.
 * When you choose to challange the opponent, the swing game initiates.
 * The class integrated with the FileTextAnalyzer, which inherently integrated with the FileWordSplitter, to track all time wins for each player.
 * 
 * @author Adam Abdulmajid
*/

public class GenerateRooms {

    static FileTextAnalyzer fileAnalyzer = new FileTextAnalyzer("Wins.txt");

    /** Reads room data from file, and generates a map of room objects with descriptions and available directions.
     * 
     * @param filename the name of the file used to define the rooms.
     * @return a hashmap containing the room name (as key), and the Room objects as values
     */
    public static Map<String, Room> generateRoomsFromFile(String filename) {
        Map<String, Room> rooms = new HashMap<>();

        
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Room")) {
                    String[] parts = line.split(": ", 2);
                    String roomName = parts[0].substring(5).toLowerCase(); // Extract room name, converted to lowercase
                    String description = parts[1];

                    // Initialize directions based on the room name
                    Map<String, String> directions = new HashMap<>();
                    if (roomName.equals("dojo")) {
                        directions.put("south", "4");
                    } else if (roomName.equals("4")) {
                        directions.put("north", "dojo");
                        directions.put("south", "bridge");
                        directions.put("west", "garden");
                        directions.put("east", "challenger");
                    } else if (roomName.equals("bridge")) {
                        directions.put("north", "4");
                    } else if (roomName.equals("garden")) {
                        directions.put("east", "4");
                    } else if (roomName.equals("challenger")) {
                        directions.put("west", "4");
                    }

                    // Create a new Room object and add it to the rooms map
                    rooms.put(roomName, new Room(description, directions));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rooms;
    }
    /** Starts the console-based game. Provides options to display win counts for each player.
     * 
     * @param rooms a map of room objects representing the text based game world.
     * @return true if the player chooses to fight the oppoent.
     */
    public static boolean explorePreface(Map<String, Room> rooms) {
        Scanner scanner = new Scanner(System.in);
        String currentRoom = "dojo";

        System.out.println("Welcome to the exploration. Navigate through rooms to find your opponent and start the game!");

        while (true) {
            Room room = rooms.get(currentRoom);
            System.out.println(room.getDescription());

            System.out.println("Type a command (direction or 'log'):");
            String input = scanner.nextLine().trim().toLowerCase();

            // Check if the input is "log" to display win counts
            if (input.equals("log")) {
                System.out.println("Player 1 Wins: " + fileAnalyzer.occurencesOf("1"));
                System.out.println("Player 2 Wins: " + fileAnalyzer.occurencesOf("2"));
            }

            // Handle direction commands
            if (room.getDirections().containsKey(input)) {
                currentRoom = room.getDirections().get(input);

                if ("challenger".equals(currentRoom)) {
                    System.out.println("You see your opponent. Do you want to start the fight? (yes/no)");
                    String response = scanner.nextLine().trim().toLowerCase();

                    if (response.equals("yes")) {
                        System.out.println("Prepare yourself! The game begins now.");
                        System.out.println("W for jumping \nS for attacking \nA & D for running");
                        System.out.println("Loading....");
                        scanner.close(); // Close the scanner before starting the game
                        return true; // Start the game
                    } else {
                        System.out.println("You chose not to fight. You can continue exploring.");
                        currentRoom = "4"; // Reset to room "4" if the player chooses not to fight
                    }
                }
            } else {
                System.out.println("You can't go that way from here.");
            }
        }
    }
}


/** The Room class represents an individual room as an object in the text based game.
 * Each room has a description and the possible directions the player can go from there.
 * 
 *  @author Adam Abdulmajid
 */

class Room {
    private String description;
    private Map<String, String> directions;
    
    /**
     * Constructor for creating a room object.
     * 
     * @param description the description of the room.
     * @param directions a map representing exits, where keys are directions and values are rooms.
     */
    public Room( String description, Map<String, String> directions) {
        this.description = description;
        this.directions = directions;
    }

    /**
     * 
     * @return a string describing the room.
     */
    public String getDescription() {
        return description;
    }
    /**
     * 
     * @return a map of possible directions from your current position.
     */
    public Map<String, String> getDirections() {
        return directions;
    }
}
