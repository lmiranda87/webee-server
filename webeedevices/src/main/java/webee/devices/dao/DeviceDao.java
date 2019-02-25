package webee.devices.dao;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.datastore.Cursor;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.IncompleteKey;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.KeyFactory;

import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.StructuredQuery.OrderBy;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.FileInputStream;

public class DeviceDao {

    private Datastore datastore;
    private KeyFactory keyFactory;
    private static final String COLLECTION_NAME = "Devices";
    private static final String CREDENTIALS_FILE = "/opt/webeedevices/keys.json";
    public DeviceDao() throws FileNotFoundException, IOException {

        datastore = DatastoreOptions.newBuilder().setProjectId(System.getenv("WEBEE_PROJECT_ID")
).setCredentials(ServiceAccountCredentials.fromStream(new FileInputStream(CREDENTIALS_FILE))).build().getService();

        //datastore = DatastoreOptions.getDefaultInstance().getService(); // Authorized Datastore service
        keyFactory = datastore.newKeyFactory().setKind(COLLECTION_NAME);      // Is used for creating keys later
    }


    public Device entityToDevice(Entity entity) {
        Device dev = new Device.Builder().build();
        dev.setMac(entity.getString(Device.MAC));
        dev.setTimestamp(entity.getString(Device.TIMESTAMP));
        dev.setId(entity.getKey().getId());
        return dev;
    }


    public Long createDevice(Device Device) {
        IncompleteKey key = keyFactory.newKey();          // Key will be assigned once written
        FullEntity<IncompleteKey> incDeviceEntity = Entity.newBuilder(key)  // Create the Entity
                .set(Device.MAC, Device.getMac())           // Add Property ("author", Device.getAuthor())
                .set(Device.TIMESTAMP, Device.getTimestamp())
                .build();
        Entity DeviceEntity = datastore.add(incDeviceEntity); // Save the Entity
        return DeviceEntity.getKey().getId();                     // The ID of the Key
    }

    public Device  readDevice(Long DeviceId) {
        Entity DeviceEntity = datastore.get(keyFactory.newKey(DeviceId)); // Load an Entity for Key(id)
        return entityToDevice(DeviceEntity);
    }

    public Device findDeviceByMac(String mac) {
        Device ret = null;
        Query<Entity> query = Query.newEntityQueryBuilder()
                .setKind(COLLECTION_NAME)
                .setFilter(PropertyFilter.eq("mac", mac))
                .build();
        QueryResults<Entity> tasks = datastore.run(query);
        if (tasks.hasNext()) {
            ret = entityToDevice(tasks.next());
        }
        return ret;
    }

    /*public List<Device> entitiesToDevices(QueryResults<Entity> resultList) {
        List<Device> resultDevices = new ArrayList<>();
        while (resultList.hasNext()) {  // We still have data
            resultDevices.add(entityToDevice(resultList.next()));      // Add the Device to the List
        }
        return resultDevices;
    }*/


    /*public List<Device> listDevices(String startCursorString) {
        Cursor startCursor = null;
        if (startCursorString != null && !startCursorString.equals("")) {
            startCursor = Cursor.fromUrlSafe(startCursorString);
        }
        Query<Entity> query = Query.newEntityQueryBuilder()
                .setKind("Device2")
                .setLimit(10)
                .setStartCursor(startCursor)
                .setOrderBy(OrderBy.asc(Device.TITLE))
                .build();
        QueryResults<Entity> resultList = datastore.run(query);   // Run the query
        List<Device> resultDevices = entitiesToDevices(resultList);     // Retrieve and convert Entities
        Cursor cursor = resultList.getCursorAfter();              // Where to start next time
        if (cursor != null && resultDevices.size() == 10) {         // Are we paging? Save Cursor
            String cursorString = cursor.toUrlSafe();               // Cursors are WebSafe
            return new List<>(resultDevices, cursorString);
        } else {
            return new List<>(resultDevices);
        }
    }

    */



}
