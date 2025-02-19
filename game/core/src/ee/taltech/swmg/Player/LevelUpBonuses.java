package ee.taltech.swmg.Player;

public enum LevelUpBonuses {
    MAX_HEALTH("buttons/HealthBuff.png"),
    HEALTH_REGEN("buttons/HealthRegenBuff.png"),
    MAX_MANA("buttons/MaxManaBuff.png"),
    MANA_REGEN("buttons/ManaRegenBuff.png"),
    DAMAGE("buttons/DamageBuff.png"),
    SPEED("buttons/SpeedBuff.png");

    private final String pathToIcon;

    LevelUpBonuses(String pathToIcon) {
        this.pathToIcon = pathToIcon;
    }

    public String getPathToIcon() {
        return pathToIcon;
    }
}
