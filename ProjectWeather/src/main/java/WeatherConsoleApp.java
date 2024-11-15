import org.json.simple.JSONObject;

import java.util.Scanner;

public class WeatherConsoleApp {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            String city;
            do {
                System.out.println("===================================================");
                System.out.print("Enter City: ");
                city = scanner.nextLine();

                if (city.equalsIgnoreCase("No")) break;

                JSONObject cityLocationData = WeatherService.getLocationData(city);

                if (cityLocationData != null) {
                    double latitude = (double) cityLocationData.get("latitude");
                    double longitude = (double) cityLocationData.get("longitude");

                    WeatherService.displayWeatherData(latitude, longitude);
                } else {
                    System.out.println("Error: Could not retrieve location data for " + city);
                }
            } while (!city.equalsIgnoreCase("No"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}