import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "weather_records")
public class WeatherRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String city;

    @Temporal(TemporalType.DATE)
    private Date date;

    private String location;
    private double temperature;
    private double humidity;

    public WeatherRecord() {
    }

    public WeatherRecord(String city, Date date, String location, double temperature, double humidity) {
        this.city = city;
        this.date = date;
        this.location = location;
        this.temperature = temperature;
        this.humidity = humidity;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public double getTemperature() { return temperature; }
    public void setTemperature(double temperature) { this.temperature = temperature; }

    public double getHumidity() { return humidity; }
    public void setHumidity(double humidity) { this.humidity = humidity; }
}