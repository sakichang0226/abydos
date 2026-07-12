package com.project.abydos.saki.common.constant;

/**
 * 共通エンドポイントパス定義.
 * 各APIパッケージのControllerでは{@code @RequestMapping(Endpoint.API_PREFIX)}を付与し、
 * メソッドレベルで各リソースパスを指定する。
 */
public class Endpoint {
    public static final String API = "/api";
    public static final String VERSION = "/v1";
    public static final String API_PREFIX = API + VERSION;
    public static final String ROOT = "/";
    public static final String HEALTH = "/health";
    public static final String LOGIN = "/login";
    public static final String LOGOUT = "/logout";
    public static final String PRODUCTS = "/products";
    public static final String ORDERS = "/orders";

    private Endpoint() {}
}
