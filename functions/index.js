// The Cloud Functions for Firebase SDK to create Cloud Functions and setup triggers.
const functions = require('firebase-functions');
const skmeans = require('skmeans');
const euc = require('euclidean-distance');
var createKDTree = require("static-kdtree");


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
            admin.database().ref('users/' + data.id + '/personal_info/').update({
                group: min_index
            });
        });
    });
});

// this function finds the fitted schedule
exports.findSchedule = functions.https.onCall((data, context) => {
    // const id = data.id;
    const group = data.group;
    const frequencyVec = data.freqVec;
    const timeVec = data.timeVec;
    var all_freqs_vec = [];
    var all_info = [];
    var info_freqsvec = [];
    var ref = admin.database().ref('simulated_users').orderByChild('group').equalTo(group);
    ref.once('value', (snap) => {
        snap.forEach(x => {
            info_freqsvec = theClosestFreqVec(x);
            all_freqs_vec.push(...info_freqsvec[0]);
            all_info.push({
                id: x.key,
                dates: info_freqsvec[1],
                time_vector: info_freqsvec[2],
                schedule: info_freqsvec[3]
            });
        });
        var tree = createKDTree(all_freqs_vec);
        var knnFreq = tree.knn(frequencyVec, 10);
        tree.dispose();
        timeVec_Sched = theClosestTimeVec(all_freqs_vec, knnFreq, all_info);
        tree = createKDTree(timeVec_Sched.time_vec);
        var res = tree.knn(timeVec, 1);
        tree.dispose();
        var chosen_sched = timeVec_Sched.schedules[res[0]];
        return chosen_sched;
    });
    function theClosestFreqVec(x) {
        var freqs_vec = [];
        var Schedules = [];
        var info = [];
        var info_freqsvec = []
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
            info.push(Schedules[date].data.time_vector);
            info.push(Schedules[date].schedule);
        }
        info_freqsvec.push(freqs_vec);
        info_freqsvec.push(...info);
        return (info_freqsvec);
    }

    function theClosestTimeVec(all_freqs_vec, knnFreq, all_info) {
        var time_vec = [];
        var schedules = [];
        for (const index in knnFreq) {
            var time_withkey = [];
            var a = Math.floor(knnFreq[index] / 21);
            var b = all_info[a].time_vector;
            for (const key in b) {
                time_withkey.push(b[key]);
            }
            time_vec.push(time_withkey);
            schedules.push(all_info[a].schedule);
        }
        return { time_vec, schedules };
    }
});