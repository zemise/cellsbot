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
@Table(name = "qq_group")
@Data
public class QQGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Q群账号-唯一
     */
    @Column(unique = true)
    private long account;

    /**
     * Q群名字
     */
    private String nick;

    /**
     * @param account Q群账号
     * @param nick    Q群名字
     */
    public QQGroup(long account, String nick) {
        this.account = account;
        this.nick = nick;
    }

    public QQGroup() {
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
     * QQGroup实体对象所拥有的群成员
     */
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "qq_user_group_mapping",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<QQUser> ownedMembers = new HashSet<>();


    /**
     * QQGroup实体对象记录的群消息
     */
    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "receiver")
    private List<QQMessage> messages = new ArrayList<>();
}
