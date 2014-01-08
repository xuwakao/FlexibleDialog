package com.yy.yyent.flexibledialog;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

/**
 * Created by xujiexing on 14-1-7.
 */
public abstract class FlexibleBindDataCallback implements Parcelable {
    public abstract void bindData(View View);

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
