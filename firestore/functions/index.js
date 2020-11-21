"use strict";

const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);
// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//


exports.sendRequestNotification = functions.firestore.document("/Libraries/{libraryName}/requests/{requestID}")
    .onCreate(async (snap, context) => {
        // Get necessary variables
        const libraryName = context.params.libraryName;
        const data = snap.data();
        const owner = data.book.owner;
        const title = data.book.title;
        const creator = data.creator.username;

        // Get registration token for user
        const userSnapshot = await admin.firestore().doc(`/Libraries/${libraryName}/users/${owner}`).get();
        const token = userSnapshot.data().registrationToken;
        
        if (token === null) {
            return null;
        }

        // Create message
        const message = {
            notification: {
              title: `A request has been made for ${title}`,
              body: `User "${creator}" has requested your book "${title}".`,
            },
        };
        
        // Send message, wait for errors
        const response = await admin.messaging().sendToDevice(token, message)
        response.results.forEach((result, index) => {
            if (result.error) {
              console.error('Failure sending notification to', token, result.error);
            }
        });
    })

exports.notifyAcceptedRequest = functions.firestore.document("/Libraries/{libraryName}/requests/{requestID}")
    .onUpdate(async (snap, context) => {
        const libraryName = context.params.libraryName;
        const before = snap.before.data();
        const after = snap.after.data();

        console.log(before['status'] === 'available', after['status'] === 'accepted')
        // Check that status changed to accepted and 
        // Other properties are the same
        if (before['status'] === 'available' && after['status'] === 'accepted') {
            const creator = after.creator.username;
            const owner = after.book.owner;
            const title = after.book.title;

            const userSnapshot = await admin.firestore().doc(`/Libraries/${libraryName}/users/${creator}`).get();
            const token = userSnapshot.data().registrationToken;

            if (token === null) {
                return null;
            }
            // Create message
            const message = {
                notification: {
                    title: `Your request for '${title}' has been accepted!`,
                    body: `User "${owner}" has accepted your request for "${title}".`,
                },
            };
            
            // Send message, wait for errors
            const response = await admin.messaging().sendToDevice(token, message)
            response.results.forEach((result, index) => {
                if (result.error) {
                console.error('Failure sending notification to', token, result.error);
                }
            });
        }

    })