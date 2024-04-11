package nz.ac.auckland.se281;

public class Venue {
  private String name;
  private String code;
  private String capacity;
  private String hireFee;

  public Venue(String name, String code, String capacity, String hireFee) {
    this.name = name;
    this.code = code;
    this.capacity = capacity;
    this.hireFee = hireFee;
  }

  public void printDetails(String nextDate) {
    MessageCli.VENUE_ENTRY.printMessage(name, code, capacity, hireFee, nextDate);
  }

  public boolean isSameCode(String venueToCompare) {
    return this.code.equals(venueToCompare);
  }

  public String getName() {
    return name;
  }

  public int getCapacity() {
    return Integer.parseInt(capacity);
  }
}
