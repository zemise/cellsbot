package com.zemise.cellsbot.common.plugin.mcping.bc.content;

import com.zemise.cellsbot.common.plugin.mcping.bc.HoverEvent;
import lombok.EqualsAndHashCode;
import lombok.ToString;


@ToString
@EqualsAndHashCode
public abstract class Content
{

    /**
     * Required action for this content type.
     *
     * @return action
     */
    public abstract HoverEvent.Action requiredAction();

    /**
     * Tests this content against an action
     *
     * @param input input to test
     * @throws UnsupportedOperationException if action incompatible
     */
    public void assertAction(HoverEvent.Action input) throws UnsupportedOperationException
    {
        if ( input != requiredAction() )
        {
            throw new UnsupportedOperationException( "Action " + input + " not compatible! Expected " + requiredAction() );
        }
    }
}
