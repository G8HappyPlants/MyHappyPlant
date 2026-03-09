package com.example.myhappyplants.dto;

import com.example.myhappyplants.entity.Tag;

public record TagResponse(Long id, String name) {

    public static TagResponse fromTag(Tag tag) {
        return new TagResponse(tag.getId(), tag.getName());
    }
}