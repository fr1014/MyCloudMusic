package com.fr1014.mycoludmusic.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.afollestad.materialdialogs.MaterialDialog;
/**
 * Create by fanrui on 2020/12/29
 * Describe:
 */
public class SwitchDialogFragment extends DialogFragment {
    public static final String[] array = {"酷我", "网易"};

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        MusicSourceCallback callback = (MusicSourceCallback) getActivity();
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity())
                .title("选择音乐源")
                .items(array)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        if (callback != null) {
                            callback.musicSource(position);
                        }
                    }
                });

        return builder.build();
    }

    public interface MusicSourceCallback {
        void musicSource(int position);
    }
}
