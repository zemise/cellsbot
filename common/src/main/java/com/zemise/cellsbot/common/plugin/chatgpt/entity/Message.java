package com.zemise.cellsbot.common.plugin.chatgpt.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * Message
 * </p>
 *
 * @author <a href="https://github.com/LiLittleCat">LiLittleCat</a>
 * @since 2023/3/2
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    @JsonProperty(value = "role")
    public String role;
    @JsonProperty(value = "content")
    public String content;

}
