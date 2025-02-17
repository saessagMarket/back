package com.market.saessag.domain.chat.dto;

import com.market.saessag.domain.chat.entity.ChatFile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ChatFileResponse {
    private Long roomId;
    private String preSignedUrl;
    private String fileType;

    public static ChatFileResponse fromEntity(ChatFile chatFile, String preSignedUrl) {
        return new ChatFileResponse(
                chatFile.getChatMessage().getChatRoom().getId(),
                preSignedUrl,
                chatFile.getFileType()
        );
    }
}
