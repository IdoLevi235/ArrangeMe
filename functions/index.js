// The Cloud Functions for Firebase SDK to create Cloud Functions and setup triggers.
const functions = require('firebase-functions');
const skmeans = require('skmeans');

// The Firebase Admin SDK to access the Firebase Realtime Database.
const admin = require('firebase-admin');
admin.initializeApp();


// this function gets all the PVs from the database using firebase cloud functions
exports.initKmeans = functions.https.onCall((data, context) => {
var ref = admin.database().ref("simulated_users");
    return ref.once("value" , function(snap){
        let vecs = [];
        let users = []
    
        snap.forEach(x => {
            users.push(x.key);
            let arr = x.val().personality_vector;
            arr.shift();
            vecs.push(arr);
        });
        var res = skmeans(vecs, 4);
    
        for (let i = 0; i < res.idxs.length; i++) {
            var postData = {
                group: res.idxs[i],
                personality_vector: vecs[i]
            };
    
            var updates = {};
            updates['/simulated_users/' + users[i]] = postData;
    
            admin.database().ref().update(updates);
        }
    });
});
