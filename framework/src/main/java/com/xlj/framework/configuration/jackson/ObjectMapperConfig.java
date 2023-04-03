package com.xlj.framework.configuration.jackson;

import cn.hutool.core.date.DateUtil;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.YearMonthDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.YearMonthSerializer;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class ObjectMapperConfig {


    /**
     * 默认日期时间格式
     */
    public static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    /**
     * 默认日期格式
     */
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    /**
     * 默认年月格式
     */
    public static final String DEFAULT_YEAR_MONTH_FORMAT = "yyyy-MM";
    /**
     * 默认时间格式
     */
    public static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";

    public static JavaTimeModule getJavaTimeMoudle() {
        JavaTimeModule javaTimeModule = new JavaTimeModule();

        // =========================================== 序列化器 =========================================================
        javaTimeModule.addSerializer(LocalDateTime.class,
                new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)));
        javaTimeModule.addSerializer(LocalDate.class,
                new LocalDateSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT)));
        javaTimeModule.addSerializer(LocalTime.class,
                new LocalTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT)));
        javaTimeModule.addSerializer(YearMonth.class,
                new YearMonthSerializer(DateTimeFormatter.ofPattern(DEFAULT_YEAR_MONTH_FORMAT)));
        javaTimeModule.addSerializer(Date.class, new JsonSerializer<Date>() {
            @Override
            public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                jsonGenerator.writeString(DateUtil.format(date, DEFAULT_DATE_TIME_FORMAT));
            }
        });


        // =========================================== 反序列化器 =======================================================
        // LocalDateTime反序列化器
        javaTimeModule.addDeserializer(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
            @Override
            public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                String text = p.getText();
                if (StringUtils.isEmpty(text)) {
                    return null;
                }
                if (StringUtils.isNumeric(text)) {
                    return Instant.ofEpochMilli(Long.parseLong(text)).atZone(ZoneId.systemDefault()).toLocalDateTime();
                } else {
                    return LocalDateTime.parse(text, DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT));
                }
            }
        });
        // LocalDate反序列化器
        javaTimeModule.addDeserializer(LocalDate.class, new JsonDeserializer<LocalDate>() {
            @Override
            public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                String text = p.getText();
                if (StringUtils.isBlank(text)) {
                    return null;
                }
                if (StringUtils.isNumeric(text)) {
                    return Instant.ofEpochMilli(Long.parseLong(text)).atZone(ZoneId.systemDefault()).toLocalDate();
                } else {
                    return LocalDate.parse(text, DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT));
                }
            }
        });
        // LocalTime反序列化器
        javaTimeModule.addDeserializer(LocalTime.class,
                new LocalTimeDeserializer(DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT)));
        // YearMonth反序列化器
        javaTimeModule.addDeserializer(YearMonth.class,
                new YearMonthDeserializer(DateTimeFormatter.ofPattern(DEFAULT_YEAR_MONTH_FORMAT)));
        // Date反序列化器
        javaTimeModule.addDeserializer(Date.class, new JsonDeserializer<Date>() {
            @Override
            public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                String text = p.getText();
                if (StringUtils.isBlank(text)) {
                    return null;
                }
                if (StringUtils.isNumeric(text)) {
                    return new Date(Long.parseLong(text));
                } else {
                    return DateUtil.parse(text);
                }
            }
        });
        return javaTimeModule;
    }

    public static ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 设置FAIL_ON_UNKNOWN_PROPERTIES为false
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // 设置useTimestamp为false
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

        JavaTimeModule javaTimeModule = getJavaTimeMoudle();
        // 自定义的放在前面
        objectMapper.registerModule(javaTimeModule);
        objectMapper.findAndRegisterModules();
        return objectMapper;
    }

}
