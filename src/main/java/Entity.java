public interface Entity{
  public String getName();
  public int getId();
  public void save();
  public void delete();
  public void updateName(String name);
  public boolean equals(Object otherObject);
}