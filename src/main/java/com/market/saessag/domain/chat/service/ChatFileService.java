package com.market.saessag.domain.chat.service;

import com.market.saessag.domain.chat.dto.ChatFileRequest;
import com.market.saessag.domain.chat.dto.ChatFileResponse;
import com.market.saessag.domain.chat.entity.ChatFile;
import com.market.saessag.domain.chat.entity.ChatMessage;
import com.market.saessag.domain.chat.entity.ChatRoom;
import com.market.saessag.domain.chat.repository.ChatFileRepository;
import com.market.saessag.domain.chat.repository.ChatMessageRepository;
import com.market.saessag.domain.chat.repository.ChatRoomRepository;
import com.market.saessag.domain.photo.service.S3Service;
import com.market.saessag.domain.user.entity.User;
import com.market.saessag.domain.user.repository.UserRepository;
import com.market.saessag.global.exception.CustomException;
import com.market.saessag.global.exception.ErrorCode;
import com.market.saessag.util.FileTypeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChatFileService {
    private final ChatFileRepository chatFileRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final S3Service s3Service;

    public List<ChatFileResponse> saveFile(Long roomId, ChatFileRequest fileRequest) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException(ErrorCode.ROOM_NOT_FOUND));

        User sender = userRepository.findById(fileRequest.getSenderId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        ChatMessage chatMessage = ChatMessage.builder()
                .chatRoom(chatRoom)
                .sender(sender)
                .content(null)
                .timeStamp(LocalDateTime.now())
                .build();

        chatMessageRepository.save(chatMessage);

        List<ChatFileResponse> responses = new ArrayList<>();

        for (String fileUrl : fileRequest.getFileURL()) {
            String fileType = FileTypeUtil.getMimeType(fileUrl);

            ChatFile chatFile = ChatFile.builder()
                    .chatMessage(chatMessage)
                    .fileUrl(fileUrl)
                    .fileType(fileType)
                    .build();

            chatFileRepository.save(chatFile);

            String presignedUrl = s3Service.getPresignedUrl(Collections.singletonList(fileUrl)).get(fileUrl);
            responses.add(ChatFileResponse.fromEntity(chatFile, presignedUrl));
        }
        return responses;
    }
}
