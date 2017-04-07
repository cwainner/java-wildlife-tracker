import org.sql2o.*;
import java.util.*;

public class Ranger implements Entity{
  private int id;
  private String name;
  private String contactInfo;
  private int badgeNo;

  public Ranger(String name, String contactInfo, int badgeNo){
    this.name = name;
    this.contactInfo = contactInfo;
    this.badgeNo = badgeNo;
  }

  @Override
  public int getId(){
    return id;
  }

  @Override
  public String getName(){
    return name;
  }

  public String getContactInfo(){
    return contactInfo;
  }

  public int getBadgeNo(){
    return badgeNo;
  }

  @Override
  public void save(){
    try(Connection con = DB.sql2o.open()){
      String sql = "INSERT INTO rangers (name, contactinfo, badgeno) VALUES (:name, :contactinfo, :badgeno)";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("name", this.name)
        .addParameter("contactinfo", this.contactInfo)
        .addParameter("badgeno", this.badgeNo)
        .executeUpdate()
        .getKey();
    }
  }

  @Override
  public void delete(){
    try(Connection con = DB.sql2o.open()){
      String sql = "DELETE FROM rangers WHERE id=:id";
      con.createQuery(sql)
        .addParameter("id", this.id)
        .executeUpdate();
    }
  }

  @Override
  public boolean equals(Object otherRanger){
    if(!(otherRanger instanceof Ranger)){
      return false;
    } else{
      Ranger newRanger = (Ranger) otherRanger;
      return this.getName().equals(newRanger.getName()) && this.getId() == newRanger.getId();
    }
  }

  public List<Sighting> getSightings(){
    try(Connection con = DB.sql2o.open()){
      String sql = "SELECT * FROM sightings WHERE ranger_id = :id";
      return con.createQuery(sql)
        .addParameter("id", this.id)
        .executeAndFetch(Sighting.class);
    }
  }

  public static List<Ranger> all(){
    try(Connection con = DB.sql2o.open()){
      String sql = "SELECT * FROM rangers";
      return con.createQuery(sql)
        .executeAndFetch(Ranger.class);
    }
  }

  public static Ranger find(int id){
    try(Connection con = DB.sql2o.open()){
      String sql = "SELECT * FROM rangers WHERE id=:id";
      return con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Ranger.class);
    }
  }
}