# esjpademo1
## 两个索引
1、newperson: 人口数据，加密的数据
2、keywordsnew: 实现自动填充的关键字，明文数据

## 注解
@OriginData 在类上加上该注解表示该实体是需要在es中保存在一个原始字段
@SensitiveField 在字段上加上该注解表示该字段会进行加密, isKeyword设置为true 说明需要把该字段的值存储到keywordsnew索引，value值为字段的加密方式可以选择RSA或MD5
```
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Order(Ordered.HIGHEST_PRECEDENCE)
public @interface SensitiveField {
    String value();
    boolean isKeyword() default false;
}

```
@SensitiveMethod 在方法上加该字段，说明该方法中的参数是需要加密的和@SensitiveField 配合使用，在使用@SensitiveField时必须给方法加上SensitiveMethod
