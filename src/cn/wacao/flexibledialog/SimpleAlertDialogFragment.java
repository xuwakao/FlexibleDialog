package cn.wacao.flexibledialog;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

/**
 * Created by xujiexing on 13-10-12.
 */
public class SimpleAlertDialogFragment extends BaseAlertDialogFragment {
    protected static String ARG_MESSAGE = "message";
    protected static String ARG_TITLE = "title";
    protected static String ARG_POSITIVE_BUTTON = "positive_button";
    protected static String ARG_NEGATIVE_BUTTON = "negative_button";
    protected static String ARG_POSITIVE_BUTTON_LISTENER = "positive_button_listener";
    protected static String ARG_NEGATIVE_BUTTON_LISTENER = "negative_button_listener";
    protected static String ARG_CLOSE_BUTTON_LISTENER = "close_button_listener";
    protected static String ARG_TITLE_ICON = "title_icon";
    protected static String ARG_CLOSE_ICON = "close_icon";

    public static SimpleAlertDialogBuilder createBuilder(Context context, FragmentManager fragmentManager) {
        return new SimpleAlertDialogBuilder(context, fragmentManager, SimpleAlertDialogFragment.class);
    }

    @Override
    protected ViewBuilder build(ViewBuilder builder) {
        builder.setTitle(getArguments().getString(ARG_TITLE));
        builder.setMessage(getArguments().getCharSequence(ARG_MESSAGE));
        builder.setNegativeButton(getArguments().getString(ARG_NEGATIVE_BUTTON), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FlexibleDialogListener listener = (FlexibleDialogListener) getArguments().getParcelable(ARG_NEGATIVE_BUTTON_LISTENER);
                if (listener != null) {
                    listener.setDialogFragment(SimpleAlertDialogFragment.this);
                    listener.onClick(v);
                }
            }
        });

        builder.setPositiveButton(getArguments().getString(ARG_POSITIVE_BUTTON), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FlexibleDialogListener listener = (FlexibleDialogListener) getArguments().getParcelable(ARG_POSITIVE_BUTTON_LISTENER);
                if (listener != null) {
                    listener.setDialogFragment(SimpleAlertDialogFragment.this);
                    listener.onClick(v);
                }
            }
        });

        builder.setTitleCloseIcon(getArguments().getInt(ARG_CLOSE_ICON, 0), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FlexibleDialogListener listener = (FlexibleDialogListener) getArguments().getParcelable(ARG_CLOSE_BUTTON_LISTENER);
                if (listener != null) {
                    listener.setDialogFragment(SimpleAlertDialogFragment.this);
                    listener.onClick(v);
                }
            }
        });
        builder.setTitleIcon(getArguments().getInt(ARG_TITLE_ICON));

        return builder;
    }

    public static class SimpleAlertDialogBuilder extends BaseAlertDialogBuilder<SimpleAlertDialogBuilder> {

        private String mTitleString;
        private int mTitleIcon;
        private CharSequence mMessageString;
        private String mPositiveTitle;
        private String mNegativeTitle;
        private int mTitleCloseIcon;
        private FlexibleDialogListener mNegativeListener;
        private FlexibleDialogListener mPositiveListener;
        private FlexibleDialogListener mCloseListener;

        public SimpleAlertDialogBuilder(Context context, FragmentManager fragmentManager, Class<? extends BaseAlertDialogFragment> clazz) {
            super(context, fragmentManager, clazz);
        }

        @Override
        protected SimpleAlertDialogBuilder self() {
            return this;
        }

        @Override
        protected Bundle prepareArguments() {
            Bundle bundle = new Bundle();
            bundle.putString(ARG_TITLE, mTitleString);
            bundle.putCharSequence(ARG_MESSAGE, mMessageString);
            bundle.putString(ARG_NEGATIVE_BUTTON, mNegativeTitle);
            bundle.putString(ARG_POSITIVE_BUTTON, mPositiveTitle);
            bundle.putInt(ARG_TITLE_ICON, mTitleIcon);
            bundle.putInt(ARG_CLOSE_ICON, mTitleCloseIcon);
            bundle.putParcelable(ARG_NEGATIVE_BUTTON_LISTENER, mNegativeListener);
            bundle.putParcelable(ARG_POSITIVE_BUTTON_LISTENER, mPositiveListener);
            return bundle;
        }

        public SimpleAlertDialogBuilder setTitle(String title) {
            mTitleString = title;
            return this;
        }

        public SimpleAlertDialogBuilder setTitle(int resId) {
            return setTitle(mResources.getString(resId));
        }

        public SimpleAlertDialogBuilder setTitlteIcon(int resId) {
            mTitleIcon = resId;
            return this;
        }

        public SimpleAlertDialogBuilder setTitlteCloseIcon(int resId, FlexibleDialogListener listener) {
            mTitleCloseIcon = resId;
            mCloseListener = listener;
            return this;
        }

        public SimpleAlertDialogBuilder setMessage(CharSequence message) {
            mMessageString = message;
            return this;
        }

        public SimpleAlertDialogBuilder setMessage(int resId) {
            mMessageString = mResources.getString(resId);
            return this;
        }

        public SimpleAlertDialogBuilder setPositiveButton(String positiveTitle, FlexibleDialogListener listener) {
            mPositiveTitle = positiveTitle;
            mPositiveListener = listener;
            return this;
        }

        public SimpleAlertDialogBuilder setPositiveButton(int resId, FlexibleDialogListener listener) {
            mPositiveTitle = mResources.getString(resId);
            mPositiveListener = listener;
            return this;
        }

        public SimpleAlertDialogBuilder setNegativeButton(String negativeTitle, FlexibleDialogListener listener) {
            mNegativeTitle = negativeTitle;
            mNegativeListener = listener;
            return this;
        }

        public SimpleAlertDialogBuilder setNegativeButton(int resId, FlexibleDialogListener listener) {
            mNegativeTitle = mResources.getString(resId);
            mNegativeListener = listener;
            return this;
        }
    }
}
