package nz.ac.auckland.se281;

public class Booking {
  private String venueName;
  private String bookingReference;
  private String bookingDate;
  private String email;
  private String attendees;

  public Booking(
      String venueName,
      String bookingReference,
      String bookingDate,
      String email,
      String attendees) {
    this.venueName = venueName;
    this.bookingReference = bookingReference;
    this.bookingDate = bookingDate;
    this.email = email;
    this.attendees = attendees;
  }

  public Boolean isSameBooking(String venueName, String bookingDate) {
    return this.venueName.equals(venueName) && this.bookingDate.equals(bookingDate);
  }

  public String getBookingDate() {
    return bookingDate;
  }

  public String getVenueName() {
    return venueName;
  }

  public String getBookingReference() {
    return bookingReference;
  }

  public String getEmail() {
    return email;
  }

  public String getAttendees() {
    return attendees;
  }
}
