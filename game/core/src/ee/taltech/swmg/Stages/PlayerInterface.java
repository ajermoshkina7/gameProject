package ee.taltech.swmg.Stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.Viewport;
import ee.taltech.swmg.Player.Wizzard;

public class PlayerInterface extends Stage {
    private Wizzard player;
    private ManaBar manaBar;
    private HealthBar healthBar;
    private Label levelLabel;
    private Label timeLabel;
    private Label xpLabel;
    private BitmapFont font;


    public PlayerInterface(Viewport viewport, Wizzard wizzard) {
        super(viewport);
        this.font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(1.5f);

        Label.LabelStyle style = new Label.LabelStyle(font, Color.WHITE);

        levelLabel = new Label("", style);
        xpLabel = new Label("", style);
        timeLabel = new Label("", style);

        Table table = new Table();
        table.top();
        table.setFillParent(true);
        manaBar = new ManaBar(400, 20);
        healthBar = new HealthBar(400, 20);
        table.add(healthBar).expandX().padTop(10);
        table.row();
        table.add(manaBar).expandX().padTop(10);
        table.row();
        table.add(levelLabel).expandX().padTop(10);
        table.row();
        table.add(xpLabel).expandX().padTop(5);
        table.row();
        table.add(timeLabel).expandX().padTop(5);
        this.player = wizzard;
        this.addActor(table);
    }

    public void draw(float delta, float currentTime) {
        healthBar.setValue((float) player.getCurrentHealth() / 100f);
        manaBar.setValue(player.getMana() / 100f);
        levelLabel.setText("Level: " + player.getLevel());
        xpLabel.setText("XP: " + player.getXp() + " / " + player.getCurrentLevelXp());
        timeLabel.setText("Time: " + String.valueOf((int) (currentTime / 60)) + ":" + String.format("%.0f", currentTime % 60));
        this.act(Math.min(delta, 1 / 30f));
        this.draw();
    }

    public void dispose() {
        font.dispose();
    }
}
