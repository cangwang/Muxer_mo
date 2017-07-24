package cangwang.com.base.greenDao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

/**
 * Created by air on 16/10/14.
 */
@Entity
public class SettingsInfo {
    @Id
    private long id;
    @Property(nameInDb = "SETTING_WIDTH")
    private int width;
    @Property(nameInDb = "SETTING_HEIGHT")
    private int height;
    @Property(nameInDb = "SETTING_DENISITY")
    private int density;
    @Property(nameInDb = "SETTING_RECORDPATH")
    private String recordPath;
    @Generated(hash = 497974359)
    public SettingsInfo(long id, int width, int height, int density,
            String recordPath) {
        this.id = id;
        this.width = width;
        this.height = height;
        this.density = density;
        this.recordPath = recordPath;
    }
    @Generated(hash = 2068276160)
    public SettingsInfo() {
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public int getWidth() {
        return this.width;
    }
    public void setWidth(int width) {
        this.width = width;
    }
    public int getHeight() {
        return this.height;
    }
    public void setHeight(int height) {
        this.height = height;
    }
    public int getDensity() {
        return this.density;
    }
    public void setDensity(int density) {
        this.density = density;
    }
    public String getRecordPath() {
        return this.recordPath;
    }
    public void setRecordPath(String recordPath) {
        this.recordPath = recordPath;
    }
}
