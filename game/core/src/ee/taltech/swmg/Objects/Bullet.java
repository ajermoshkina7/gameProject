package ee.taltech.swmg.Objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import ee.taltech.swmg.Player.Wizzard;
import ee.taltech.swmg.Screen.ShadowWizardMoneyGang;

import java.io.Serializable;

public class Bullet implements Serializable {
    private final float speed = 10;
    private Vector2 bulletPosition;

    private transient final int size = 48;
    private transient final int halfSize = size / 2;
    private transient Vector2 velocity;
    private transient float angle;
    private transient Rectangle hitbox =new Rectangle();
    private transient boolean alive = true;

    public Bullet() {
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    /**
     * Constructor.
     *
     * @param origin the location of player
     * @param position the position of mouse
     */
    public Bullet(Vector2 origin, Vector2 position) {
        bulletPosition = origin;
        this.angle = (float) Math.atan2(position.y - origin.y, position.x - origin.x);
        velocity = new Vector2((float) Math.cos(angle) * speed, (float) Math.sin(angle) * speed);
    }
    public void updateHitbox() {
        hitbox.set(bulletPosition.x, bulletPosition.y, size, size);
    }

    /**
     * Update the location of bullet.
     */
    public void update(Wizzard owner) {
        bulletPosition.add(velocity.x, velocity.y);
        Vector3 screenCoordinates = new Vector3(bulletPosition.x, bulletPosition.y, 0);
        owner.getCam().project(screenCoordinates);
        if (screenCoordinates.x < 0
                || screenCoordinates.x > ShadowWizardMoneyGang.V_WIDTH
                || screenCoordinates.y < 0
                || screenCoordinates.y > ShadowWizardMoneyGang.V_HEIGHT) {
            this.alive = false;
        }
    }

    /**
     * Render Bullet
     *
     * @param batch to render
     */
    public void render(Batch batch, TextureRegion textureRegion, Wizzard owner) {
        batch.draw(textureRegion,
                bulletPosition.x,
                bulletPosition.y,
                halfSize,
                halfSize,
                size,
                size,
                1,
                1,
                angle
        );
    }

    public Rectangle getHitbox() {
        return hitbox;
    }
}
