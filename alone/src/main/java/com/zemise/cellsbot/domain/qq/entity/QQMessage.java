package com.zemise.cellsbot.domain.qq.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author <a href= "https://github.com/zemise">Zemise</a>
 * @since 2023/10/26
 */
@Entity
@Table(name = "qq_message")
@Data
public class QQMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    /**
     *
     * @param content 消息内容
     */
    public QQMessage(String content) {
        this.content = content;
    }

    public QQMessage() {
    }

    // 乐观锁
    //private @Version Long version;

    /**
     * 实体创建时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    protected Date dateCreated = new Date();


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sender_id")
    private QQUser sender;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "receiver_id")
    private QQGroup receiver;
}
