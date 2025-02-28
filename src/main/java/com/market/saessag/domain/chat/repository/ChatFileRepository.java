package com.market.saessag.domain.chat.repository;

import com.market.saessag.domain.chat.entity.ChatFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatFileRepository extends JpaRepository<ChatFile, Long> {

}
