const firebase = require('firebase/app');
require('firebase/auth');
require('firebase/database');
const skmeans = require('skmeans');

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

firebase.initializeApp(firebaseConfig);

var db = firebase.database();


function first(){
db.ref('simulated_users/').once('value', (snap) => {
    let vecs = [];
    let users = [];

    snap.forEach(x => {
        users.push(x.key);
        let arr = x.val().personality_vector;
        arr.shift();
        vecs.push(arr);
    });
    var res = skmeans(vecs, 4);
    console.log(res);

    for (let i = 0; i < res.idxs.length; i++) {
        var postData = {
            group: res.idxs[i],
            personality_vector: vecs[i]
        };

        var updates = {};
        updates['/simulated_users/' + users[i]] = postData;

        db.ref().update(updates);
    }

    let centroids=[];
    for (let i = 0; i < res.centroids.length; i++) { 
 
    centroids.push(res.centroids[i]);

    var updates2 = {};
    updates2['/details/kmeans/'] = centroids;

    db.ref().update(updates2);
    }


})
}

function classifyUser(){
    var id = data;
    //data from the fucntion - user id
    db.ref('users/').once('value', (snap) => {
    
    
    })
    }