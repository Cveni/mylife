package mylife.org.mylife;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;

/**
 * Created by Cveni on 2015-01-06.
 */

public class EditTextPreferenceNew extends EditTextPreference
{
    public EditTextPreferenceNew(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public EditTextPreferenceNew(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EditTextPreferenceNew(Context context) {
        super(context);
        init();
    }

    private void init()
    {
        setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue)
            {
                preference.setSummary(newValue.toString());

                return true;
            }
        });
    }

    @Override
    public CharSequence getSummary()
    {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getContext());

        return settings.getString(getKey(), "");
    }
}