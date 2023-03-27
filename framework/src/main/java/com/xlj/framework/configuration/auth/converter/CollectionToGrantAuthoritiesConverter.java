package com.xlj.framework.configuration.auth.converter;

import com.fasterxml.jackson.databind.util.StdConverter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CollectionToGrantAuthoritiesConverter extends StdConverter<List<Map<String, String>>, Collection<SimpleGrantedAuthority>> {

    private String authorityPrefix = "";

    @Override
    public Collection<SimpleGrantedAuthority> convert(List<Map<String, String>> value) {
        List<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<>();
        for (Map<String, String> stringStringMap : value) {
            simpleGrantedAuthorities.add(new SimpleGrantedAuthority(this.authorityPrefix + stringStringMap.get("role")));
        }
        return simpleGrantedAuthorities;
    }
}
