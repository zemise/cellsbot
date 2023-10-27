package com.zemise.cellsbot.event.custom.handle;

import com.zemise.cellsbot.common.evnet.EventHandler;
import com.zemise.cellsbot.common.evnet.Listener;
import com.zemise.cellsbot.common.util.miraiUtil.BotOperator;
import com.zemise.cellsbot.domain.qq.entity.QQGroup;
import com.zemise.cellsbot.domain.qq.service.QQService;
import com.zemise.cellsbot.event.custom.BotOnlineEvent;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.ContactList;
import net.mamoe.mirai.contact.Group;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Zemise_
 * @Date 2023/5/14
 * @Description 监听QQ bot登陆后，完成后续一系列操作
 */
@Slf4j
public class BotOnlineEventHandle implements Listener {

    private final QQService qqService;

    public BotOnlineEventHandle(QQService qqService) {
        this.qqService = qqService;
    }

    @EventHandler
    //@Transactional
    public void handle(BotOnlineEvent event) {
        log.info("QQ登陆成功，现在开始处理相关数据");

        ContactList<Group> groups = BotOperator.getBot().getGroups();
        updateGroupTable(groups);
        updateUserTable(groups);
    }

    public void updateGroupTable(ContactList<Group> groups) {
        List<QQGroup> groupListToInsert = new ArrayList<>();

        groups.forEach(item -> {
            QQGroup group = qqService.findQqGroup(item.getId());
            if (null == group) {
                group = new QQGroup();
                group.setNick(item.getName());
                group.setAccount(item.getId());

//                Set<QQUser> ownedMembers = group.getOwnedMembers();
//                ContactList<NormalMember> members = item.getMembers();
//                members.forEach(member -> {
//                    QQUser qqUser = new QQUser();
//                    qqUser.setId(member.getId());
//                    qqUser.setNick(member.getNick());
//                    ownedMembers.add(qqUser);
//                });
            } else {
                group.setNick(item.getName());
            }
            groupListToInsert.add(group);
        });
        qqService.saveAll(groupListToInsert);
    }

    public void updateUserTable(ContactList<Group> groups) {
//        List<QQUser> QQUserListToInsert = new ArrayList<>();
//
//        groups.forEach(group -> {
//            // 各个群的成员
//            ContactList<NormalMember> members = group.getMembers();
//
//            // 本群的表对象
//            QQGroup qGroup = groupRepository.findByGroupNumber(group.getId());
//
//            members.forEach(member -> {
//                QQUser user = userRepository.findUserByQq(member.getId());
//
//                if (null == user) {
//                    user = new QQUser();
//                    user.setAccount(member.getId());
//                    user.setNick(member.getNick());
//
//                    List<QQGroup> userNewGroups = new ArrayList<>();
//
//                    userNewGroups.add(qGroup);
//                    user.setOwnedGroups(userNewGroups);
//                } else { // 若数据库中存在该User对象，则更新(按理qq号是不变的，改变的只有昵称，以及新加入的群)
//                    user.setNick(member.getNick());
//
//                    List<QQGroup> gs = user.getGroups();
//                    if (!gs.contains(qGroup)) {
//                        gs.add(qGroup);
//                        user.setGroups(gs);
//                    }
//                }
//                QQUserListToInsert.add(user);
//            });
//
//            userRepository.saveAll(QQUserListToInsert);
//        });
    }
}
