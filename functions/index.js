// The Cloud Functions for Firebase SDK to create Cloud Functions and setup triggers.
const functions = require('firebase-functions');
const skmeans = require('skmeans');
const euc = require('euclidean-distance');


// The Firebase Admin SDK to access the Firebase Realtime Database.
const admin = require('firebase-admin');
admin.initializeApp();


// this function gets all the PVs from the database using firebase cloud functions
exports.initKmeans = functions.https.onCall((data, context) => {
    var ref = admin.database().ref("simulated_users");
    return ref.once("value", function (snap) {
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
            // var updates = {};
            // updates['/simulated_users/' + users[i]] = postData;
            admin.database().ref('/simulated_users/' + users[i]).update({
                group: res.idxs[i]
            });
        }

        let centroids = [];
        for (let i = 0; i < res.centroids.length; i++) {
            centroids.push(res.centroids[i]);
        }

        admin.database().ref('/details/kmeans/').set(centroids);

    });
});

// this function classify each new user to his sub group
exports.classifyUser = functions.https.onCall((data, context) => {
    admin.database().ref('users/' + data.id).once('value', (snap) => {
        var pv = snap.val().personality_vector;
        var results = [];
        pv.shift();
        admin.database().ref('details/kmeans/').once('value', (snap) => {
            snap.forEach(x => {
                results.push(euc(x.val(), pv));
            })
            var min_index = results.indexOf(Math.min(...results));
            admin.database().ref('users/' + data.id+'/personal_info/').update({
                group: min_index
            });
        });
    });
});
