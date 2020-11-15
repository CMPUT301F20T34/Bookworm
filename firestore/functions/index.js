const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();
"use strict";
// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//


exports.sendRequestNotification = functions.firestore.document("/Libraries/{libraryName}/requests/{requestID}")
    .onCreate(async (snap, context) => {
        const libraryName = context.params.libraryName;

        const data = snap.data();
        const owner = data.book.owner;
        const title = data.book.title;
        const creator = data.creator.username;

        const tokenSnapshot = await admin.database().ref(`/Libraries/${libraryName}/users/${owner}`).once('value');

        const token = tokenSnapshot.data().registrationToken;

        const message = {
            notification: {
              title: `A request has been made for ${title}`,
              body: `${creator} requested the book ${title}. Please respond as soon as possible.`,
            },
        };
        
        const response = await admin.messaging().sendToDevice(token, message)
        response.results.forEach((result, index) => {
            const error = result.error;
            if (error) {
              console.error('Failure sending notification to', token, error);
            }
        });
    })