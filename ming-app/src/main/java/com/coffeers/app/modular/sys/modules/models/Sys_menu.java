package com.coffeers.app.modular.sys.modules.models;

import com.coffeers.app.framework.annotation.entity.*;
import com.coffeers.app.framework.bean.BaseModel;
import com.coffeers.app.framework.enums.ColType;

/**
 * Created by Administrator on 2017/8/16 0016.
 *
 */
@Entity("Sys_menu")
public class Sys_menu extends BaseModel{

    @Id(constraints=@Constraints(primaryKey=true,allowNull = false,unique = true))
    @Column
    @ColumnType(type = ColType.VARCHAR,width = 32)
    @Comment("编号")
    private String id;

    @Column
    @ColumnType(type = ColType.VARCHAR,width = 32)
    @Comment("菜单名称")
    private String menu_name;

    @Column
    @ColumnType(type = ColType.VARCHAR,width = 32)
    @Comment("菜单别名")
    private String alias_name;

    @Column
    @ColumnType(type = ColType.INT,width = 12)
    @Comment("菜单位置")
    private Integer menu_location;

    @Column
    @Comment("父级ID")
    private String parentId;

    @Column
    @Comment("树路径")
    private String path;

    @Column
    @Comment("所属系统")
    @ColumnType(type = ColType.VARCHAR, width = 10)
    private String system;

    @Column
    @Comment("资源类型")
    @ColumnType(type = ColType.VARCHAR, width = 10)
    private String type;

    @Column
    @Comment("菜单链接")
    @ColumnType(type = ColType.VARCHAR, width = 255)
    private String href;

    @Column
    @Comment("打开方式")
    @ColumnType(type = ColType.VARCHAR, width = 50)
    private String target;

    @Column
    @Comment("菜单图标")
    @ColumnType(type = ColType.VARCHAR, width = 50)
    private String icon;

    @Column
    @Comment("是否显示")
    @ColumnType(type = ColType.BOOLEAN)
    private boolean isShow;

    @Column
    @Comment("是否禁用")
    @ColumnType(type = ColType.BOOLEAN)
    private boolean disabled;

    @Column
    @Comment("权限标识")
    @ColumnType(type = ColType.VARCHAR, width = 255)
    private String permission;

    @Column
    @Comment("菜单介绍")
    @ColumnType(type = ColType.VARCHAR, width = 255)
    private String note;

    @Column
    @Comment("菜单鼠标悬停特效")
    @ColumnType(type = ColType.VARCHAR, width = 255)
    private String menu_hover;

    @Column
    @Comment("菜单鼠标离开特效")
    @ColumnType(type = ColType.VARCHAR, width = 255)
    private String menu_return;


    @Column
    @Comment("有子节点")
    private boolean hasChildren;
}
