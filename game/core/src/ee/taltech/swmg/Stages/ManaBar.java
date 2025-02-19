package ee.taltech.swmg.Stages;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;

/**
 * The progress bar which reassembles the behaviour of the health bar.
 *
 * @author serhiy
 */
public class ManaBar extends ProgressBar {

    @Override
    public boolean setValue(float value) {
        if (value > getMaxValue()) {
            setRange(0f, value);
        }
        return super.setValue(value);
    }

    /**
     * @param width of the health bar
     * @param height of the health bar
     */
    public ManaBar(int width, int height) {
        super(0f, 1f, 0.01f, false, new ProgressBarStyle());
        getStyle().background = Utils.getColoredDrawable(width + 2, height + 2, Color.BLACK);
        getStyle().knob = Utils.getColoredDrawable(4, height, Color.BLACK);
        getStyle().knobBefore = Utils.getColoredDrawable(width, height, Color.BLUE);
        getStyle().knobAfter = Utils.getColoredDrawable(width, height, Color.LIGHT_GRAY);


        setWidth(width);
        setHeight(height);

        setAnimateDuration(0.0f);
        setValue(1f);

        setAnimateDuration(0.25f);
    }
}
