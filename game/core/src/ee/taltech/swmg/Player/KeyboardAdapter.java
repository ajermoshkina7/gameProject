package ee.taltech.swmg.Player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;

import java.util.Optional;

public class KeyboardAdapter extends InputAdapter {
    private boolean leftPressed, rightPressed, upPressed, downPressed, firePressed, debugOnePressed;
    private final Vector2 mousePosition  = new Vector2();
    private final Vector2 direction = new Vector2();
    private float speed = 5f; // this value controls speed of the player

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.A) leftPressed = true;
        if (keycode == Input.Keys.D) rightPressed = true;
        if (keycode == Input.Keys.W) upPressed = true;
        if (keycode == Input.Keys.S) downPressed = true;
        if (keycode == Input.Keys.P) debugOnePressed = true;
        return false;
    }
    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.A) leftPressed = false;
        if (keycode == Input.Keys.D) rightPressed = false;
        if (keycode == Input.Keys.W) upPressed = false;
        if (keycode == Input.Keys.S) downPressed = false;
        if (keycode == Input.Keys.P) debugOnePressed = false;
        return false;
    }
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (!firePressed) {
            firePressed = true;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        firePressed = false;
        return false;
    }
    public Vector2 getMousePosition() {
        return mousePosition;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        mousePosition.set(screenX, screenY);
        return false;
    }
    /**
     * Get the direction based on the pressed keys.
     *
     * @return the direction vector
     */
    public Vector2 getDirection() {
        direction.set(0, 0);
        if (leftPressed) direction.add(-1, 0);
        if (rightPressed) direction.add(1, 0);
        if (upPressed) direction.add(0, 1);
        if (downPressed) direction.add(0, -1);

        // Normalize the direction vector to have a magnitude of 1
        // This ensures consistent movement speed in all directions
        if (direction.len() > 0) {
            direction.nor(); // Normalize the vector to unit length
            // Scale the normalized vector to your desired speed
            direction.scl(speed);
        }

        return direction;
    }
    /**
     * getFirePressed returns the mouse position if fire is pressed, otherwise returns empty.
     *
     * @return Optional of Vector2 if fire is pressed, empty otherwise
     */
    public Optional<Vector2> getFirePressed() {
        if (firePressed) {
            firePressed = false;
            return Optional.of(mousePosition);
        } else {
            return Optional.empty();
        }
    }

    public boolean getDebugOnePressed() {
        if (debugOnePressed) {
            debugOnePressed = false;
            return true;
        } else {
            return false;
        }
    }


    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}
