// The Cloud Functions for Firebase SDK to create Cloud Functions and setup triggers.
const functions = require('firebase-functions');

// The Firebase Admin SDK to access the Firebase Realtime Database.
const admin = require('firebase-admin');
admin.initializeApp();


// this function gets all the PVs from the database using firebase cloud functions
exports.getAllPersonalityVectors = functions.https.onCall((data, context) => {
var vectors = [];
var ref = admin.database().ref("simulated_users");
    return ref.once("value") .then(function(snapshot){
        snapshot.forEach(function(childSnapshot){
        vectors.push(childSnapshot.child("personality_vector").val());
            });
        return vectors;
    });

});

