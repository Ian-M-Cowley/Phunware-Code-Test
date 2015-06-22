package com.iancowley.nfl12bars.ui.adapter;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.iancowley.nfl12bars.model.ScheduleItem;
import com.iancowley.nfl12bars.model.Venue;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ian.cowley on 6/18/15.
 */
public class VenueGsonAdapter implements JsonDeserializer<Venue> {

    /**
     * {
     * "zip": "70130 ",
     * "phone": "(555) 555-5555",
     * "ticket_link": "http://www.ticketmaster.com/New-Orleans-Convention-Center-tickets-New-Orleans/venue/221251",
     * "state": "LA",
     * "pcode": 4,
     * "city": "New Orleans",
     * "id": 15174,
     * "tollfreephone": "",
     * "schedule": [
     * {
     * "end_date": "2013-01-30 20:00:00 -0800",
     * "start_date": "2013-01-30 13:00:00 -0800"
     * },
     * {
     * "end_date": "2013-01-31 20:00:00 -0800",
     * "start_date": "2013-01-31 08:00:00 -0800"
     * },
     * {
     * "end_date": "2013-02-01 20:00:00 -0800",
     * "start_date": "2013-02-01 08:00:00 -0800"
     * },
     * {
     * "end_date": "2013-02-02 20:00:00 -0800",
     * "start_date": "2013-02-02 08:00:00 -0800"
     * },
     * {
     * "end_date": "2013-02-03 14:00:00 -0800",
     * "start_date": "2013-02-03 08:00:00 -0800"
     * }
     * ],
     * "address": "900 Convention Center Blvd",
     * "image_url": "http://lorempixel.com/900/500/nightlife/?v=-632944306",
     * "description": "",
     * "name": "NFL Experience, Driven by GMC",
     * "longitude": -90.06418,
     * "latitude": 29.943351
     * }
     */
    @Override
    public Venue deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Venue venue = new Venue();

        JsonObject jsonObj = json.getAsJsonObject();

        // Grab all of the values for this venue from the json.
        venue.setZip(jsonObj.get("zip").getAsString());
        venue.setPhone(jsonObj.get("phone").getAsString());
        venue.setTicketLink(jsonObj.get("ticket_link").getAsString());
        venue.setState(jsonObj.get("state").getAsString());
        venue.setPcode(jsonObj.get("pcode").getAsInt());
        venue.setCity(jsonObj.get("city").getAsString());
        venue.setId(jsonObj.get("id").getAsLong());
        venue.setTollFreePhone(jsonObj.get("tollfreephone").getAsString());
        venue.setAddress(jsonObj.get("address").getAsString());
        venue.setImageUrl(jsonObj.get("image_url").getAsString());
        venue.setDescription(jsonObj.get("description").getAsString());
        venue.setName(jsonObj.get("name").getAsString());
        venue.setLongitude(jsonObj.get("longitude").getAsDouble());
        venue.setLatitude(jsonObj.get("latitude").getAsDouble());

        // Parse all of the schedule items if they exist.
        JsonArray scheduleArray = jsonObj.get("schedule").getAsJsonArray();
        List<ScheduleItem> scheduleItemList = new ArrayList<>();
        for (int i = 0; i < scheduleArray.size(); i++) {
            ScheduleItem scheduleItem = new ScheduleItem();
            JsonElement scheduleItemElement = scheduleArray.get(i);
            try {
                scheduleItem.setEndDate(scheduleItemElement.getAsJsonObject().get("end_date").getAsString());
                scheduleItem.setStartDate(scheduleItemElement.getAsJsonObject().get("start_date").getAsString());
                // Only add the schedule item if we successfully parse both the start and end date.
                scheduleItemList.add(scheduleItem);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        venue.setSchedule(scheduleItemList);

        return venue;
    }
}
