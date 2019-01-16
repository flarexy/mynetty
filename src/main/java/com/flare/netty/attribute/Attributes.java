package com.flare.netty.attribute;

import io.netty.util.AttributeKey;

/**
 * @ClassName Attributes
 * @Description 属性
 * @Author xieyi
 * @Data 2019/1/16
 */
public interface Attributes {
    AttributeKey<Boolean> LOGIN = AttributeKey.newInstance("login");
}
