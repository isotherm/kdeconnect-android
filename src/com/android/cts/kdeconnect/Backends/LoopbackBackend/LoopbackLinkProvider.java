/*
 * SPDX-FileCopyrightText: 2014 Albert Vaca Cintora <albertvaka@gmail.com>
 *
 * SPDX-License-Identifier: GPL-2.0-only OR GPL-3.0-only OR LicenseRef-KDE-Accepted-GPL
*/

package com.android.cts.kdeconnect.Backends.LoopbackBackend;

import android.content.Context;

import com.android.cts.kdeconnect.Backends.BaseLinkProvider;
import com.android.cts.kdeconnect.NetworkPacket;

public class LoopbackLinkProvider extends BaseLinkProvider {

    private final Context context;

    public LoopbackLinkProvider(Context context) {
        this.context = context;
    }

    @Override
    public void onStart() {
        onNetworkChange();
    }

    @Override
    public void onStop() {
    }

    @Override
    public void onNetworkChange() {
        NetworkPacket np = NetworkPacket.createIdentityPacket(context);
        connectionAccepted(np, new LoopbackLink(context, this));
    }
/*
    @Override
    public int getPriority() {
        return 0;
    }
*/
    @Override
    public String getName() {
        return "LoopbackLinkProvider";
    }
}
