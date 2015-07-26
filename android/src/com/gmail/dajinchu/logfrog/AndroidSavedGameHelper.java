package com.gmail.dajinchu.logfrog;

import android.os.AsyncTask;
import android.util.Log;

import com.gmail.dajinchu.SavedGameHelper;
import com.gmail.dajinchu.SavedGameListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesStatusCodes;
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
    private static final int MAX_SNAPSHOT_RESOLVE_RETRIES = 3;

    public AndroidSavedGameHelper(GoogleApiClient GAC){
        mGoogleApiClient = GAC;
    }

    @Override
    public void write(final byte[] data) {
        if(!mGoogleApiClient.isConnected())return;
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
    public void load(final SavedGameListener game) {
        if(!mGoogleApiClient.isConnected())return;

        AsyncTask<Void, Void, Integer> load = new AsyncTask<Void, Void, Integer>() {
            public byte[] mSaveGameData;

            @Override
            protected Integer doInBackground(Void... params) {
                // Open the saved game using its name.
                Snapshots.OpenSnapshotResult result = Games.Snapshots.open(mGoogleApiClient,
                        "default", true).await();

                com.google.android.gms.common.api.Status status = result.getStatus();
                // Check the result of the open operation
                if (status.isSuccess()) {
                    Snapshot snapshot = result.getSnapshot();
                    // Read the byte content of the saved game.
                    try {
                        mSaveGameData = snapshot.getSnapshotContents().readFully();
                    } catch (IOException e) {
                        Log.e(TAG, "Error while reading Snapshot.", e);
                    }
                } else {
                    Log.e(TAG, "Error while loading: " + status.getStatusCode());
                    if(status.getStatusCode() == GamesStatusCodes.STATUS_SNAPSHOT_CONFLICT){
                        try {
                            mSaveGameData = processSnapshotOpenResult(game, result, 0).getSnapshotContents().readFully();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }


                return result.getStatus().getStatusCode();
            }
            @Override
            protected void onPostExecute(Integer status){
                if(status != GamesStatusCodes.STATUS_OK){
                    return;
                }
                if(mSaveGameData.length<1){
                    game.onGameLoad(new byte[]{(byte) 1});
                }else {
                    game.onGameLoad(mSaveGameData);
                }
            }
        };
        load.execute();
    }
    private Snapshot processSnapshotOpenResult(SavedGameListener game, Snapshots.OpenSnapshotResult result, int retryCount) {
        Snapshot mResolvedSnapshot;
        retryCount++;

        int status = result.getStatus().getStatusCode();
        Log.i(TAG, "Save Result status: " + status);

        if (status == GamesStatusCodes.STATUS_OK) {
            return result.getSnapshot();
        } else if (status == GamesStatusCodes.STATUS_SNAPSHOT_CONTENTS_UNAVAILABLE) {
            return result.getSnapshot();
        } else if (status == GamesStatusCodes.STATUS_SNAPSHOT_CONFLICT) {
            Snapshot snapshot = result.getSnapshot();
            Snapshot conflictSnapshot = result.getConflictingSnapshot();

            mResolvedSnapshot = snapshot;
            Snapshot[] snaps = new Snapshot[]{snapshot,conflictSnapshot};
            try {
                int which = game.resolveConflict(snaps[0].getSnapshotContents().readFully(),
                        snaps[1].getSnapshotContents().readFully());
                mResolvedSnapshot=snaps[which];
            } catch (IOException e) {
                e.printStackTrace();
            }

            Snapshots.OpenSnapshotResult resolveResult = Games.Snapshots.resolveConflict(
                    mGoogleApiClient, result.getConflictId(), mResolvedSnapshot).await();

            if (retryCount < MAX_SNAPSHOT_RESOLVE_RETRIES) {
                // Recursively attempt again
                return processSnapshotOpenResult(game, resolveResult, retryCount);
            } else {
                // Failed, log error
                String message = "Could not resolve snapshot conflicts";
                Log.e(TAG, message);
            }

        }

        // Fail, return null.
        return null;
    }
    @Override
    public void setStepsAchievement(String id, int steps){
        Games.Achievements.setSteps(mGoogleApiClient,id,steps);
    }
}
