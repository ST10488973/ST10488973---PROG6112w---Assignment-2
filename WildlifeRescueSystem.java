package wildlifesa;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// ==================== INTERFACE (Design Requirement) ====================
interface RescueOperations {
    void startRescueOperation();
    void completeRescueOperation();
    String generateRescueSummary();
}

// ==================== ABSTRACT PARENT (Abstraction + Encapsulation + Inheritance) ====================
abstract class RescueCase implements RescueOperations {
    protected String rescueCaseId;
    protected String animalName;
    protected String species;
    protected String rescueLocation;
    protected String assignedRanger;
    protected int numberOfRescueDays;
    protected double dailyCareCost;
    protected String currentRescueStatus;
    protected String conservationClassification;
    protected double securityCost;
    protected boolean specialistTeamRequired;

    public RescueCase(String rescueCaseId, String animalName, String species, 
                      String rescueLocation, String assignedRanger, 
                      int numberOfRescueDays, double dailyCareCost, 
                      String currentRescueStatus,
                      String conservationClassification,
                      double securityCost,
                      boolean specialistTeamRequired) {
        this.rescueCaseId = rescueCaseId;
        this.animalName = animalName;
        this.species = species;
        this.rescueLocation = rescueLocation;
        this.assignedRanger = assignedRanger;
        this.numberOfRescueDays = numberOfRescueDays;
        this.dailyCareCost = dailyCareCost;
        this.currentRescueStatus = currentRescueStatus;
        this.conservationClassification = conservationClassification;
        this.securityCost = securityCost;
        this.specialistTeamRequired = specialistTeamRequired;
    }

    // Encapsulation - Getters
    public String getRescueCaseId() { return rescueCaseId; }
    public String getAnimalName() { return animalName; }
    public String getSpecies() { return species; }
    public String getConservationClassification() { return conservationClassification; }
    public double getSecurityCost() { return securityCost; }
    public boolean isSpecialistTeamRequired() { return specialistTeamRequired; }
    public String getCurrentRescueStatus() { return currentRescueStatus; }
    public void setCurrentRescueStatus(String s) { this.currentRescueStatus = s; }
    public int getNumberOfRescueDays() { return numberOfRescueDays; }
    public void setNumberOfRescueDays(int d) { this.numberOfRescueDays = d; }
    public double getDailyCareCost() { return dailyCareCost; }
    public String getRescueLocation() { return rescueLocation; }
    public String getAssignedRanger() { return assignedRanger; }
    public void setAssignedRanger(String r) { this.assignedRanger = r; }

    @Override
    public void startRescueOperation() {
        this.currentRescueStatus = "In Treatment";
        System.out.println("Rescue operation STARTED for " + rescueCaseId);
    }

    @Override
    public void completeRescueOperation() {
        this.currentRescueStatus = "Released";
        System.out.println("Rescue operation COMPLETED for " + rescueCaseId);
    }

    // Abstract methods - each type must implement
    public abstract double calculateTotalRescueCost();
    public abstract String determineRescuePriority();
    public abstract void displayRescueInfo();

    @Override
    public String generateRescueSummary() {
        return String.format(
            "===== RESCUE SUMMARY =====\n" +
            "Rescue Case ID: %s\n" +
            "Rescue Type: %s\n" +
            "Species: %s (%s)\n" +
            "Assigned Ranger: %s\n" +
            "Rescue Priority: %s\n" +
            "Current Status: %s\n" +
            "Total Rescue Cost: R%.2f\n" +
            "--- Additional Details ---\n" +
            "Animal Name: %s | Location: %s\n" +
            "Classification: %s | Days: %d | Daily: R%.2f | Security: R%.2f | Specialist Team: %s\n" +
            "===========================",
            rescueCaseId, this.getClass().getSimpleName(), species, animalName,
            assignedRanger, determineRescuePriority(), currentRescueStatus,
            calculateTotalRescueCost(), animalName, rescueLocation,
            conservationClassification, numberOfRescueDays, dailyCareCost, securityCost, specialistTeamRequired ? "Yes (+R8000)" : "No"
        );
    }
    
    protected double calculateBaseCost() {
        double base = (numberOfRescueDays * dailyCareCost) + securityCost;
        if (specialistTeamRequired) base += 8000;
        return base;
    }

    @Override
    public String toString() {
        return String.format("%s | %s (%s) | %s | Cost: R%.2f | Priority: %s | Status: %s",
            rescueCaseId, animalName, species, conservationClassification,
            calculateTotalRescueCost(), determineRescuePriority(), currentRescueStatus);
    }
}

// ==================== SUBCLASS 1 - Method Overriding + Inheritance ====================
class EndangeredRescueCase extends RescueCase {
    private final boolean requiresRehabilitationCenter;

    public EndangeredRescueCase(String rescueCaseId, String animalName, String species, 
                                String rescueLocation, String assignedRanger, 
                                int numberOfRescueDays, double dailyCareCost, 
                                String currentRescueStatus,
                                String conservationClassification,
                                double securityCost,
                                boolean specialistTeamRequired,
                                boolean requiresRehabilitationCenter) {
        super(rescueCaseId, animalName, species, rescueLocation, assignedRanger, 
              numberOfRescueDays, dailyCareCost, currentRescueStatus,
              conservationClassification, securityCost, specialistTeamRequired);
        this.requiresRehabilitationCenter = requiresRehabilitationCenter;
    }

    @Override
    public double calculateTotalRescueCost() {
        return calculateBaseCost() * 1.25; // 25% conservation levy
    }

    @Override
    public String determineRescuePriority() {
        if (conservationClassification.equalsIgnoreCase("Critically Endangered")) return "CRITICAL - IMMEDIATE";
        else if (conservationClassification.equalsIgnoreCase("Endangered")) return "HIGH";
        else return "MEDIUM-HIGH";
    }

    @Override
    public void displayRescueInfo() {
        System.out.println("\n--- ENDANGERED SPECIES RESCUE ---");
        System.out.println(generateRescueSummary());
        System.out.println("Specific: Requires Rehabilitation Center: " + (requiresRehabilitationCenter ? "Yes" : "No"));
        System.out.println("Protocol: 24/7 monitoring, vet specialist on standby.");
    }
    
    @Override
    public void startRescueOperation() {
        setCurrentRescueStatus("In Critical Care");
        System.out.println("ENDANGERED PROTOCOL STARTED for " + rescueCaseId + " - Priority: " + determineRescuePriority());
    }
}

// ==================== SUBCLASS 2 ====================
class StandardRescueCase extends RescueCase {
    private String injuryType;

    public StandardRescueCase(String rescueCaseId, String animalName, String species, 
                              String rescueLocation, String assignedRanger, 
                              int numberOfRescueDays, double dailyCareCost, 
                              String currentRescueStatus,
                              String conservationClassification,
                              double securityCost,
                              boolean specialistTeamRequired,
                              String injuryType) {
        super(rescueCaseId, animalName, species, rescueLocation, assignedRanger, 
              numberOfRescueDays, dailyCareCost, currentRescueStatus,
              conservationClassification, securityCost, specialistTeamRequired);
        this.injuryType = injuryType;
    }

    @Override
    public double calculateTotalRescueCost() { return calculateBaseCost(); }

    @Override
    public String determineRescuePriority() {
        if (specialistTeamRequired) return "MEDIUM";
        if (numberOfRescueDays > 30) return "MEDIUM";
        return "LOW";
    }

    @Override
    public void displayRescueInfo() {
        System.out.println("\n--- STANDARD WILDLIFE RESCUE ---");
        System.out.println(generateRescueSummary());
        System.out.println("Specific: Injury Type: " + injuryType);
        System.out.println("Protocol: Standard treatment and observation.");
    }
}

// ==================== SUBCLASS 3 ====================
class HighRiskRescueCase extends RescueCase {
    private boolean requiresArmedEscort;
    private String threatLevel;

    public HighRiskRescueCase(String rescueCaseId, String animalName, String species, 
                              String rescueLocation, String assignedRanger, 
                              int numberOfRescueDays, double dailyCareCost, 
                              String currentRescueStatus,
                              String conservationClassification,
                              double securityCost,
                              boolean specialistTeamRequired,
                              boolean requiresArmedEscort,
                              String threatLevel) {
        super(rescueCaseId, animalName, species, rescueLocation, assignedRanger, 
              numberOfRescueDays, dailyCareCost, currentRescueStatus,
              conservationClassification, securityCost, specialistTeamRequired);
        this.requiresArmedEscort = requiresArmedEscort;
        this.threatLevel = threatLevel;
    }

    @Override
    public double calculateTotalRescueCost() {
        double base = calculateBaseCost();
        double cost = base * 1.4;
        if (requiresArmedEscort) cost += 12000;
        return cost;
    }

    @Override
    public String determineRescuePriority() {
        if (threatLevel.equalsIgnoreCase("Extreme") || requiresArmedEscort) return "CRITICAL - SECURITY ALERT";
        return "HIGH - SECURITY REQUIRED";
    }

    @Override
    public void displayRescueInfo() {
        System.out.println("\n--- HIGH-RISK / ANTI-POACHING RESCUE ---");
        System.out.println(generateRescueSummary());
        System.out.println("Specific: Armed Escort: " + (requiresArmedEscort ? "Yes" : "No") + " | Threat Level: " + threatLevel);
        System.out.println("Protocol: Secure perimeter, anti-poaching unit, GPS tracking.");
    }
    
    @Override
    public void startRescueOperation() {
        setCurrentRescueStatus("Under Secure Protection");
        System.out.println("HIGH-RISK PROTOCOL STARTED for " + rescueCaseId + " with armed escort.");
    }
}

// ==================== MANAGER - ArrayList Storage (Assumption) ====================
class RescueCaseManager {
    private ArrayList<RescueCase> rescueCases = new ArrayList<>();

    public boolean addRescueCase(RescueCase rc) {
        if (findById(rc.getRescueCaseId()) != null) return false;
        rescueCases.add(rc);
        return true;
    }

    public RescueCase findById(String id) {
        for (RescueCase rc : rescueCases) {
            if (rc.getRescueCaseId().equalsIgnoreCase(id)) return rc;
        }
        return null;
    }

    public List<RescueCase> searchByAnimalName(String name) {
        List<RescueCase> results = new ArrayList<>();
        for (RescueCase rc : rescueCases) {
            if (rc.getAnimalName().toLowerCase().contains(name.toLowerCase())) results.add(rc);
        }
        return results;
    }

    public List<RescueCase> getAllCases() { return rescueCases; }

    public boolean deleteRescueCase(String id) {
        RescueCase rc = findById(id);
        if (rc != null) { rescueCases.remove(rc); return true; }
        return false;
    }

    public String generateFullRescueReport() {
        if (rescueCases.isEmpty()) {
            return "===== WILDLIFE SA RESCUE REPORT =====\nNo rescue cases currently stored.\n=====================================";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("=================================================================\n");
        sb.append("        WILDLIFE SA - FULL RESCUE REPORT\n");
        sb.append("=================================================================\n");
        double totalEstimatedCost = 0;
        for (int i = 0; i < rescueCases.size(); i++) {
            RescueCase rc = rescueCases.get(i);
            double cost = rc.calculateTotalRescueCost();
            totalEstimatedCost += cost;
            sb.append(String.format("--- Rescue Case %d ---\n", i + 1));
            sb.append(String.format("Rescue Case ID: %s\n", rc.getRescueCaseId()));
            sb.append(String.format("Rescue Type: %s\n", rc.getClass().getSimpleName()));
            sb.append(String.format("Species: %s\n", rc.getSpecies()));
            sb.append(String.format("Rescue Location: %s\n", rc.getRescueLocation()));
            sb.append(String.format("Assigned Ranger: %s\n", rc.getAssignedRanger()));
            sb.append(String.format("Rescue Priority: %s\n", rc.determineRescuePriority()));
            sb.append(String.format("Current Status: %s\n", rc.getCurrentRescueStatus()));
            sb.append(String.format("Total Rescue Cost: R%.2f\n\n", cost));
        }
        sb.append("=================================================================\n");
        sb.append(String.format("Total number of rescue cases: %d\n", rescueCases.size()));
        sb.append(String.format("Total estimated rescue cost: R%.2f\n", totalEstimatedCost));
        sb.append("=================================================================\n");
        return sb.toString();
    }

    public String getProgressReport() {
        if (rescueCases.isEmpty()) return "No rescue cases recorded.";
        StringBuilder sb = new StringBuilder("=== RESCUE PROGRESS MONITOR ===\n");
        for (RescueCase rc : rescueCases) sb.append(rc.toString()).append("\n");
        return sb.toString();
    }
}

// ==================== MAIN APP - Validation + Menu ====================
public class WildlifeRescueSystem {
    private static RescueCaseManager manager = new RescueCaseManager();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        seedData();
        int choice;
        do {
            System.out.println("\n====== WildLife SA Rescue Operations System ======");
            System.out.println("1. Record New Rescue Case");
            System.out.println("2. View All Rescue Cases");
            System.out.println("3. Search by ID + Display Specific Info (Polymorphism Demo)");
            System.out.println("4. Start Rescue Operation");
            System.out.println("5. Complete Rescue Operation");
            System.out.println("6. Generate Rescue Summary (per case)");
            System.out.println("7. Generate FULL Rescue Report (Final Spec)");
            System.out.println("8. Delete Case");
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");
            String raw = scanner.nextLine();
            try { choice = Integer.parseInt(raw); }
            catch (NumberFormatException e) {
                System.out.println("ERROR: Menu selection must be a number 0-8.");
                choice = -1; continue;
            }
            if (choice < 0 || choice > 8) {
                System.out.println("ERROR: Invalid menu selection. Choose 0-8.");
                continue;
            }
            switch (choice) {
                case 1: addNewCase(); break;
                case 2: viewAll(); break;
                case 3: searchAndDisplay(); break;
                case 4: startOperation(); break;
                case 5: completeOperation(); break;
                case 6: generateSummary(); break;
                case 7: System.out.println(manager.generateFullRescueReport()); break;
                case 8: deleteCase(); break;
                case 0: System.out.println("Exiting... Thank you for protecting wildlife!"); break;
            }
        } while (choice != 0);
    }

    // Validation helpers (5 Marks)
    private static String readNonBlank(String prompt, String fieldName) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            if (input == null || input.trim().isEmpty()) {
                System.out.println("ERROR: " + fieldName + " is not blank - please enter a value.");
            } else return input.trim();
        }
    }
    private static int readPositiveInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                int val = Integer.parseInt(scanner.nextLine());
                if (val <= 0) System.out.println("ERROR: Numeric values must be greater than zero.");
                else return val;
            } catch (NumberFormatException e) { System.out.println("ERROR: Please enter a valid number > 0."); }
        }
    }
    private static double readPositiveDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                double val = Double.parseDouble(scanner.nextLine());
                if (val <= 0) System.out.println("ERROR: Numeric values must be greater than zero.");
                else return val;
            } catch (NumberFormatException e) { System.out.println("ERROR: Please enter a valid number > 0."); }
        }
    }
    private static double readNonNegativeDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                double val = Double.parseDouble(scanner.nextLine());
                if (val < 0) System.out.println("ERROR: Cannot be negative.");
                else return val;
            } catch (NumberFormatException e) { System.out.println("ERROR: Please enter a valid number."); }
        }
    }
    private static boolean readBoolean(String prompt) {
        while (true) {
            System.out.print(prompt + " (true/false): ");
            String s = scanner.nextLine().trim().toLowerCase();
            if (s.equals("true") || s.equals("t") || s.equals("yes") || s.equals("y")) return true;
            if (s.equals("false") || s.equals("f") || s.equals("no") || s.equals("n")) return false;
            System.out.println("ERROR: Please enter true or false.");
        }
    }

    private static void addNewCase() {
        System.out.println("\n--- New Rescue Case (Validated) ---");
        String id;
        while (true) {
            id = readNonBlank("Rescue Case ID (e.g. WC004): ", "Rescue Case ID");
            if (manager.findById(id) != null) System.out.println("ERROR: Rescue Case ID is unique - ID '" + id + "' already exists.");
            else break;
        }
        String animal = readNonBlank("Animal Name: ", "Animal Name");
        String species = readNonBlank("Species: ", "Species");
        String loc = readNonBlank("Rescue Location: ", "Rescue Location");
        String ranger = readNonBlank("Assigned Ranger: ", "Assigned Ranger");
        int days = readPositiveInt("Number of Rescue Days: ");
        double daily = readPositiveDouble("Daily Care Cost (R): ");
        String status = readNonBlank("Current Status [Rescued/In Treatment/Recovering/Released]: ", "Current Status");
        String classification = readNonBlank("Conservation Classification [Critically Endangered/Endangered/Vulnerable/Least Concern]: ", "Conservation Classification");
        double secCost = readNonNegativeDouble("Security Cost (R): ");
        boolean specialist = readBoolean("Specialist Team Required?");

        System.out.println("\nChoose Rescue Type:\n1. Endangered Species Rescue\n2. Standard Wildlife Rescue\n3. High-Risk / Anti-Poaching Rescue");
        int typeChoice;
        while (true) {
            typeChoice = readPositiveInt("Type (1-3): ");
            if (typeChoice >= 1 && typeChoice <= 3) break;
            System.out.println("ERROR: Type must be 1, 2 or 3.");
        }
        RescueCase rc = null;
        switch (typeChoice) {
            case 1: boolean rehab = readBoolean("Requires Rehabilitation Center?"); rc = new EndangeredRescueCase(id, animal, species, loc, ranger, days, daily, status, classification, secCost, specialist, rehab); break;
            case 2: String injury = readNonBlank("Injury Type: ", "Injury Type"); rc = new StandardRescueCase(id, animal, species, loc, ranger, days, daily, status, classification, secCost, specialist, injury); break;
            case 3: boolean armed = readBoolean("Requires Armed Escort?"); String threat = readNonBlank("Threat Level [Low/Medium/High/Extreme]: ", "Threat Level"); rc = new HighRiskRescueCase(id, animal, species, loc, ranger, days, daily, status, classification, secCost, specialist, armed, threat); break;
        }
        if (manager.addRescueCase(rc)) { System.out.println("Case added successfully!"); rc.displayRescueInfo(); }
        else System.out.println("ERROR: Could not add - duplicate ID.");
    }

    private static void viewAll() {
        if (manager.getAllCases().isEmpty()) { System.out.println("No cases"); return; }
        for (RescueCase rc : manager.getAllCases()) System.out.println(rc.toString());
    }
    private static void searchAndDisplay() {
        String id = readNonBlank("Enter Case ID: ", "Rescue Case ID");
        RescueCase rc = manager.findById(id);
        if (rc == null) System.out.println("Not found"); else rc.displayRescueInfo();
    }
    private static void startOperation() {
        String id = readNonBlank("Enter Case ID to START: ", "Rescue Case ID");
        RescueCase rc = manager.findById(id);
        if (rc != null) { rc.startRescueOperation(); System.out.println(rc.generateRescueSummary()); } else System.out.println("Not found");
    }
    private static void completeOperation() {
        String id = readNonBlank("Enter Case ID to COMPLETE: ", "Rescue Case ID");
        RescueCase rc = manager.findById(id);
        if (rc != null) { rc.completeRescueOperation(); System.out.println(rc.generateRescueSummary()); } else System.out.println("Not found");
    }
    private static void generateSummary() {
        String id = readNonBlank("Enter Case ID for summary: ", "Rescue Case ID");
        RescueCase rc = manager.findById(id);
        if (rc != null) System.out.println(rc.generateRescueSummary()); else System.out.println("Not found");
    }
    private static void deleteCase() {
        String id = readNonBlank("Enter Case ID to delete: ", "Rescue Case ID");
        System.out.println(manager.deleteRescueCase(id) ? "Deleted" : "Not found");
    }
    private static void seedData() {
        manager.addRescueCase(new EndangeredRescueCase("WC001","Thandi","White Rhino","Kruger National Park","Ranger Nkosi",45,850.50,"Rescued","Critically Endangered",5000,true,true));
        manager.addRescueCase(new StandardRescueCase("WC002","Leo","African Lion","Madikwe","Ranger Smith",20,1200,"Rescued","Vulnerable",2000,false,"Snare Injury"));
        manager.addRescueCase(new HighRiskRescueCase("WC003","Zara","Cheetah","Pilanesberg","Ranger Botha",60,950.75,"Rescued","Endangered",8000,true,true,"High"));
    }
}