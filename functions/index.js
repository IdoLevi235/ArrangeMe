// The Cloud Functions for Firebase SDK to create Cloud Functions and setup triggers.
const functions = require('firebase-functions');

// The Firebase Admin SDK to access the Firebase Realtime Database.
const admin = require('firebase-admin');
admin.initializeApp();


exports.addMessage = functions.https.onCall((data, context) => {
// Saving the new message to the Realtime Database.

var ref = admin.database().ref("users/46rozrdHUmbQ7ZDOu0G8fY1brVg2");
ref.once("value") .then(function(snapshot) {
    var q = snapshot.child("personality_vector").val(); // "Ada"
    return q;

  });


});




// Test for the existence of certain keys within a DataSnapshot
