import org.sql2o.*;
import java.util.*;

public class Animal implements Entity{
  private String name;
  private int id;
  private String health;
  private String age;
  
  private final boolean endangered = false;

  public Animal(String name, String health, String age) {
    if(name.equals("") || health.equals("") || age.equals("")){
      throw new IllegalArgumentException("You cannot have an empty input");
    }
    this.name = name;
    this.health = health;
    this.age = age;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public int getId() {
    return id;
  }

  public String getHealth(){
    return health;
  }

  public String getAge(){
    return age;
  }

  public boolean getStatus(){
    return endangered;
  }

  @Override
  public boolean equals(Object otherAnimal) {
    if(!(otherAnimal instanceof Animal)) {
      return false;
    } else {
      Animal newAnimal = (Animal) otherAnimal;
      return this.getName().equals(newAnimal.getName());
    }
  }

  @Override
  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO animals (name, health, age) VALUES (:name, :health, :age);";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("name", this.name)
        .addParameter("health", this.health)
        .addParameter("age", this.age)
        .executeUpdate()
        .getKey();
    }
  }

  public void updateHealth(String health) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE animals SET health=:health WHERE id=:id;";
      con.createQuery(sql)
        .addParameter("id", id)
        .addParameter("health", health)
        .executeUpdate();
    }
  }

  public void updateAge(String age) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE animals SET age=:age WHERE id=:id;";
      con.createQuery(sql)
        .addParameter("id", id)
        .addParameter("age", age)
        .executeUpdate();
    }
  }

  @Override
  public void delete() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "DELETE FROM animals WHERE id=:id;";
      con.createQuery(sql)
        .addParameter("id", this.id)
        .executeUpdate();

      String sightingsDeleteQuery = "DELETE FROM sightings WHERE animal_id = :id AND status = :status";
      con.createQuery(sightingsDeleteQuery)
        .addParameter("id", this.id)
        .addParameter("status", this.endangered)
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

  public static List<Animal> all() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM animals;";
      return con.createQuery(sql)
        .executeAndFetch(Animal.class);
    }
  }

  public static Animal find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM animals WHERE id=:id;";
      Animal animal = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Animal.class);
      return animal;
    }
  }
}
