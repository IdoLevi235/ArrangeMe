package com.example.arrangeme;

/**
 * Popup interface, implemented ny task popup and anchor popup
 */
public interface Popup {
   /**
    * this function defines the popup size
    */
   void definePopUpSize();

   /**
    * This function blocks the views at the background of the popup
    */
   void disableViews();

   /**
    * Show the details of the item in the popup
    */
   void showDetails();

   /**
    * shows the image of the item inside the popup
    */
   void showImage();

   /**
    * Controls the edit mode of the popup
    */
   void editMode();
}
