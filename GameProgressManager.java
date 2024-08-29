package com.example.gameprogress;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class GameProgressManager extends CordovaPlugin {

    private static final int REQUEST_CODE_SAVE = 1;
    private static final int REQUEST_CODE_LOAD = 2;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        switch (action) {
            case "detectAutosaveMethod":
                detectAutosaveMethod(callbackContext);
                return true;
            case "exportGameProgress":
                exportGameProgress(callbackContext);
                return true;
            case "importGameProgress":
                importGameProgress(callbackContext);
                return true;
            case "saveProgressToLocalStorage":
                saveProgressToLocalStorage(args.getJSONObject(0), callbackContext);
                return true;
            case "loadProgressFromLocalStorage":
                loadProgressFromLocalStorage(callbackContext);
                return true;
            case "saveProgressToIndexedDB":
                saveProgressToIndexedDB(args.getJSONObject(0), callbackContext);
                return true;
            case "loadProgressFromIndexedDB":
                loadProgressFromIndexedDB(callbackContext);
                return true;
            default:
                return false;
        }
    }

    private void detectAutosaveMethod(CallbackContext callbackContext) {
        // For simplicity, we assume the game is using LocalStorage or IndexedDB.
        // In reality, you'd have a more sophisticated detection mechanism.
        JSONObject detectedMethod = new JSONObject();
        try {
            detectedMethod.put("method", "LocalStorage"); // or "IndexedDB"
            callbackContext.success(detectedMethod);
        } catch (JSONException e) {
            callbackContext.error("Failed to detect autosave method");
        }
    }

    private void exportGameProgress(CallbackContext callbackContext) {
        // Assume we're exporting from LocalStorage or IndexedDB, depending on detection
        // You would need to pass the actual game state here.
        String gameState = "{}"; // Placeholder: should be obtained from JavaScript
        String encodedGameState = Base64.encodeToString(gameState.getBytes(StandardCharsets.UTF_8), Base64.DEFAULT);

        // Create the Intent for saving the file
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TITLE, "gameprogress.txt");
        cordova.startActivityForResult(this, intent, REQUEST_CODE_SAVE);

        PluginResult pluginResult = new PluginResult(PluginResult.Status.NO_RESULT);
        pluginResult.setKeepCallback(true);
        callbackContext.sendPluginResult(pluginResult);
    }

    private void importGameProgress(CallbackContext callbackContext) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/plain");
        cordova.startActivityForResult(this, intent, REQUEST_CODE_LOAD);

        PluginResult pluginResult = new PluginResult(PluginResult.Status.NO_RESULT);
        pluginResult.setKeepCallback(true);
        callbackContext.sendPluginResult(pluginResult);
    }

    private void saveProgressToLocalStorage(JSONObject gameState, CallbackContext callbackContext) {
        // Save the game state to LocalStorage (this is more likely to be done in JavaScript)
        callbackContext.success("Game progress saved to LocalStorage");
    }

    private void loadProgressFromLocalStorage(CallbackContext callbackContext) {
        // Load the game state from LocalStorage (this is more likely to be done in JavaScript)
        callbackContext.success("{}"); // Return the loaded game state
    }

    private void saveProgressToIndexedDB(JSONObject gameState, CallbackContext callbackContext) {
        // Save the game state to IndexedDB (this is more likely to be done in JavaScript)
        callbackContext.success("Game progress saved to IndexedDB");
    }

    private void loadProgressFromIndexedDB(CallbackContext callbackContext) {
        // Load the game state from IndexedDB (this is more likely to be done in JavaScript)
        callbackContext.success("{}"); // Return the loaded game state
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_SAVE && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            if (uri != null) {
                try {
                    String encodedGameState = ""; // This should be passed to the save function
                    writeToFile(uri, encodedGameState);
                    callbackContext.success("Game progress exported successfully");
                } catch (IOException e) {
                    callbackContext.error("Failed to save game progress");
                }
            }
        } else if (requestCode == REQUEST_CODE_LOAD && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            if (uri != null) {
                try {
                    String loadedGameState = readFromFile(uri);
                    String decodedGameState = new String(Base64.decode(loadedGameState, Base64.DEFAULT), StandardCharsets.UTF_8);
                    callbackContext.success(decodedGameState);
                } catch (IOException e) {
                    callbackContext.error("Failed to load game progress");
                }
            }
        }
    }

    private void writeToFile(Uri uri, String data) throws IOException {
        FileOutputStream outputStream = (FileOutputStream) cordova.getActivity().getContentResolver().openOutputStream(uri);
        if (outputStream != null) {
            outputStream.write(data.getBytes(StandardCharsets.UTF_8));
            outputStream.close();
        }
    }

    private String readFromFile(Uri uri) throws IOException {
        return new String(cordova.getActivity().getContentResolver().openInputStream(uri).readAllBytes(), StandardCharsets.UTF_8);
    }
}
