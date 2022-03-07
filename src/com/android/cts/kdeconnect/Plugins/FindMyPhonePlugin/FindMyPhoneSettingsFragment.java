/*
 * SPDX-FileCopyrightText: 2018 Erik Duisters <e.duisters1@gmail.com>
 *
 * SPDX-License-Identifier: GPL-2.0-only OR GPL-3.0-only OR LicenseRef-KDE-Accepted-GPL
 */

package com.android.cts.kdeconnect.Plugins.FindMyPhonePlugin;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;

import com.android.cts.kdeconnect.UserInterface.PluginSettingsFragment;
import com.android.cts.kdeconnect_tp.R;

import androidx.annotation.NonNull;
import androidx.preference.Preference;
import androidx.preference.PreferenceManager;

public class FindMyPhoneSettingsFragment extends PluginSettingsFragment {
    private static final int REQUEST_CODE_SELECT_RINGTONE = 1000;

    private String preferenceKeyRingtone;
    private SharedPreferences sharedPreferences;
    private Preference ringtonePreference;

    public static FindMyPhoneSettingsFragment newInstance(@NonNull String pluginKey) {
        FindMyPhoneSettingsFragment fragment = new FindMyPhoneSettingsFragment();
        fragment.setArguments(pluginKey);

        return fragment;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        super.onCreatePreferences(savedInstanceState, rootKey);

        preferenceKeyRingtone = getString(R.string.findmyphone_preference_key_ringtone);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());

        ringtonePreference = getPreferenceScreen().findPreference(preferenceKeyRingtone);

        setRingtoneSummary();
    }

    private void setRingtoneSummary() {
        String ringtone = sharedPreferences.getString(preferenceKeyRingtone, Settings.System.DEFAULT_RINGTONE_URI.toString());

        Uri ringtoneUri = Uri.parse(ringtone);

        ringtonePreference.setSummary(RingtoneManager.getRingtone(requireContext(), ringtoneUri).getTitle(requireContext()));
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        /*
         * There is no RingtonePreference in support library nor androidx, this is the workaround proposed here:
         * https://issuetracker.google.com/issues/37057453
         */

        if (preference.hasKey() && preference.getKey().equals(preferenceKeyRingtone)) {
            Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false);
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_DEFAULT_URI, Settings.System.DEFAULT_NOTIFICATION_URI);

            String existingValue = sharedPreferences.getString(preferenceKeyRingtone, null);
            if (existingValue != null) {
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, Uri.parse(existingValue));
            } else {
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, Settings.System.DEFAULT_RINGTONE_URI);
            }

            startActivityForResult(intent, REQUEST_CODE_SELECT_RINGTONE);
            return true;
        }
        return super.onPreferenceTreeClick(preference);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_SELECT_RINGTONE && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);

            if (uri != null) {
                sharedPreferences.edit()
                        .putString(preferenceKeyRingtone, uri.toString())
                        .apply();

                setRingtoneSummary();
            }
        }
    }
}
