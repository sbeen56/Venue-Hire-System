package nz.ac.auckland.se281;

import java.util.ArrayList;
import nz.ac.auckland.se281.Types.CateringType;
import nz.ac.auckland.se281.Types.FloralType;

public class VenueHireSystem {
  private ArrayList<Venue> venueList = new ArrayList<Venue>();
  private String systemDate;
  private ArrayList<Booking> bookingList = new ArrayList<Booking>();

  public VenueHireSystem() {}

  public void printVenues() {
    if (venueList.isEmpty()) {
      MessageCli.NO_VENUES.printMessage();
    } else {
      int venueListSize = venueList.size();
      String isOrAre = getIsOrAre(venueListSize);
      String quantity = getQuantityString(venueListSize);
      String singularOrPlural = getSingularOrPlural(venueListSize);
      MessageCli.NUMBER_VENUES.printMessage(isOrAre, quantity, singularOrPlural);

      for (Venue venue : venueList) {
        if (systemDate == null) {
          venue.printDetails("");
        } else {
          venue.printDetails(nextAvailableDate(venue.getName()));
        }
      }
    }
  }

  public void createVenue(
      String venueName, String venueCode, String capacityInput, String hireFeeInput) {
    if (nameIsNotEmpty(venueName)
        && isPositiveNumber(capacityInput, "capacity")
        && isPositiveNumber(hireFeeInput, "hire fee")
        && noDuplicateCodeExists(venueCode)) {
      Venue newVenue = new Venue(venueName, venueCode, capacityInput, hireFeeInput);
      venueList.add(newVenue);
      MessageCli.VENUE_SUCCESSFULLY_CREATED.printMessage(venueName, venueCode);
    }
  }

  public void setSystemDate(String dateInput) {
    systemDate = dateInput;
    MessageCli.DATE_SET.printMessage(dateInput);
  }

  public void printSystemDate() {
    if (systemDate == null) {
      MessageCli.CURRENT_DATE.printMessage("not set");
    } else {
      MessageCli.CURRENT_DATE.printMessage(systemDate);
    }
  }

  public void makeBooking(String[] options) {
    if (systemDate == null) {
      MessageCli.BOOKING_NOT_MADE_DATE_NOT_SET.printMessage();
    } else if (venueList.isEmpty()) {
      MessageCli.BOOKING_NOT_MADE_NO_VENUES.printMessage();
    } else {
      String bookingReference = BookingReferenceGenerator.generateBookingReference();
      String venueName = null;
      int venueCapacity = 0;
      for (Venue venue : venueList) {
        if (venue.isSameCode(options[0])) {
          venueName = venue.getName();
          venueCapacity = venue.getCapacity();
          break;
        }
      }
      if (isNotNull(venueName, options[0])
          && dateIsNotInPast(options[1])
          && isNotBooked(venueName, options[1])) {
        int attendees = Integer.parseInt(options[3]);
        if (attendees > venueCapacity) {
          attendees = venueCapacity;
          MessageCli.BOOKING_ATTENDEES_ADJUSTED.printMessage(
              options[3], Integer.toString(attendees), Integer.toString(venueCapacity));
        } else if (attendees < venueCapacity * 0.25) {
          attendees = (int) (venueCapacity * 0.25);
          MessageCli.BOOKING_ATTENDEES_ADJUSTED.printMessage(
              options[3], Integer.toString(attendees), Integer.toString(venueCapacity));
        }
        Booking newBooking = new Booking(venueName, bookingReference, options[1]);
        bookingList.add(newBooking);
        MessageCli.MAKE_BOOKING_SUCCESSFUL.printMessage(
            bookingReference, venueName, options[1], Integer.toString(attendees));
      }
    }
  }

  public void printBookings(String venueCode) {
    if (venueExists(venueCode)) {
      if (!bookingExists(venueCode)) {
        for (Venue venue : venueList) {
          if (venue.isSameCode(venueCode)) {
            String venueName = venue.getName();
            MessageCli.PRINT_BOOKINGS_NONE.printMessage(venueName);
          }
        }
      }
    } else {
      MessageCli.PRINT_BOOKINGS_VENUE_NOT_FOUND.printMessage(venueCode);
    }
  }

  public void addCateringService(String bookingReference, CateringType cateringType) {
    if (bookingReferenceExists(bookingReference)) {
      String service = "Catering (" + cateringType.getName() + ")";
      MessageCli.ADD_SERVICE_SUCCESSFUL.printMessage(service, bookingReference);
    } else {
      MessageCli.SERVICE_NOT_ADDED_BOOKING_NOT_FOUND.printMessage("Catering", bookingReference);
    }
  }

  public void addServiceMusic(String bookingReference) {
    if (bookingReferenceExists(bookingReference)) {
      MessageCli.ADD_SERVICE_SUCCESSFUL.printMessage("Music", bookingReference);
    } else {
      MessageCli.SERVICE_NOT_ADDED_BOOKING_NOT_FOUND.printMessage("Music", bookingReference);
    }
  }

  public void addServiceFloral(String bookingReference, FloralType floralType) {
    if (!bookingReferenceExists(bookingReference)) {
      MessageCli.SERVICE_NOT_ADDED_BOOKING_NOT_FOUND.printMessage("Floral", bookingReference);
    }
  }

  public void viewInvoice(String bookingReference) {
    // TODO implement this method
  }

  private boolean isPositiveNumber(String stringToCheck, String propartyName) {
    try {
      int parsedNumber = Integer.parseInt(stringToCheck);
      if (parsedNumber <= 0) {
        MessageCli.VENUE_NOT_CREATED_INVALID_NUMBER.printMessage(propartyName, " positive");
        return false;
      }
      return true;
    } catch (NumberFormatException e) {
      MessageCli.VENUE_NOT_CREATED_INVALID_NUMBER.printMessage(propartyName, "");
      return false;
    }
  }

  private String getIsOrAre(int venueListSize) {
    if (venueListSize == 1) {
      return "is";
    } else {
      return "are";
    }
  }

  private String getQuantityString(int venueListSize) {
    if (venueListSize < 10) {
      String[] numberNames = {
        "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"
      };
      return numberNames[venueListSize - 1];
    } else {
      return Integer.toString(venueListSize);
    }
  }

  private String getSingularOrPlural(int venueListSize) {
    if (venueListSize == 1) {
      return "";
    } else {
      return "s";
    }
  }

  private boolean noDuplicateCodeExists(String venueCode) {
    for (Venue venue : venueList) {
      if (venue.isSameCode(venueCode)) {
        MessageCli.VENUE_NOT_CREATED_CODE_EXISTS.printMessage(venueCode, venue.getName());
        return false;
      }
    }
    return true;
  }

  private boolean nameIsNotEmpty(String venueName) {
    if (venueName.trim().isEmpty()) {
      MessageCli.VENUE_NOT_CREATED_EMPTY_NAME.printMessage();
      return false;
    }
    return true;
  }

  private boolean isNotBooked(String venueName, String bookingDate) {
    for (Booking booking : bookingList) {
      if (booking.isSameBooking(venueName, bookingDate)) {
        MessageCli.BOOKING_NOT_MADE_VENUE_ALREADY_BOOKED.printMessage(venueName, bookingDate);
        return false;
      }
    }
    return true;
  }

  private boolean dateIsNotInPast(String bookingDate) {
    String[] bookingDateParts = bookingDate.split("/");
    int bookingDay = Integer.parseInt(bookingDateParts[0]);
    int bookingMonth = Integer.parseInt(bookingDateParts[1]);
    int bookingYear = Integer.parseInt(bookingDateParts[2]);

    String[] systemDateParts = systemDate.split("/");
    int systemDay = Integer.parseInt(systemDateParts[0]);
    int systemMonth = Integer.parseInt(systemDateParts[1]);
    int systemYear = Integer.parseInt(systemDateParts[2]);

    if (bookingYear > systemYear) {
      return true;
    } else if (bookingYear == systemYear) {
      if (bookingMonth > systemMonth) {
        return true;
      } else if (bookingMonth == systemMonth) {
        if (bookingDay >= systemDay) {
          return true;
        }
      }
    }
    MessageCli.BOOKING_NOT_MADE_PAST_DATE.printMessage(bookingDate, systemDate);
    return false;
  }

  private boolean isNotNull(String venueName, String venueCode) {
    if (venueName == null) {
      MessageCli.BOOKING_NOT_MADE_VENUE_NOT_FOUND.printMessage(venueCode);
      return false;
    }
    return true;
  }

  private String increaseDate(String dateToIncrease) {
    String[] dateToIncreaseParts = dateToIncrease.split("/");
    int dateToIncreaseDay = Integer.parseInt(dateToIncreaseParts[0]);
    int dateToIncreaseMonth = Integer.parseInt(dateToIncreaseParts[1]);
    int dateToIncreaseYear = Integer.parseInt(dateToIncreaseParts[2]);

    int daysInMonth[] = {31, 28, 31, 30, 31, 30, 31, 30, 31, 30, 31, 30};

    dateToIncreaseDay++;
    if (dateToIncreaseDay > daysInMonth[dateToIncreaseMonth - 1]) {
      dateToIncreaseDay = 1;
      dateToIncreaseMonth++;
      if (dateToIncreaseMonth > 12) {
        dateToIncreaseMonth = 1;
        dateToIncreaseYear++;
      }
    }
    return String.format(
        "%02d/%02d/%04d", dateToIncreaseDay, dateToIncreaseMonth, dateToIncreaseYear);
  }

  private String nextAvailableDate(String venueName) {
    boolean dateAvailability = false;
    String nextAvailableDate = systemDate;

    if (bookingList == null) {
      return systemDate;
    }

    while (!dateAvailability) {
      dateAvailability = true;
      for (Booking booking : bookingList) {
        if (booking.isSameBooking(venueName, nextAvailableDate)) {
          dateAvailability = false;
          nextAvailableDate = increaseDate(nextAvailableDate);
          break;
        }
      }
    }

    return nextAvailableDate;
  }

  private boolean venueExists(String venueCode) {
    for (Venue venue : venueList) {
      if (venue.isSameCode(venueCode)) {
        MessageCli.PRINT_BOOKINGS_HEADER.printMessage(venue.getName());
        return true;
      }
    }
    return false;
  }

  private boolean bookingExists(String venueCode) {
    boolean bookingExists = false;
    for (Booking booking : bookingList) {
      for (Venue venue : venueList) {
        if (venue.isSameCode(venueCode)) {
          String venueName = venue.getName();
          if (booking.getVenueName().equals(venueName)) {
            MessageCli.PRINT_BOOKINGS_ENTRY.printMessage(
                booking.getBookingReference(), booking.getBookingDate());
            bookingExists = true;
          }
        }
      }
    }
    if (bookingExists) {
      return true;
    }
    return false;
  }

  private boolean bookingReferenceExists(String bookingReference) {
    for (Booking booking : bookingList) {
      if (booking.getBookingReference().equals(bookingReference)) {
        return true;
      }
    }
    return false;
  }
}
