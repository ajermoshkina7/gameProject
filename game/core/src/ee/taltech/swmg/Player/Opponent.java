package ee.taltech.swmg.Player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import ee.taltech.swmg.Objects.Bullet;
import ee.taltech.swmg.packages.PlayerPackage;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Opponent extends Wizzard {
    private int id;
    public Opponent() {

    }
    public void receivePacket(PlayerPackage packege) {
        this.bullets = packege.getBullets();
        this.setPosition(packege.getPosition());

    }
    public void initilize() {
        super.texture = new Texture(Gdx.files.internal("players/Wizard2.jpg"));
        super.textureRegion = new TextureRegion(super.texture);
        super.bulletTextureRegion = new TextureRegion(new Texture(Gdx.files.internal("objects/fireball.png")));
    }
    @Override
    public void render(Batch batch) {
        batch.draw(textureRegion,
                position.x,
                position.y,
                halfSize,
                halfSize,
                size,
                size,
                1,
                1,
                angle.angleDeg()
        );
        bullets.forEach(bullet -> {
            bullet.render(batch, bulletTextureRegion, this);
        });
    }

    public int getId() {
        return id;
    }
}
