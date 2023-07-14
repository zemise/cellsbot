package com.zemise.cellsbot.common.plugin.mcping.bc.content;

import com.google.gson.*;
import com.zemise.cellsbot.common.plugin.mcping.bc.BaseComponent;

import java.lang.reflect.Type;

public class TextSerializer implements JsonSerializer<Text>, JsonDeserializer<Text>
{

    @Override
    public Text deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException
    {
        if ( element.isJsonArray() )
        {
            return new Text( context.<BaseComponent[]>deserialize( element, BaseComponent[].class ) );
        } else if ( element.isJsonPrimitive() )
        {
            return new Text( element.getAsJsonPrimitive().getAsString() );
        } else
        {
            return new Text( new BaseComponent[]
            {
                context.deserialize( element, BaseComponent.class )
            } );
        }
    }

    @Override
    public JsonElement serialize(Text content, Type type, JsonSerializationContext context)
    {
        return context.serialize( content.getValue() );
    }
}
