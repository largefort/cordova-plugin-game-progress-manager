var exec = require('cordova/exec');

var GameProgressManager = {
    detectAutosaveMethod: function(successCallback, errorCallback) {
        exec(successCallback, errorCallback, 'GameProgressManager', 'detectAutosaveMethod', []);
    },

    exportGameProgress: function(successCallback, errorCallback) {
        exec(successCallback, errorCallback, 'GameProgressManager', 'exportGameProgress', []);
    },

    importGameProgress: function(successCallback, errorCallback) {
        exec(successCallback, errorCallback, 'GameProgressManager', 'importGameProgress', []);
    },

    saveProgressToLocalStorage: function(gameState, successCallback, errorCallback) {
        exec(successCallback, errorCallback, 'GameProgressManager', 'saveProgressToLocalStorage', [gameState]);
    },

    loadProgressFromLocalStorage: function(successCallback, errorCallback) {
        exec(successCallback, errorCallback, 'GameProgressManager', 'loadProgressFromLocalStorage', []);
    },

    saveProgressToIndexedDB: function(gameState, successCallback, errorCallback) {
        exec(successCallback, errorCallback, 'GameProgressManager', 'saveProgressToIndexedDB', [gameState]);
    },

    loadProgressFromIndexedDB: function(successCallback, errorCallback) {
        exec(successCallback, errorCallback, 'GameProgressManager', 'loadProgressFromIndexedDB', []);
    }
};

module.exports = GameProgressManager;
