package nz.ac.auckland.se281;

import java.util.ArrayList;
import nz.ac.auckland.se281.Types.CateringType;
import nz.ac.auckland.se281.Types.FloralType;

public class VenueHireSystem {
  private ArrayList<Venue> venueList = new ArrayList<Venue>();
  private String systemDate;
  private ArrayList<Booking> bookingList = new ArrayList<Booking>();
  private ArrayList<Service> serviceList = new ArrayList<Service>();

  public VenueHireSystem() {}

  // printVenues is a method that prints venues
  public void printVenues() {
    // Check if venueList empty
    if (venueList.isEmpty()) {
      MessageCli.NO_VENUES.printMessage();
    } else {
      // Get appropriate grammar, quantity, and singular or plural
      int venueListSize = venueList.size();
      String isOrAre = getIsOrAre(venueListSize);
      String quantity = getQuantityString(venueListSize);
      String singularOrPlural = getSingularOrPlural(venueListSize);
      MessageCli.NUMBER_VENUES.printMessage(isOrAre, quantity, singularOrPlural);

      // Iterate through venueList and print details
      for (Venue venue : venueList) {
        if (systemDate == null) {
          venue.printDetails("");
        } else {
          venue.printDetails(nextAvailableDate(venue.getName()));
        }
      }
    }
  }

  // createVenue is a method that creates a new venue
  public void createVenue(
      String venueName, String venueCode, String capacityInput, String hireFeeInput) {
    if (nameIsNotEmpty(venueName)
        && isPositiveNumber(capacityInput, "capacity")
        && isPositiveNumber(hireFeeInput, "hire fee")
        && noDuplicateCodeExists(venueCode)) {
      // Create new venue and add to venueList
      Venue newVenue = new Venue(venueName, venueCode, capacityInput, hireFeeInput);
      venueList.add(newVenue);
      MessageCli.VENUE_SUCCESSFULLY_CREATED.printMessage(venueName, venueCode);
    }
  }

  // setSystemDate is a method that sets the system date
  public void setSystemDate(String dateInput) {
    systemDate = dateInput;
    MessageCli.DATE_SET.printMessage(dateInput);
  }

  // printSystemDate is a method that prints the system date
  public void printSystemDate() {
    // If systemDate does not exist print message
    if (systemDate == null) {
      MessageCli.CURRENT_DATE.printMessage("not set");
    } else {
      // If systemDate exists print systemDate
      MessageCli.CURRENT_DATE.printMessage(systemDate);
    }
  }

  // makeBooking is a method that makes a booking
  public void makeBooking(String[] options) {
    if (systemDate == null) {
      MessageCli.BOOKING_NOT_MADE_DATE_NOT_SET.printMessage();
    } else if (venueList.isEmpty()) {
      MessageCli.BOOKING_NOT_MADE_NO_VENUES.printMessage();
    } else {
      // Generate booking reference
      String bookingReference = BookingReferenceGenerator.generateBookingReference();
      String venueName = null;
      int venueCapacity = 0;
      // Find venue in venueList by code
      for (Venue venue : venueList) {
        if (venue.isSameCode(options[0])) {
          venueName = venue.getName();
          venueCapacity = venue.getCapacity();
          break;
        }
      }
      // Validate booking options
      if (isNotNull(venueName, options[0])
          && dateIsNotInPast(options[1])
          && isNotBooked(venueName, options[1])) {
        int attendees = Integer.parseInt(options[3]);
        // Adjust attendees when attendees are more than 100% or less than 25% of the venueCapacity
        if (attendees > venueCapacity) {
          attendees = venueCapacity;
          MessageCli.BOOKING_ATTENDEES_ADJUSTED.printMessage(
              options[3], Integer.toString(attendees), Integer.toString(venueCapacity));
        } else if (attendees < venueCapacity * 0.25) {
          attendees = (int) (venueCapacity * 0.25);
          MessageCli.BOOKING_ATTENDEES_ADJUSTED.printMessage(
              options[3], Integer.toString(attendees), Integer.toString(venueCapacity));
        }
        // Create a new booking and add to bookingList
        Booking newBooking =
            new Booking(
                venueName, bookingReference, options[1], options[2], Integer.toString(attendees));
        bookingList.add(newBooking);
        MessageCli.MAKE_BOOKING_SUCCESSFUL.printMessage(
            bookingReference, venueName, options[1], Integer.toString(attendees));
      }
    }
  }

  // printBookings is a method that prints bookings for a specific venue
  public void printBookings(String venueCode) {
    if (venueExists(venueCode)) {
      if (!bookingExists(venueCode)) {
        // Print message when existing venue has no bookings
        for (Venue venue : venueList) {
          if (venue.isSameCode(venueCode)) {
            String venueName = venue.getName();
            MessageCli.PRINT_BOOKINGS_NONE.printMessage(venueName);
          }
        }
      }
    } else {
      // Print message when venue does not exist
      MessageCli.PRINT_BOOKINGS_VENUE_NOT_FOUND.printMessage(venueCode);
    }
  }

  // addCateringService is a method that adds catering service to a booking
  public void addCateringService(String bookingReference, CateringType cateringType) {
    if (bookingReferenceExists(bookingReference)) {
      for (Booking booking : bookingList) {
        if (booking.getBookingReference().equals(bookingReference)) {
          // Create a new catering service and add to serviceList
          Catering newService =
              new Catering(bookingReference, booking.getAttendees(), cateringType);
          serviceList.add(newService);
        }
      }
      String service = "Catering (" + cateringType.getName() + ")";
      MessageCli.ADD_SERVICE_SUCCESSFUL.printMessage(service, bookingReference);
    } else {
      // Print error message when booking does not exist
      MessageCli.SERVICE_NOT_ADDED_BOOKING_NOT_FOUND.printMessage("Catering", bookingReference);
    }
  }

  // addServiceMusic is a method that adds music service to a booking
  public void addServiceMusic(String bookingReference) {
    if (bookingReferenceExists(bookingReference)) {
      for (Booking booking : bookingList) {
        if (booking.getBookingReference().equals(bookingReference)) {
          // Create a new music service and add to serviceList
          Music newService = new Music(bookingReference, booking.getAttendees());
          serviceList.add(newService);
        }
      }
      MessageCli.ADD_SERVICE_SUCCESSFUL.printMessage("Music", bookingReference);
    } else {
      // Print error message when booking does not exist
      MessageCli.SERVICE_NOT_ADDED_BOOKING_NOT_FOUND.printMessage("Music", bookingReference);
    }
  }

  // addServiceFloral is a method that adds floral service to a booking
  public void addServiceFloral(String bookingReference, FloralType floralType) {
    if (bookingReferenceExists(bookingReference)) {
      for (Booking booking : bookingList) {
        if (booking.getBookingReference().equals(bookingReference)) {
          // Create a new floral service and add to serviceList
          Floral newService = new Floral(bookingReference, booking.getAttendees(), floralType);
          serviceList.add(newService);
        }
      }
      String service = "Floral (" + floralType.getName() + ")";
      MessageCli.ADD_SERVICE_SUCCESSFUL.printMessage(service, bookingReference);
    } else {
      // Print error message when booking does not exist
      MessageCli.SERVICE_NOT_ADDED_BOOKING_NOT_FOUND.printMessage("Floral", bookingReference);
    }
  }

  // viewInvoice is a method that views invoice for a booking
  public void viewInvoice(String bookingReference) {
    if (bookingReferenceExists(bookingReference)) {
      for (Booking booking : bookingList) {
        if (booking.getBookingReference().equals(bookingReference)) {
          // Print bottom half of invoice
          MessageCli.INVOICE_CONTENT_TOP_HALF.printMessage(
              bookingReference,
              booking.getEmail(),
              systemDate,
              booking.getBookingDate(),
              booking.getAttendees(),
              booking.getVenueName());

          int totalCost = 0;
          // Print venue hireFee and add to totalCost
          for (Venue venue : venueList) {
            if (venue.getName().equals(booking.getVenueName())) {
              totalCost += Integer.parseInt(venue.getHireFee());
              MessageCli.INVOICE_CONTENT_VENUE_FEE.printMessage(venue.getHireFee());
            }
          }

          // Print additional service costs and add to totalCost
          for (Service service : serviceList) {
            if (service.getBookingReference().equals(bookingReference)) {
              totalCost += service.cost();
              if (service instanceof Catering) {
                Catering catering = (Catering) service;
                MessageCli.INVOICE_CONTENT_CATERING_ENTRY.printMessage(
                    catering.getTypeName(), Integer.toString(service.cost()));
              }
              if (service instanceof Music) {
                MessageCli.INVOICE_CONTENT_MUSIC_ENTRY.printMessage(
                    Integer.toString(service.cost()));
              }
              if (service instanceof Floral) {
                Floral floral = (Floral) service;
                MessageCli.INVOICE_CONTENT_FLORAL_ENTRY.printMessage(
                    floral.getTypeName(), Integer.toString(service.cost()));
              }
            }
          }
          // Print bottom half of invoice with totalCost
          MessageCli.INVOICE_CONTENT_BOTTOM_HALF.printMessage(Integer.toString(totalCost));
        }
      }
    } else {
      // Print error message when booking does not exist
      MessageCli.VIEW_INVOICE_BOOKING_NOT_FOUND.printMessage(bookingReference);
    }
  }

  // isPositiveNumber is a method that checks if a string represents a positive number
  private boolean isPositiveNumber(String stringToCheck, String propartyName) {
    try {
      // Converts string number to integer number
      int parsedNumber = Integer.parseInt(stringToCheck);
      // If number is negative return false
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

  // getIsOrAre is a method that gets the appropriate grammar 'is' or 'are'
  private String getIsOrAre(int venueListSize) {
    if (venueListSize == 1) {
      return "is";
    } else {
      return "are";
    }
  }

  // getQuantityString is a method that gets the appropriate quantity string for venue count
  private String getQuantityString(int venueListSize) {
    // If number is smaller than 10, change number to quantity string
    if (venueListSize < 10) {
      // Make list that has quantity strings between one to nine
      String[] numberNames = {
        "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"
      };
      return numberNames[venueListSize - 1];
    } else {
      // If number is not smaller than 10, don't change
      return Integer.toString(venueListSize);
    }
  }

  // getSingularOrPlural is a method that gets if venueList has a single venue or multiple venues
  private String getSingularOrPlural(int venueListSize) {
    if (venueListSize == 1) {
      return "";
    } else {
      return "s";
    }
  }

  // noDuplicateCodeExists is a method that checks is venueCode already exists in system
  private boolean noDuplicateCodeExists(String venueCode) {
    for (Venue venue : venueList) {
      if (venue.isSameCode(venueCode)) {
        MessageCli.VENUE_NOT_CREATED_CODE_EXISTS.printMessage(venueCode, venue.getName());
        return false;
      }
    }
    return true;
  }

  // nameIsNotEmpty is a method that checks if venue name is empty
  private boolean nameIsNotEmpty(String venueName) {
    if (venueName.trim().isEmpty()) {
      MessageCli.VENUE_NOT_CREATED_EMPTY_NAME.printMessage();
      return false;
    }
    return true;
  }

  // isNotBooked is a method that checks if a venue is not already booked on a specific date
  private boolean isNotBooked(String venueName, String bookingDate) {
    for (Booking booking : bookingList) {
      if (booking.isSameBooking(venueName, bookingDate)) {
        MessageCli.BOOKING_NOT_MADE_VENUE_ALREADY_BOOKED.printMessage(venueName, bookingDate);
        return false;
      }
    }
    return true;
  }

  // dateIsNotpast is a method that checks if booking date is not in the past
  private boolean dateIsNotInPast(String bookingDate) {
    // Compare bookingDate with the systemDate
    String[] bookingDateParts = bookingDate.split("/");
    int bookingDay = Integer.parseInt(bookingDateParts[0]);
    int bookingMonth = Integer.parseInt(bookingDateParts[1]);
    int bookingYear = Integer.parseInt(bookingDateParts[2]);

    String[] systemDateParts = systemDate.split("/");
    int systemDay = Integer.parseInt(systemDateParts[0]);
    int systemMonth = Integer.parseInt(systemDateParts[1]);
    int systemYear = Integer.parseInt(systemDateParts[2]);

    // Compare in order of year to month to day
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

  // isNotNull is a method that finds if venue name is null and prints error message when it is
  private boolean isNotNull(String venueName, String venueCode) {
    if (venueName == null) {
      MessageCli.BOOKING_NOT_MADE_VENUE_NOT_FOUND.printMessage(venueCode);
      return false;
    }
    return true;
  }

  // increaseDate is a method that increases a given date
  private String increaseDate(String dateToIncrease) {
    String[] dateToIncreaseParts = dateToIncrease.split("/");
    int dateToIncreaseDay = Integer.parseInt(dateToIncreaseParts[0]);
    int dateToIncreaseMonth = Integer.parseInt(dateToIncreaseParts[1]);
    int dateToIncreaseYear = Integer.parseInt(dateToIncreaseParts[2]);

    int[] daysInMonth = {31, 28, 31, 30, 31, 30, 31, 30, 31, 30, 31, 30};

    // Increase in order of day to month to year
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

  // nextAvailableDate is a method that finds the next available date for a venue
  private String nextAvailableDate(String venueName) {
    boolean dateAvailability = false;
    String nextAvailableDate = systemDate;

    // If venue has no bookings return systemDate which is the fastest date
    if (bookingList == null) {
      return systemDate;
    }

    // Check if date is available and when not increase date and check until available date
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

  // venueExists is a method that checks if venue exists
  private boolean venueExists(String venueCode) {
    for (Venue venue : venueList) {
      if (venue.isSameCode(venueCode)) {
        MessageCli.PRINT_BOOKINGS_HEADER.printMessage(venue.getName());
        return true;
      }
    }
    return false;
  }

  // bookingExists is a method that checks if booking exists
  private boolean bookingExists(String venueCode) {
    boolean bookingExists = false;
    for (Booking booking : bookingList) {
      for (Venue venue : venueList) {
        if (venue.isSameCode(venueCode)) {
          String venueName = venue.getName();
          // Compare venue name found by venue code and venue name in booking list
          if (booking.getVenueName().equals(venueName)) {
            MessageCli.PRINT_BOOKINGS_ENTRY.printMessage(
                booking.getBookingReference(), booking.getBookingDate());
            bookingExists = true;
          }
        }
      }
    }
    // if booking exists return true
    if (bookingExists) {
      return true;
    }
    return false;
  }

  // bookingReferenceExists is a method that checks if a booking exists
  private boolean bookingReferenceExists(String bookingReference) {
    for (Booking booking : bookingList) {
      if (booking.getBookingReference().equals(bookingReference)) {
        return true;
      }
    }
    return false;
  }
}
