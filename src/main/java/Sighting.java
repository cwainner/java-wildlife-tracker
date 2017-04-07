import org.sql2o.*;

import java.sql.Timestamp;
import static java.text.DateFormat.*;

import java.util.Date;
import java.util.List;
public class Sighting {
  private int animal_id;
  private String location;
  private int ranger_id;
  private int id;
  private boolean animalEndangeredStatus;
  private Timestamp sighting_time;

  public Sighting(int animal_id, String location, int ranger_id, boolean status) {
    if(animal_id <= 0 || location.equals("") || ranger_id <= 0){
      throw new IllegalArgumentException("You cannot have empty inputs.");
    }
    this.animal_id = animal_id;
    this.location = location;
    this.ranger_id = ranger_id;
    this.animalEndangeredStatus = status;
    this.sighting_time = new Timestamp(new Date().getTime());
  }

  public int getId() {
    return id;
  }

  public int getAnimalId() {
    return animal_id;
  }

  public String getLocation() {
    return location;
  }

  public int getRangerId() {
    return ranger_id;
  }

  public String getRangerName(){
    try(Connection con = DB.sql2o.open()){
      String sql = "SELECT name FROM rangers WHERE id=:id";
      return con.createQuery(sql)
        .addParameter("id", this.ranger_id)
        .executeAndFetchFirst(String.class);
    }
  }

  public String getLastSighting(){
    return getDateTimeInstance().format(sighting_time);
  }

  public boolean getStatus(){
    return animalEndangeredStatus;
  }

  public Object getAnimal(){
    try(Connection con = DB.sql2o.open()){
      if(this.getStatus() == true){
        String sql = "SELECT * FROM endangered_animals WHERE id=:id";
        return con.createQuery(sql)
          .addParameter("id", animal_id)
          .executeAndFetchFirst(EndangeredAnimal.class);
      } else{
        String sql = "SELECT * FROM animals WHERE id=:id";
        return con.createQuery(sql)
          .addParameter("id", animal_id)
          .executeAndFetchFirst(Animal.class);
      }
    }
  }

  @Override
  public boolean equals(Object otherSighting) {
    if(!(otherSighting instanceof Sighting)) {
      return false;
    } else {
      Sighting newSighting = (Sighting) otherSighting;
      return this.getAnimalId() == (newSighting.getAnimalId()) && this.getLocation().equals(newSighting.getLocation()) && this.getRangerId() == newSighting.getRangerId();
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO sightings (animal_id, location, ranger_id, sighting_time, status) VALUES (:animal_id, :location, :ranger_id, :sighting_time, :status);";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("animal_id", this.animal_id)
        .addParameter("location", this.location)
        .addParameter("ranger_id", this.ranger_id)
        .addParameter("sighting_time", this.sighting_time)
        .addParameter("status", this.animalEndangeredStatus)
        .throwOnMappingFailure(false)
        .executeUpdate()
        .getKey();
    }
  }

  public static List<Sighting> all() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM sightings;";
      return con.createQuery(sql)
        .throwOnMappingFailure(false)
        .executeAndFetch(Sighting.class);
    }
  }

  public static Sighting find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM sightings WHERE id=:id;";
      Sighting sighting = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Sighting.class);
      return sighting;
    } catch (IndexOutOfBoundsException exception) {
      return null;
    }
  }

}
