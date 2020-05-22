
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
var mygroup=3;

firebase.initializeApp(firebaseConfig);

var db = firebase.database();

db.ref('simulated_users/').once('value', (snap) => {
    var group = snap.val().group;
    var user_id = snap.key;
    var schedules_map=[];
    if(group=mygroup)
        {
            db.ref('simulated_users/'+user_id+'/schedules/').once('value', (snap) => {
                snap.forEach(x => {
                    var isSuccesful = x.child('data/successful');
                    let schedules=[];
                    let schedule = {
                        date: x.child().val(),
                        schedules: x.child('data/schedule')
                      };
                    if(isSuccesful==="yes"){
                        schedules_map.entries(schedule);
                        console.log(schedules_map);
                        console.log(schedules_map.length());
                        theClosestFreqVec();
                    }
                })
            });
        }
});


function theClosestFreqVec(){

}


