package mysql;

import com.zemise.cellsbot.Main;
import com.zemise.cellsbot.domain.qq.entity.QQUser;
import com.zemise.cellsbot.domain.qq.repository.QQUserRepository;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *
 * </p>
 *
 * @author <a href= "https://github.com/zemise">Zemise</a>
 * @since 2023/10/26
 */

@SpringBootTest(classes = Main.class)
public class TestMysql {
    @Resource
    QQUserRepository QQUserRepository;

    @Test
    @Transactional
    @Commit
    void testInsert(){
        QQUser QQUser = new QQUser();
        QQUser.setAccount(373933306);

        QQUserRepository.save(QQUser);

    }
}
