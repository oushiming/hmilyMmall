package com.hmily.util;


import com.google.common.collect.Lists;
import com.hmily.pojo.TestPojo;
import com.hmily.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class JsonUtil {

    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        //对象的所有字段全部列入
        objectMapper.setSerializationInclusion(Inclusion.ALWAYS);
        //取消默认转换timestamps形式
        objectMapper.configure(SerializationConfig.Feature.WRITE_DATE_KEYS_AS_TIMESTAMPS, false);
        //忽略空Bean转json的错误
        objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
        //所有的日期格式都统一为以下的样式，即yyyy-MM-dd HH:mm:ss
        objectMapper.setDateFormat(new SimpleDateFormat(DateTimeUtil.STANDARD_FORMAT));
        //忽略 在json字符串中存在，但是在java对象中不存在对应属性的情况。防止错误
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static <T> String objToString(T obj){
        if(obj == null){
            return null;
        }
        try {
            return  obj instanceof String ? (String)obj : objectMapper.writeValueAsString(obj);
        } catch (IOException e) {
            log.warn("Parse Object to String error", e);
            return null;
        }
    }

    //格式化后的json，打印方便查看
    public static <T> String objToStringPretty(T obj){
        if (obj == null){
            return null;
        }
        try {
            return obj instanceof String ? (String)obj : objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (IOException e) {
            log.warn("Parse Object to String error", e);
            return null;
        }
    }

    public static <T> T stringToObj(String str, Class<T> clazz){
        if(StringUtils.isEmpty(str) || clazz == null){
            return null;
        }
        try {
            return clazz.equals(String.class) ?  (T)str : objectMapper.readValue(str, clazz);
        } catch (IOException e) {
            log.warn("Parse String to Object error", e);
            return null;
        }
    }

    public static <T> T stringToObj(String str, TypeReference<T> typeReference){
        if(StringUtils.isEmpty(str) || typeReference == null){
            return null;
        }
        try {
            return (T)(typeReference.getType().equals(String.class) ? str : objectMapper.readValue(str, typeReference));
        } catch (IOException e) {
            log.warn("Parse String to Object error", e);
            return null;
        }
    }

    public static <T> T stringToObj(String str,Class<?> collectionClass,Class<?>... elementClasses){
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(collectionClass,elementClasses);
        try {
            return objectMapper.readValue(str,javaType);
        } catch (Exception e) {
            log.warn("Parse String to Object error",e);
            return null;
        }
    }

    public static void main(String[] args) {
        TestPojo testPojo = new TestPojo();
        testPojo.setName("hmily");
        testPojo.setId(666);

        //{"name":"hmily","id":666}
        String json = "{\"name\":\"hmily\",\"color\":\"blue\",\"id\":666}";
        TestPojo testPojoObject = JsonUtil.stringToObj(json,TestPojo.class);
        String testPojoJson = JsonUtil.objToString(testPojo);
//        log.info("testPojoJson:{}",testPojoJson);

        log.info("end");

        User user = new User();
        user.setId(2);
        user.setEmail("admin@hmilymmall.com");
        user.setCreateTime(new Date());
        String userJsonPretty = JsonUtil.objToStringPretty(user);
        log.info("userJson:{}",userJsonPretty);


        User u2 = new User();
        u2.setId(2);
        u2.setEmail("geelyu2@happymmall.com");



        String user1Json = JsonUtil.objToString(user);

        String user1JsonPretty = JsonUtil.objToStringPretty(u2);

        log.info("user1Json:{}",user1Json);

        log.info("user1JsonPretty:{}",user1JsonPretty);


        User u1 = (User)JsonUtil.stringToObj(user1Json,User.class);


        List<User> userList = Lists.newArrayList();
        userList.add(u1);
        userList.add(u2);

        String userListStr = JsonUtil.objToStringPretty(userList);

        log.info("==================");

        log.info(userListStr);


        List<User> userListObj1 = JsonUtil.stringToObj(userListStr, new TypeReference<List<User>>() {
        });


        List<User> userListObj2 = JsonUtil.stringToObj(userListStr, List.class, User.class);

        System.out.println("end");
    }

}