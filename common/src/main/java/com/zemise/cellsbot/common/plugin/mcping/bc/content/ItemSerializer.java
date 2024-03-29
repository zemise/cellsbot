package com.zemise.cellsbot.common.plugin.mcping.bc.content;

import com.google.gson.*;
import com.zemise.cellsbot.common.plugin.mcping.bc.ItemTag;

import java.lang.reflect.Type;

public class ItemSerializer implements JsonSerializer<Item>, JsonDeserializer<Item>
{

    @Override
    public Item deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException
    {
        JsonObject value = element.getAsJsonObject();

        int count = -1;
        if ( value.has( "Count" ) )
        {
            JsonPrimitive countObj = value.get( "Count" ).getAsJsonPrimitive();

            if ( countObj.isNumber() )
            {
                count = countObj.getAsInt();
            } else if ( countObj.isString() )
            {
                String cString = countObj.getAsString();
                char last = cString.charAt( cString.length() - 1 );
                // Check for all number suffixes
                if ( last == 'b' || last == 's' || last == 'l' || last == 'f' || last == 'd' )
                {
                    cString = cString.substring( 0, cString.length() - 1 );
                }
                try
                {
                    count = Integer.parseInt( cString );
                } catch ( NumberFormatException ex )
                {
                    throw new JsonParseException( "Could not parse count: " + ex );
                }
            }
        }

        return new Item(
                ( value.has( "id" ) ) ? value.get( "id" ).getAsString() : null,
                count,
                ( value.has( "tag" ) ) ? context.deserialize( value.get( "tag" ), ItemTag.class ) : null
        );
    }

    @Override
    public JsonElement serialize(Item content, Type type, JsonSerializationContext context)
    {
        JsonObject object = new JsonObject();
        object.addProperty( "id", ( content.getId() == null ) ? "minecraft:air" : content.getId() );
        if ( content.getCount() != -1 )
        {
            object.addProperty( "Count", content.getCount() );
        }
        if ( content.getTag() != null )
        {
            object.add( "tag", context.serialize( content.getTag() ) );
        }
        return object;
    }
}
