package com.leyou.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

//只提供get方法
@Getter
//自动生成无参数构造函数
@NoArgsConstructor
//自动生成全参数构造函数
@AllArgsConstructor
public enum  ExceptionEnums {
    PRICE_CANNOT_BE_NULL(400,"价格不能为空"),
    CATEGORY_NOT_FOUND(400,"商品分类未找到！"),
    BRAND_SAVE_ERROR  (400,"品牌保存失败！"),
    BRAND_EDIT_ERROR(400,"品牌修改失败！"),
    SPEC_PARAM_NOT_FOUND(404,"商品规格参数不存在")
    ;

    private int code;
    private String  msg;

}
