package cangwang.com.base.bean;

/**
 * Created by air on 16/10/13.
 */

public class ReadItemBean {
    private String curPath;
    private String recordName;
    private String recordSize;
    private String recordCreateTime;

    public ReadItemBean(String curPath, String recordName, String recordSize, String recordCreateTime) {
        this.curPath = curPath;
        this.recordName = recordName;
        this.recordSize = recordSize;
        this.recordCreateTime = recordCreateTime;
    }

    public String getCurPath() {
        return curPath;
    }

    public void setCurPath(String curPath) {
        this.curPath = curPath;
    }

    public String getRecordName() {
        return recordName;
    }

    public void setRecordName(String recordName) {
        this.recordName = recordName;
    }

    public String getRecordSize() {
        return recordSize;
    }

    public void setRecordSize(String recordSize) {
        this.recordSize = recordSize;
    }

    public String getRecordCreateTime() {
        return recordCreateTime;
    }

    public void setRecordCreateTime(String recordCreateTime) {
        this.recordCreateTime = recordCreateTime;
    }
}
