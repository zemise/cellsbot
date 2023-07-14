package com.zemise.cellsbot.common.plugin.mcping.bc.content;

import com.zemise.cellsbot.common.plugin.mcping.bc.HoverEvent;
import com.zemise.cellsbot.common.plugin.mcping.bc.ItemTag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = false)
public class Item extends Content
{

    /**
     * Namespaced item ID. Will use 'minecraft:air' if null.
     */
    private String id;
    /**
     * Optional. Size of the item stack.
     */
    private int count = -1;
    /**
     * Optional. Item tag.
     */
    private ItemTag tag;

    @Override
    public HoverEvent.Action requiredAction()
    {
        return HoverEvent.Action.SHOW_ITEM;
    }
}
