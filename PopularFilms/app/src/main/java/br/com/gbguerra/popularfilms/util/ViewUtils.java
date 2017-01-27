package br.com.gbguerra.popularfilms.util;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by Giovani Guerra on 30/12/2016.
 * An utility class for work with dimensions with views
 */

public abstract class ViewUtils {

    public static int dpToPx(Context ctx, float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, ctx.getResources().getDisplayMetrics());
    }

}
