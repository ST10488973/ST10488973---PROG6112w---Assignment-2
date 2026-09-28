package wildlifesa;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class RescueCaseTest {

    private RescueCaseManager manager;
    private RescueCase endangeredCase;
    private RescueCase standardCase;
    private RescueCase highRiskCase;

    @BeforeEach
    public void setUp() {
        manager = new RescueCaseManager();
        
        // Endangered: Days=10, Daily=1000, Security=2000, Specialist=true (+8000)
        // Base = (10*1000)+2000+8000=20000, *1.25 = 25000
        endangeredCase = new EndangeredRescueCase(
            "TEST001", "Thandi", "White Rhino", "Kruger", "Ranger Nkosi",
            10, 1000.0, "Rescued", "Critically Endangered", 2000, true, true
        );

        // Standard: Days=5, Daily=500, Security=1000, Specialist=false
        // Base = 5*500+1000=3500
        standardCase = new StandardRescueCase(
            "TEST002", "Leo", "African Lion", "Madikwe", "Ranger Smith",
            5, 500.0, "Rescued", "Vulnerable", 1000, false, "Snare Injury"
        );

        // HighRisk: Days=10, Daily=1000, Security=2000, Specialist=true (+8000), Armed=true (+12000)
        // Base=20000, *1.4=28000, +12000=40000
        highRiskCase = new HighRiskRescueCase(
            "TEST003", "Zara", "Cheetah", "Pilanesberg", "Ranger Botha",
            10, 1000.0, "Rescued", "Endangered", 2000, true, true, "High"
        );

        manager.addRescueCase(endangeredCase);
        manager.addRescueCase(standardCase);
    }

    // 1. Rescue cost calculations
    @Test
    public void testRescueCostCalculations() {
        // Endangered 25% levy
        assertEquals(25000.0, endangeredCase.calculateTotalRescueCost(), 0.01,
            "Endangered cost should be base * 1.25");

        // Standard base only
        assertEquals(3500.0, standardCase.calculateTotalRescueCost(), 0.01,
            "Standard cost should be base only");

        // HighRisk 40% + armed escort
        assertEquals(40000.0, highRiskCase.calculateTotalRescueCost(), 0.01,
            "HighRisk cost should be base*1.4 + 12000");

        // Test without specialist team
        RescueCase noSpecialist = new StandardRescueCase(
            "TEST004", "Birdy", "Eagle", "Drakensberg", "Ranger Lee",
            2, 300, "Rescued", "Least Concern", 500, false, "Broken Wing"
        );
        // (2*300)+500=1100
        assertEquals(1100.0, noSpecialist.calculateTotalRescueCost(), 0.01);
    }

    // 2. Rescue priority calculations
    @Test
    public void testRescuePriorityCalculations() {
        assertEquals("CRITICAL - IMMEDIATE", endangeredCase.determineRescuePriority(),
            "Critically Endangered should be CRITICAL - IMMEDIATE");

        RescueCase endangered = new EndangeredRescueCase(
            "E001", "A", "B", "C", "D", 1, 100, "Rescued", "Endangered", 0, false, false
        );
        assertEquals("HIGH", endangered.determineRescuePriority());

        // Standard with specialist should be MEDIUM
        RescueCase standardWithSpecialist = new StandardRescueCase(
            "S001", "A", "B", "C", "D", 5, 100, "Rescued", "Least Concern", 0, true, "Injury"
        );
        assertEquals("MEDIUM", standardWithSpecialist.determineRescuePriority());

        // Standard without specialist and short stay = LOW
        assertEquals("LOW", standardCase.determineRescuePriority());

        // HighRisk with Extreme threat = CRITICAL - SECURITY ALERT
        assertTrue(highRiskCase.determineRescuePriority().contains("CRITICAL"));
    }

    // 3. Rescue status updates
    @Test
    public void testRescueStatusUpdates() {
        assertEquals("Rescued", standardCase.getCurrentRescueStatus());

        standardCase.startRescueOperation();
        assertEquals("In Treatment", standardCase.getCurrentRescueStatus(),
            "When operation begins, status must be updated appropriately");

        standardCase.completeRescueOperation();
        assertEquals("Released", standardCase.getCurrentRescueStatus(),
            "When operation completed, status must be updated accordingly");

        // Test polymorphic status update for Endangered
        endangeredCase.startRescueOperation();
        assertEquals("In Critical Care", endangeredCase.getCurrentRescueStatus());

        // HighRisk
        highRiskCase.startRescueOperation();
        assertEquals("Under Secure Protection", highRiskCase.getCurrentRescueStatus());
    }

    // 4. Searching for an existing rescue case
    @Test
    public void testSearchingForExistingRescueCase() {
        RescueCase found = manager.findById("TEST001");
        assertNotNull(found, "Should find existing case");
        assertEquals("Thandi", found.getAnimalName());
        assertEquals("White Rhino", found.getSpecies());

        // Search by animal name
        assertFalse(manager.searchByAnimalName("Thandi").isEmpty());
        assertTrue(manager.searchByAnimalName("NonExistent").isEmpty());

        // Search non-existing ID
        assertNull(manager.findById("DOESNOTEXIST"));
    }

    // 5. Preventing duplicate Rescue Case IDs
    @Test
    public void testPreventingDuplicateRescueCaseIDs() {
        // ID TEST001 already exists in manager
        RescueCase duplicate = new StandardRescueCase(
            "TEST001", "Duplicate", "Lion", "Kruger", "Ranger Test",
            1, 100, "Rescued", "Vulnerable", 0, false, "Test"
        );

        boolean added = manager.addRescueCase(duplicate);
        assertFalse(added, "Should prevent duplicate Rescue Case IDs");
        assertEquals(2, manager.getAllCases().size(), "Size should remain 2 after duplicate attempt");

        // Adding unique ID should succeed
        RescueCase unique = new StandardRescueCase(
            "UNIQUE001", "Unique", "Lion", "Kruger", "Ranger Test",
            1, 100, "Rescued", "Vulnerable", 0, false, "Test"
        );
        assertTrue(manager.addRescueCase(unique));
        assertEquals(3, manager.getAllCases().size());
    }

    @Test
    public void testRescueSummaryContainsRequiredFields() {
        String summary = endangeredCase.generateRescueSummary();
        // Must include: ID, Type, Species, Ranger, Priority, Status, Total Cost
        assertTrue(summary.contains("Rescue Case ID: TEST001"));
        assertTrue(summary.contains("Rescue Type:"));
        assertTrue(summary.contains("Species:"));
        assertTrue(summary.contains("Assigned Ranger:"));
        assertTrue(summary.contains("Rescue Priority:"));
        assertTrue(summary.contains("Current Status:"));
        assertTrue(summary.contains("Total Rescue Cost:"));
    }

    @Test
    public void testFullReportTotals() {
        String report = manager.generateFullRescueReport();
        assertTrue(report.contains("Total number of rescue cases: 2"));
        // 25000 + 3500 = 28500
        assertTrue(report.contains("Total estimated rescue cost: R28500.00"));
    }
}