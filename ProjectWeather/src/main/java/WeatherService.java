import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class WeatherService {

    public static JSONObject getLocationData(String city) {
        city = city.replaceAll(" ", "+");
        String urlString = "https://geocoding-api.open-meteo.com/v1/search?name=" +
                city + "&count=1&language=en&format=json";

        try {
            HttpURLConnection apiConnection = fetchApiResponse(urlString);
            if (apiConnection.getResponseCode() != 200) {
                System.out.println("Error: Could not connect to API");
                return null;
            }

            String jsonResponse = readApiResponse(apiConnection);
            JSONParser parser = new JSONParser();
            JSONObject resultsJsonObj = (JSONObject) parser.parse(jsonResponse);

            JSONArray locationData = (JSONArray) resultsJsonObj.get("results");
            return (JSONObject) locationData.get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void displayWeatherData(double latitude, double longitude) {
        try {
            String url = "https://api.open-meteo.com/v1/forecast?latitude=" + latitude +
                    "&longitude=" + longitude + "&current_weather=true";
            HttpURLConnection apiConnection = fetchApiResponse(url);

            if (apiConnection.getResponseCode() != 200) {
                System.out.println("Error: Could not connect to API");
                return;
            }

            String jsonResponse = readApiResponse(apiConnection);
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(jsonResponse);
            JSONObject currentWeatherJson = (JSONObject) jsonObject.get("current_weather");

            //printohen te dhenat mbi motin si temperatura, shpejtesia e eres dhe lloji i motit
            String time = (String) currentWeatherJson.getOrDefault("time", "N/A");
            System.out.println("Current Time: " + time);

            Double temperature = (Double) currentWeatherJson.get("temperature");
            System.out.println("Current Temperature (C): " + (temperature != null ? temperature : "N/A"));

            Double windSpeed = (Double) currentWeatherJson.get("windspeed");
            System.out.println("Wind Speed: " + (windSpeed != null ? windSpeed : "N/A"));

            Long weatherCodeLong = (Long) currentWeatherJson.get("weathercode");
            int weatherCode = (weatherCodeLong != null) ? weatherCodeLong.intValue() : -1;
            String weatherType = getWeatherType(weatherCode);
            System.out.println("Weather Type: " + (weatherType != null ? weatherType : "Unknown"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String readApiResponse(HttpURLConnection apiConnection) {
        try {
            StringBuilder resultJson = new StringBuilder();
            Scanner scanner = new Scanner(apiConnection.getInputStream());
            while (scanner.hasNext()) {
                resultJson.append(scanner.nextLine());
            }
            scanner.close();
            return resultJson.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static HttpURLConnection fetchApiResponse(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            return conn;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getWeatherType(int code) {
        Map<Integer, String> weatherTypes = new HashMap<>();
        weatherTypes.put(0, "Clear");
        weatherTypes.put(1, "Mainly clear");
        weatherTypes.put(2, "Partly cloudy");
        weatherTypes.put(3, "Overcast");
        weatherTypes.put(45, "Fog");
        weatherTypes.put(48, "Depositing rime fog");
        weatherTypes.put(51, "Drizzle: Light");
        weatherTypes.put(53, "Drizzle: Moderate");
        weatherTypes.put(55, "Drizzle: Dense intensity");
        weatherTypes.put(61, "Rain: Slight");
        weatherTypes.put(63, "Rain: Moderate");
        weatherTypes.put(65, "Rain: Heavy intensity");
        weatherTypes.put(71, "Snow fall: Slight");
        weatherTypes.put(73, "Snow fall: Moderate");
        weatherTypes.put(75, "Snow fall: Heavy intensity");
        weatherTypes.put(80, "Rain showers: Slight");
        weatherTypes.put(81, "Rain showers: Moderate");
        weatherTypes.put(82, "Rain showers: Violent");

        return weatherTypes.getOrDefault(code, "moderate");
    }
}