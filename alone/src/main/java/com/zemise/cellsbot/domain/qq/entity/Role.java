package com.zemise.cellsbot.domain.qq.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author <a href= "https://github.com/zemise">Zemise</a>
 * @since 2023/10/26
 */

//@Entity
//@Data
//public class Role {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String name;
//
//    public Role(String name) {
//        this.name = name;
//    }
//
//    public Role() {
//    }
//
//    // 乐观锁
//    private @Version Long version;
//
//    /**
//     * 实体创建时间
//     */
//    @Temporal(TemporalType.TIMESTAMP)
//    @CreatedDate
//    protected Date dateCreated = new Date();
//
//    /**
//     * 实体修改时间
//     */
//    @Temporal(TemporalType.TIMESTAMP)
//    @LastModifiedDate
//    protected Date dateModified = new Date();
//}
