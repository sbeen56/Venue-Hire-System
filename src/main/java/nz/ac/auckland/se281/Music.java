package nz.ac.auckland.se281;

public class Music extends Service {
  public Music(String bookingReference, String attendees) {
    super(bookingReference, attendees);
  }

  @Override
  public int cost() {
    return 500;
  }
}
