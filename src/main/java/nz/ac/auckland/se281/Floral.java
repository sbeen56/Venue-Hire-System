package nz.ac.auckland.se281;

import nz.ac.auckland.se281.Types.FloralType;

public class Floral extends Service {
  private FloralType type;

  public Floral(String bookingReference, String attendees, FloralType type) {
    super(bookingReference, attendees);
    this.type = type;
  }

  @Override
  public int cost() {
    return Integer.parseInt(getAttendees()) * type.getCost();
  }
}
