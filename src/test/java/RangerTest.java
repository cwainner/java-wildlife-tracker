import org.junit.*;
import static org.junit.Assert.*;

import java.util.Arrays;

public class RangerTest{
  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void ranger_initializesCorrectly(){
    Ranger testRanger = new Ranger("Joe", "999-999-9999", 1);
    assertTrue(testRanger instanceof Ranger);
  }

  @Test
  public void getName_returnsName(){
    Ranger testRanger = new Ranger("Joe", "999-999-9999", 1);
    assertEquals("Joe", testRanger.getName());
  }

  @Test
  public void getContactInfo_returnsContactInfo(){
    Ranger testRanger = new Ranger("Joe", "999-999-9999", 1);
    assertEquals("999-999-9999", testRanger.getContactInfo());
  }

  @Test
  public void getBadgeNo_returnsBadgeNo(){
    Ranger testRanger = new Ranger("Joe", "999-999-9999", 1);
    assertEquals(1, testRanger.getBadgeNo());
  }

  @Test
  public void save_savesIntoDB(){
    Ranger testRanger = new Ranger("Joe", "999-999-9999", 1);
    testRanger.save();
    assertEquals(Ranger.all().get(0), testRanger);
  }

  @Test
  public void all_returnsAllInstancesOfRanger(){
    Ranger firstRanger = new Ranger("Joe", "999-999-9999", 1);
    firstRanger.save();
    Ranger secondRanger = new Ranger("Amy", "999-999-9999", 2);
    secondRanger.save();
    Ranger[] rangers = new Ranger[] {firstRanger, secondRanger};
    assertTrue(Ranger.all().containsAll(Arrays.asList(rangers)));
  }

  @Test
  public void find_returnsCorrectRanger(){
    Ranger testRanger = new Ranger("Joe", "999-999-9999", 1);
    testRanger.save();
    Ranger savedRanger = Ranger.find(testRanger.getId());
    assertEquals(testRanger, savedRanger);
  }

  @Test
  public void save_assignsId(){
    Ranger testRanger = new Ranger("Joe", "999-999-9999", 1);
    testRanger.save();
    Ranger saveRanger = Ranger.find(testRanger.getId());
    assertEquals(saveRanger.getId(), testRanger.getId());
  }

  @Test
  public void getSightings_returnsAllSightingsForRanger(){
    Ranger testRanger = new Ranger("Joe", "999-999-9999", 1);
    testRanger.save();
    Sighting testSighting = new Sighting(1, "River", testRanger.getId(), false);
    testSighting.save();
    assertTrue(testRanger.getSightings().get(0).equals(testSighting));
  }
}