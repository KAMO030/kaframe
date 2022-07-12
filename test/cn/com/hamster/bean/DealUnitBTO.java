package cn.com.hamster.bean;

import java.io.Serializable;

public class DealUnitBTO  implements Serializable {
    /**
     * 单位编号
     */
    private int duId;
    /**
     * 单位名称
     */
    private String duName;

    /**
     * 联系电话
     */
    private String duTelephone;
    /**
     * 联系地址
     */
    private String duAddress;
    private String utSupply;
    /**
     * 禁用状态
     */
    private int duDefault;
    /**
     * 默认单位
     */
    private int duDefunit;
    /**
     * 备注
     */
    private String duNote;

    @Override
    public String toString() {
        return "DealUnitBTO{" +

                " duName='" + duName + '\'' +
                ", duTelephone='" + duTelephone + '\'' +
                ", duAddress='" + duAddress + '\'' +
                ", utSupply='" + utSupply + '\'' +
                ", duDefault=" + duDefault +
                ", duDefunit=" + duDefunit +
                ", duNote='" + duNote + '\'' +
                "}\n";
    }
}
