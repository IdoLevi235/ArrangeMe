
const firebase = require('firebase/app');
var createKDTree = require("static-kdtree")

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
var all_freqs_vec = [];
var all_info = [];
var info_freqsvec = [];
var ref = db.ref('simulated_users').orderByChild('group').equalTo(mygroup);
ref.once('value').then((snap) => {
    snap.forEach(x => {
        info_freqsvec = theClosestFreqVec(x);
        all_freqs_vec.push(info_freqsvec[0]);
        all_info.push[info_freqsvec[1]];
        all_info.push(x);
    });
    var tree = createKDTree(all_freqs_vec);
    console.log("index of 100 closest points to [2,1,0,1,0,1,0,1,2] are ", tree.knn([2,1,0,1,0,1,0,1,2], 100));
    tree.dispose();
});


function theClosestFreqVec(x) {
    var freqs_vec = [];
    var Schedules = [];
    var info = [];
    var info_freqsvec=[]
    Schedules = x.val().Schedules;
    for (const date in Schedules) {
        var freq_withkey = [];
        freq_withkey = Schedules[date].data.frequency_vector;
        var freq = [];
        for (const key in freq_withkey) {
            freq.push(freq_withkey[key]);
        }
    freqs_vec.push(freq);
    info.push(date);
    }
    info_freqsvec.push(freqs_vec);
    info_freqsvec.push(info);
    return (info_freqsvec);
}

