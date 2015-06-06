package com.gmail.dajinchu.logfrog;

import android.os.AsyncTask;
import android.util.Log;

import com.gmail.dajinchu.MainGame;
import com.gmail.dajinchu.SavedGameHelper;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.snapshot.Snapshot;
import com.google.android.gms.games.snapshot.SnapshotMetadataChange;
import com.google.android.gms.games.snapshot.Snapshots;

import java.io.IOException;

/**
 * Created by Da-Jin on 5/30/2015.
 */
public class AndroidSavedGameHelper implements SavedGameHelper {


    private final GoogleApiClient mGoogleApiClient;
    private String TAG = "Savegamehelper";

    public AndroidSavedGameHelper(GoogleApiClient GAC){
        mGoogleApiClient = GAC;
    }

    @Override
    public void write(final byte[] data) {
        AsyncTask<Void, Void, Boolean> write = new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                Snapshots.OpenSnapshotResult open = Games.Snapshots.open(
                        mGoogleApiClient, "default", true).await();

                if (!open.getStatus().isSuccess()) {
                    Log.w(TAG, "Could not open Snapshot for update.");
                    return false;
                }

                // Change data but leave existing metadata
                Snapshot snapshot = open.getSnapshot();
                snapshot.getSnapshotContents().writeBytes(data);

                Snapshots.CommitSnapshotResult commit = Games.Snapshots.commitAndClose(
                        mGoogleApiClient, snapshot, SnapshotMetadataChange.EMPTY_CHANGE).await();

                if (!commit.getStatus().isSuccess()) {
                    Log.w(TAG, "Failed to commit Snapshot.");
                    return false;
                }

                Log.d(TAG,"commited");
                return true;
            }
        };
        write.execute();
    }

    @Override
    public void load(final MainGame game) {

        AsyncTask<Void, Void, Integer> load = new AsyncTask<Void, Void, Integer>() {
            public byte[] mSaveGameData;

            @Override
            protected Integer doInBackground(Void... params) {
                // Open the saved game using its name.
                Snapshots.OpenSnapshotResult result = Games.Snapshots.open(mGoogleApiClient,
                        "default", true).await();

                // Check the result of the open operation
                if (result.getStatus().isSuccess()) {
                    Snapshot snapshot = result.getSnapshot();
                    // Read the byte content of the saved game.
                    try {
                        mSaveGameData = snapshot.getSnapshotContents().readFully();
                    } catch (IOException e) {
                        Log.e(TAG, "Error while reading Snapshot.", e);
                    }
                } else {
                    Log.e(TAG, "Error while loading: " + result.getStatus().getStatusCode());
                }

                return result.getStatus().getStatusCode();
            }
            @Override
            protected void onPostExecute(Integer status){
                game.loadLevel((int)mSaveGameData[0]);
            }
        };
        load.execute();
    }
}
