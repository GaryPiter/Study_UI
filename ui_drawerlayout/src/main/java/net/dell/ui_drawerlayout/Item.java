package net.dell.ui_drawerlayout;

/**
 * Created by dell on 2016/4/26.
 */
public class Item {

    private int iconId;
    private String iconName;

    public Item(int iconId, String iconName) {
        this.iconId = iconId;
        this.iconName = iconName;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public String getIconName() {
        return iconName;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }
}
