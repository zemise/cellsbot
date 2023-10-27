package com.zemise.cellsbot.domain.qq.service;

import com.zemise.cellsbot.domain.qq.entity.QQGroup;
import com.zemise.cellsbot.domain.qq.entity.QQMessage;
import com.zemise.cellsbot.domain.qq.entity.QQUser;
import com.zemise.cellsbot.domain.qq.repository.QQGroupRepository;
import com.zemise.cellsbot.domain.qq.repository.QQMessageRepository;
import com.zemise.cellsbot.domain.qq.repository.QQUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * @author Zemise
 */
@Service
public class QQService {
    QQUserRepository userRepository;
    QQGroupRepository groupRepository;
    QQMessageRepository messageRepository;

    @Autowired
    public QQService(QQUserRepository userRepository,
                     QQGroupRepository groupRepository,
                     QQMessageRepository messageRepository) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.messageRepository = messageRepository;
    }

    /**
     * @param account QQ账号
     * @return QQUser
     */
    public QQUser findQqUser(Long account) {
        return userRepository.findByAccount(account);
    }

    /**
     * @param account QQ群号
     * @return QQGroup
     */
    public QQGroup findQqGroup(Long account) {
        return groupRepository.findByAccount(account);
    }


    public <T> void save(T entity) {
        Assert.notNull(entity, "Entity must not be null");

        if (entity instanceof QQUser) {
            userRepository.save((QQUser) entity);
        } else if (entity instanceof QQGroup) {
            groupRepository.save((QQGroup) entity);
        } else if (entity instanceof QQMessage) {
            messageRepository.save((QQMessage) entity);
        } else {
            throw new IllegalArgumentException("Unsupported entity type");
        }
    }


    /**
     * Saves all given entities.
     * entities – must not be null nor must it contain null.
     * @param entities 表实体对象集合
     * @param <T>
     */
    public <T> void saveAll(Iterable<T> entities) {
        Assert.notNull(entities, "Entities must not be null");

        if (!entities.iterator().hasNext()) {
            return;  // 无需处理空的集合
        }

        T next = entities.iterator().next();

        if (next instanceof QQUser) {
            userRepository.saveAll((Iterable<QQUser>) entities);
        } else if (next instanceof QQGroup) {
            groupRepository.saveAll((Iterable<QQGroup>) entities);
        } else if (next instanceof QQMessage) {
            messageRepository.saveAll((Iterable<QQMessage>) entities);
        } else {
            throw new IllegalArgumentException("Unsupported entity type");
        }
    }
}
