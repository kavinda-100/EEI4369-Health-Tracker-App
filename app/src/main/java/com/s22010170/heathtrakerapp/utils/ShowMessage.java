package com.s22010170.heathtrakerapp.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;

import androidx.appcompat.app.AlertDialog;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ShowMessage {

    // Show a message dialog with a title and message.
    public void show(String title, String message, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

}
