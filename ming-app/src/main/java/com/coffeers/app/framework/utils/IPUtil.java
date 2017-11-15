package com.coffeers.app.framework.utils;


import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

public class IPUtil {

    private final static String IP_SEPARATOR = ",";

    /**
     * return 机器名
     */
    public static String getHostName() {
        try {
            InetAddress ia = InetAddress.getByName("127.0.0.1");
            return ia.getHostName();
        } catch (UnknownHostException e) {
            return null;
        }
    }

    /**
     * 判断是否开发或测试机 (Hard Code)
     */
    public static boolean isDev() {
        String[] ips = getIps();
        if (ips == null) {
            return false;
        }
        for (String ip : ips) {
            if (ip.startsWith("10.10")) {
                return true;
            }
            if (ip.startsWith("192.168")) {
                return true;
            }
        }
        return false;
    }


    /**
     * 获取客户端Ip
     *
     * @param request
     */
    public static String getIP(HttpServletRequest request) {

        String ip = request.getHeader("X-Real-IP");

        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("x-forwarded-for");
        } else {
            return ip;
        }

        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        } else {
            // 当有多级反向代理时，x-forwarded-for值为多个时取第一个ip地址
            if (ip.indexOf(IP_SEPARATOR) != -1) {
                ip = ip.substring(0, ip.indexOf(IP_SEPARATOR));
            }
            return ip;
        }

        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        } else {
            return ip;
        }

        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        } else {
            return ip;
        }

        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = "";
        }
        return ip;
    }

    /**
     * 获取当前机器的所有IP
     */
    public static String[] getIps() {

        List<String> ips = new ArrayList<String>();
        try {
            Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
            for (NetworkInterface netint : Collections.list(nets))
                if (netint.getHardwareAddress() != null) {
                    List<InterfaceAddress> list = netint.getInterfaceAddresses();
                    for (InterfaceAddress interfaceAddress : list) {
                        String localIp = interfaceAddress.getAddress().getHostAddress();
                        if (StringUtils.isNotBlank(localIp)) {
//                            if (StringUtils.isNoneBlank(localIp)) {
                            ips.add(localIp);
                        }
                    }
                }
        } catch (SocketException e1) {
            return null;
        }
        return ips.toArray(new String[ips.size()]);
    }

}