package nz.ac.auckland.se281;

import nz.ac.auckland.se281.Types.CateringType;

public class Catering extends Service {
  private CateringType type;

  public Catering(String bookingReference, String attendees, CateringType type) {
    super(bookingReference, attendees);
    this.type = type;
  }

  @Override
  public int cost() {
    return Integer.parseInt(getAttendees()) * type.getCostPerPerson();
  }
}
