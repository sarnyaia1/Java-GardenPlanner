package service;

import domain.GardenProperties;
import domain.PlantType;
import lombok.Data;

import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Collectors;

@Data
public class GardenService {

    public GardenProperties gardenProperties;
    double areaInput = 0;
    double waterInput = 0;
    Map<String, Integer> plantInputs = new HashMap<>();
    Scanner inputReader = new Scanner(System.in);
    Scanner selectedPlant = new Scanner(System.in);

    public GardenService(){
    }

    //Hard code the plants
    private final List<PlantType> plantDatabase = List.of(
            new PlantType("Corn", 0.4, 10),
            new PlantType("Pumpkin", 2, 5),
            new PlantType("Grape", 3, 5),
            new PlantType("Tomato", 0.3, 10),
            new PlantType("Cucumber", 0.4, 10)
    );

    //Get a list of all the plants
    public List<PlantType> getPlantTypes(){
        return plantDatabase;
    }

    //Set the garden's area and the water-supply
    public void setGardenProperties(GardenProperties gardenProperties){
        this.gardenProperties = gardenProperties;
    }

    //Check the input plant is exist
    private PlantType getPlantByName(String plantName) throws IllegalArgumentException {
        return plantDatabase.stream()
                .filter(plantType -> plantName.equals(plantType.getName()))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("Plant is not present in the database: " + plantName));
    }

    // Need to discuss it!!
    public Result evaluatePlan(Map<String, Integer> plants) throws IllegalArgumentException {
        List<PlantType> plantTypesFromUser = plants.keySet()
                .stream()
                .map(this::getPlantByName)
                .collect(Collectors.toList());

        double neededArea = plantTypesFromUser.stream()
                .mapToDouble(plantType -> plantType.getArea() * plants.get(plantType.getName()))
                .sum();

        double neededWater = plantTypesFromUser.stream()
                .mapToDouble(plantType -> plantType.getWaterAmount() * plants.get(plantType.getName()) * plantType.getArea())
                .sum();

        boolean isAreaEnough = gardenProperties.getArea() >= neededArea;
        boolean isWaterEnough = gardenProperties.getWaterSupply() >= neededWater;

        return new Result(neededArea, neededWater, isAreaEnough, isWaterEnough);
    }

    public double getGardenSize(){
        //Handling negative numbers and InputMismatchException in garden size
        do{
            try{
                System.out.println("Size (square meter):");
                areaInput = inputReader.nextDouble();
                if(areaInput < 0 ) {
                    System.out.println("Garden size can't be a negative number! Try again!");
                }
            } catch (InputMismatchException ex){
                System.out.println("Illegal input format!");
                System.exit(0);
            }
        } while (areaInput < 0);
        return areaInput;
    }

    public double getWaterSupply(){
        //Handling negative numbers and InputMismatchException in water supply
        do {
            try{
                System.out.println("Water supply (in liter):");
                waterInput = inputReader.nextDouble();
                if(waterInput < 0 ) {
                    System.out.println("Water supply can't be a negative number! Try again!");
                }
            }  catch (InputMismatchException ex){
                System.out.println("Illegal input format!");
                System.exit(0);
            }
        } while (waterInput < 0);
        return waterInput;
    }

    public Result checkPlants(){
        try {
            while (inputReader.hasNextLine()) {
                System.out.println("Enter plant (format: name,amount):");
                String plantUserInput = selectedPlant.nextLine();
                if (plantUserInput.equals("done")) {
                    break;
                }
                String[] plantDetails = plantUserInput.split(",");
                plantInputs.put(plantDetails[0], Integer.parseInt(plantDetails[1])); //The first index is key, the second index is value
            }
        } catch (InputMismatchException ex) {
            System.exit(0);
        } catch (ArrayIndexOutOfBoundsException ex){
            System.out.println("Invalid plant format!");
            System.exit(0);
        }

        Result result = evaluatePlan(plantInputs);
        return result;
    }

    public void showResult(){
        Result result = evaluatePlan(plantInputs);
        NumberFormat formatter = NumberFormat.getNumberInstance(Locale.getDefault());
        formatter.setMaximumIntegerDigits(2);

        System.out.println(result); //Test
        System.out.println("Required area: " + formatter.format(result.getArea()) + " m2");
        System.out.println("Water need: " + formatter.format(result.getWaterAmount()) + " l");

        if (result.isWaterOK() && result.isAreaOk()) {
            if (result.getArea() !=0 && result.getWaterAmount() != 0){
                System.out.println("Plan is feasible in your garden! :)");
            } else {
                System.out.println("You didn't give me a valid plant");
            }
        } else {
            System.out.println("Plan is NOT feasible in your garden! :(\nReason(s):");
            if (!result.isAreaOk()) {
                System.out.println("- Not enough area");
            }
            if (!result.isWaterOK()) {
                System.out.println(" - Not enough water");
            }
        }
    }



}
