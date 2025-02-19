package ee.taltech.swmg.Stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import ee.taltech.swmg.Player.KeyboardAdapter;
import ee.taltech.swmg.Player.LevelUpBonuses;
import ee.taltech.swmg.Screen.ShadowWizardMoneyGang;

import java.util.Random;

public class PopUpMenu extends Stage {

    private ImageButton button1;
    private ImageButton button2;
    private ImageButton button3;
    private ImageButton closeButton;
    private boolean isVisible = false;
    private final KeyboardAdapter backupInputAdapter;
    private final ShadowWizardMoneyGang game;

    public PopUpMenu(KeyboardAdapter keyboardAdapter, ShadowWizardMoneyGang game) {
        super(new ScreenViewport());
        backupInputAdapter = keyboardAdapter;
        this.game = game;

        // Initialize your buttons here
        initButtons();
    }

    private void initButtons() {
//        Drawable chooseDrawableUp = new TextureRegionDrawable(new Texture("buttons/Choose.png"));
//        Drawable chooseDrawableDown = new TextureRegionDrawable(new Texture("buttons/Choose.png"));
//        Drawable closeDrawableUp = new TextureRegionDrawable(new Texture("buttons/Close.png"));
//        Drawable closeDrawableDown = new TextureRegionDrawable(new Texture("buttons/Close.png"));
        LevelUpBonuses effect1 = getRandomEffect();
        LevelUpBonuses effect2 = getRandomEffect();
        LevelUpBonuses effect3 = getRandomEffect();

        Drawable button1img = new TextureRegionDrawable(new Texture(getButtonImage(effect1)));
        Drawable button2img = new TextureRegionDrawable(new Texture(getButtonImage(effect2)));
        Drawable button3img = new TextureRegionDrawable(new Texture(getButtonImage(effect3)));
        Drawable closeDrawableUp = new TextureRegionDrawable(new Texture("buttons/Close.png"));

        button1 = new ImageButton(button1img, button1img);
        button2 = new ImageButton(button2img, button2img);
        button3 = new ImageButton(button3img, button3img);
        closeButton = new ImageButton(closeDrawableUp, closeDrawableUp);

        float centerX = Gdx.graphics.getWidth() / 2f - button1.getWidth() / 2;
        float centerY = Gdx.graphics.getHeight() / 2f - button1.getHeight() / 2;

        button1.setPosition(centerX, centerY); //  + button1.getHeight()
        button2.setPosition(centerX - 260, centerY); //  + button2.getHeight()
        button3.setPosition(centerX + 260, centerY); //  + button3.getHeight()
        closeButton.setPosition(centerX, centerY - 100);

        button1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isVisible = false;
                Gdx.input.setInputProcessor(backupInputAdapter);
                applyEffect(effect1);
            }
        });

        button2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isVisible = false;
                Gdx.input.setInputProcessor(backupInputAdapter);
                applyEffect(effect2);
            }
        });

        button3.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isVisible = false;
                Gdx.input.setInputProcessor(backupInputAdapter);
                applyEffect(effect3);
            }
        });

        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isVisible = false;
                Gdx.input.setInputProcessor(backupInputAdapter);
            }
        });

        this.addActor(button1);
        this.addActor(button2);
        this.addActor(button3);
        this.addActor(closeButton);
    }

    private String getButtonImage(LevelUpBonuses levelUpBonuses) {
        return levelUpBonuses.getPathToIcon();
    }

    private LevelUpBonuses getRandomEffect() {
        Random random = new Random();
        return LevelUpBonuses.values()[(random.nextInt(LevelUpBonuses.values().length))];
    }

    public void toggleVisibility() {
        boolean visible = !this.isVisible();
        this.setVisible(visible);
        if (visible) {
            refreshButtons();
            Gdx.input.setInputProcessor(this);
        } else {
            Gdx.input.setInputProcessor(backupInputAdapter);
        }
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public void refreshButtons() {
        button1.remove();
        button2.remove();
        button3.remove();
        closeButton.remove();
        initButtons();
    }

    private void applyEffect(LevelUpBonuses effect) {
        switch (effect) {
            case MAX_HEALTH:
                game.getWizard().setCurrentMaxHealth(game.getWizard().getCurrentMaxHealth() + 20f);
                break;
            case HEALTH_REGEN:
                game.getWizard().setHealthRegen(game.getWizard().getHealthRegen() + 0.05);
                break;
            case MAX_MANA:
                game.getWizard().setMaxMana(game.getWizard().getMaxMana() + 20);
                break;
            case MANA_REGEN:
                game.getWizard().setManaRestoration(game.getWizard().getManaRestoration() + 0.15f);
                break;
            case DAMAGE:
                game.getWizard().setDamage(game.getWizard().getDamage() + 3);
                break;
            case SPEED:
                game.getInputAdapter().setSpeed(game.getInputAdapter().getSpeed() + 0.5f);
                break;
        }
    }
}