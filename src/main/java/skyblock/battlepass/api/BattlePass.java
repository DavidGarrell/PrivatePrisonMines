package skyblock.battlepass.api;

public class BattlePass {

    private int level;

    private int xp;

    private int MAX_LEVEL;

    private int XP_NEED_PER_LEVEL;

    private boolean isPremium;


    public BattlePass(int level, int xp, boolean isPremium){
        this.xp = xp;
        this.level = level;
        this.MAX_LEVEL = 20;
        this.isPremium = isPremium;
    }


    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public int getMAX_LEVEL() {
        return MAX_LEVEL;
    }

    public int getXP_NEED_PER_LEVEL() {
        return XP_NEED_PER_LEVEL;
    }

    public boolean isPremium() {
        return isPremium;
    }

    public void setPremium(boolean premium) {
        isPremium = premium;
    }
}
