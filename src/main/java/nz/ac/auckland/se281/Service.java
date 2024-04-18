package nz.ac.auckland.se281;

public abstract class Service {
  private String bookingReference;
  private String attendees;

  public Service(String bookingReference, String attendees) {
    this.bookingReference = bookingReference;
    this.attendees = attendees;
  }

  public String getBookingReference() {
    return bookingReference;
  }

  public String getAttendees() {
    return attendees;
  }

  public abstract int cost();
}
