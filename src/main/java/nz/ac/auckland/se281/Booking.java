package nz.ac.auckland.se281;

public class Booking {
  private String venueName;
  private String bookingReference;
  private String bookingDate;

  public Booking(String venueName, String bookingReference, String bookingDate) {
    this.venueName = venueName;
    this.bookingReference = bookingReference;
    this.bookingDate = bookingDate;
  }

  public Boolean isSameBooking(String venueName, String bookingDate) {
    return this.venueName.equals(venueName) && this.bookingDate.equals(bookingDate);
  }

  public String getBookingDate() {
    return bookingDate;
  }
}
