package com.zemise.cellsbot.common.util.miraiUtil;

import net.mamoe.mirai.contact.ContactList;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.MemberPermission;
import net.mamoe.mirai.contact.NormalMember;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @Author Zemise_
 * @Date 2023/4/16
 * @Description Mirai群聊事件处理
 */
public class MiraiMessageUtil {
    /**
     * 获取群号
     *
     * @param event 群消息事件
     * @return 群ID
     */
    public static Long getGroupId(GroupMessageEvent event) {
        return event.getGroup().getId();
    }

    /**
     * 获取群号
     *
     * @param event 群消息事件
     * @return 消息发送者ID
     */
    public static Long getSenderId(GroupMessageEvent event) {
        return event.getSender().getId();
    }

    /**
     * 是否是群聊事件
     *
     * @param event 群消息事件
     * @return boolean
     */
    public static boolean isGroupMessageEvent(MessageEvent event) {
        return event instanceof GroupMessageEvent;
    }

    /**
     * 检查某人是否被 @
     *
     * @param event 群消息事件
     * @param qq    qqID
     * @return boolean
     */
    public static boolean isAt(MessageEvent event, Long qq) {
        for (SingleMessage message : event.getMessage()) {
            if (message instanceof At) {
                At at = (At) message;
                if (qq == at.getTarget()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 机器人是否被 at，此方法会忽略 @all 消息
     *
     * @return boolean
     */
    public static boolean isAtBot(MessageEvent event) {
        return isAt(event, BotOperator.getBot().getId());
    }

    /**
     * 将消息转换为群聊消息
     *
     * @param event 各种消息事件
     * @return 若转换失败，则返回 null
     */
    public static GroupMessageEvent getGroupMessageEvent(MessageEvent event) {
        try {
            return (GroupMessageEvent) event;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取群成员列表，不包含机器人自己
     *
     * @param event 群消息事件
     * @return 若获取失败则返回 null
     */
    public static ContactList<NormalMember> getMembers(MessageEvent event) {
        GroupMessageEvent group = getGroupMessageEvent(event);
        if (group != null) {
            return group.getGroup().getMembers();
        }
        return null;
    }

    /**
     * 以 List 的形式返回群内所有群成员的 群内名称，如果群内名称为空，则使用用户的QQ昵称替代
     *
     * @param event 群消息事件
     * @return 群成员名称列表
     */
    public static List<String> getMembersGroupName(GroupMessageEvent event) {
        ContactList<NormalMember> members = null;
        List<String> ans = new ArrayList<>();
        if ((members = getMembers(event)) != null) {
            for (NormalMember member : members) {
                String s = member.getNameCard();
                if (s.isEmpty()) {
                    s = member.queryProfile().getNickname();
                }
                ans.add(s);
            }
        }
        return ans;
    }

    /**
     * 获取所有群成员的 qq，不包含机器人自己
     *
     * @param event 群消息事件
     * @return 群成员ID列表
     */
    protected List<Long> getMembersIds(GroupMessageEvent event) {
        ContactList<NormalMember> members = null;
        List<Long> ans = new ArrayList<>();
        if ((members = getMembers(event)) != null) {
            for (NormalMember member : members) {
                member.getNameCard();
                ans.add(member.getId());
            }
        }
        return ans;
    }

    /**
     * 获取群成员的 qq 号
     *
     * @param event     群消息事件
     * @param groupName 群成员的群名片，如果为空，则为群成员的昵称
     * @return 未搜索到返回 -1
     */
    protected Long getMemberIdByName(GroupMessageEvent event, String groupName) {
        ContactList<NormalMember> members;
        long ans = -1;
        if ((members = getMembers(event)) != null) {
            for (NormalMember member : members) {
                String s = member.getNameCard();
                if (s.isEmpty())
                    s = member.queryProfile().getNickname();
                if (s.equals(groupName))
                    return member.getId();
            }
        }
        return ans;
    }

    /**
     * 获取发送者权限
     *
     * @param event 群消息事件
     * @return 0 是成员、1 是管理员，2 是群主
     */
    public static int getSenderPermission(GroupMessageEvent event) {
        MemberPermission permission = getGroupMessageEvent(event).getPermission();
        return permission.getLevel();
    }

    /**
     * 撤回一条消息，如果撤回失败请考虑是否是权限不够
     *
     * @param source
     * @return 成功返回 true，否则返回 false
     */
    public static boolean cancelMessage(MessageSource source) {
        try {
            MessageSource.recall(source);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 撤回一条消息，如果撤回失败请考虑是否是权限不够
     *
     * @param event 消息事件
     * @return 成功返回 true，否则返回 false
     * @throws RuntimeException 权限不足
     */
    protected boolean cancelMessage(MessageEvent event) {
        if (((GroupMessageEvent) event).getPermission().getLevel() > 0) {
            throw new RuntimeException("权限不足");
        }
        return cancelMessage(event.getSource());
    }

    /**
     * 提取纯文本消息，消息将不会包含图片、表情等任何非文字
     *
     * @param event 消息时间
     * @return 纯文本
     */
    public static String getPlantContent(MessageEvent event) {
        return getOnlyPlainContent(event);
    }

    /**
     * 检查事件消息是否以关键字开头
     *
     * @param event    传入的事件
     * @param keywords 是否包含集合关键字
     * @return 此事件是否以集合内的关键字开始
     */
    public static boolean startWithKeywords(MessageEvent event, Collection<String> keywords) {
        String content = getPlantContent(event);
        if (content != null) {
            for (String keyword : keywords) {
                if (content.startsWith(keyword)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 检查此事件是否是群事件，并检查事件消息是否以关键词开头
     *
     * @param event    消息事件
     * @param keywords 关键词
     * @return 如果都为真返回 true
     */
    public static boolean startWithKeywords(MessageEvent event, String keywords) {
        return isGroupMessageEvent(event) && event.getMessage().contentToString().startsWith(keywords);
    }

    /**
     * 禁言群成员
     *
     * @param qq             群成员 qq
     * @param group          群
     * @param durationSecond 秒
     */
    protected void mute(String qq, Group group, int durationSecond) {
        for (NormalMember member : group.getMembers()) {
            if (member.getId() == Long.parseLong(qq)) {
                member.mute(durationSecond);
                return;
            }
        }
    }

    /**
     * 禁言发言者
     *
     * @param event          消息事件
     * @param durationSecond 时长、秒
     */
    protected void muteSend(MessageEvent event, int durationSecond) {
        getGroupMessageEvent(event).getSender().mute(durationSecond);
    }

    public static MessageChain buildMessageChain(String... m) {
        MessageChainBuilder builder = new MessageChainBuilder();
        for (String s : m) {
            builder.append(s);
        }
        return builder.build();
    }

    public static MessageChain buildMessageChain(Object... m) {
        MessageChainBuilder builder = new MessageChainBuilder();
        for (Object s : m) {
            if (s == null) {
                continue;
            }
            if (s instanceof String) {
                s = new PlainText((CharSequence) s);
            }
            if (s instanceof StringBuilder) {
                s = new PlainText((CharSequence) s.toString());
            }
            if (s instanceof SingleMessage) {
                builder.append((SingleMessage) s);
            }
        }
        return builder.build();
    }

    public static String getContent(MessageEvent event) {
        if (event == null) {
            return null;
        }
        return getContent(event.getMessage());
    }

    public static String getContent(MessageChain chain) {
        if (chain == null) {
            return null;
        }
        return chain.serializeToMiraiCode();
    }

    public static String getOnlyPlainContent(MessageEvent event) {
        if (event == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (SingleMessage singleMessage : event.getMessage()) {
            if (singleMessage instanceof PlainText) {
                sb.append(singleMessage);
            }
        }
        return sb.toString().trim();
    }

    /**
     * 引用回复一条消息
     *
     * @param event mirai消息事件
     * @param msg 增添的MessageChain消息
     * @return 包含引用回复的消息
     */
    public static MessageChain quoteReply(MessageEvent event, MessageChain msg) {
        return buildMessageChain(getQuoteReply(event), msg);
    }
    public static MessageChain quoteReply(MessageEvent event, String msg) {
        return buildMessageChain(getQuoteReply(event), msg);
    }


    /**
     * 去除mirai消息中特定的字符串
     *
     * @param event mirai消息事件
     * @param commandStr 特定的字符串，如#command
     * @return MessageChain 剔除特定字符串的消息
     */
    public static MessageChain getNoCommandMessage(MessageEvent event, String commandStr) {
        if (event == null) {
            return null;
        }

        MessageChainBuilder messages = new MessageChainBuilder();
        for (SingleMessage singleMessage : event.getMessage()) {
            if (singleMessage instanceof PlainText) {
                String content = ((PlainText) singleMessage).getContent().trim();
                String replace = content.replace(commandStr, "");

                messages.append(replace);
            } else {
                messages.append(singleMessage);
            }
        }
        return messages.build();
    }

    /**
     * 设置引用回复，如果失败，则返回 null<br/>
     * 如果想回复某消息，你可以这样做：chainBuilder.append(getQuoteReply(e))<br/>或者调用父类方法：buildMessageChain(getQuoteReply(e), msg) 以构造一条消息链<br/>或者使用 getQuoteReply 方法回复一条简单文本信息
     *
     * @param event
     * @return MessageSource
     * @see #buildMessageChain(Object...)
     * @see #quoteReply(MessageEvent, MessageChain)
     */
    public static QuoteReply getQuoteReply(MessageEvent event) {
        return new QuoteReply(event.getMessage());
    }


}
