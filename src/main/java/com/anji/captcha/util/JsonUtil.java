package com.anji.captcha.util;

import com.anji.captcha.model.vo.PointVO;

import java.util.ArrayList;
import java.util.List;

/**
 * 简单 JSON 工具。
 */
public final class JsonUtil {

    private JsonUtil() {
    }

    /**
     * 解析点选坐标数组。
     *
     * @param json JSON 数组
     * @param clazz 点对象类型
     * @return 点对象列表
     */
    public static List<PointVO> parseArray(String json, Class<PointVO> clazz) {
        if (json == null) {
            return null;
        }
        String content = json.trim();
        if (content.length() == 0 || "[]".equals(content)) {
            return new ArrayList<PointVO>();
        }
        if (content.startsWith("[")) {
            content = content.substring(1);
        }
        if (content.endsWith("]")) {
            content = content.substring(0, content.length() - 1);
        }
        List<PointVO> list = new ArrayList<PointVO>();
        int depth = 0;
        int startIndex = -1;
        for (int index = 0; index < content.length(); index++) {
            char current = content.charAt(index);
            if (current == '{') {
                if (depth == 0) {
                    startIndex = index;
                }
                depth++;
            } else if (current == '}') {
                depth--;
                if (depth == 0 && startIndex >= 0) {
                    String item = content.substring(startIndex, index + 1);
                    list.add(parseObject(item, clazz));
                    startIndex = -1;
                }
            }
        }
        return list;
    }

    /**
     * 解析点对象。
     *
     * @param json JSON 片段
     * @param clazz 点对象类型
     * @return 点对象
     */
    public static PointVO parseObject(String json, Class<PointVO> clazz) {
        if (json == null) {
            return null;
        }
        try {
            return clazz.getDeclaredConstructor().newInstance().parse(json);
        } catch (Exception throwable) {
            return null;
        }
    }

    /**
     * 序列化为字符串。
     *
     * @param object 对象
     * @return JSON 字符串
     */
    public static String toJSONString(Object object) {
        if (object == null) {
            return "{}";
        }
        if (object instanceof PointVO) {
            return ((PointVO) object).toJsonString();
        }
        if (object instanceof List) {
            List<?> list = (List<?>) object;
            StringBuilder builder = new StringBuilder("[");
            for (int index = 0; index < list.size(); index++) {
                Object item = list.get(index);
                if (item instanceof PointVO) {
                    builder.append(((PointVO) item).toJsonString()).append(',');
                }
            }
            if (builder.length() > 1) {
                builder.deleteCharAt(builder.length() - 1);
            }
            builder.append(']');
            return builder.toString();
        }
        return object.toString();
    }
}
