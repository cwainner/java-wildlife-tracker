import org.sql2o.*;
import java.util.*;

public class EndangeredAnimal implements Entity{
  private String name;
  private int id;
  private String health;
  private String age;

  private final boolean endangered = true;

  public EndangeredAnimal(String name, String health, String age) {
    if(name.equals("") || health.equals("") || age.equals("")){
      throw new IllegalArgumentException("You cannot have empty inputs");
    }
    this.name = name;
    this.health = health;
    this.age = age;
  }

  public String getHealth() {
    return health;
  }

  public String getAge() {
    return age;
  }

  public boolean getStatus(){
    return endangered;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public int getId() {
    return id;
  }

  @Override
  public boolean equals(Object otherEndangeredAnimal) {
    if(!(otherEndangeredAnimal instanceof EndangeredAnimal)) {
      return false;
    } else {
      EndangeredAnimal newEndangeredAnimal = (EndangeredAnimal) otherEndangeredAnimal;
      return this.getName().equals(newEndangeredAnimal.getName()) && this.getHealth().equals(newEndangeredAnimal.getHealth()) && this.getAge().equals(newEndangeredAnimal.getAge());
    }
  }

  @Override
  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO endangered_animals (name, health, age) VALUES (:name, :health, :age);";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("name", this.name)
        .addParameter("health", this.health)
        .addParameter("age", this.age)
        .executeUpdate()
        .getKey();
    }
  }

  @Override
  public void delete(){
    try(Connection con = DB.sql2o.open()){
      String sql = "DELETE FROM endangered_animals WHERE id=:id";
      con.createQuery(sql)
        .addParameter("id", this.getId())
        .executeUpdate();
      
      String sightingDeleteQuery = "DELETE FROM sightings WHERE animal_id = :id AND status=:status";
      con.createQuery(sightingDeleteQuery)
        .addParameter("id", this.id)
        .addParameter("status", this.endangered)
        .executeUpdate();
    }
  }

  public void updateHealth(String health) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE endangered_animals SET health=:health WHERE id=:id;";
      con.createQuery(sql)
        .addParameter("id", id)
        .addParameter("health", health)
        .executeUpdate();
    }
  }

  public void updateAge(String age) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE endangered_animals SET age=:age WHERE id=:id;";
      con.createQuery(sql)
        .addParameter("id", id)
        .addParameter("age", age)
        .executeUpdate();
    }
  }

  public List<Sighting> getSightings() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM sightings WHERE animal_id=:id AND status=:status;";
        List<Sighting> sightings = con.createQuery(sql)
          .addParameter("id", id)
          .addParameter("status", this.endangered)
          .executeAndFetch(Sighting.class);
      return sightings;
    }
  }

  public static List<EndangeredAnimal> all() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM endangered_animals;";
      return con.createQuery(sql)
        .executeAndFetch(EndangeredAnimal.class);
    }
  }

  public static EndangeredAnimal find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM endangered_animals WHERE id=:id;";
      EndangeredAnimal endangeredanimal = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(EndangeredAnimal.class);
      return endangeredanimal;
    }
  }
}
