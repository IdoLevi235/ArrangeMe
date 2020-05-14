// The Cloud Functions for Firebase SDK to create Cloud Functions and setup triggers.
const functions = require('firebase-functions');

// The Firebase Admin SDK to access the Firebase Realtime Database.
const admin = require('firebase-admin');
admin.initializeApp();

exports.addMessage = functions.https.onCall((data, context) => {
const text = data;
// Saving the new message to the Realtime Database.
return admin.database().ref('/messages').push({
  text: text
}).then(() => {
  console.log('New written');
  // Returning the sanitized message to the client.
  return { text: text };
})

});
