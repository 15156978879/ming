package com.coffeers.app.modular.index.modules.models;

import com.coffeers.app.framework.annotation.entity.*;
import com.coffeers.app.framework.bean.BaseModel;
import com.coffeers.app.framework.enums.ColType;

/**
 * Created by Administrator on 2017/8/16 0016.
 *
 */
@Entity("hello")
public class Hello extends BaseModel{

    @Id(constraints=@Constraints(primaryKey=true,allowNull = false,unique = true))
    @Column
    @ColumnType(type = ColType.VARCHAR,width = 32)
    @Comment("编号")
    private String id;

    @Column
    @ColumnType(type = ColType.VARCHAR,width = 22)
    @Comment("姓名")
    private String name;

    @Column
    @ColumnType(type = ColType.INT,width = 12)
    @Comment("年龄")
    @Default("0")
    private Integer age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Hello{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
