public interface Entity{
  public String getName();
  public int getId();
  public void save();
  public void delete();
  public boolean equals(Object otherObject);
}