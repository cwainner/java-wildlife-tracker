import org.junit.*;
import static org.junit.Assert.*;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.Date;

public class SightingTest {

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void sighting_instantiatesCorrectly_true() {
    Animal testAnimal = new Animal("Deer");
    testAnimal.save();
    Sighting testSighting = new Sighting(testAnimal.getName(), testAnimal.getId(), "45.472428, -121.946466", 1, testAnimal.getStatus());
    assertEquals(true, testSighting instanceof Sighting);
  }

  @Test
  public void getAnimalId_returnsCorrectAnimalId(){
    Animal testAnimal = new Animal("Deer");
    testAnimal.save();
    Sighting testSighting = new Sighting(testAnimal.getName(), testAnimal.getId(), "45.472428, -121.946466", 1, testAnimal.getStatus());
    assertEquals(testAnimal.getId(), testSighting.getAnimalId());
  }

  @Test
  public void getAnimalName_returnsCorrectAnimalName(){
    Animal testAnimal = new Animal("Deer");
    testAnimal.save();
    Sighting testSighting = new Sighting(testAnimal.getName(), testAnimal.getId(), "45.472428, -121.946466", 1, testAnimal.getStatus());
    assertEquals(testAnimal.getName(), testSighting.getAnimalName());
  }

  @Test
  public void getLocation_returnsCorrectLocation(){
    Animal testAnimal = new Animal("Deer");
    testAnimal.save();
    Sighting testSighting = new Sighting(testAnimal.getName(), testAnimal.getId(), "45.472428, -121.946466", 1, testAnimal.getStatus());
    assertEquals("45.472428, -121.946466", testSighting.getLocation());
  }

  @Test
  public void getRangerId_returnsRangerId(){
    Animal testAnimal = new Animal("Deer");
    testAnimal.save();
    Sighting testSighting = new Sighting(testAnimal.getName(), testAnimal.getId(), "45.472428, -121.946466", 1, testAnimal.getStatus());
    assertEquals(1, testSighting.getRangerId());
  }

  @Test
  public void equals_returnsTrueIfLocationAndDescriptionAreSame_true() {
    Animal testAnimal = new Animal("Deer");
    testAnimal.save();
    Sighting testSighting = new Sighting(testAnimal.getName(), testAnimal.getId(), "45.472428, -121.946466", 1, testAnimal.getStatus());
    Sighting anotherSighting = new Sighting(testAnimal.getName(), testAnimal.getId(), "45.472428, -121.946466", 1, testAnimal.getStatus());
    assertTrue(testSighting.equals(anotherSighting));
  }

  @Test
  public void save_insertsObjectIntoDatabase_Sighting() {
    Animal testAnimal = new Animal("Deer");
    testAnimal.save();
    Sighting testSighting = new Sighting (testAnimal.getName(), testAnimal.getId(), "45.472428, -121.946466", 1, testAnimal.getStatus());
    testSighting.save();
    assertEquals(true, Sighting.all().get(0).equals(testSighting));
  }

  @Test
  public void all_returnsAllInstancesOfSighting_true() {
    Animal testAnimal = new Animal("Deer");
    testAnimal.save();
    Sighting testSighting = new Sighting (testAnimal.getName(), testAnimal.getId(), "45.472428, -121.946466", 1, testAnimal.getStatus());
    testSighting.save();
    Animal secondTestAnimal = new Animal("Badger");
    secondTestAnimal.save();
    Sighting secondTestSighting = new Sighting (testAnimal.getName(), testAnimal.getId(), "45.472428, -121.946466", 2, testAnimal.getStatus());
    secondTestSighting.save();
    assertEquals(true, Sighting.all().get(0).equals(testSighting));
    assertEquals(true, Sighting.all().get(1).equals(secondTestSighting));
  }

  @Test
  public void find_returnsSightingWithSameId_secondSighting() {
    Animal testAnimal = new Animal("Deer");
    testAnimal.save();
    Sighting testSighting = new Sighting (testAnimal.getName(), testAnimal.getId(), "45.472428, -121.946466", 1, testAnimal.getStatus());
    testSighting.save();
    Animal secondTestAnimal = new Animal("Badger");
    secondTestAnimal.save();
    Sighting secondTestSighting = new Sighting (secondTestAnimal.getName(), testAnimal.getId(), "45.472428, -121.946466", 2, testAnimal.getStatus());
    secondTestSighting.save();
    assertEquals(Sighting.find(secondTestSighting.getId()), secondTestSighting);
  }

  @Test
  public void find_returnsNullWhenNoAnimalFound_null() {
    assertTrue(Animal.find(999) == null);
  }

  @Test
  public void getLastSighting_returnsLastSightingTimestamp(){
    Animal testAnimal = new Animal("Deer");
    testAnimal.save();
    Sighting testSighting = new Sighting (testAnimal.getName(), testAnimal.getId(), "45.472428, -121.946466", 1, testAnimal.getStatus());
    testSighting.save();
    Timestamp rightNow = new Timestamp(new Date().getTime());
    assertEquals(DateFormat.getDateTimeInstance().format(rightNow), testSighting.getLastSighting());
  }

}
