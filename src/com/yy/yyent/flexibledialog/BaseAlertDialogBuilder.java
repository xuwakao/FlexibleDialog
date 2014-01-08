package com.yy.yyent.flexibledialog;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by xujiexing on 13-10-12.
 */
public abstract class BaseAlertDialogBuilder<T extends BaseAlertDialogBuilder<T>> {
    public static final String DEFAULT_TAG = "BaseAlertDialogBuilder";
    public static final String CANCELABLE_TOUCH_OUTSIDE = "cancelable_touch_outside";
    public static String ARG_REQUEST_CODE = "request_code";
    private static final int DEFAULT_REQUEST_CODE = 0x10;

    protected final Context mContext;
    protected final FragmentManager mFragmentManager;
    protected final Class<? extends BaseAlertDialogFragment> mClazz;
    protected final Resources mResources;
    private boolean mCancelable = true;
    private boolean mCancelableOnTouchOutside = true;
    private int mRequestCode = DEFAULT_REQUEST_CODE;
    private Fragment mTargetFragment;
    private BaseAlertDialogFragment.FlexibleOnDismissListener mOnDismissListener;
    private BaseAlertDialogFragment.FlexibleOnKeyListener mOnKeyListener;

    public BaseAlertDialogBuilder(Context context, FragmentManager fragmentManager, Class<? extends BaseAlertDialogFragment> clazz) {
        mContext = context.getApplicationContext();
        mFragmentManager = fragmentManager;
        mClazz = clazz;
        mResources = mContext.getResources();
    }

    public T setCancelable(boolean cancelable) {
        mCancelable = cancelable;
        return self();
    }

    public T setCancelableOnTouchOutside(boolean cancelable) {
        mCancelableOnTouchOutside = cancelable;
        if (cancelable) {
            mCancelable = cancelable;
        }
        return self();
    }

    public T setTargetFragment(Fragment fragment, int requestCode) {
        mTargetFragment = fragment;
        mRequestCode = requestCode;
        return self();
    }

    public T setRequestCode(int requestCode) {
        mRequestCode = requestCode;
        return self();
    }

    public T setOnDismissListener(BaseAlertDialogFragment.FlexibleOnDismissListener onDismissListener) {
        mOnDismissListener = onDismissListener;
        return self();
    }

    public T setOnKeyListener(BaseAlertDialogFragment.FlexibleOnKeyListener mOnKeyListener) {
        this.mOnKeyListener = mOnKeyListener;
        return self();
    }

    protected abstract T self();

    protected abstract Bundle prepareArguments();

    public DialogFragment show() {
        Bundle arguments = prepareArguments();

        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        Fragment prev = mFragmentManager.findFragmentByTag(DEFAULT_TAG);
        if (prev != null) {
            ft.remove(prev);
        }
//        ft.addToBackStack(null);

        arguments.putBoolean(CANCELABLE_TOUCH_OUTSIDE, mCancelableOnTouchOutside);
        if (mOnDismissListener != null)
            arguments.putParcelable(BaseAlertDialogFragment.DIALOG_ONCANCEL_LISTENER, mOnDismissListener);
        if (mOnKeyListener != null)
            arguments.putParcelable(BaseAlertDialogFragment.DIALOG_ONKEY_LISTENER, mOnKeyListener);
        final BaseAlertDialogFragment fragment = (BaseAlertDialogFragment) Fragment.instantiate(mContext, mClazz.getName(), arguments);

        if (mTargetFragment != null) {
            fragment.setTargetFragment(mTargetFragment, mRequestCode);
        } else {
            arguments.putInt(ARG_REQUEST_CODE, mRequestCode);
        }
        fragment.setCancelable(mCancelable);
        fragment.show(ft, DEFAULT_TAG);
        return fragment;
    }
}
