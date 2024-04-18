package nz.ac.auckland.se281;

public abstract class Service {
  private String bookingReference;

  public Service(String bookingReference) {
    this.bookingReference = bookingReference;
  }

  public String getBookingReference() {
    return bookingReference;
  }
}
