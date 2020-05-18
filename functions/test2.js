
const firebase = require('firebase/app');
require('firebase/auth');
require('firebase/database');
const euc = require('euclidean-distance');

var firebaseConfig = {
    apiKey: "AIzaSyDMWj1qvYW2baOqTQRZI1WSzc1zWdY0uFM",
    authDomain: "arrangeme-b5809.firebaseapp.com",
    databaseURL: "https://arrangeme-b5809.firebaseio.com",
    // projectId: "arrangeme-b5809",
    storageBucket: "arrangeme-b5809.appspot.com",
    // messagingSenderId: "sender-id",
    // appId: "1:24099753586:android:e9bcbc2f7a6e094f603ceb",
    // measurementId: "G-measurement-id",
};

var id = 'fe9whDdDCFP59S8wIARe27Aa5H12';
firebase.initializeApp(firebaseConfig);

var db = firebase.database();

db.ref('users/' + id).once('value', (snap) => {
    var pv = snap.val().personality_vector;
    var results =[];
    pv.shift();
    db.ref('details/kmeans/').once('value', (snap) => {
        snap.forEach(x => {
            results.push(euc(x.val(),pv));
        })
        var min_index = results.indexOf(Math.min(...results));
        db.ref('users/'+id).update({
            group: min_index
        });
    });
});





