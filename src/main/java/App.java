import domain.GardenProperties;
import service.GardenService;

public class App {
    public static void main(String[] args) {

        //Create a Garden Service
        GardenService gardenService = new GardenService();

        // Greetings and add garden properties
        System.out.println("***Welcome to Garden Planner***\n");
        System.out.println("Please enter your garden properties.");

        //Save the value of garden size and water supply
        GardenProperties gardenProperties = new GardenProperties(gardenService.getGardenSize(), gardenService.getWaterSupply());
        gardenService.setGardenProperties(gardenProperties);
        System.out.println("Your area is:" + gardenProperties.getArea() + " m2"); //Can be deleted
        System.out.println("Your water-supply is:" + gardenProperties.getWaterSupply() + " l"); //Can be deleted

        //List existing plant types
        System.out.println("Known plant types:");
        gardenService.getPlantTypes().forEach(plantType -> System.out.println("- " + plantType.getName())); // ???

        //Select plant and the amount of plants
        System.out.println("\nPlease enter the plants you would like to put in your garden. Write 'done' and press enter when you are done.");
        gardenService.checkPlants();

        //Show the result
        System.out.println("\n***Result***\n");
        gardenService.showResult();

    }
}