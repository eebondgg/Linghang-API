package com.my.yuapigateway;

import com.my.myapi.common.model.entity.InterfaceInfo;
import com.my.myapi.common.model.entity.User;
import com.my.myapi.common.service.InnerInterfaceInfoService;
import com.my.myapi.common.service.InnerUserInterfaceInfoService;
import com.my.myapi.common.service.InnerUserService;
import com.my.myapiclientsdk.utils.SignUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 全局过滤
 */
@Slf4j
@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered {
    @DubboReference
    private InnerUserService innerUserService;

    @DubboReference
    private InnerUserInterfaceInfoService innerUserInterfaceInfoService;

    @DubboReference
    private InnerInterfaceInfoService innerInterfaceInfoService;

    private static final List<String> IP_WHITE_LIST = Arrays.asList("127.0.0.1");

    private static final String INTERFACE_HOST = "http://localhost:8123";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //1. 请求日志
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        String path = INTERFACE_HOST + request.getPath().value();
        String method = request.getMethod().toString();

        log.info("请求id:"+request.getId());
        log.info("请求URI:"+request.getURI());
        log.info("请求PATH:"+request.getPath());
        log.info("请求参数:"+request.getQueryParams());
        log.info("本地请求地址:"+request.getLocalAddress());
        log.info("请求地址："+request.getRemoteAddress());

        //2. 访问控制  黑白名单
//        if(!IP_WHITE_LIST.contains(sourceAddress)){
//            response.setStatusCode(HttpStatus.FORBIDDEN);
//            return response.setComplete();
//        }

        //3. 鉴权
        HttpHeaders headers = request.getHeaders();
        String accessKey = headers.getFirst("accessKey");
        String nonce = headers.getFirst("nonce");
        String timestamp = headers.getFirst("timestamp");
        String sign = headers.getFirst("sign");
        String body = headers.getFirst("body");
        User invokeUser = null;
        try{
            invokeUser = innerUserService.getInvokeUser(accessKey);
        }catch (Exception e){
            log.error("getInvokeUser error",e);
        }
        if (invokeUser==null) {
            return handleNotAuth(response);
        }

//        if (!"laowang".equals(accessKey)){
//            return handleNotAuth(response);
//        }
        if (Long.parseLong(nonce) > 10000L){
            return handleNotAuth(response);
        }
        Long currentTime = System.currentTimeMillis() / 1000;
        final Long FINE_MINUTES = 60 * 5L;
        if ((currentTime - Long.parseLong(timestamp)) >= FINE_MINUTES){
            return handleNotAuth(response);
        }
        String secretKey = invokeUser.getSecretKey();
        String serverSign = SignUtils.genSign(body, secretKey);
        if (sign == null || !sign.equals(serverSign)) {
            return handleNotAuth(response);
        }
        //4. 请求的模拟接口是否存在
        InterfaceInfo interfaceInfo = null;
        try{
            interfaceInfo = innerInterfaceInfoService.getInterfaceInfo(path, method);
            System.out.println(interfaceInfo);
        }catch (Exception e){
            log.info("远程调用获取被调用接口信息失败");
            e.printStackTrace();
        }
        if (interfaceInfo == null) {
            log.error("接口不存在！！！！");
            return handleNotAuth(response);
        }
        //5. 请求转发，调用模拟接口 + 响应日志
        //Mono<Void> filter = chain.filter(exchange);
        //        return filter;
        return handleResponse(exchange, chain, interfaceInfo.getId(), invokeUser.getId());
    }

    /**
     * 处理响应
     * @param exchange
     * @param chain
     * @return
     */
    public Mono<Void> handleResponse(ServerWebExchange exchange, GatewayFilterChain chain, long interfaceInfoId, long userId){
        try {
            ServerHttpResponse originalResponse = exchange.getResponse();
            DataBufferFactory bufferFactory = originalResponse.bufferFactory();
            HttpStatus statusCode = originalResponse.getStatusCode();
            if (statusCode != HttpStatus.OK) {
                return chain.filter(exchange);//降级处理返回数据
            }
            ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
                @Override
                public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                    if (body instanceof Flux) {
                        Flux<? extends DataBuffer> fluxBody = Flux.from(body);

                        return super.writeWith(
                                fluxBody.map(dataBuffer -> {
                                    //7. 调用成功，调用接口次数 + 1  invokeCount
                                    try {
                                        innerUserInterfaceInfoService.invokeCount(interfaceInfoId,userId);
                                    } catch (Exception e) {
                                        log.error("invokeCount error",e);
                                    }
                                    // 合并多个流集合，解决返回体分段传输
                            byte[] content = new byte[dataBuffer.readableByteCount()];
                            dataBuffer.read(content);
                            DataBufferUtils.release(dataBuffer);//释放掉内存

                            // 构建返回日志
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("<--- {} {} \n");
                            List<Object> rspArgs = new ArrayList<>();
                            rspArgs.add(originalResponse.getStatusCode().value());
                            rspArgs.add(exchange.getRequest().getURI());
                            String data = new String(content, StandardCharsets.UTF_8);
                            sb2.append(data);
                            //打印日志
                            log.info("响应结果：" + data);
                            return bufferFactory.wrap(content);
                        }));
                    } else {
                        //8. 调用失败，返回一个规范的错误码
                        log.error("<-- {} 响应code异常", getStatusCode());
                    }
                    return super.writeWith(body);
                }
            };
            return chain.filter(exchange.mutate().response(decoratedResponse).build());

        } catch (Exception e) {
            log.error("网关降级响应异常" + e);
            return chain.filter(exchange);
        }
    }

    @Override
    public int getOrder() {
        return -1;
    }

    public Mono<Void> handleNotAuth(ServerHttpResponse response){
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();
    }

    public Mono<Void> handleInvokeError(ServerHttpResponse response){
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        return response.setComplete();
    }
}