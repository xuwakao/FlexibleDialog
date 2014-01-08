package cn.wacao.flexibledialog;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;

/**
 * Created by xujiexing on 13-10-12.
 */
public class CustomViewDialogFragment extends BaseAlertDialogFragment {
    protected static String ARG_TITLE = "title";
    protected static String ARG_POSITIVE_BUTTON = "positive_button";
    protected static String ARG_NEGATIVE_BUTTON = "negative_button";
    protected static String ARG_POSITIVE_BUTTON_LISTENER = "positive_button_listener";
    protected static String ARG_NEGATIVE_BUTTON_LISTENER = "negative_button_listener";
    protected static String ARG_CLOSE_BUTTON_LISTENER = "close_button_listener";
    protected static String ARG_TITLE_ICON = "title_icon";
    protected static String ARG_CLOSE_ICON = "close_icon";
    private static final String ARG_CUSTOM_VIEW = "custom_view";
    private static final String ARG_BIND_DATA_CALLABCK = "bind_data_callback";

    public static CustomViewDialogBuilder createBuilder(Context context, FragmentManager fragmentManager) {
        return new CustomViewDialogBuilder(context, fragmentManager, CustomViewDialogFragment.class);
    }

    @Override
    protected ViewBuilder build(ViewBuilder builder) {
        builder.setTitle(getArguments().getString(ARG_TITLE));
        builder.setView(getArguments().getInt(ARG_CUSTOM_VIEW));
        builder.setNegativeButton(getArguments().getString(ARG_NEGATIVE_BUTTON), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FlexibleDialogListener listener = (FlexibleDialogListener) getArguments().getParcelable(ARG_NEGATIVE_BUTTON_LISTENER);
                if (listener != null) {
                    listener.setDialogFragment(CustomViewDialogFragment.this);
                    listener.onClick(v);
                }
            }
        });

        builder.setPositiveButton(getArguments().getString(ARG_POSITIVE_BUTTON), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FlexibleDialogListener listener = (FlexibleDialogListener) getArguments().getParcelable(ARG_POSITIVE_BUTTON_LISTENER);
                if (listener != null) {
                    listener.setDialogFragment(CustomViewDialogFragment.this);
                    listener.onClick(v);
                }
            }
        });

        builder.setTitleCloseIcon(getArguments().getInt(ARG_CLOSE_ICON, 0), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FlexibleDialogListener listener = (FlexibleDialogListener) getArguments().getParcelable(ARG_CLOSE_BUTTON_LISTENER);
                if (listener != null) {
                    listener.setDialogFragment(CustomViewDialogFragment.this);
                    listener.onClick(v);
                }
            }
        });
        builder.setTitleIcon(getArguments().getInt(ARG_TITLE_ICON));

        FlexibleBindDataCallback callback = getArguments().getParcelable(ARG_BIND_DATA_CALLABCK);
        if (callback != null)
            callback.bindData(builder.getCustomView());

        return builder;
    }

    public static class CustomViewDialogBuilder extends BaseAlertDialogBuilder<CustomViewDialogBuilder> {
        private String mTitleString;
        private int mTitleIcon;
        private String mPositiveTitle;
        private String mNegativeTitle;
        private int mTitleCloseIcon;
        private FlexibleDialogListener mNegativeListener;
        private FlexibleDialogListener mPositiveListener;
        private FlexibleDialogListener mCloseListener;
        private int mCustomView;
        private FlexibleBindDataCallback mBindDataCallback;

        public CustomViewDialogBuilder(Context context, FragmentManager fragmentManager, Class<? extends BaseAlertDialogFragment> clazz) {
            super(context, fragmentManager, clazz);
        }

        @Override
        protected CustomViewDialogBuilder self() {
            return this;
        }

        @Override
        protected Bundle prepareArguments() {
            Bundle bundle = new Bundle();
            bundle.putString(ARG_TITLE, mTitleString);
            bundle.putString(ARG_NEGATIVE_BUTTON, mNegativeTitle);
            bundle.putString(ARG_POSITIVE_BUTTON, mPositiveTitle);
            bundle.putInt(ARG_TITLE_ICON, mTitleIcon);
            bundle.putInt(ARG_CLOSE_ICON, mTitleCloseIcon);
            bundle.putParcelable(ARG_NEGATIVE_BUTTON_LISTENER, mNegativeListener);
            bundle.putParcelable(ARG_POSITIVE_BUTTON_LISTENER, mPositiveListener);
            bundle.putInt(ARG_CUSTOM_VIEW, mCustomView);
            bundle.putParcelable(ARG_BIND_DATA_CALLABCK, mBindDataCallback);
            return bundle;
        }

        public CustomViewDialogBuilder setTitle(String title) {
            mTitleString = title;
            return this;
        }

        public CustomViewDialogBuilder setTitle(int resId) {
            return setTitle(mResources.getString(resId));
        }

        public CustomViewDialogBuilder setTitlteIcon(int resId) {
            mTitleIcon = resId;
            return this;
        }

        public CustomViewDialogBuilder setTitlteCloseIcon(int resId, FlexibleDialogListener listener) {
            mTitleCloseIcon = resId;
            mCloseListener = listener;
            return this;
        }

        public CustomViewDialogBuilder setPositiveButton(String positiveTitle, FlexibleDialogListener listener) {
            mPositiveTitle = positiveTitle;
            mPositiveListener = listener;
            return this;
        }

        public CustomViewDialogBuilder setPositiveButton(int resId, FlexibleDialogListener listener) {
            mPositiveTitle = mResources.getString(resId);
            mPositiveListener = listener;
            return this;
        }

        public CustomViewDialogBuilder setNegativeButton(String negativeTitle, FlexibleDialogListener listener) {
            mNegativeTitle = negativeTitle;
            mNegativeListener = listener;
            return this;
        }

        public CustomViewDialogBuilder setNegativeButton(int resId, FlexibleDialogListener listener) {
            mNegativeTitle = mResources.getString(resId);
            mNegativeListener = listener;
            return this;
        }

        public CustomViewDialogBuilder setCustomView(int resId) {
            mCustomView = resId;
            return this;
        }

        public CustomViewDialogBuilder setBindDataCallback(FlexibleBindDataCallback callback) {
            mBindDataCallback = callback;
            return this;
        }
    }
}
