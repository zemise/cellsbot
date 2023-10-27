package com.zemise.cellsbot.event.miari;

import com.zemise.cellsbot.domain.qq.entity.QQGroup;
import com.zemise.cellsbot.domain.qq.entity.QQMessage;
import com.zemise.cellsbot.domain.qq.entity.QQUser;
import com.zemise.cellsbot.domain.qq.service.QQService;
import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import org.jetbrains.annotations.NotNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;


/**
 * <p>
 *
 * </p>
 *
 * @author <a href= "https://github.com/zemise">Zemise</a>
 * @since 2023/10/25
 */
public class RecordMessage extends SimpleListenerHost {
    QQService qqService;

    public RecordMessage(QQService qqService) {
        this.qqService = qqService;
    }

    @EventHandler
    @Transactional
    public void recordMessage(GroupMessageEvent event) {
        System.out.println("发信人：" + event.getSender().getNick() + ",说：" + event.getMessage().contentToString());

        Member miraiMember = event.getSender();
        String message = event.getMessage().contentToString();

        QQUser qqUser = qqService.findQqUser(miraiMember.getId());

        if(null == qqUser){
            // 保存新建用户
            qqUser = new QQUser();
        }

        qqUser.setAccount(miraiMember.getId());
        qqUser.setNick(miraiMember.getNick());
        Set<QQGroup> ownedGroups = qqUser.getOwnedGroups();
        QQGroup receiverGroup = qqService.findQqGroup(event.getGroup().getId());
        ownedGroups.add(receiverGroup);
        qqUser.setOwnedGroups(ownedGroups);

        QQMessage qqMessage = new QQMessage();
        qqMessage.setContent(message);
        qqMessage.setSender(qqUser);
        qqMessage.setReceiver(receiverGroup );

        qqService.save(qqUser);
        qqService.save(qqMessage);
        System.out.println("=========>>>>");
    }

    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
        System.out.println("to fix handle");
        //super.handleException(context, exception);
    }
}
