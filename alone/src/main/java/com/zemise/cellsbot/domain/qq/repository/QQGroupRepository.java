package com.zemise.cellsbot.domain.qq.repository;

import com.zemise.cellsbot.domain.qq.entity.QQGroup;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *
 * </p>
 *
 * @author <a href= "https://github.com/zemise">Zemise</a>
 * @since 2023/10/26
 */
@Repository
public interface QQGroupRepository extends CrudRepository<QQGroup, Long> {
    QQGroup findByAccount(long id);
}
