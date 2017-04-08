import org.junit.*;
import static org.junit.Assert.*;
import org.sql2o.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class AnimalTest {
  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void animal_instantiatesCorrectly_false() {
    Animal testAnimal = new Animal("Deer", "healthy", "Adult");
    assertEquals(true, testAnimal instanceof Animal);
  }

  @Test
  public void getName_animalInstantiatesWithName_Deer() {
    Animal testAnimal = new Animal("Deer", "healthy", "Adult");
    assertEquals("Deer", testAnimal.getName());
  }

  @Test
  public void equals_returnsTrueIfNameIsTheSame_false() {
    Animal firstAnimal = new Animal("Deer", "healthy", "Adult");
    Animal anotherAnimal = new Animal("Deer", "healthy", "Adult");
    assertTrue(firstAnimal.equals(anotherAnimal));
  }

  @Test
  public void save_assignsIdToObjectAndSavesObjectToDatabase() {
    Animal testAnimal = new Animal("Deer", "healthy", "Adult");
    testAnimal.save();
    Animal savedAnimal = Animal.all().get(0);
    assertEquals(testAnimal.getId(), savedAnimal.getId());
  }

  @Test
  public void all_returnsAllInstancesOfAnimal_false() {
    Animal firstAnimal = new Animal("Deer", "healthy", "Adult");
    firstAnimal.save();
    Animal secondAnimal = new Animal("Black Bear", "healthy", "Adult");
    secondAnimal.save();
    assertEquals(true, Animal.all().get(0).equals(firstAnimal));
    assertEquals(true, Animal.all().get(1).equals(secondAnimal));
  }

  @Test
  public void find_returnsAnimalWithSameId_secondAnimal() {
    Animal firstAnimal = new Animal("Deer", "healthy", "Adult");
    firstAnimal.save();
    Animal secondAnimal = new Animal("Black Bear", "healthy", "Adult");
    secondAnimal.save();
    assertEquals(Animal.find(secondAnimal.getId()), secondAnimal);
  }

  @Test
  public void delete_deletesAnimalFromDatabase_0() {
    Animal testAnimal = new Animal("Deer", "healthy", "Adult");
    testAnimal.save();
    testAnimal.delete();
    assertEquals(0, Animal.all().size());
  }

  public void updateHealth_updatesAnimalHealthInDatabase_String() {
    Animal testAnimal = new Animal("Deer", "healthy", "Adult");
    testAnimal.save();
    testAnimal.updateHealth("Okay");
    assertEquals("Okay", testAnimal.getHealth());
  }

  public void updateAge_updatesAgeInDB() {
    Animal testAnimal = new Animal("Deer", "healthy", "young");
    testAnimal.save();
    testAnimal.updateAge("adult");
    assertEquals("adult", testAnimal.getAge());
  }

  @Test
  public void find_returnsNullWhenNoAnimalFound_null() {
    assertTrue(Animal.find(999) == null);
  }

}
