package by.it_academy.jd2.golubev_107.mail_sender.storage.connection.factory;


import by.it_academy.jd2.golubev_107.mail_sender.platform.impl.PropertyReader;
import by.it_academy.jd2.golubev_107.mail_sender.storage.connection.IConnectionManager;
import by.it_academy.jd2.golubev_107.mail_sender.storage.connection.impl.ConnectionManagerImpl;

import java.util.HashMap;
import java.util.Map;

public class ConnectionManagerFactory {

    private static final ConnectionManagerFactory INSTANCE =
            new ConnectionManagerFactory("/postgres.properties");

    private final Map<String, IConnectionManager> connManagers;

    private ConnectionManagerFactory(String dbConfig) {
        connManagers = new HashMap<>();
        connManagers.put("default", new ConnectionManagerImpl(new PropertyReader(dbConfig).getAll()));
    }

    public static ConnectionManagerFactory getInstance() {
        return INSTANCE;
    }

    public IConnectionManager get() {
        return connManagers.get("default");
    }

    public IConnectionManager get(String className) {
        if (!connManagers.containsKey(className)) {
            throw new RuntimeException("No specific connection manager found for this class :" + className);
        }
        return connManagers.get(className);
    }

    public void destroy() {
        for (Map.Entry<String, IConnectionManager> closeable : connManagers.entrySet()) {
            IConnectionManager connManager = closeable.getValue();
            try {
                if (connManager != null) {
                    connManager.close();
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

}
