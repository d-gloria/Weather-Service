import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.Date;
import java.util.List;

public class WeatherRecordDAO {

    public void save(WeatherRecord record) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(record);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public List<WeatherRecord> getRecords(String city, Date date, String location) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM WeatherRecord WHERE city = :city AND date = :date AND location = :location";
            return session.createQuery(hql, WeatherRecord.class)
                    .setParameter("city", city)
                    .setParameter("date", date)
                    .setParameter("location", location)
                    .list();
        }
    }
}