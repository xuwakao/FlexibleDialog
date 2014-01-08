package com.yy.yyent.flexibledialog;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.DialogFragment;
import android.view.View;

import java.io.Serializable;

/**
 * Created by xujiexing on 13-10-17.
 */
public abstract class FlexibleDialogListener implements View.OnClickListener, Parcelable {
    DialogFragment dialogFragment;

    public void setDialogFragment(DialogFragment dialogFragment) {
        this.dialogFragment = dialogFragment;
    }

    public DialogFragment getDialogFragment() {
        return dialogFragment;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    @Override
    public int describeContents() {
        return 0;
    }
}
