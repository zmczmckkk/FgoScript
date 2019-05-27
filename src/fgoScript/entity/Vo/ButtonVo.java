package fgoScript.entity.Vo;

import com.melloware.jintellitype.JIntellitype;

/**
 * @description: 按钮参数类
 * @author: RENZHEHAO
 * @create: 2019-05-22 06:11
 **/
public class ButtonVo {
    private String fromPerson;
    private int fromSkill;
    private String name;
    private int shortcunt01;
    private int shortcunt02;
    private boolean excuteble;
    private boolean enableStatus;
    private int style;
    private String className;
    private String methodName;

    public ButtonVo(String fromPerson, int fromSkill, String name, String shortcunt01, char shortcunt02, boolean excuteble, boolean enableStatus, int style, String className, String methodName) {
        this.fromPerson = fromPerson;
        this.fromSkill = fromSkill;
        this.name = name;
        this.shortcunt01 = convertShortCut01(shortcunt01);
        this.shortcunt02 = convertShortCut02(shortcunt02);
        this.excuteble = excuteble;
        this.enableStatus = enableStatus;
        this.style = style;
        this.className = className;
        this.methodName = methodName;
    }
    private int convertShortCut01(String shortcunt01){
        int value;
        switch(shortcunt01){
            case "shift" : {
                value = JIntellitype.MOD_SHIFT;
                break;
            }
            case "ctrl" : {
                value = JIntellitype.MOD_CONTROL;
                break;
            }
            case "alt" : {
                value = JIntellitype.MOD_ALT;
                break;
            }
            default : {
                value = 0;
                break;
            }
        }
        return value;
    }
    private int convertShortCut02(char shortcunt02){
        return (int) shortcunt02;
    }
    public String getFromPerson() {
        return fromPerson;
    }

    public void setFromPerson(String fromPerson) {
        this.fromPerson = fromPerson;
    }

    public int getFromSkill() {
        return fromSkill;
    }

    public void setFromSkill(int fromSkill) {
        this.fromSkill = fromSkill;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getShortcunt01() {
        return shortcunt01;
    }

    public void setShortcunt01(int shortcunt01) {
        this.shortcunt01 = shortcunt01;
    }

    public int getShortcunt02() {
        return shortcunt02;
    }

    public void setShortcunt02(int shortcunt02) {
        this.shortcunt02 = shortcunt02;
    }

    public boolean isExcuteble() {
        return excuteble;
    }

    public void setExcuteble(boolean excuteble) {
        this.excuteble = excuteble;
    }

    public boolean isEnableStatus() {
        return enableStatus;
    }

    public void setEnableStatus(boolean enableStatus) {
        this.enableStatus = enableStatus;
    }

    public int getStyle() {
        return style;
    }

    public void setStyle(int style) {
        this.style = style;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
}
