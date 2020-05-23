
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

var id = 'sim1';
var mygroup = 3;

firebase.initializeApp(firebaseConfig);

var db = firebase.database();
var ref = db.ref('simulated_users').orderByChild('group').equalTo('3');
console.log(ref);
ref.once('value',(snap) => {
    
    console.log(snap.val());
});
db.ref('simulated_users/').once('value', (snap) => {
    snap.forEach(x => {
        var group = x.val().group;
        var user_id = x.key;
        var schedules_map = [];
        var schedules = x.val().Schedules;
        if (group = mygroup) {          
            for (const iterator of schedules) {
                var isSuccesful = iterator.val().data.successful;
                let schedules = [];
            if (isSuccesful === "yes") {
                theClosestFreqVec();
            }
            } 
        }
    })
});


function theClosestFreqVec() {

}


