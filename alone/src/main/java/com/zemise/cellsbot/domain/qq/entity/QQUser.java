package com.zemise.cellsbot.domain.qq.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.*;

/**
 * <p>
 *
 * </p>
 *
 * @author <a href= "https://github.com/zemise">Zemise</a>
 * @since 2023/10/25
 */
@Entity
@Table(name = "qq_user")
@Data
public class QQUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * QQ账号-唯一
     */
    @Column(unique = true)
    private long account;

    /**
     * QQ昵称
     */
    private String nick;

    /**
     * @param account QQ账号
     * @param nick    QQ昵称
     */
    public QQUser(long account, String nick) {
        this.account = account;
        this.nick = nick;
    }

    public QQUser() {
    }

    // 乐观锁
    private @Version Long version;

    /**
     * 实体创建时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    protected Date dateCreated = new Date();

    /**
     * 实体修改时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    protected Date dateModified = new Date();

    /**
     * 实体QQUser所拥有加入的群
     */
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "qq_user_group_mapping",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "group_id")})
    private Set<QQGroup> ownedGroups = new HashSet<>();


    /**
     * 实体QQUser所发的消息
     */
    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "sender")
    private List<QQMessage> messages = new ArrayList<>();


}
