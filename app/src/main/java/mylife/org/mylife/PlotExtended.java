package mylife.org.mylife;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

import com.androidplot.Plot;
import com.androidplot.ui.AnchorPosition;
import com.androidplot.ui.XLayoutStyle;
import com.androidplot.ui.YLayoutStyle;
import com.androidplot.ui.widget.TextLabelWidget;
import com.androidplot.xy.XYGraphWidget;
import com.androidplot.xy.XYPlotZoomPan;
import com.androidplot.xy.XYStepMode;

/**
 * Created by Cveni on 2015-01-22.
 */

public class PlotExtended extends XYPlotZoomPan
{
    final float labelScale = 1.8f;
    final float valueScale = 1.3f;
    final static float lineScale = 1.8f;
    final static int lineColor = Color.rgb(251, 126, 20);
    final static int fillColor = Color.rgb(51, 181, 229);

    public PlotExtended(Context context, String title, RenderMode mode)
    {
        super(context, title, mode);
        init();
    }

    public PlotExtended(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public PlotExtended(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    public PlotExtended(Context context, String title)
    {
        super(context, title);
        init();
    }

    public void init()
    {
        setPlotMargins(0, 0, 0, 0);
        setPlotPadding(0, 0, 0, 0);
        setBorderStyle(Plot.BorderStyle.NONE, 0f, 0f);

        XYGraphWidget widget = getGraphWidget();
        widget.getBackgroundPaint().setColor(Color.TRANSPARENT);
        widget.getGridBackgroundPaint().setColor(Color.TRANSPARENT);
        widget.setMargins(0, 0, 0, 0);
        widget.setPadding(6, 12, 20, 20);

        widget.getRangeLabelPaint().setTextSize(widget.getRangeLabelPaint().getTextSize()*valueScale);
        widget.getDomainLabelPaint().setTextSize(widget.getDomainLabelPaint().getTextSize()*valueScale);

        getLegendWidget().setVisible(false);

        setDomainStep(XYStepMode.INCREMENT_BY_VAL.SUBDIVIDE, 10);
        setRangeStep(XYStepMode.INCREMENT_BY_VAL.SUBDIVIDE, 10);
    }

    public void setLabels(String xAxis, String yAxis)
    {
        setDomainLabel(xAxis);
        setRangeLabel(yAxis);

        TextLabelWidget widgetX = getDomainLabelWidget();
        widgetX.getLabelPaint().setTextSize(widgetX.getLabelPaint().getTextSize()*labelScale);
        widgetX.setWidth(widgetX.getWidthPix(1)*labelScale);
        widgetX.setHeight(widgetX.getHeightPix(1)*labelScale);

        widgetX.position(0, XLayoutStyle.ABSOLUTE_FROM_CENTER, 0, YLayoutStyle.RELATIVE_TO_BOTTOM, AnchorPosition.BOTTOM_MIDDLE);

        TextLabelWidget widgetY = getRangeLabelWidget();
        widgetY.getLabelPaint().setTextSize(widgetY.getLabelPaint().getTextSize()*labelScale);
        widgetY.setWidth(widgetY.getWidthPix(1)*labelScale);
        widgetY.setHeight(widgetY.getHeightPix(1)*labelScale);
    }
}
