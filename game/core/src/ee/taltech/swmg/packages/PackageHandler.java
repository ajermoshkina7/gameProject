package ee.taltech.swmg.packages;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.kryonet.Client;
import ee.taltech.swmg.Enemy.Enemy;
import ee.taltech.swmg.GameClient;
import ee.taltech.swmg.Player.Opponent;
import ee.taltech.swmg.Player.Wizzard;
import ee.taltech.swmg.Screen.ShadowWizardMoneyGang;
import ee.taltech.swmg.Screen.StartScreen;
import ee.taltech.swmg.Screen.YouWonScreen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class PackageHandler {
    private final List<String> textureList = new ArrayList<>(Arrays.asList("enemies/enemy1.png","enemies/enemy2.png",
            "enemies/enemy3.png", "enemies/enemy4.png"));
    private Wizzard wizard;
    private Client client;
    private Batch batch;
    private GameClient gameClient;
    private float previousTime = 0;
    private ShadowWizardMoneyGang shadowWizardMoneyGang;


    public  void handlePackages(Map<Integer, PlayerPackage> receivedGamePackages, List<EnemyPackage> enemyPackages, List<Enemy> enemies, Map<Integer, Opponent> opponents) throws IOException {
        for (EnemyPackage package1 : enemyPackages) {
            if (enemies.stream().noneMatch(enemy -> enemy.getId() == package1.getId())) {
                enemies.add(new Enemy(package1));
            } else {
                enemies.stream().filter(enemy -> enemy.getId() == package1.getId()).findFirst().get().assimilatePackage(package1);
            }
        }
        enemies.removeIf(enemy -> enemyPackages.stream().noneMatch(package1 -> package1.getId() == enemy.getId()));
        enemies.forEach(
                i -> {
                    if (!i.getTexture().isPresent()) {
                        i.setTextureToEnemy();
                    }
                    i.attack(wizard);
                    i.takeDamage(wizard, client);
                    i.render(batch, wizard);

                });
        for (Map.Entry<Integer, PlayerPackage> entry : receivedGamePackages.entrySet()) {
            if (entry.getKey() != client.getID()) {
                if (!opponents.containsKey(entry.getKey())) {
                    opponents.put(entry.getKey(), new Opponent());
                }
                opponents.get(entry.getKey()).receivePacket(entry.getValue());
            }
        }
        List<Integer> deadPlayers = new ArrayList<>(opponents.keySet());
        deadPlayers.removeAll(receivedGamePackages.keySet());
        if (!deadPlayers.isEmpty()) {
            shadowWizardMoneyGang.dispose();
            gameClient.setScreen(new YouWonScreen(gameClient));
        }
        for (Map.Entry<Integer, Opponent> entry : opponents.entrySet()) {
            Opponent opponent = entry.getValue();
            if (opponent.getTextureRegion() == null) {
                opponent.initilize();
            }
            opponent.render(batch);
        }
    }
    public void handleEnemies(List<Enemy> enemies, float currentTime) {
        enemies.forEach(i -> {
            i.moveTo(wizard.getPosition());
            i.attack(wizard);
            i.takeDamage(wizard);
            i.render(batch, wizard);
        });
        enemies.removeIf(enemy -> !enemy.isAlive());
        if (currentTime - previousTime >= 1) {
            enemies.add(new Enemy());
            previousTime = currentTime;
        }
    }

    public void setWizard(Wizzard wizard) {
        this.wizard = wizard;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setBatch(Batch batch) {
        this.batch = batch;
    }

    public void setGameClient(GameClient gameClient) {
        this.gameClient = gameClient;
    }

    public ShadowWizardMoneyGang getShadowWizardMoneyGang() {
        return shadowWizardMoneyGang;
    }

    public void setShadowWizardMoneyGang(ShadowWizardMoneyGang shadowWizardMoneyGang) {
        this.shadowWizardMoneyGang = shadowWizardMoneyGang;
    }
}
