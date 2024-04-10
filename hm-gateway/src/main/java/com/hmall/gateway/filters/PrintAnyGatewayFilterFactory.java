package com.hmall.gateway.filters;

import lombok.Data;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 固定后缀： GatewayFilterFactory，方便配置使用
 * 前缀 PrintAny 写在配置文件中
 *
 * GlobalFilter直接对所有请求生效，
 * 而GatewayFilter则需要在yaml文件配置指定作用的路由范围
 */
@Component
public class PrintAnyGatewayFilterFactory extends AbstractGatewayFilterFactory<PrintAnyGatewayFilterFactory.Config> {
    @Override
    public GatewayFilter apply(Config config) {
       /* return (exchange, chain) -> {
            // 编写过滤器逻辑
            System.out.println("PrintAny filter 执行了");
            // 放行
            return chain.filter(exchange);
        };*/

        // 装饰类，带优先级
        return new OrderedGatewayFilter((exchange, chain) -> {
            String a = config.getA();
            String b = config.getB();
            String c = config.getC();
            System.out.println("参数值：a = " + a);
            System.out.println("参数值：b = " + b);
            System.out.println("参数值：c = " + c);
            System.out.println("PrintAny filter 执行了");
            return chain.filter(exchange);
        }, 1);
    }

    // 自定义配置属性，成员变量名称很重要，下面会用到
    @Data
    public static class Config {
        private String a;
        private String b;
        private String c;
    }

    // 将变量名称依次返回，顺序很重要， 将来读取参数时需要按顺序获取
    @Override
    public List<String> shortcutFieldOrder() {
        return List.of("a", "b", "c");
    }

    // 将Config字节码传递给父类，父类负责帮我们读取yaml配置
    public PrintAnyGatewayFilterFactory() {
        super(Config.class);
    }
}
