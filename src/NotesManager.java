import java.io.*;
import java.util.*;

public class NotesManager {
    private static final String FILE_NAME = "notes.txt";
    private static List<String[]> notes = new ArrayList<>();
    private static int nextId = 1;

    public static void main(String[] args) {
        loadNotesFromFile();

        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n===== Notes Manager =====");
            System.out.println("1. Add Note");
            System.out.println("2. List Notes");
            System.out.println("3. Search Notes");
            System.out.println("4. Delete Note");
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");
            choice = sc.nextInt();
            sc.nextLine(); // clear newline

            switch (choice) {
                case 1 -> addNote(sc);
                case 2 -> listNotes();
                case 3 -> searchNotes(sc);
                case 4 -> deleteNote(sc);
                case 0 -> System.out.println("Exiting...");
                default -> System.out.println("Invalid choice.");
            }
        } while (choice != 0);

        sc.close();
    }

    private static void loadNotesFromFile() {
        notes.clear();
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("Error creating notes file: " + e.getMessage());
            }
        }

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|", 2);
                if (parts.length == 2) {
                    notes.add(parts);
                    int id = Integer.parseInt(parts[0]);
                    if (id >= nextId) {
                        nextId = id + 1;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading notes: " + e.getMessage());
        }
    }

    private static void saveNotesToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (String[] note : notes) {
                bw.write(note[0] + "|" + note[1]);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving notes: " + e.getMessage());
        }
    }

    private static void addNote(Scanner sc) {
        System.out.print("Enter note content: ");
        String content = sc.nextLine();
        notes.add(new String[]{String.valueOf(nextId++), content});
        saveNotesToFile();
        System.out.println("Note added successfully.");
    }

    private static void listNotes() {
        if (notes.isEmpty()) {
            System.out.println("No notes found.");
            return;
        }
        System.out.println("\n--- All Notes ---");
        for (String[] note : notes) {
            System.out.println(note[0] + " | " + note[1]);
        }
    }

    private static void searchNotes(Scanner sc) {
        System.out.print("Enter keyword to search: ");
        String keyword = sc.nextLine().toLowerCase();
        boolean found = false;

        for (String[] note : notes) {
            if (note[1].toLowerCase().contains(keyword)) {
                System.out.println(note[0] + " | " + note[1]);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No matching notes found.");
        }
    }

    private static void deleteNote(Scanner sc) {
        System.out.print("Enter note ID to delete: ");
        String idToDelete = sc.nextLine();
        boolean removed = notes.removeIf(note -> note[0].equals(idToDelete));

        if (removed) {
            saveNotesToFile();
            System.out.println("Note deleted successfully.");
        } else {
            System.out.println("Note ID not found.");
        }
    }
}
