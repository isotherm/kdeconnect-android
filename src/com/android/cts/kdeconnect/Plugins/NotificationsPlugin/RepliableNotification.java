package com.android.cts.kdeconnect.Plugins.NotificationsPlugin;

import android.app.PendingIntent;

import java.util.ArrayList;
import java.util.UUID;

class RepliableNotification {
    final String id = UUID.randomUUID().toString();
    PendingIntent pendingIntent;
    final ArrayList<android.app.RemoteInput> remoteInputs = new ArrayList<>();
    String packageName;
    String tag;
}
