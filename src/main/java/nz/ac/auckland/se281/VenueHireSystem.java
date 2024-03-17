package nz.ac.auckland.se281;

import java.util.ArrayList;

import nz.ac.auckland.se281.Types.CateringType;
import nz.ac.auckland.se281.Types.FloralType;

public class VenueHireSystem {
  private ArrayList<Venue> venueList = new ArrayList<Venue>();

  public VenueHireSystem() {}

  public void printVenues() {
    if (venueList.isEmpty()) {
      MessageCli.NO_VENUES.printMessage();
    }
    else {
      int venueListSize = venueList.size();
      String isOrAre = getIsOrAre(venueListSize);
      String quantity = getQuantityString(venueListSize);
      String singularOrPlural = getSingularOrPlural(venueListSize);
      MessageCli.NUMBER_VENUES.printMessage(isOrAre, quantity, singularOrPlural);

      for (Venue venue : venueList) {
        venue.PrintDetails();
      }
    }
  }

  public void createVenue(
      String venueName, String venueCode, String capacityInput, String hireFeeInput) {
    if (venueName.isEmpty()) {
      MessageCli.VENUE_NOT_CREATED_EMPTY_NAME.printMessage();
    }
    else if (capacityInput.charAt(0) == '-') {
      MessageCli.VENUE_NOT_CREATED_INVALID_NUMBER.printMessage("capacity", " positive");
    }
    else if (!isNumber(hireFeeInput)) {
      MessageCli.VENUE_NOT_CREATED_INVALID_NUMBER.printMessage("hire fee", "");
    }
    else {
      Venue newVenue = new Venue(venueName, venueCode, capacityInput, hireFeeInput);
      venueList.add(newVenue);
      MessageCli.VENUE_SUCCESSFULLY_CREATED.printMessage(venueName, venueCode);
    }


  }

  public void setSystemDate(String dateInput) {
    // TODO implement this method
  }

  public void printSystemDate() {
    // TODO implement this method
  }

  public void makeBooking(String[] options) {
    // TODO implement this method
  }

  public void printBookings(String venueCode) {
    // TODO implement this method
  }

  public void addCateringService(String bookingReference, CateringType cateringType) {
    // TODO implement this method
  }

  public void addServiceMusic(String bookingReference) {
    // TODO implement this method
  }

  public void addServiceFloral(String bookingReference, FloralType floralType) {
    // TODO implement this method
  }

  public void viewInvoice(String bookingReference) {
    // TODO implement this method
  }

  private boolean isNumber(String stringToCheck) { 
    try {  
      Integer.parseInt(stringToCheck);
      return true;
    }
    catch (NumberFormatException e) {  
      return false;  
    }  
  }

  private String getIsOrAre(int venueListSize) {
    if (venueListSize == 1) {
      return "is";
    }
    else {
      return "are";
    }
  }

  private String getQuantityString(int venueListSize) {
    if (venueListSize < 10) {
      String[] numberNames = {"one", "two", "three", "four", "five", "six", "seven", "eight", "nine"};
      return numberNames[venueListSize - 1];
    }
    else {
      return Integer.toString(venueListSize);
    }
  }

  private String getSingularOrPlural(int venueListSize) {
    if (venueListSize == 1) {
      return "";
    }
    else {
      return "s";
    }
  }
}
